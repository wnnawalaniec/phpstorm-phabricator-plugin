package wn.phabricator.api.method.maniphest.search;

import javafx.util.Pair;
import wn.phabricator.api.method.BaseMethod;
import wn.phabricator.api.model.PhabricatorCursor;

import java.util.ArrayList;
import java.util.List;

public class ManiphestSearch extends BaseMethod {

    private static final String NAME = "maniphest.search";

    private ManiphestSearch(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    static public ManiphestSearch ByTaskId(String id) {
        ArrayList<Pair<String, String>> params = new ArrayList<>(new PhabricatorCursor(1).asParams());
        params.add(new Pair<>("queryKey", "active"));
        return new ManiphestSearch(
                NAME,
                params
        );
    }

    static public ManiphestSearch ByProject(String projectPhid, boolean onlyOpen) {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>("queryKey", onlyOpen ? "open" : "all"));
        params.add(new Pair<>("constraints[projects][0]", projectPhid));
        return new ManiphestSearch(
                NAME,
                params
        );
    }

    static public ManiphestSearch ByProjectAndOwner(String projectPhid, String ownerPhId, boolean onlyOpen) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
