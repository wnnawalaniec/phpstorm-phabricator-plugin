package wn.phabricator.task;

import com.intellij.tasks.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wn.phabricator.api.model.PhabricatorIssue;
import wn.phabricator.api.model.PhabricatorProject;

import javax.swing.*;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PhabricatorTask extends Task {
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\n");
    private Repository repository;
    private PhabricatorIssue issue;
    private PhabricatorProject project;

    public PhabricatorTask(Repository repository, PhabricatorIssue issue, PhabricatorProject project) {
        this.repository = repository;
        this.issue = issue;
        this.project = project;
    }

    @NotNull
    @Override
    public String getId() {
        return issue.getPhid();
    }

    @NotNull
    @Override
    public String getSummary() {
        return issue.title();
    }

    @Nullable
    @Override
    public String getDescription() {
        return NEW_LINE_PATTERN.splitAsStream(issue.description()).collect(Collectors.joining("<br/>"));
    }

    @NotNull
    @Override
    public Comment[] getComments() {
        return new Comment[0];
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return RepositoryType.ICON;
    }

    @NotNull
    @Override
    public TaskType getType() {
        return TaskType.OTHER;
    }

    @Nullable
    @Override
    public Date getUpdated() {
        return issue.getDateModified();
    }

    @Nullable
    @Override
    public Date getCreated() {
        return issue.getDateCreated();
    }

    @Override
    public boolean isClosed() {
        return repository.isClosed(issue.status().getValue());
    }

    @Override
    public boolean isIssue() {
        return true;
    }

    @Nullable
    @Override
    public String getIssueUrl() {
        return repository.getUrl() + "/" + getPresentableId();
    }

    @NotNull
    @Override
    public String getPresentableId() {
        return "T" + getNumber();
    }

    @Nullable
    @Override
    public TaskRepository getRepository() {
        return repository;
    }

    @Override
    public String getPresentableName() {
        return getPresentableId() + " - " + issue.title();
    }

    @NotNull
    @Override
    public String getNumber() {
        return String.valueOf(issue.getId());
    }

    @Nullable
    @Override
    public String getProject() {
        return project.getName();
    }
}
