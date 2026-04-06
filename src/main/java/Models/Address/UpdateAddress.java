package Models.Address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAddress {

    @SerializedName(".id")
    public String id;
    public String address;

}