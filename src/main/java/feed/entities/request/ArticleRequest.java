package feed.entities.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This is the java equivalent of the json payload that is received to add an article to a feed.
 * @see feed.serialization.GsonJerseyProvider
 */
@RequiredArgsConstructor
@Getter
public class ArticleRequest {
    private final String title;
    private final String url;
}