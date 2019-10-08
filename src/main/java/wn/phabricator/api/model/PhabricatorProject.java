package wn.phabricator.api.model;

import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
@Tag("PhabricatorProject")
public class PhabricatorProject {
    private int id;
    private String phid;
    private Fields fields = new Fields();

    @Attribute("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    @Attribute("phid")
    public String getPhid() {
        return phid;
    }

    public void setPhid(String phid) {
        this.phid = phid;
    }

    @Nullable
    @Attribute("name")
    public String getName() {
        return fields.name;
    }

    public void setName(String name) {
        if (fields == null) {
            fields = new Fields();
        }

        fields.name = name;
    }

    @Nullable
    @Attribute("description")
    public String getDescription() {
        return fields.description;
    }

    public void setDescription(String description) {
        if (fields == null) {
            fields = new Fields();
        }

        fields.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhabricatorProject)) return false;
        return id == ((PhabricatorProject) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }

    private static class Fields {
        private String name;
        private String description;
    }
}
