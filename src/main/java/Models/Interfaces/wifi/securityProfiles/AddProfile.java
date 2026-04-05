package Models.Interfaces.wifi.securityProfiles;

import com.google.gson.annotations.SerializedName;

public class AddProfile {

    @SerializedName(".id")
    public String id;

    public String name;

    public boolean disabled;
}
