package ApiClient;

import Models.Dns;
import Models.DnsCache;
import Models.DnsRecord;
import Models.SystemResources;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;

import static ApiClient.MikrotikConfig.*;

public class ApiClient {

    private final String url;
    private final String user;
    private final String pass;

    public ApiClient() {
        this.url = baseUrl;
        this.user = username;
        this.pass = password;
    }


    public List<DnsCache> getCacheDns() throws Exception {
        String json = sendRequest("/ip/dns/cache");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DnsCache.class)
        );
    }

    public List<DnsRecord> getDnsRecord() throws Exception {
        String json = sendRequest("/ip/dns/static");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DnsRecord.class)
        );
    }


    public Dns  getDnsConfig() throws Exception {
        String json = sendRequest("/ip/dns");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                Dns.class
        );
    }


    public SystemResources getSystemResources() throws Exception {
        String json = sendRequest("/system/resource");
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(
                json,
                SystemResources.class
        );
    }




    private String sendRequest(String endpoint) throws Exception {
        URL urlObj = new URL(url + endpoint);
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();

        conn.setSSLSocketFactory(getInsecureSSLContext().getSocketFactory());
        conn.setHostnameVerifier((hostname, session) -> true);

        String credentials = Base64.getEncoder()
                .encodeToString((user + ":" + pass).getBytes());
        conn.setRequestProperty("Authorization", "Basic " + credentials);
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return sb.toString();
    }

    private static SSLContext getInsecureSSLContext() {
        try {
            TrustManager[] trustAll = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAll, new java.security.SecureRandom());
            return sc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}