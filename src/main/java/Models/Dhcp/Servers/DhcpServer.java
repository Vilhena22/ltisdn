package Models.Dhcp.Servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DhcpServer {
    @JsonProperty(value = ".id")
    public String id;
    @JsonProperty(value = "address-lists")
    public String addressLists;
    @JsonProperty(value = "address-pool")
    public String addressPool;
    public String disabled;
    public String dynamic;
    @JsonProperty(value = "interface")
    public String interfaceName;
    public String invalid;
    @JsonProperty(value = "lease-script")
    public String leaseScript;
    public String script;
    public String name;
    @JsonProperty(value = "use-radius")
    public String useRadius;
    @JsonProperty(value = "use-reconfigure")
    public String useReconfigure;


}
