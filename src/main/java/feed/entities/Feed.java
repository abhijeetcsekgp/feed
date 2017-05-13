package feed.entities;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class Feed {
    @Expose
    private final long id;
    @Expose
    private final String title;
    @Expose
    private final String description;
}
