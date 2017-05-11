package feed.services;

import feed.daos.ArticleDao;
import feed.daos.FeedDao;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/feeds")
@Service
public class FeedService {

    @Inject
    private FeedDao feedDao;

    @Inject
    private ArticleDao articleDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFeed(String payload) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(payload);
        String name = (String) jsonObject.get("name");
        String description = (String) jsonObject.get("description");
        Long id = feedDao.createFeed(name, description);
        JSONObject response = new JSONObject();
        response.put("id", id);
        return Response.status(Response.Status.OK)
                .entity(response.toJSONString())
                .build();
    }

    @POST
    @Path("/{feedId}/add-article")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addArticle(@PathParam("feedId") long feedId, String payload) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(payload);
        String title = (String) jsonObject.get("title");
        String url = (String) jsonObject.get("url");
        Long articleId = articleDao.createArticle(feedId, title, url);
        JSONObject response = new JSONObject();
        response.put("id", articleId);
        return Response.status(Response.Status.OK)
                .entity(response.toJSONString())
                .build();
    }
}
