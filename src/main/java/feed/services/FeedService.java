package feed.services;

import feed.daos.ArticleDao;
import feed.daos.FeedDao;
import feed.entities.Article;
import feed.entities.Feed;
import feed.entities.request.ArticleRequest;
import feed.entities.request.FeedRequest;
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
    public Response createFeed(FeedRequest feedRequest) throws ParseException {
        Feed feed = feedDao.createFeed(feedRequest.getName(), feedRequest.getDescription());
        return Response.status(Response.Status.OK)
                .entity(feed)
                .build();
    }

    @POST
    @Path("/{feedId}/add-article")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addArticle(@PathParam("feedId") long feedId, ArticleRequest articleRequest) throws ParseException {
        Article article = articleDao.createArticle(feedId, articleRequest.getTitle(), articleRequest.getUrl());
        return Response.status(Response.Status.OK)
                .entity(article)
                .build();
    }
}
