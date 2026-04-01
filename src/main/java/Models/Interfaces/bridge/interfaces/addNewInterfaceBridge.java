package Models.Interfaces.bridge.interfaces;

import com.google.gson.annotations.SerializedName;

public class addNewInterfaceBridge {

    public String name;

    public boolean disabled;

    @SerializedName(".id")
    public String id;

    public boolean running;
}
