package wn.phabricator.task;

import com.google.gson.Gson;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.Task;
import com.intellij.tasks.TaskRepositoryType;
import com.intellij.tasks.impl.gson.TaskGsonUtil;
import com.intellij.tasks.impl.httpclient.NewBaseRepositoryImpl;
import com.intellij.tasks.impl.httpclient.TaskResponseUtil;
import com.intellij.util.xmlb.annotations.Tag;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wn.phabricator.api.Method;
import wn.phabricator.api.Response;
import wn.phabricator.api.method.maniphest.search.ManiphestSearch;
import wn.phabricator.api.method.project.seach.ProjectSearch;
import wn.phabricator.api.method.project.seach.ProjectSearchResponse;
import wn.phabricator.api.model.Project;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag("Phabricator")
public class Repository extends NewBaseRepositoryImpl {
    public static final String REST_API_PATH_PREFIX = "/api/";
    public static final Gson GSON = TaskGsonUtil.createDefaultBuilder().create();

    private Project currentProject;
    private List<Project> projects = new ArrayList<>();

    /**
     * Serialization constructor
     */
    @SuppressWarnings("UnusedDeclaration")
    public Repository() {
    }

    /**
     * Normal instantiation constructor
     */
    public Repository(TaskRepositoryType type) {
        super(type);
    }

    /**
     * Cloning constructor
     */
    public Repository(Repository other) {
        super(other);
        this.currentProject = other.currentProject;
    }

    @Nullable
    @Override
    public Task findTask(@NotNull String id) throws Exception {
        Method method = ManiphestSearch.ByTaskId(id);
        return null;
    }

    @Nullable
    @Override
    public CancellableConnection createCancellableConnection() {
        return new HttpTestConnection(methodToRequest(ProjectSearch.AllActiveProjects(null)));
    }

    @NotNull
    @Override
    public String getRestApiPathPrefix() {
        return REST_API_PATH_PREFIX;
    }

    @Override
    public boolean isConfigured() {
        return super.isConfigured() && StringUtil.isNotEmpty(myPassword);
    }

    @NotNull
    @Override
    public Repository clone() {
        return new Repository(this);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Repository repository = (Repository) o;
        if (!Comparing.equal(currentProject, repository.currentProject)) return false;
        return true;
    }

    @Nullable
    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(@Nullable Project currentProject) {
        this.currentProject = currentProject;
    }

    public List<Project> fetchProjects() throws IOException {
        ProjectSearchResponse response = callPhabricator(ProjectSearch.AllActiveProjects(null), ProjectSearchResponse.class);
        projects.addAll(response.getResult().getData());

        while (response.getResult().getCursor().hasNextPage()) {
            response = callPhabricator(ProjectSearch.AllActiveProjects(response.getResult().getCursor()), ProjectSearchResponse.class);
            projects.addAll(response.getResult().getData());
        }

        return projects;
    }

    private <T extends Response> T callPhabricator(@NotNull Method method, Class<T> responseClass) throws IOException {
        HttpPost post = methodToRequest(method);
        HttpResponse response = getHttpClient().execute(post);
        return deserialize(response, responseClass);
    }

    private HttpPost methodToRequest(@NotNull Method method) {
        HttpPost post = new HttpPost(getRestApiUrl(method.name()));

        List<NameValuePair> params = convertParamsToNameValuePair(method);
        params.add(new BasicNameValuePair("api.token", myPassword));

        try {
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //
        }

        return post;
    }

    private <T extends Response> T deserialize(HttpResponse response, Class<T> responseClass) throws IOException {
        TaskResponseUtil.GsonSingleObjectDeserializer<T> responseHandler =
                new TaskResponseUtil.GsonSingleObjectDeserializer<>(GSON, responseClass);
        return responseHandler.handleResponse(response);
    }

    private List<NameValuePair> convertParamsToNameValuePair(@NotNull Method method) {
        return method.params()
                .stream()
                .map(pair -> new BasicNameValuePair(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
    }
}
