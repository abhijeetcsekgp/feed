package feed.entities;

import lombok.Data;

@Data
public class Feed {
    private final long id;
    private final String title;
    private final String description;
}
