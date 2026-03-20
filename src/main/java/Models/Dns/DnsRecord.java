package Models.Dns;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DnsRecord {
    @JsonProperty(value = ".id",access =  JsonProperty.Access.READ_ONLY)
    public String id;
    public String address;
    public String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String disabled;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String dynamic;
    public Integer ttl;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String type;

    @Override
    public String toString() {
        return "DnsRecord{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", disabled='" + disabled + '\'' +
                ", dynamic='" + dynamic + '\'' +
                ", ttl=" + ttl +
                ", type='" + type + '\'' +
                '}';
    }
}



