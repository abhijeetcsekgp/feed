package feed.entities.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ArticleRequest {
    private final String title;
    private final String url;
}