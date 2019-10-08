package wn.phabricator.api.model;

import java.util.Date;

public class PhabricatorIssue {
    private int id;
    private String phid;
    private Fields fields = new Fields();

    public int getId() {
        return id;
    }

    public String getPhid() {
        return phid;
    }

    public String title() {
        return fields.name;
    }

    public String description() {
        return fields.description.raw;
    }

    public Fields.Status status() {
        return fields.status;
    }

    public Date getDateCreated() {
        return fields.dateCreated;
    }

    public Date getDateModified() {
        return fields.dateModified;
    }

    public static class Fields {
        private String name;
        private Description description;
        private Status status;
        private Date dateCreated;
        private Date dateModified;

        public static class Description {
            private String raw;
        }

        public static class Status {
            private String value;
            private String name;

            public String getValue() {
                return value;
            }

            public String getName() {
                return name;
            }
        }
    }
}
