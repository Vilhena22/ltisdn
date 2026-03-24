package ApiClient;

import Models.Address.GetAddress;
import Models.Dhcp.Clients.DhcpClient;
import Models.Dhcp.Leases.DhcpLease;
import Models.Dhcp.Pools.DhcpPool;
import Models.Dhcp.Servers.DhcpServer;
import Models.Dns.Dns;
import Models.Dns.DnsCache;
import Models.Dns.DnsRecord;
import Models.Interfaces.bridge.interfaces.addNewInterfaceBridge;
import Models.Interfaces.bridge.interfaces.getInterfaceBridge;
import Models.Interfaces.bridge.ports.AddBridgePort;
import Models.Interfaces.bridge.ports.GetPorts;
import Models.Interfaces.getAllInterfaces;
import Models.Interfaces.wifi.interfaces.AddInterface;
import Models.Interfaces.wifi.interfaces.GetInterfaces;
import Models.Interfaces.wifi.securityProfiles.AddProfile;
import Models.Interfaces.wifi.securityProfiles.GetProfiles;
import Models.System.SystemResources;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.*;
import java.io.*;
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

    //DNS Cache
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

    //DNS Records
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
        dnsRecord.ttl = "3600";
        dnsRecord.disabled = "yes";
        dnsRecord.type = "A";

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //Converte o objeto em Json
        String jsonString = mapper.writeValueAsString(dnsRecord);
        //Realiza o post
        return sendRequestPost("/ip/dns/static/add",  jsonString);

    }

    public Integer deleteDnsRecord(Integer id) throws Exception {
        return sendRequestDelete("/ip/dns/static/*"+id.toString());
    }

    //DNS Configuration
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

    //DHCP
        //Pools
    public List<DhcpPool> getDhcpPools() throws Exception {
        String json = sendRequestGet("/ip/dhcp-server/network");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DhcpPool.class));
    }

    public String postDhcpPool() throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        DhcpPool dhcpPool = new DhcpPool();
        dhcpPool.address ="192.168.100.0/24";
        dhcpPool.dnsServer = "192.168.100.1";
        dhcpPool.gateway ="192.168.100.100";

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpPool);


        return sendRequestPost("/ip/dhcp-server/network/add",jsonString);
    }

    public Integer deleteDhcpPool(Integer id) throws Exception {
        return sendRequestDelete("/ip/dhcp-server/network/*"+ id.toString());
    }

        //Leases
    public List<DhcpLease> getDhcpLeases() throws Exception {
        String json = sendRequestGet("/ip/dhcp-server/lease");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DhcpLease.class));
    }


    public String postDhcpLease() throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        DhcpLease dhcpLease = new DhcpLease();

        dhcpLease.address ="192.168.100.100";
        dhcpLease.clientId = "*9";
        dhcpLease.server ="dhcp1";

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpLease);


        return sendRequestPost("/ip/dhcp-server/lease/add",jsonString);
    }

    public Integer deleteDhcpLease(Integer id) throws Exception {
        return sendRequestDelete("/ip/dhcp-server/lease/*"+ id.toString());
    }


    //clients
    public List<DhcpClient> getDhcpClients() throws Exception {
        String json = sendRequestGet("/ip/dhcp-client");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DhcpClient.class));
    }


    public String postDhcpClients() throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        DhcpClient dhcpClient = new DhcpClient();

        dhcpClient.interfaceName = "wlan2";
        dhcpClient.addDefaultRoute = "yes";
        dhcpClient.usePeerDns = "yes";
        dhcpClient.usePeerNtp = "true";

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpClient);


        return sendRequestPost("/ip/dhcp-client/add",jsonString);
    }

    public Integer deleteDhcpClients(String id) throws Exception {
        return sendRequestDelete("/ip/dhcp-client/*"+ id);
    }


    public String postDesActivateClient(String id, Boolean state) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DhcpClient dhcpClient = new DhcpClient();
        dhcpClient.id = "*"+id;
        dhcpClient.disabled = state.toString();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpClient);
        return sendRequestPost("/ip/dhcp-client/set",jsonString);
    }


    //Server
    public List<DhcpServer> getDhcpServer() throws Exception {
        String json = sendRequestGet("/ip/dhcp-server");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DhcpServer.class));
    }


    public String postDhcpServer() throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        DhcpServer dhcpServer = new DhcpServer();

        dhcpServer.addressPool = "dhcp_pool0";
        dhcpServer.disabled = "true";
        dhcpServer.interfaceName = "wlan2";
        dhcpServer.name = "dhcpTeste";

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpServer);


        return sendRequestPost("/ip/dhcp-server/add",jsonString);
    }

    public Integer deleteDhcpServer(String id) throws Exception {
        return sendRequestDelete("/ip/dhcp-server/*"+ id);
    }





    public String postDesActivateServer(String id, Boolean state) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DhcpServer dhcpServer = new DhcpServer();
        dhcpServer.id = "*"+id;
        dhcpServer.disabled = state.toString();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpServer);
        return sendRequestPost("/ip/dhcp-server/set",jsonString);
    }



    private String sendRequestGet(String endpoint) throws Exception {
        HttpsURLConnection conn = getHttpsURLConnection(endpoint);
        conn.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return sb.toString();
    }

    private String sendRequestPost(String endpoint,String jsonBody) throws Exception {
        HttpsURLConnection conn = getHttpsURLConnection(endpoint);

        conn.setRequestProperty("Content-Type", "application/json");
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
    }

    private String sendRequestPut(String endpoint,String jsonBody) throws Exception {
        HttpsURLConnection conn = getHttpsURLConnection(endpoint);

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("PUT");
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
    }


    private Integer sendRequestDelete(String endpoint) throws Exception {
        HttpsURLConnection conn = getHttpsURLConnection(endpoint);
        conn.setRequestMethod("DELETE");
        return conn.getResponseCode();
    }

    private HttpsURLConnection getHttpsURLConnection(String endpoint) throws IOException {
        URL urlObj = new URL(url + endpoint);
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();

        conn.setSSLSocketFactory(getInsecureSSLContext().getSocketFactory());
        conn.setHostnameVerifier((hostname, session) -> true);
        String credentials = Base64.getEncoder()
                .encodeToString((user + ":" + pass).getBytes());
        conn.setRequestProperty("Authorization", "Basic " + credentials);
        return conn;
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

        //converter objeto para json
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
    public Integer DeleteAddress(String id) throws Exception {
        Integer endpointResult = sendRequestDelete("/ip/address/"+id);

        if (endpointResult == 200){
            System.out.println("Ip apagado!\n");
            return endpointResult;
        }
        return 500;
    }

    //POST: desativar/ativar ip
    public String EstadoIPAddress(String id, boolean state) throws Exception{
        //troca o estado do ip
        boolean NovoEstado = !state;

        // cria o JSON que a Mikrotik espera
        String payload = "{disabled: " + NovoEstado + "}";

        //envia o post com os novos dados
        String endpoint = sendRequestPost("/ip/adddres/" + id, payload);

        return endpoint;
    }

    /// INTERFACES

    //WiFi
    //Interfaces

    public String AddInterfaceWifi() throws Exception {

        AddInterface novaInterface = new AddInterface();
        novaInterface.name = "wlan10";
        novaInterface.master_interface = "wlan1";
        novaInterface.mode = "ap-bridge";
        novaInterface.ssid = "Rede_PublicaTESTE";
        novaInterface.band = "2ghz-b/g/n";
        novaInterface.channel_width = "20mhz";
        novaInterface.disabled = true;

        //converter objeto para json
        Gson gson = new Gson();
        String payload = gson.toJson(novaInterface);

        String endpoint = sendRequestPost("/interface/wireless/add", payload);

        return endpoint;
    }

    public List<GetInterfaces> GetInterfacesWifi() throws Exception {
        String endpoint = sendRequestGet("/interface/wireless");

        //converte json em objeto
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GetInterfaces>>(){}.getType();

        return gson.fromJson(endpoint, listType);
    }

    public Integer DeleteInterfaceWifi(String id) throws Exception {
        return sendRequestDelete("/interface/wifi/"+id);
    }

    // Security Profiles

    public String AddProfile() throws Exception {

        AddProfile novoProfile = new AddProfile();
        novoProfile.name = "wlan10";
        novoProfile.authentication_types = "wlan1";
        novoProfile.mode = "ap-bridge";
        novoProfile.wpa2_pre_shared_key = "Rede_PublicaTESTE";
        novoProfile.unicast_ciphers = "2ghz-b/g/n";
        novoProfile.group_ciphers = "20mhz";

        //converter objeto para json
        Gson gson = new Gson();
        String payload = gson.toJson(novoProfile);

        String endpoint = sendRequestPost("/interface/wifi/security/", payload);
        return endpoint;
    }

    public List<GetProfiles> GetProfiles() throws Exception {
        String endpoint = sendRequestGet("/interface/wifi/security");

        //converte json em objeto
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GetProfiles>>(){}.getType();

        return gson.fromJson(endpoint, listType);
    }

    public Integer DeleteSecurityProfile(String id) throws Exception {
        return sendRequestDelete("/interface/wifi/security/" + id);
    }

    // BRIDGE
    // ports

    public List<GetPorts> getBridgePorts() throws Exception {
        String endpoint = sendRequestGet("/interface/bridge/port");

        //converte json em objeto
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GetPorts>>(){}.getType();

        return gson.fromJson(endpoint, listType);
    }

    public Integer deleteBridgePort(String id) throws Exception {
        return sendRequestDelete("/interface/bridge/port/" + id);
    }

    public String addBridgePort() throws Exception {

        AddBridgePort novaBridge = new AddBridgePort();
        novaBridge.interfaceAtual = "oioi";
        novaBridge.bridge = "bem vindo";

        Gson gson = new Gson();
        String payload = gson.toJson(novaBridge);

        return sendRequestPost("/interface/wifi/security/", payload);
    }

    // interfaces

    public List<getInterfaceBridge> getBridgeInterfaces() throws Exception {
        String endpoint = sendRequestGet("/interface/bridge");

        //converte json em objeto
        Gson gson = new Gson();
        Type listType = new TypeToken<List<getInterfaceBridge>>(){}.getType();

        return gson.fromJson(endpoint, listType);
    }

    public Integer deleteInterfaceBridge(String id) throws Exception {
        return sendRequestDelete("/interface/bridge/" + id);
    }

    public String addInterfaceBridge() throws Exception {

        addNewInterfaceBridge novaInterfaceBridge = new addNewInterfaceBridge();
        novaInterfaceBridge.name = "oioi";
        novaInterfaceBridge.disabled = true;

        Gson gson = new Gson();
        String payload = gson.toJson(novaInterfaceBridge);

        return sendRequestPost("/interface/bridge/add", payload);
    }

    // geral

    public List<getAllInterfaces> getAllInterfaces() throws Exception {
        String endpoint = sendRequestGet("/interface");

        //converte json em objeto
        Gson gson = new Gson();
        Type listType = new TypeToken<List<getAllInterfaces>>(){}.getType();

        return gson.fromJson(endpoint, listType);
    }

    public Integer deleteInterface(String id) throws Exception {
        return sendRequestDelete("/interface/" + id);
    }

    public String estadoInterface(String id, boolean state) throws Exception {
        //troca o estado da interface
        boolean NovoEstado = !state;

        // cria o JSON que a Mikrotik espera
        String payload = "{disabled: " + NovoEstado + "}";

        //envia o post com os novos dados
        String endpoint = sendRequestPost("/interface/" + id, payload);

        return endpoint;
    }




























































}