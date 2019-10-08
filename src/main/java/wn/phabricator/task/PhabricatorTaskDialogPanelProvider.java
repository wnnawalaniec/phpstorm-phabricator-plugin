package wn.phabricator.task;

import com.intellij.openapi.project.Project;
import com.intellij.tasks.LocalTask;
import com.intellij.tasks.Task;
import com.intellij.tasks.ui.TaskDialogPanel;
import com.intellij.tasks.ui.TaskDialogPanelProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhabricatorTaskDialogPanelProvider extends TaskDialogPanelProvider {
    @Nullable
    @Override
    public TaskDialogPanel getOpenTaskPanel(@NotNull Project project, @NotNull Task task) {
        if (task.getRepository() instanceof Repository) {
            return new PhabricatorTaskDialogPanel((Repository) task.getRepository(), task);
        }

        return null;
    }

    @Nullable
    @Override
    public TaskDialogPanel getCloseTaskPanel(@NotNull Project project, @NotNull LocalTask task) {
        if (task.getRepository() instanceof Repository) {
            return new PhabricatorTaskDialogPanel((Repository) task.getRepository(), task);
        }

        return null;
    }
}
