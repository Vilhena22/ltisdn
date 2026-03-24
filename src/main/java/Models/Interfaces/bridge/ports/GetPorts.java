package Models.Interfaces.bridge.ports;

import com.google.gson.annotations.SerializedName;

public class GetPorts {

    @SerializedName(".id")
    public String id;

    public String bridge;

    @SerializedName("interface")
    public String interfaceAtual;

    public String status;

    public boolean disabled;
}
