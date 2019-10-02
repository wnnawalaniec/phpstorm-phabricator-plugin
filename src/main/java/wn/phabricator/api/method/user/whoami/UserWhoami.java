package wn.phabricator.api.method.user.whoami;

import com.intellij.openapi.util.Pair;
import wn.phabricator.api.method.BaseMethod;

import java.util.ArrayList;
import java.util.List;

public class UserWhoami extends BaseMethod {

    private static final String NAME = "user.whoami";

    protected UserWhoami(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    static public UserWhoami whoami()
    {
        return new UserWhoami(NAME, new ArrayList<>());
    }
}
