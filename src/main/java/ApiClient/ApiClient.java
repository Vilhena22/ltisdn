package ApiClient;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Base64;

import Models.GetAddress;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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

    // Para metodos com GET
    private String sendRequest(String endpoint,String requestType) throws Exception {
        URL urlObj = new URL(url + endpoint);
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();

        conn.setSSLSocketFactory(getInsecureSSLContext().getSocketFactory());
        conn.setHostnameVerifier((hostname, session) -> true);

        String credentials = Base64.getEncoder()
                .encodeToString((user + ":" + pass).getBytes());
        conn.setRequestProperty("Authorization", "Basic " + credentials);

        if (requestType == "GET") {
            conn.setRequestMethod("GET");
        }else if(requestType == "POST"){
            System.out.println("Post ok");
        }else if(requestType == "DELETE"){
            System.out.println("Delete ok");
        }else if(requestType == "PUT"){
            System.out.println("PUT ok");
        }else {
            System.out.println("PATCH ok");
        }

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

    /// ADDRESSES

    //GET: vai buscar todos os ips
    public String GetAddress() throws Exception {
        String endpoint = sendRequest("/ip/address", "GET");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<GetAddress>>(){}.getType();
        List<GetAddress> addresses = gson.fromJson(endpoint, listType);

        // aceder aos dados
        for (GetAddress address : addresses) {
            System.out.println("id: " + address.id + "\nactual-interface: " + address.actual_interface + "\nip: " + address.address + "\nrunning: " + address.running + "\n");
        }
        return endpoint;
    }

    //POST: atualizar um ip
    public String UpdateAddress(String id, String newAddress) throws Exception {
        String endpoint = sendRequest("/ip/address/set", "POST");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<GetAddress>>(){}.getType();
        List<GetAddress> addresses = gson.fromJson(endpoint, listType);

        // print da atualizacao
        for (GetAddress address : addresses) {
            System.out.println("id: " + address.id + "\nnovo ip: " + address.address + "\n");
        }
        return endpoint;
    }

    //DELETE: apagar ip
    public String DeleteAddress(String id) throws Exception {
        String endpoint = sendRequest("/ip/address/"+id, "DELETE");

        System.out.println("Ip apagado!\n");
        return endpoint;
    }
}