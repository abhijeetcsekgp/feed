package feed.config;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationResourceConfig extends ResourceConfig {

    public ApplicationResourceConfig() {
        //registers resources and providers using package scanning
        packages("feed");
    }
}
