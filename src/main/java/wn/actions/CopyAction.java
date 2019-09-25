package wn.actions;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.CodeSnippet;
import wn.phabricator.codeSnippet.formatter.WithoutLineNumbers;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopyAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        e.getPresentation().setEnabledAndVisible(project != null
                && editor != null
                && editor.getSelectionModel().hasSelection()
        );
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final VirtualFile virtualFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final Language language = psiFile.getLanguage();

        Path projectPath = Paths.get(project.getBasePath());
        Path filePath = Paths.get(virtualFile.getCanonicalPath());
        Path relativeFilePath = projectPath.relativize(filePath);


        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();

        CodeSnippet snippet = new CodeSnippet(relativeFilePath.toString(), language.getDisplayName(), primaryCaret.getSelectedText());
        StringSelection selection = new StringSelection(new WithoutLineNumbers().format(snippet));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
