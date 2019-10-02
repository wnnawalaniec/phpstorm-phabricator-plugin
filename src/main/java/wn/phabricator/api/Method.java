package wn.phabricator.api;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Method {
    @NotNull
    String name();

    @NotNull
    List<Pair<String, String>> params();
}
