package wn.phabricator.api.model;

public class PhabricatorColumn {
    private int id;
    private String phid;
    private Fields fields = new Fields();

    public int getId() {
        return id;
    }

    public String getPhid() {
        return phid;
    }

    public String name() {
        return fields.name;
    }

    @Override
    public String toString() {
        return name();
    }

    public static class Fields {
        private String name;
    }
}
