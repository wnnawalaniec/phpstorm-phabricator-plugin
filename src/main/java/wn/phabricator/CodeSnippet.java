package wn.phabricator;

public class CodeSnippet {
    private String path;
    private String lang;
    private String code;

    public CodeSnippet(String path, String lang, String code) {
        this.path = path;
        this.lang = lang;
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public String getLang() {
        return lang;
    }

    public String getCode() {
        return code;
    }
}
