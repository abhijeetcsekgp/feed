package feed.entities;

import lombok.Data;

@Data
public class FeedArticle {
    private final long feedId;
    private final long articleId;
}
