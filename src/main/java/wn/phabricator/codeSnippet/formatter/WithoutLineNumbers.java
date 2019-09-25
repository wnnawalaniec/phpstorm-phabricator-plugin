package wn.phabricator.codeSnippet.formatter;

import org.jetbrains.annotations.NotNull;
import wn.phabricator.CodeSnippet;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WithoutLineNumbers extends Base {
    @Override
    protected String formatCode(CodeSnippet snippet) {
        return Arrays.stream(this.codeAsLinesOfText(snippet.getCode()))
                .parallel()
                .map(this::makeLine)
                .collect(Collectors.joining());
    }

    private String[] codeAsLinesOfText(@NotNull String code) {
        return code.split("\\r?\\n");
    }
}
