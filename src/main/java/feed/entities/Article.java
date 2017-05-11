package feed.entities;

import lombok.Data;

@Data
public class Article {
    private final long id;
    private final String title;
    private final String content;
}
