package Models.Interfaces.wifi.interfaces;

import com.google.gson.annotations.SerializedName;

public class GetInterfaces {

    @SerializedName(".id")
    public String id;

    public String arp;

    public String band;

    @SerializedName("bridge-mode")
    public String bridge_mode;

    @SerializedName("channel-width")
    public String channel_width;

    public boolean disabled;

    @SerializedName("mac-address")
    public String mac_address;
}
