package feed.services;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Service
public class UserService {

    @POST
    @Path("/{emailId}/subscribe/{feedId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscribe(@PathParam("emailId") String emailId, @PathParam("feedId") long feedId) {
        System.out.println("EmailID: " + emailId);
        System.out.println("FeedId: " + feedId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{emailId}/subscription")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubscription(@PathParam("emailId") String emailId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "Hello");
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
