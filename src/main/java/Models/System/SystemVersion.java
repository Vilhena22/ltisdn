package Models.System;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)

public class SystemVersion {
    public String channel;
    @JsonProperty("installed-version")
    public String installedVersion;
    @JsonProperty("latest-version")
    public String latestVersion;
    public String status;
}
