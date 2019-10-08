package wn.phabricator.api.method.maniphest.status;

import com.intellij.tasks.impl.RequestFailedException;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.PhabricatorStatus;

import java.util.List;

public class ManiphestStatusSearchResponse extends Response {
    private Result result;

    @NotNull
    public List<PhabricatorStatus> getPhabricatorStatuses() {
        return result.data;
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

    static public class Result {
        private List<PhabricatorStatus> data;
    }
}
