package Models.Dhcp.Leases;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DhcpLease {
    @JsonProperty(".id")
    public String id;
    @JsonProperty("active-address")
    public String activeAddress;
    @JsonProperty("active-client-id")
    public String activeClientId;
    @JsonProperty("active-mac-address")
    public String activeMacAddress;
    @JsonProperty("active-server")
    public String activeServer;
    public String address;
    @JsonProperty("address-lists")
    public String addressLists;
    public String age;
    public String blocked;
    @JsonProperty("class-id")
    public String classId;

    @JsonProperty("client-id")
    public String clientId;

    @JsonProperty("dhcp-option")
    public String dhcpOption;

    public boolean disabled;
    public String dynamic;
    @JsonProperty("expires-after")
    public String expiresAfter;
    @JsonProperty("host-name")
    public String hostName;
    @JsonProperty("last-seen")
    public String lastSeen;
    @JsonProperty("mac-address")
    public String macAddress;
    public String radius;
    public String server;
    public String status;

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public String getServer() {
        return server;
    }

}
