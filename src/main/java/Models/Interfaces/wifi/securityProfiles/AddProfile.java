package Models.Interfaces.wifi.securityProfiles;

import com.google.gson.annotations.SerializedName;

public class AddProfile {

    public String name;

    @SerializedName("authentication-types")
    public String authentication_types;

    public String mode;

    @SerializedName("wpa2-pre-shared-key")
    public String wpa2_pre_shared_key;

    @SerializedName("unicast-ciphers")
    public String unicast_ciphers;

    @SerializedName("group-ciphers")
    public String group_ciphers;
}
