package wn.phabricator.api.method.project.column;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.method.BaseMethod;
import wn.phabricator.api.model.PhabricatorProject;

import java.util.ArrayList;
import java.util.List;

public class ProjectColumnSearch extends BaseMethod {
    private static final String NAME = "project.column.search";

    private ProjectColumnSearch(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    static public ProjectColumnSearch ByProject(
            @NotNull PhabricatorProject project
    ) {
        ArrayList<Pair<String, String>> params = new ArrayList<>();

        params.add(new Pair<>("constraints[projects][0]", project.getPhid()));
        return new ProjectColumnSearch(
                NAME,
                params
        );
    }
}
