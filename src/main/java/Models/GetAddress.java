package Models;

import com.google.gson.annotations.SerializedName;

public class GetAddress {

    @SerializedName(".id")
    public String id;

    @SerializedName("actual-interface")
    public String actual_interface;

    @SerializedName("disabled")
    public boolean running;

    public String address;
}