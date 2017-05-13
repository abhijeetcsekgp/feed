package feed.exception;

import com.google.gson.Gson;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps java exceptions to Response
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    @Override
    public Response toResponse(AppException e) {
        return Response.status(e.getStatus())
                .entity(new Gson().toJson(new ErrorMessage(e)))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
