package wn.phabricator.codeSnippet.formatter;

import org.junit.jupiter.api.Test;
import wn.phabricator.CodeSnippet;

import static org.junit.jupiter.api.Assertions.*;

class WithoutLineNumbersTest {
    @Test
    public void testFormatting() {
        String code = "    function foo($bar): string\n" +
                "    {\n" +
                "        return $bar;\n" +
                "    }\n";
        String lang = "php";
        String path = "api/router.php";
        String expected = "```\n" +
                "lang=php, name=api/router.php\n" +
                "    function foo($bar): string\n" +
                "    {\n" +
                "        return $bar;\n" +
                "    }\n" +
                "```";
        CodeSnippet snippet = new CodeSnippet(path, lang, code);
        WithoutLineNumbers formatter = new WithoutLineNumbers();

        String formatted = formatter.format(snippet);

        assertEquals(expected, formatted);
    }
}