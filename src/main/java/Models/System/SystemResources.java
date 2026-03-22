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


    public String getArchitectureName() {
        return architectureName;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public String getCpu() {
        return cpu;
    }

    public String getCpuCount() {
        return cpuCount;
    }

    public String getCpuFrequency() {
        return cpuFrequency;
    }

    public String getCpuLoad() {
        return cpuLoad;
    }

    public String getFactorySoftware() {
        return factorySoftware;
    }

    public String getFreeHddSpace() {
        return freeHddSpace;
    }

    public String getFreeMemory() {
        return freeMemory;
    }

    public String getPlatform() {
        return platform;
    }

    public String getTotalHddSpace() {
        return totalHddSpace;
    }

    public String getTotalMemory() {
        return totalMemory;
    }

    public String getUptime() {
        return uptime;
    }

    public String getVersion() {
        return version;
    }

    public String getWriteSinceReboot() {
        return writeSinceReboot;
    }

    public String getWriteSectTotal() {
        return writeSectTotal;
    }
}
