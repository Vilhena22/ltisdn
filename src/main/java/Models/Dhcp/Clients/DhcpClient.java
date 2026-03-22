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
    public String disabled;
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

    public String getAbout() {
        return about;
    }

    public String getId() {
        return id;
    }

    public String getAddDefaultRoute() {
        return addDefaultRoute;
    }

    public String getAllowReconfigure() {
        return allowReconfigure;
    }

    public String getCheckGateway() {
        return checkGateway;
    }

    public String getComment() {
        return comment;
    }

    public String getDefaultRouteDistance() {
        return defaultRouteDistance;
    }

    public String getDefaultRouteTables() {
        return defaultRouteTables;
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

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getInvalid() {
        return invalid;
    }

    public String getStatus() {
        return status;
    }

    public String getUseBroadcast() {
        return useBroadcast;
    }

    public String getUsePeerDns() {
        return usePeerDns;
    }

    public String getUsePeerNtp() {
        return usePeerNtp;
    }
}
