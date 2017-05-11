package feed.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class ApplicationResourceConfig extends ResourceConfig {

    public ApplicationResourceConfig() {
        register(RolesAllowedDynamicFeature.class);
        register(MultiPartFeature.class);
        packages("feed");
    }
}
