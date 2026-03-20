package Models.System;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemResources {
    @JsonProperty("architecture-name")
    public String architectureName;
    @JsonProperty("board-name")
    public String boardName;
    @JsonProperty("build-time")
    public String buildTime;
    public String cpu;
    @JsonProperty("cpu-count")
    public String cpuCount;
    @JsonProperty("cpu-frequency")
    public String cpuFrequency;
    @JsonProperty("cpu-load")
    public String cpuLoad;
    @JsonProperty("factory-software")
    public String factorySoftware;
    @JsonProperty("free-hdd-space")
    public String freeHddSpace;
    @JsonProperty("free-memory")
    public String freeMemory;
    public String platform;
    @JsonProperty("total-hdd-space")
    public String totalHddSpace;
    @JsonProperty("total-memory")
    public String totalMemory;
    public String uptime;
    public String version;
    @JsonProperty("write-sect-since-reboot")
    public String writeSinceReboot;
    @JsonProperty("write-sect-total")
    public String writeSectTotal;
}
