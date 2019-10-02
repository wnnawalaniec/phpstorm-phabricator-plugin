package wn.phabricator.api.method.user.whoami;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.PhabricatorUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserWhoamiResponse extends Response {
    @SerializedName("result")
    private PhabricatorUser phabricatorUser;

    @NotNull
    public PhabricatorUser getPhabricatorUser() {
        return phabricatorUser;
    }

    @NotNull
    @Override
    protected List<String> validationErrors() {
        super.validate();

        if (phabricatorUser == null) {
            return Collections.singletonList("null result given");
        }

        return new ArrayList<>();
    }
}
