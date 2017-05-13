package feed.exception;

import lombok.Getter;

import javax.ws.rs.WebApplicationException;

@Getter
public class AppException extends WebApplicationException {

    private final int status;
    private final String message;

    public AppException(int status, String message) {
        super(message, status);
        this.status = status;
        this.message = message;
    }
}
