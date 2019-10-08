package wn.phabricator.api;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.impl.RequestFailedException;

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

    public void validate() throws RequestFailedException {
        if (StringUtil.isNotEmpty(errorInfo)) {
            throw new RequestFailedException(errorInfo);
        }
    }
}
