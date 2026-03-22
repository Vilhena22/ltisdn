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

    public String disabled;
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

    public String getActiveAddress() {
        return activeAddress;
    }

    public String getActiveClientId() {
        return activeClientId;
    }

    public String getActiveMacAddress() {
        return activeMacAddress;
    }

    public String getActiveServer() {
        return activeServer;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressLists() {
        return addressLists;
    }

    public String getAge() {
        return age;
    }

    public String getBlocked() {
        return blocked;
    }

    public String getClassId() {
        return classId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDhcpOption() {
        return dhcpOption;
    }

    public String getDisabled() {
        return disabled;
    }

    public String getDynamic() {
        return dynamic;
    }

    public String getExpiresAfter() {
        return expiresAfter;
    }

    public String getHostName() {
        return hostName;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getRadius() {
        return radius;
    }

    public String getServer() {
        return server;
    }

    public String getStatus() {
        return status;
    }
}
