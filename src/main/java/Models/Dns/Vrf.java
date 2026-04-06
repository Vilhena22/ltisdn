package Models.Dns;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vrf {
    @JsonProperty(".id")
    public String id;
    public String name;
    public boolean builtin;
    public boolean disabled;
    public String interfaces;
}
