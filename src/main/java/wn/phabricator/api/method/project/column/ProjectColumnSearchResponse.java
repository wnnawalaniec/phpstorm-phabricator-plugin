package wn.phabricator.api.method.project.column;

import com.intellij.tasks.impl.RequestFailedException;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.PhabricatorColumn;

import java.util.List;

public class ProjectColumnSearchResponse extends Response {
    private Result result;

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
        private List<PhabricatorColumn> data;

        public List<PhabricatorColumn> getData() {
            return data;
        }
    }
}
