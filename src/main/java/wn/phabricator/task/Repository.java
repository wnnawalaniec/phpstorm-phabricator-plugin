package wn.phabricator.task;

import com.intellij.tasks.Task;
import com.intellij.tasks.TaskRepositoryType;
import com.intellij.tasks.impl.BaseRepository;
import com.intellij.tasks.impl.httpclient.NewBaseRepositoryImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Repository extends NewBaseRepositoryImpl {
    public Repository(TaskRepositoryType type) {
        super(type);
    }

    @Nullable
    @Override
    public Task findTask(@NotNull String id) throws Exception {
        return null;
    }

    @NotNull
    @Override
    public BaseRepository clone() {
        return null;
    }
}
