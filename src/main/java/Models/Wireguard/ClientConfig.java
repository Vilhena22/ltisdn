package Models.Wireguard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ClientConfig {
    public String conf;
    public String qr;

    public String getConf() {
        return conf;
    }

    public String getQr() {
        return qr;
    }
}
