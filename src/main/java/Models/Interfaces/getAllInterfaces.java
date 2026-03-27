package Models.Interfaces;

import com.google.gson.annotations.SerializedName;

public class getAllInterfaces {

    @SerializedName(".id")
    public String id;

    public String name;

    public String running;

    public boolean disabled;
    @SerializedName("tx-byte")
    public long txByte;
    @SerializedName("tx-queue-drop")
    public long txQueueDrop;

    @SerializedName("tx-drop")
    public long txDrop;
    @SerializedName("rx-byte")
    public long rxByte;
    @SerializedName("rx-drop")
    public long rxDrop;

}
