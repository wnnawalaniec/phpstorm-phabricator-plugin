package wn.phabricator.api.method.project.seach;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.Nullable;
import wn.phabricator.api.method.BaseMethod;
import wn.phabricator.api.model.PhabricatorCursor;

import java.util.ArrayList;
import java.util.List;

public class ProjectSearch extends BaseMethod {
    private static final String NAME = "project.search";

    private ProjectSearch(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    static public ProjectSearch AllActiveProjects(@Nullable PhabricatorCursor pagination) {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        if (pagination != null) {
            params.addAll(pagination.asParams());
        }

        return new ProjectSearch(
                "project.search",
                params
        );
    }
}
