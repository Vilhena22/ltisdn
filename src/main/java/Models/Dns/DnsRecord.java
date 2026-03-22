package Models.Dns;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DnsRecord {
    @JsonProperty(value = ".id")
    public String id;
    public String address;
    public String name;
    public String disabled;
    public String dynamic;
    public String ttl;
    public String type;
}



