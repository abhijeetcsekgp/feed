package feed.entities.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This is the java equivalent of the json payload that is received to create a feed
 * @see feed.serialization.GsonJerseyProvider
 */
@RequiredArgsConstructor
@Getter
public class FeedRequest {
    private final String name;
    private final String description;
}
