package wn.phabricator.api.model;

import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Tag;

@Tag("user")
public class PhabricatorUser {
    private String phid;

    @Attribute("phid")
    public String getPhid() {
        return phid;
    }

    public void setPhid(String phid) {
        this.phid = phid;
    }
}
