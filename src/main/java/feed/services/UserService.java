package feed.services;

import feed.daos.UserDao;
import feed.entities.Article;
import feed.entities.Feed;
import feed.exception.AppException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status;

@Path("/users")
@Service
public class UserService {

    @Inject
    private UserDao userDao;

    @POST
    @Path("/{emailId}/subscribe/{feedId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscribe(@PathParam("emailId") String emailId, @PathParam("feedId") long feedId) {
        validateEmailAddress(emailId);
        userDao.subscribeUser(feedId, emailId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{emailId}/unsubscribe/{feedId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unsubscribe(@PathParam("emailId") String emailId, @PathParam("feedId") long feedId) {
        validateEmailAddress(emailId);
        userDao.unsubscribeUser(feedId, emailId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{emailId}/subscription")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubscription(@PathParam("emailId") String emailId) {
        validateEmailAddress(emailId);
        List<Feed> subscribedFeeds = userDao.getSubscribedFeeds(emailId);
        return Response.status(Response.Status.OK)
                .entity(subscribedFeeds)
                .build();
    }

    @GET
    @Path("/{emailId}/articles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticles(@PathParam("emailId") String emailId) {
        validateEmailAddress(emailId);
        List<Article> articles = userDao.getSubscribedArticles(emailId);
        return Response.status(Response.Status.OK)
                .entity(articles)
                .build();
    }

    private void validateEmailAddress(String emailId) {
        if (!EmailValidator.getInstance().isValid(emailId)) {
            throw new AppException(Status.BAD_REQUEST.getStatusCode(), "Invalid EmailId");
        }
    }
}
