package wn.phabricator.api.model;

import org.jetbrains.annotations.NotNull;

public class PhabricatorStatus {
    private String name;
    private String value;
    private Boolean closed;

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    @NotNull
    public Boolean isClosed() {
        return closed;
    }
}
