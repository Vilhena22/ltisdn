package Models.Wireguard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Peer {
    @JsonProperty(value = ".id")
    public String id;
    @JsonProperty(value = "allowed-address")
    public String allowedAddress;
    @JsonProperty(value = "client-endpoint")
    public String clientEndpoint;
    @JsonProperty(value = "current-endpoint-address")
    @JsonIgnore
    public String currentEndpointAddress;
    @JsonProperty(value = "endpoint-address")
    public String endpointAddress;
    @JsonProperty(value = "endpoint-port")
    public int endpointPort;
    @JsonProperty(value = "preshared-key")
    public String presharedKey;
    @JsonProperty(value = "private-key")
    public String privateKey;
    @JsonProperty(value = "public-key")
    public String publicKey;
    public boolean disabled;
    @JsonIgnore
    public boolean dynamic;
    @JsonProperty(value = "interface")
    public String inter;
    public String name;
    @JsonIgnore
    public int rx;
    @JsonIgnore
    public int tx;

}
