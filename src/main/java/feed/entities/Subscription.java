package feed.entities;

import lombok.Data;

@Data
public class Subscription {
    private final long userId;
    private final long feedId;
}
