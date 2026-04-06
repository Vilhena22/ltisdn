package Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    public int error;
    public String detail;
    public String message;
    public String ret;

    @Override
    public String toString() {
        return "ApiResponse{" +
                "error=" + error +
                ", detail='" + detail + '\'' +
                ", message='" + message + '\'' +
                ", ret='" + ret + '\'' +
                '}';
    }
}
