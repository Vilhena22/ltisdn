package Models.Interfaces.wifi.interfaces;

import com.google.gson.annotations.SerializedName;

public class AddInterface {

    public String name;

    @SerializedName("master-interface")
    public String master_interface;

    public String mode;

    public String ssid;

    public String band;

    @SerializedName("channel-width")
    public String channel_width;

    public boolean disabled;
}
