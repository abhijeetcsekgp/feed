package feed.services;

import feed.daos.UserDao;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Service
public class UserService {

    @Inject
    private UserDao userDao;

    @POST
    @Path("/{emailId}/subscribe/{feedId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscribe(@PathParam("emailId") String emailId, @PathParam("feedId") long feedId) {
        userDao.subscribeUser(feedId, emailId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{emailId}/subscription")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubscription(@PathParam("emailId") String emailId) {
        List<String> subscribedFeeds = userDao.getSubscribedFeeds(emailId);
        JSONArray jsonArray = new JSONArray();
        for (String feed : subscribedFeeds) {
            jsonArray.add(feed);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subscription", jsonArray);
        return Response.status(Response.Status.OK)
                .entity(jsonObject.toJSONString())
                .build();
    }

    @GET
    @Path("/{emailId}/articles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticles(@PathParam("emailId") String emailId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "Hello");
        return Response.status(Response.Status.OK)
                .entity(jsonObject.toJSONString())
                .build();
    }
}
