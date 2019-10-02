package wn.phabricator.api.method.project.seach;

import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.PhabricatorCursor;
import wn.phabricator.api.model.PhabricatorProject;

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
        super.validate();

        if (result == null) {
            return Collections.singletonList("null response");
        }

        if (result.data == null) {
            return Collections.singletonList("null response data");
        }

        List<String> errors = new ArrayList<>();
        for (PhabricatorProject phabricatorProject : result.data) {
            errors.addAll(phabricatorProject.validate());
        }

        return errors;
    }

    public static class Result {
        private List<PhabricatorProject> data;
        private PhabricatorCursor cursor;

        public List<PhabricatorProject> getData() {
            return data;
        }

        public PhabricatorCursor getCursor() {
            return cursor;
        }
    }
}
