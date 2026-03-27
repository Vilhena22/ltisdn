package Models.Dhcp.Networks;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DhcpNetwork {
    @JsonProperty(value = ".id")
    public String id;
    public String address;
    @JsonProperty(value = "caps-manager")
    public String capsManager;
    @JsonProperty(value = "dhcp-option")
    public String dhcpOption;
    @JsonProperty(value = "dns-server")
    public String dnsServer;
    public String dynamic;
    public String gateway;
    @JsonProperty(value = "ntp-server")
    public String ntpServer;
    @JsonProperty(value = "wins-server")
    public String winsServer;
}
