package wn.phabricator.task;

import com.intellij.openapi.project.Project;
import com.intellij.tasks.config.BaseRepositoryEditor;
import com.intellij.util.Consumer;
import com.intellij.util.ui.UIUtil;

public class RepositoryEditor extends BaseRepositoryEditor<Repository> {
    public RepositoryEditor(Project project, Repository repository, Consumer<Repository> changeListener) {
        super(project, repository, changeListener);
        myPasswordLabel.setText("Token:");

        myUsernameLabel.setVisible(false);
        myUserNameText.setVisible(false);

        myTestButton.setEnabled(myRepository.isConfigured());

        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                initialize();
            }
        });
    }

    private void initialize() {

    }
}
