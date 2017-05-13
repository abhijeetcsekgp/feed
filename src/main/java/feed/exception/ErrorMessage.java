package feed.exception;

import lombok.Getter;

@Getter
public class ErrorMessage {
    private final int status;
    private final String message;

    public ErrorMessage(AppException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
    }
}
