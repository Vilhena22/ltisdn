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
        String state = "not running";
        if (running.equals("true")){
            state = "running";
        }

        return String.format("Interface: %-20s Status: %s", name, state);
    }
}
