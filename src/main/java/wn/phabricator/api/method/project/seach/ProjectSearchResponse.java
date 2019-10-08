package wn.phabricator.api.method.project.seach;

import com.intellij.tasks.impl.RequestFailedException;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.PhabricatorCursor;
import wn.phabricator.api.model.PhabricatorProject;

import java.util.List;

public class ProjectSearchResponse extends Response {
    private Result result;

    @NotNull
    public Result getResult() {
        return result;
    }

    @Override
    public void validate() {
        super.validate();

        if (result == null) {
            throw new RequestFailedException("null response");
        }

        if (result.data == null) {
            throw new RequestFailedException("null response data");
        }
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
