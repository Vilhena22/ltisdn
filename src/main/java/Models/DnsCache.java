package Models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class DnsCache {
    @JsonProperty(".id")
    public String id;
    @JsonProperty("data")
    public String data;
    @JsonProperty("name")
    public String name;
    @JsonProperty("static")
    public String _static;
    public String ttl;
    public String type;

}
