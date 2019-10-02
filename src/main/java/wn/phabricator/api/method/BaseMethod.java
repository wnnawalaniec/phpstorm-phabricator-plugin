package wn.phabricator.api.method;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.Method;

import java.util.List;

/**
 * Base Phabricator Conduit Api method abstraction
 */
public abstract class BaseMethod implements Method {
    private String name;
    private List<Pair<String, String>> params;

    // Non of methods should be created manually
    protected BaseMethod(String name, List<Pair<String, String>> params) {
        this.name = name;
        this.params = params;
    }

    @NotNull
    @Override
    public String name() {
        return name;
    }

    @NotNull
    @Override
    public List<Pair<String, String>> params() {
        return params;
    }
}
