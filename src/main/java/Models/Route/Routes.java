package Models.Route;

import com.google.gson.annotations.SerializedName;

public class Routes {

    @SerializedName(".id")
    public String id;

    public String gateway;

    public boolean disabled;

    @SerializedName("static")
    public String _static;

    @SerializedName("dst-address")
    public String dst_address;

    @SerializedName("routing-table")
    public String routing_table;

}