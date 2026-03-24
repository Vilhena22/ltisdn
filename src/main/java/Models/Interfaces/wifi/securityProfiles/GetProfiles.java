package Models.Interfaces.wifi.securityProfiles;

import com.google.gson.annotations.SerializedName;

public class GetProfiles {

    @SerializedName(".id")
    public String id;

    public boolean disabled;

    public String name;
}
