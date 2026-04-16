package Models.Interfaces;

import com.google.gson.annotations.SerializedName;

public class getAllInterfaces {

    @SerializedName(".id")
    public String id;

    public String name;

    public String running;
    public String address;

    public boolean disabled;

    @SerializedName("tx-drop")
    public long txDrop;
    @SerializedName("rx-drop")
    public long rxDrop;


    public String getAllInterfacesRunnung(){
        return "Interface: " + name + " Status: " + running;
    }
}
