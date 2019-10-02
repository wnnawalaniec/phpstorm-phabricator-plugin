package wn.phabricator.task;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.tasks.config.BaseRepositoryEditor;
import com.intellij.tasks.impl.TaskUiUtil;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.util.Consumer;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wn.phabricator.api.model.PhabricatorProject;

import javax.swing.*;
import java.util.List;

public class RepositoryEditor extends BaseRepositoryEditor<Repository> {
    private JBLabel myProjectLabel;
    private ComboBox<PhabricatorProject> myProjectComboBox;
    private JBCheckBox myOnlyAssignedToMeButton;

    public RepositoryEditor(Project project, Repository repository, Consumer<Repository> changeListener) {
        super(project, repository, changeListener);
        myPasswordLabel.setText("Token:");

        myUsernameLabel.setVisible(false);
        myUserNameText.setVisible(false);

        myTestButton.setEnabled(myRepository.isConfigured());

        installListener(myProjectComboBox);
        installListener(myOnlyAssignedToMeButton);

        UIUtil.invokeLaterIfNeeded(this::initialize);
    }

    private void initialize() {
        if (myRepository.getOnlyAssigned() != null) {
            myOnlyAssignedToMeButton.setSelected(myRepository.getOnlyAssigned());
        }

        PhabricatorProject currentPhabricatorProject = myRepository.getCurrentPhabricatorProject();
        if (currentPhabricatorProject != null && myRepository.isConfigured()) {
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
        myRepository.setCurrentPhabricatorProject((PhabricatorProject) myProjectComboBox.getSelectedItem());
        myRepository.setOnlyAssigned(myOnlyAssignedToMeButton.isSelected());
        myTestButton.setEnabled(myRepository.isConfigured());
    }

    @Nullable
    @Override
    protected JComponent createCustomPanel() {
        myOnlyAssignedToMeButton = new JBCheckBox("Only tasks already assigned to me.", true);
        myProjectLabel = new JBLabel("Project:", SwingConstants.RIGHT);
        myProjectComboBox = new ComboBox<>(300);
        myProjectComboBox.setRenderer(SimpleListCellRenderer.create("Enter url and token first.", Object::toString));
        myProjectLabel.setLabelFor(myProjectComboBox);
        return new FormBuilder()
                .addLabeledComponent(myProjectLabel, myProjectComboBox)
                .addComponentToRightColumn(myOnlyAssignedToMeButton)
                .getPanel();
    }

    private class FetchProjectsTask extends TaskUiUtil.ComboBoxUpdater<PhabricatorProject> {
        private FetchProjectsTask() {
            super(RepositoryEditor.this.myProject, "Downloading Phabricator projects...", myProjectComboBox);
        }

        @Nullable
        @Override
        public PhabricatorProject getSelectedItem() {
            return myRepository.getCurrentPhabricatorProject();
        }

        @NotNull
        @Override
        protected List<PhabricatorProject> fetch(@NotNull ProgressIndicator indicator) throws Exception {
            return myRepository.fetchProjects();
        }
    }
}
