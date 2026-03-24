package Models.Interfaces;

import com.google.gson.annotations.SerializedName;

public class stateInterface extends getAllInterfaces{

    @SerializedName(".id")
    public String id;

    public boolean disabled;
}