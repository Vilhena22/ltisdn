package Models.Interfaces.bridge.ports;

import com.google.gson.annotations.SerializedName;

public class AddBridgePort {

    public String bridge;

    @SerializedName("interface")
    public String interfaceAtual;
}
