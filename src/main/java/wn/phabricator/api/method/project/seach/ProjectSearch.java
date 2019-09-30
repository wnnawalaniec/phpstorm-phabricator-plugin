package wn.phabricator.api.method.project.seach;

import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;
import wn.phabricator.api.method.BaseMethod;
import wn.phabricator.api.model.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ProjectSearch extends BaseMethod {
    private static final String NAME = "project.search";

    private ProjectSearch(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    static public ProjectSearch AllActiveProjects(@Nullable Cursor pagination) {
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
