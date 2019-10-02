package wn.phabricator.api.method.maniphest.edit;

import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;

import java.util.ArrayList;
import java.util.List;

public class ManiphestEditResponse extends Response {
    @NotNull
    @Override
    protected List<String> validationErrors() {
        super.validate();
        return new ArrayList<>();
    }
}
