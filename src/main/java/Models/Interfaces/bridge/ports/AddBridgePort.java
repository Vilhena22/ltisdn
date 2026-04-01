package Models.Interfaces.bridge.ports;

import com.google.gson.annotations.SerializedName;

public class AddBridgePort {

    public String bridge;

    @SerializedName("interface")
    public String interfaceAtual;

    @SerializedName(".id")
    public String id;

    public boolean disabled;
}
