package wn.phabricator.api.method.maniphest.edit;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.method.BaseMethod;

import java.util.ArrayList;
import java.util.List;

public class ManiphestEdit extends BaseMethod {

    static private String NAME = "maniphest.edit";

    protected ManiphestEdit(String name, List<Pair<String, String>> params) {
        super(name, params);
    }

    @NotNull
    static public ManiphestEdit ChangeStatus(@NotNull String taskPhid, @NotNull String statusValue) {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>("objectIdentifier", taskPhid));
        params.add(new Pair<>("transactions[0][type]", "status"));
        params.add(new Pair<>("transactions[0][value]", statusValue));
        return new ManiphestEdit(NAME, params);
    }
}
