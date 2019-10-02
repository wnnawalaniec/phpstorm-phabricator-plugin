package wn.phabricator.api.method.maniphest.search;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wn.phabricator.api.method.BaseMethod;
import wn.phabricator.api.model.PhabricatorCursor;
import wn.phabricator.api.model.PhabricatorProject;
import wn.phabricator.api.model.PhabricatorUser;

import java.util.ArrayList;
import java.util.List;

public class ManiphestSearch extends BaseMethod {

    private static final String NAME = "maniphest.search";

    private ManiphestSearch(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    static public ManiphestSearch ByTaskId(@NotNull String id, @NotNull PhabricatorProject project) {
        ArrayList<Pair<String, String>> params = new ArrayList<>(new PhabricatorCursor(1).asParams());
        params.add(new Pair<>("constraints[phids][0]", id));
        params.add(new Pair<>("constraints[projects][0]", project.getPhid()));
        return new ManiphestSearch(
                NAME,
                params
        );
    }

    static public ManiphestSearch ByProject(
            @NotNull PhabricatorProject project,
            boolean onlyOpen,
            @Nullable String fullTextQuery,
            @Nullable PhabricatorCursor pagination
    ) {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        if (pagination != null) {
            params.addAll(pagination.asParams());
        }

        params.add(new Pair<>("queryKey", onlyOpen ? "open" : "all"));
        params.add(new Pair<>("constraints[projects][0]", project.getPhid()));
        return new ManiphestSearch(
                NAME,
                params
        );
    }

    static public ManiphestSearch ByProjectAndOwner(
            @NotNull PhabricatorProject project,
            @NotNull PhabricatorUser phabricatorUser,
            boolean onlyOpen,
            @Nullable String fullTextQuery,
            @Nullable PhabricatorCursor pagination
    ) {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        if (pagination != null) {
            params.addAll(pagination.asParams());
        }

        params.add(new Pair<>("queryKey", onlyOpen ? "open" : "all"));
        params.add(new Pair<>("constraints[projects][0]", project.getPhid()));
        params.add(new Pair<>("constraints[assigned][0]", phabricatorUser.getPhid()));
        return new ManiphestSearch(
                NAME,
                params
        );
    }
}
