package wn.phabricator.task;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.tasks.config.BaseRepositoryEditor;
import com.intellij.tasks.impl.TaskUiUtil;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.Consumer;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class RepositoryEditor extends BaseRepositoryEditor<Repository> {
    private JBLabel myProjectLabel;
    private ComboBox<wn.phabricator.api.model.Project> myProjectComboBox;

    public RepositoryEditor(Project project, Repository repository, Consumer<Repository> changeListener) {
        super(project, repository, changeListener);
        myPasswordLabel.setText("Token:");

        myUsernameLabel.setVisible(false);
        myUserNameText.setVisible(false);

        myTestButton.setEnabled(myRepository.isConfigured());

        installListener(myProjectComboBox);

        UIUtil.invokeLaterIfNeeded(this::initialize);
    }

    private void initialize() {
        wn.phabricator.api.model.Project currentProject = myRepository.getCurrentProject();
        if (currentProject != null && myRepository.isConfigured()) {
            new FetchProjectsTask().queue();
        }
    }

    @Override
    public void setAnchor(@Nullable JComponent anchor) {
        super.setAnchor(anchor);
        myProjectLabel.setAnchor(anchor);
    }

    @Override
    protected void afterTestConnection(boolean connectionSuccessful) {
        if (connectionSuccessful) {
            new FetchProjectsTask().queue();
        }
    }

    @Override
    public void apply() {
        super.apply();
        myRepository.setCurrentProject((wn.phabricator.api.model.Project) myProjectComboBox.getSelectedItem());
        myTestButton.setEnabled(myRepository.isConfigured());
    }

    @Nullable
    @Override
    protected JComponent createCustomPanel() {
        myProjectLabel = new JBLabel("Project:", SwingConstants.RIGHT);
        myProjectComboBox = new ComboBox<wn.phabricator.api.model.Project>(300);
        myProjectComboBox.setRenderer(SimpleListCellRenderer.create("Enter url and token first.", Object::toString));
        myProjectLabel.setLabelFor(myProjectComboBox);
        return new FormBuilder().addLabeledComponent(myProjectLabel, myProjectComboBox).getPanel();
    }

    private class FetchProjectsTask extends TaskUiUtil.ComboBoxUpdater<wn.phabricator.api.model.Project> {
        private FetchProjectsTask() {
            super(RepositoryEditor.this.myProject, "Downloading Phabricator projects...", myProjectComboBox);
        }

        @Nullable
        @Override
        public wn.phabricator.api.model.Project getSelectedItem() {
            return myRepository.getCurrentProject();
        }

        @NotNull
        @Override
        protected List<wn.phabricator.api.model.Project> fetch(@NotNull ProgressIndicator indicator) throws Exception {
            return myRepository.fetchProjects();
        }
    }
}
