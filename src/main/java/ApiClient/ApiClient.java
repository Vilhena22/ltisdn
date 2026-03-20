package ApiClient;

import Models.*;
import Models.Dns.Dns;
import Models.Dns.DnsCache;
import Models.Dns.DnsRecord;
import Models.System.SystemResources;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

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
        String json = sendRequestGet("/ip/dns/cache");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DnsCache.class)
        );
    }

    public String postClearDnsCache() throws Exception {
        return sendRequestPost("/ip/dns/cache/flush","{}");
    }

    public List<DnsRecord> getDnsRecord() throws Exception {
        String json = sendRequestGet("/ip/dns/static");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DnsRecord.class)
        );
    }

    public String postDnsRecord() throws Exception {

        //Cria o objeto para enviar
        ObjectMapper mapper = new ObjectMapper();
        DnsRecord dnsRecord = new DnsRecord();
        dnsRecord.name = "app.intranet";
        dnsRecord.address = "10.0.0.20";
        dnsRecord.ttl = 3600;
        dnsRecord.disabled = "yes";
        dnsRecord.type = "A";

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //Converte o objeto em Json
        String jsonString = mapper.writeValueAsString(dnsRecord);

        //Realiza o post
        String status = sendRequestPost("/ip/dns/static/add",  jsonString);
        /*if (status == 200) {
            return status;
        }
        return 0;*/
        return status;
    }

    public Integer deleteDnsRecord(Integer id) throws Exception {
        return sendRequestDelete("/ip/dns/static/*"+id.toString());
    }


    public Dns getDnsConfig() throws Exception {
        String json = sendRequestGet("/ip/dns");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                Dns.class
        );
    }

    public String postDnsConfig() throws Exception {

        //Cria o objeto para enviar
        ObjectMapper mapper = new ObjectMapper();
        Dns dns = new Dns();
        dns.addressListExtraTime ="0s";
        dns.allowRemoteRequests = "true";
        dns.cacheMaxTtl= "1w";
        dns.cacheSize= "1024";
        dns.verifyDohCert = "no";
        dns.dohMaxConcurrentQueries =50;
        dns.dohMaxServerConnections = 5;
        dns.dohTimeout = "5s";
        dns.maxConcurrentQueries ="100";
        dns.maxConcurrentTcpSessions ="20";
        dns.maxUdpPacketSize ="4096";
        dns.queryServerTimeout = "2s";
        dns.queryTotalTimeout = "10s";
        dns.vrf = "main";

        //Converte o objeto em Json
        String jsonString = mapper.writeValueAsString(dns);

        //Realiza o post
        String status = sendRequestPost("/ip/dns/set",  jsonString);

        return status;

    }


    public SystemResources getSystemResources() throws Exception {
        String json = sendRequestGet("/system/resource");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                SystemResources.class
        );
    }




    private String sendRequestGet(String endpoint) throws Exception {
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

    private String sendRequestPost(String endpoint,String jsonBody) throws Exception {
        URL urlObj = new URL(url + endpoint);
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();

        conn.setSSLSocketFactory(getInsecureSSLContext().getSocketFactory());
        conn.setHostnameVerifier((hostname, session) -> true);
        conn.setRequestProperty("Content-Type", "application/json");
        String credentials = Base64.getEncoder()
                .encodeToString((user + ":" + pass).getBytes());
        conn.setRequestProperty("Authorization", "Basic " + credentials);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        //Converte o Json para enviar
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        int status = conn.getResponseCode();

        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return sb.toString();



        //Devolve 200 OK ou o codigo Erro
        //return conn.getResponseCode();
    }

    private Integer sendRequestDelete(String endpoint) throws Exception {
        URL urlObj = new URL(url + endpoint);
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
        conn.setSSLSocketFactory(getInsecureSSLContext().getSocketFactory());
        conn.setHostnameVerifier((hostname, session) -> true);
        String credentials = Base64.getEncoder()
                .encodeToString((user + ":" + pass).getBytes());
        conn.setRequestProperty("Authorization", "Basic " + credentials);
        conn.setRequestMethod("DELETE");
        return conn.getResponseCode();
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
        String endpoint = sendRequestGet("/ip/address");

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
        String endpoint = sendRequestGet("/ip/address/set");

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
        String endpoint = sendRequestGet("/ip/address/"+id);

        System.out.println("Ip apagado!\n");
        return endpoint;
    }
}