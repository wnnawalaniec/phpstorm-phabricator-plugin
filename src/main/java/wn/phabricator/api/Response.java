package wn.phabricator.api;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.impl.RequestFailedException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Response {
    @SerializedName("error_code")
    private String errorCode;

    @SerializedName("error_info")
    private String errorInfo;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void validate() {
        if (StringUtil.isNotEmpty(errorInfo)) {
            throw new RequestFailedException(errorInfo);
        }

        List<String> validationErrors = validationErrors();
        if (!validationErrors.isEmpty()) {
            throw new RequestFailedException("Invalid data received: " + StringUtil.join(validationErrors, "\n"));
        }
    }

    @NotNull
    protected abstract List<String> validationErrors();
}
