package wn.phabricator.api.method.maniphest.search;

import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.PhabricatorCursor;
import wn.phabricator.api.model.PhabricatorIssue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManiphestSearchResponse extends Response {
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
        for (PhabricatorIssue task : result.data) {
            errors.addAll(task.validate());
        }

        return errors;
    }

    public static class Result {
        private List<PhabricatorIssue> data;
        private PhabricatorCursor cursor;

        public List<PhabricatorIssue> getData() {
            return data;
        }

        public PhabricatorCursor getCursor() {
            return cursor;
        }
    }
}
