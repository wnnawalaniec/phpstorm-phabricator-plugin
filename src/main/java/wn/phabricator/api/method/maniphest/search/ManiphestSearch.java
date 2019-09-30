package wn.phabricator.api.method.maniphest.search;

import javafx.util.Pair;
import wn.phabricator.api.method.BaseMethod;
import wn.phabricator.api.model.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ManiphestSearch extends BaseMethod {

    private static final String NAME = "maniphest.search";

    private ManiphestSearch(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    static public ManiphestSearch ByTaskId(String id) {
        ArrayList<Pair<String, String>> params = new ArrayList<>(new Cursor(1).asParams());
        params.add(new Pair<>("queryKey", "active"));
        return new ManiphestSearch(
                NAME,
                params
        );
    }
}
