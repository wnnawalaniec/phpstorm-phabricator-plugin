package wn.phabricator.task;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.config.TaskRepositoryEditor;
import com.intellij.tasks.impl.BaseRepositoryType;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class RepositoryType extends BaseRepositoryType<Repository> {
    private static final Icon ICON = IconLoader.getIcon("/icon/phabricator_logo.svg");
    @NotNull
    @Override
    public String getName() {
        return "Phabricator";
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return ICON;
    }

    @NotNull
    @Override
    public TaskRepositoryEditor createEditor(Repository repository, Project project, Consumer<Repository> changeListener) {
        return new RepositoryEditor(project, repository, changeListener);
    }

    @NotNull
    @Override
    public TaskRepository createRepository() {
        return new Repository(this);
    }

    @Override
    public Class<Repository> getRepositoryClass() {
        return Repository.class;
    }
}
