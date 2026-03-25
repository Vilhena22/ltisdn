package Models.Address;

import com.google.gson.annotations.SerializedName;

public class GetAddress {

    @SerializedName(".id")
    public String id;

    @SerializedName("actual-interface")
    public String actual_interface;

    public boolean disabled;

    public String address;

}