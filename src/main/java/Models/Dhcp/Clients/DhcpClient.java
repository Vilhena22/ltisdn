package Models.Dhcp.Clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DhcpClient {
    @JsonProperty(value = ".about")
    public String about;
    @JsonProperty(value = ".id")
    public String id;
    @JsonProperty(value = "add-default-route")
    public String addDefaultRoute;
    @JsonProperty(value = "allow-reconfigure")
    public String allowReconfigure;
    @JsonProperty(value = "check-gateway")
    public String checkGateway;
    public String comment;
    @JsonProperty(value = "default-route-distance")
    public String defaultRouteDistance;
    @JsonProperty(value = "default-route-tables")
    public String defaultRouteTables;
    @JsonProperty(value = "dhcp-option")
    public String dhcpOption;
    public boolean disabled;
    public String dynamic;
    @JsonProperty(value = "interface")
    public String interfaceName;
    public String invalid;
    public String status;
    @JsonProperty(value = "use-broadcast")
    public String useBroadcast;
    @JsonProperty(value = "use-peer-dns")
    public String usePeerDns;
    @JsonProperty(value = "use-peer-ntp")
    public String usePeerNtp;

    public String getId() {
        return id;
    }

    public boolean getDisabled() {
        return disabled;
    }
}
