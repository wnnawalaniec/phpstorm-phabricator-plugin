package wn.phabricator.api.model;

import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Tag("PhabricatorProject")
public class Project {
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

    public List<String> validate()
    {
        List<String> errors = new ArrayList<>();
        if (id < 1) {
            errors.add("Invalid id given");
        }

        if (phid == null || phid.isEmpty()) {
            errors.add("PhId must be given and not empty");
        }

        if (fields == null) {
            errors.add("Fields must be given");
        } else {
            if (fields.name == null || fields.name.isEmpty()) {
                errors.add("Name must be given, and cannot be empty");
            }

            if (fields.description == null) {
                errors.add("Description must be given");
            }
        }

        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        return id == ((Project) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static class Fields {
        private String name;
        private String description;
    }
}
