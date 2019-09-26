package wn.phabricator.codeSnippet.formatter;

import org.bouncycastle.util.Strings;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.CodeSnippet;

public abstract class Base {
    final private static String EOL = "\n";
    final private static String BLOCK_INDICATOR = "```";

    public String format(@NotNull CodeSnippet snippet) {
        return this.begin(snippet)
                .concat(this.formatCode(snippet))
                .concat(this.end());
    }

    private String begin(@NotNull CodeSnippet snippet) {
        return this.makeLine(BLOCK_INDICATOR)
                + this.makeLine(languageAndFileInfo(snippet));
    }

    String makeLine(@NotNull String text) {
        return text + EOL;
    }

    private String languageAndFileInfo(@NotNull CodeSnippet snippet) {
        return String.format("lang=%s, name=%s", snippet.getLang().toLowerCase(), snippet.getPath());
    }

    protected abstract String formatCode(CodeSnippet snippet);

    private String end() {
        return BLOCK_INDICATOR;
    }
}
