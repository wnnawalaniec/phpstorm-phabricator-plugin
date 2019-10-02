package wn.phabricator.api.method.maniphest.status;

import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.PhabricatorStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManiphestStatusSearchResponse extends Response {
    private Result result;

    @NotNull
    public List<PhabricatorStatus> getPhabricatorStatuses() {
        return result.data;
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

        return new ArrayList<>();
    }

    static public class Result {
        private List<PhabricatorStatus> data;
    }
}
