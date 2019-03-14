package org.jenkinsci.plugins.docker.swarm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DockerServerEndpointMixin {

    @JsonCreator
    public DockerServerEndpointMixin(
            @JsonProperty("uri") String uri,
            @JsonProperty("credentialsId") String credentialsId) {
    }
}
