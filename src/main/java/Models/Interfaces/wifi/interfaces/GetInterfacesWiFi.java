package Models.Interfaces.wifi.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

public class GetInterfacesWiFi {

    @SerializedName(".id")
    public String id;

    public String name;
    @JsonIgnore
    public String address;

    @SerializedName("master-interface")
    public String master_interface;

    public String mode;

    public String ssid;

    public String band;

    @SerializedName("channel-width")
    public String channel_width;

    public boolean disabled;

}
