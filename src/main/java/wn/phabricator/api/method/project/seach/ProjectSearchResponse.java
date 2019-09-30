package wn.phabricator.api.method.project.seach;

import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.Cursor;
import wn.phabricator.api.model.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectSearchResponse extends Response {
    private Result result;

    @NotNull
    public Result getResult() {
        return result;
    }

    @NotNull
    @Override
    protected List<String> validationErrors() {
        if (result == null) {
            return Collections.singletonList("null response");
        }

        if (result.data == null) {
            return Collections.singletonList("null response data");
        }

        List<String> errors = new ArrayList<>();
        for (Project project : result.data) {
            errors.addAll(project.validate());
        }

        return errors;
    }

    public static class Result {
        private List<Project> data;
        private Cursor cursor;

        public List<Project> getData() {
            return data;
        }

        public Cursor getCursor() {
            return cursor;
        }
    }
}
