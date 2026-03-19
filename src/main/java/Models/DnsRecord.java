package Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DnsRecord {
    @JsonProperty(".id")
    public String id;
    public String address;
    public String name;
    public String disabled;
    public String dynamic;
    public String ttl;
    public String type;

}
