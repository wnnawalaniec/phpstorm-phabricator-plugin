package wn.phabricator.api.method.user.whoami;

import com.google.gson.annotations.SerializedName;
import com.intellij.tasks.impl.RequestFailedException;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Response;
import wn.phabricator.api.model.PhabricatorUser;

public class UserWhoamiResponse extends Response {
    @SerializedName("result")
    private PhabricatorUser phabricatorUser;

    @NotNull
    public PhabricatorUser getPhabricatorUser() {
        return phabricatorUser;
    }

    @Override
    public void validate() {
        super.validate();

        if (phabricatorUser == null) {
            throw new RequestFailedException("null result given");
        }
    }
}
