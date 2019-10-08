package wn.phabricator.task;

import com.google.gson.Gson;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.CustomTaskState;
import com.intellij.tasks.Task;
import com.intellij.tasks.TaskRepositoryType;
import com.intellij.tasks.impl.gson.TaskGsonUtil;
import com.intellij.tasks.impl.httpclient.NewBaseRepositoryImpl;
import com.intellij.tasks.impl.httpclient.TaskResponseUtil;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xmlb.annotations.Attribute;
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
import wn.phabricator.api.method.maniphest.edit.ManiphestEdit;
import wn.phabricator.api.method.maniphest.edit.ManiphestEditResponse;
import wn.phabricator.api.method.maniphest.search.ManiphestSearch;
import wn.phabricator.api.method.maniphest.search.ManiphestSearchResponse;
import wn.phabricator.api.method.maniphest.status.ManiphestStatusSearch;
import wn.phabricator.api.method.maniphest.status.ManiphestStatusSearchResponse;
import wn.phabricator.api.method.project.column.ProjectColumnSearch;
import wn.phabricator.api.method.project.column.ProjectColumnSearchResponse;
import wn.phabricator.api.method.project.seach.ProjectSearch;
import wn.phabricator.api.method.project.seach.ProjectSearchResponse;
import wn.phabricator.api.method.user.whoami.UserWhoami;
import wn.phabricator.api.method.user.whoami.UserWhoamiResponse;
import wn.phabricator.api.model.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag("Phabricator")
public class Repository extends NewBaseRepositoryImpl {
    private static final String REST_API_PATH_PREFIX = "/api/";
    private static final Gson GSON = TaskGsonUtil.createDefaultBuilder().create();

    private PhabricatorUser currentPhabricatorUser;
    private Boolean onlyAssigned;
    private PhabricatorProject currentPhabricatorProject;
    private List<PhabricatorProject> phabricatorProjects = new ArrayList<>();
    private PhabricatorCursor currentCursor;
    private List<PhabricatorStatus> phabricatorStatuses;
    private List<PhabricatorColumn> phabricatorColumns;

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
    private Repository(Repository other) {
        super(other);
        this.currentPhabricatorProject = other.currentPhabricatorProject;
        this.onlyAssigned = other.onlyAssigned;
        this.currentPhabricatorUser = other.currentPhabricatorUser;
    }

    @Nullable
    @Override
    public Task findTask(@NotNull String id) throws IOException {
        ManiphestSearchResponse response = callPhabricator(
                ManiphestSearch.ByTaskId(id, currentPhabricatorProject),
                ManiphestSearchResponse.class
        );
        List<PhabricatorIssue> result = response.getResult().getData();
        return result.isEmpty() ? null : new PhabricatorTask(this, result.get(0), currentPhabricatorProject);
    }

    @Override
    public Task[] getIssues(@Nullable String query, int offset, int limit, boolean withClosed) throws Exception {
        final List<PhabricatorIssue> issues = fetchIssues(query, offset, limit, withClosed);
        return ContainerUtil.map2Array(issues, PhabricatorTask.class, issue -> new PhabricatorTask(this, issue, currentPhabricatorProject));
    }

    @Override
    public boolean isConfigured() {
        return super.isConfigured() && StringUtil.isNotEmpty(myPassword);
    }

    @Override
    public boolean isSupported(int feature) {
        if (feature == STATE_UPDATING) {
            return true;
        }

        return super.isSupported(feature);
    }

    @NotNull
    public Boolean isClosed(@NotNull String statusValue) {
        try {
            ensureStatusesAreObtained();
            return phabricatorStatuses.stream()
                    .anyMatch(s -> s.getValue().equals(statusValue) && s.isClosed());
        } catch (IOException e) {
            return false;
        }
    }

    @Nullable
    @Override
    public CancellableConnection createCancellableConnection() {
        return new HttpTestConnection(methodToRequest(ProjectSearch.AllActiveProjects(null)));
    }

    public List<PhabricatorProject> fetchProjects() throws IOException {
        ProjectSearchResponse response = callPhabricator(ProjectSearch.AllActiveProjects(null), ProjectSearchResponse.class);

        phabricatorProjects.addAll(response.getResult().getData());

        while (response.getResult().getCursor().hasNextPage()) {
            response = callPhabricator(ProjectSearch.AllActiveProjects(response.getResult().getCursor()), ProjectSearchResponse.class);
            phabricatorProjects.addAll(response.getResult().getData());
        }

        return phabricatorProjects;
    }

    private List<PhabricatorIssue> fetchIssues(String query, int offset, int limit, boolean withClosed) throws IOException {
        limit = Math.min(limit, 100);

        if (currentCursor == null) {
            currentCursor = new PhabricatorCursor(limit);
        } else {
            currentCursor.setLimit(limit);
        }

        ManiphestSearch method;
        if (onlyAssigned) {
            ensureUserIsObtained();
            method = ManiphestSearch.ByProjectAndOwner(currentPhabricatorProject, currentPhabricatorUser, !withClosed, query, currentCursor);
        } else {
            method = ManiphestSearch.ByProject(currentPhabricatorProject, !withClosed, query, currentCursor);
        }
        ManiphestSearchResponse response = callPhabricator(
                method,
                ManiphestSearchResponse.class
        );

        currentCursor = response.getResult().getCursor();
        return response.getResult().getData();
    }

    private List<PhabricatorColumn> fetchColumns() throws IOException {
        ProjectColumnSearchResponse response = callPhabricator(
                ProjectColumnSearch.ByProject(this.currentPhabricatorProject),
                ProjectColumnSearchResponse.class
        );

        return response.getResult().getData();
    }

    private void ensureStatusesAreObtained() throws IOException {
        if (phabricatorStatuses == null) {
            ManiphestStatusSearchResponse response = callPhabricator(ManiphestStatusSearch.Search(), ManiphestStatusSearchResponse.class);
            phabricatorStatuses = response.getPhabricatorStatuses();
        }
    }

    private void ensureUserIsObtained() throws IOException {
        if (currentPhabricatorUser == null) {
            UserWhoamiResponse response = callPhabricator(UserWhoami.whoami(), UserWhoamiResponse.class);
            currentPhabricatorUser = response.getPhabricatorUser();
        }
    }

    private void ensureColumnsAreObtained() throws IOException {
        if (phabricatorColumns == null) {
            phabricatorColumns = fetchColumns();
        }
    }

    private <T extends Response> T callPhabricator(@NotNull Method method, Class<T> responseClass) throws IOException {
        HttpPost post = methodToRequest(method);
        HttpResponse response = getHttpClient().execute(post);
        T deserializedResponse = deserialize(response, responseClass);
        deserializedResponse.validate();
        return deserializedResponse;
    }

    @NotNull
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
                .map(pair -> new BasicNameValuePair(pair.first, pair.second))
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public String getRestApiPathPrefix() {
        return REST_API_PATH_PREFIX;
    }

    @Nullable
    public PhabricatorProject getCurrentPhabricatorProject() {
        return currentPhabricatorProject;
    }

    public void setCurrentPhabricatorProject(@Nullable PhabricatorProject currentPhabricatorProject) {
        this.currentPhabricatorProject = currentPhabricatorProject;
    }

    @Attribute("onlyAssigned")
    @Nullable
    public Boolean getOnlyAssigned() {
        return onlyAssigned;
    }

    public void setOnlyAssigned(@Nullable Boolean onlyAssigned) {
        this.onlyAssigned = onlyAssigned;
    }

    @Nullable
    public PhabricatorUser getCurrentPhabricatorUser() {
        return currentPhabricatorUser;
    }

    public void setCurrentPhabricatorUser(@Nullable PhabricatorUser currentPhabricatorUser) {
        this.currentPhabricatorUser = currentPhabricatorUser;
    }

    @NotNull
    @Override
    public Set<CustomTaskState> getAvailableTaskStates(@NotNull Task task) throws Exception {
        ensureStatusesAreObtained();
        return phabricatorStatuses.stream()
                .map(s -> new CustomTaskState(s.getValue(), s.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public void setTaskState(@NotNull Task task, @NotNull CustomTaskState state) throws Exception {
        callPhabricator(ManiphestEdit.ChangeStatus(task.getId(), state.getId()), ManiphestEditResponse.class);
    }

    public void setTaskColumn(@NotNull Task task, @NotNull PhabricatorColumn column) throws IOException {
        callPhabricator(ManiphestEdit.ChangeColumn(task.getId(), column.getPhid()), ManiphestEditResponse.class);
    }

    public List<PhabricatorColumn> getPhabricatorColumns() throws IOException {
        ensureColumnsAreObtained();
        return phabricatorColumns;
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
        return Comparing.equal(currentPhabricatorProject, repository.currentPhabricatorProject)
                && Comparing.equal(onlyAssigned, repository.onlyAssigned)
                && Comparing.equal(currentPhabricatorUser, repository.currentPhabricatorUser);
    }
}
