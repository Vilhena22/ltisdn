package Models.Wireguard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceWG {
    @JsonProperty(value = ".id")
    public String id;
    public boolean disabled;
    @JsonProperty(value = "listen-port")
    public int listenPort;
    @JsonIgnore
    public int mtu;
    public String name;
    @JsonProperty(value = "private-key")
    public String privateKey;
    @JsonProperty(value = "public-key")
    public String publicKey;
    @JsonIgnore
    public boolean running;
}
