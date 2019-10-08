package wn.phabricator.task;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.tasks.Task;
import com.intellij.tasks.impl.RequestFailedException;
import com.intellij.tasks.ui.TaskDialogPanel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;
import wn.phabricator.api.model.PhabricatorColumn;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.io.IOException;

public class PhabricatorTaskDialogPanel extends TaskDialogPanel {
    private final Repository repository;
    private final Task task;
    private final JBCheckBox wantMove;
    private final ComboBox<PhabricatorColumn> columnsComboBox;

    public PhabricatorTaskDialogPanel(Repository repository, Task task) {
        this.repository = repository;
        this.task = task;

        columnsComboBox = new ComboBox<>(300);
        wantMove = new JBCheckBox("Move to column", false);
        wantMove.addItemListener(itemEvent -> {
            columnsComboBox.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
        });

        try {
            repository.getPhabricatorColumns().forEach(columnsComboBox::addItem);
        } catch (IOException ignored) {
            wantMove.setEnabled(false);
            columnsComboBox.setEnabled(false);
        }
    }

    @NotNull
    @Override
    public JComponent getPanel() {
        return new FormBuilder()
                .addSeparator()
                .addLabeledComponent(wantMove, columnsComboBox)
                .getPanel();
    }

    @Override
    public void commit() {
        if (!wantMove.isSelected()) {
            return;
        }

        if (columnsComboBox.getSelectedItem() == null) {
            return;
        }

        try {
            repository.setTaskColumn(task, (PhabricatorColumn) columnsComboBox.getSelectedItem());
        } catch (IOException | RequestFailedException e) {
            Messages.showErrorDialog("Task wasn't moved", "Error - Close Task - Change Column");
        }
    }
}
