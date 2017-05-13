package feed.entities.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FeedRequest {
    private final String name;
    private final String description;
}
