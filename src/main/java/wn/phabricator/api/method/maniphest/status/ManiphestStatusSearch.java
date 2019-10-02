package wn.phabricator.api.method.maniphest.status;

import com.intellij.openapi.util.Pair;
import wn.phabricator.api.method.BaseMethod;

import java.util.ArrayList;
import java.util.List;

public class ManiphestStatusSearch extends BaseMethod {

    private static final String NAME = "maniphest.status.search";

    protected ManiphestStatusSearch(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    static public ManiphestStatusSearch Search() {
        return new ManiphestStatusSearch(NAME, new ArrayList<>());
    }
}
