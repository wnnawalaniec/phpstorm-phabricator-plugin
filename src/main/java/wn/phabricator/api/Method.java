package wn.phabricator.api;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Method {
    @NotNull
    public String name();

    @NotNull
    public List<Pair<String, String>> params();
}
