package ApiClient;

import Models.Address.AddAddress;
import Models.Address.GetAddress;
import Models.Address.UpdateAddress;
import Models.ApiResponse;
import Models.Dhcp.Clients.DhcpClient;
import Models.Dhcp.Leases.DhcpLease;
import Models.Dhcp.Networks.DhcpNetwork;
import Models.Dhcp.Servers.DhcpServer;
import Models.Dns.Dns;
import Models.Dns.DnsCache;
import Models.Dns.DnsRecord;
import Models.Interfaces.bridge.interfaces.addNewInterfaceBridge;
import Models.Interfaces.bridge.interfaces.getInterfaceBridge;
import Models.Interfaces.bridge.ports.AddBridgePort;
import Models.Interfaces.bridge.ports.GetPorts;
import Models.Interfaces.getAllInterfaces;
import Models.Interfaces.wifi.interfaces.AddInterfaceWiFi;
import Models.Interfaces.wifi.interfaces.GetInterfacesWiFi;
import Models.Interfaces.wifi.securityProfiles.AddProfile;
import Models.Interfaces.wifi.securityProfiles.GetProfiles;
import Models.Route.Routes;
import Models.System.SystemResources;
import Models.System.SystemVersion;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
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

    public ApiClient(String username, String password,String host) {
        this.url = "https://"+ host + "/rest";
        this.user = username;
        this.pass = password;
    }

    public String checkCredentials() throws Exception {
        return sendRequestGet("/system/identity");
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

    public void postClearDnsCache() throws Exception {
        sendRequestPost("/ip/dns/cache/flush", "{}");
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


    public String postEditDnsRecord(DnsRecord dnsRecord) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //Converte o objeto em Json
        String jsonString = mapper.writeValueAsString(dnsRecord);
        //Realiza o post
        return sendRequestPost("/ip/dns/static/set",  jsonString);
    }

    public String postDnsRecord(DnsRecord dnsRecord) throws Exception {

        //Cria o objeto para enviar
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //Converte o objeto em Json
        String jsonString = mapper.writeValueAsString(dnsRecord);
        //Realiza o post
        return sendRequestPost("/ip/dns/static/add",  jsonString);

    }

    public Integer deleteDnsRecord(String id) throws Exception {
        return sendRequestDelete("/ip/dns/static/"+id);
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

    public List<SystemVersion> getSystemVersion() throws Exception {
        String json = sendRequestPost("/system/package/update/check-for-updates","{}");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, SystemVersion.class));
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
    public List<DhcpNetwork> getDhcpNetworks() throws Exception {
        String json = sendRequestGet("/ip/dhcp-server/network");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DhcpNetwork.class));
    }

    public ApiResponse postDhcpNetwork(DhcpNetwork dhcpNetwork) throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpNetwork);

        JsonNode node = mapper.readTree(sendRequestPost("/ip/dhcp-server/network/add",jsonString));
        ApiResponse response;

        if (node.isArray()) {
            // Caso sucesso: lista (possivelmente vazia)
            if (node.isEmpty()) {
                response = new ApiResponse();
                response.error = 200;
                response.message = "OK";
            } else {
                // Se vier lista com conteúdo
                response = mapper.treeToValue(node.get(0), ApiResponse.class);
            }
        } else {
            // Caso erro: objeto
            response = mapper.treeToValue(node, ApiResponse.class);
        }

        return response;
    }

    public Integer deleteDhcpNetwork(String id) throws Exception {
        return sendRequestDelete("/ip/dhcp-server/network/"+ id);
    }

    public ApiResponse postEditDhcpNetwork(DhcpNetwork dhcpNetwork) throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpNetwork);

        JsonNode node = mapper.readTree(sendRequestPost("/ip/dhcp-server/network/set",jsonString));

        ApiResponse response;

        if (node.isArray()) {
            // Caso sucesso: lista (possivelmente vazia)
            if (node.isEmpty()) {
                response = new ApiResponse();
                response.error = 200;
                response.message = "OK";
            } else {
                // Se vier lista com conteúdo
                response = mapper.treeToValue(node.get(0), ApiResponse.class);
            }
        } else {
            // Caso erro: objeto
            response = mapper.treeToValue(node, ApiResponse.class);
        }


        return response;
    }

        //Leases
    public List<DhcpLease> getDhcpLeases() throws Exception {
        String json = sendRequestGet("/ip/dhcp-server/lease");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DhcpLease.class));
    }


    public String postDhcpLease(DhcpLease dhcpLease) throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpLease);


        return sendRequestPost("/ip/dhcp-server/lease/add",jsonString);
    }

    public Integer deleteDhcpLease(String id) throws Exception {
        return sendRequestDelete("/ip/dhcp-server/lease/"+ id);
    }

    public String postEditDhcpLease(DhcpLease dhcpLease) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpLease);
        return sendRequestPost("/ip/dhcp-server/lease/set",jsonString);

    }


    //clients
    public List<DhcpClient> getDhcpClients() throws Exception {
        String json = sendRequestGet("/ip/dhcp-client");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, DhcpClient.class));
    }


    public String postDhcpClients(DhcpClient dhcpClient) throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpClient);
        return sendRequestPost("/ip/dhcp-client/add",jsonString);
    }

    public String postEditDhcpClient(DhcpClient dhcpClient) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpClient);
        return sendRequestPost("/ip/dhcp-client/set",jsonString);
    }

    public Integer deleteDhcpClients(String id) throws Exception {
        return sendRequestDelete("/ip/dhcp-client/"+ id);
    }


    public String postDesActivateClient(String id, Boolean state) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DhcpClient dhcpClient = new DhcpClient();
        dhcpClient.id = "*"+id;
        dhcpClient.disabled = Boolean.parseBoolean(state.toString());
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


    public String postDhcpServer(DhcpServer dhcpServer) throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpServer);
        return sendRequestPost("/ip/dhcp-server/add",jsonString);
    }

    public Integer deleteDhcpServer(String id) throws Exception {
        return sendRequestDelete("/ip/dhcp-server/"+ id);
    }

    public String postEditDhcpServer(DhcpServer dhcpServer) throws Exception {
        //Converte o objeto em Json
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpServer);
        return sendRequestPost("/ip/dhcp-server/set",jsonString);
    }


    public String postDesActivateServer(String id, Boolean state) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DhcpServer dhcpServer = new DhcpServer();
        dhcpServer.id = "*"+id;
        dhcpServer.disabled = Boolean.parseBoolean(state.toString());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = mapper.writeValueAsString(dhcpServer);
        return sendRequestPost("/ip/dhcp-server/set",jsonString);
    }



    private String sendRequestGet(String endpoint) throws Exception {
        HttpsURLConnection conn = getHttpsURLConnection(endpoint);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() != 200) {
            return conn.getResponseMessage();
        }

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
    public List<GetAddress> GetAddress() throws Exception {
        String endpoint = sendRequestGet("/ip/address");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<GetAddress>>(){}.getType();
        return gson.fromJson(endpoint, listType);

    }

    //POST: atualizar um ip
    public String UpdateAddress(String id, String newAddress) throws Exception {

        UpdateAddress novoIP = new UpdateAddress();
        novoIP.id = id;
        novoIP.address = newAddress;


        //converter objeto para json
        Gson gson = new Gson();
        String payload = gson.toJson(novoIP);

        return sendRequestPost("/ip/address/set", payload);
    }

    //POST: adicionar um address
    public String AddAddress(String interf, String ip) throws Exception {

        AddAddress newAddr = new AddAddress();
        newAddr.address = ip;
        newAddr.interf = interf;

        Gson gson = new Gson();
        String payload = gson.toJson(newAddr);

        return sendRequestPost("/ip/address/add", payload);
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
    public String EstadoIPAddress(String id, boolean disabled) throws Exception{

        boolean newState = !disabled;
        // cria o JSON que a Mikrotik espera
        String payload = "{ \".id\": \"" + id +"\",\n" +
                         "\"disabled\": " + newState + "}";

        //envia o post com os novos dados

        return sendRequestPost("/ip/address/set", payload);
    }

    /// INTERFACES

    //WiFi
    //Interfaces

    public String AddInterfaceWiFi(String name, String master, String mode, String ssid, String band, String channel, boolean dis) throws Exception {

        AddInterfaceWiFi novaInterface = new AddInterfaceWiFi();
        novaInterface.name = name;
        novaInterface.master_interface = master;
        novaInterface.mode = mode;
        novaInterface.ssid = ssid;
        novaInterface.band = band;
        novaInterface.channel_width = channel;
        novaInterface.disabled = dis;

        //converter objeto para json
        Gson gson = new Gson();
        String payload = gson.toJson(novaInterface);

        return sendRequestPost("/interface/wireless/add", payload);
    }

    public List<GetInterfacesWiFi> GetInterfacesWiFi() throws Exception {
        String endpoint = sendRequestGet("/interface/wireless");

        //converte json em objeto
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GetInterfacesWiFi>>(){}.getType();

        return gson.fromJson(endpoint, listType);
    }

    public Integer DeleteInterfaceWiFi(String id) throws Exception {
        return sendRequestDelete("/interface/wifi/"+id);
    }

    // Security Profiles

    public String AddProfile(String name, boolean dis) throws Exception {

        AddProfile novoProfile = new AddProfile();
        novoProfile.name = name;
        novoProfile.disabled = dis;

        //converter objeto para json
        Gson gson = new Gson();
        String payload = gson.toJson(novoProfile);

        return sendRequestPost("/interface/wifi/security/add", payload);
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

    public String addBridgePort(String bridgeName, String interfaceName) throws Exception {

        AddBridgePort novaBridge = new AddBridgePort();
        novaBridge.interfaceAtual = interfaceName;
        novaBridge.bridge = bridgeName;

        Gson gson = new Gson();
        String payload = gson.toJson(novaBridge);

        return sendRequestPost("/interface/bridge/port/add", payload);
    }

    public String bridgePortState(String id, boolean state) throws Exception {
        boolean novoEstado = !state;
        String payload = "{ \".id\": \"" + id +"\",\n" +
                "\"disabled\": " + novoEstado + "}";

        return sendRequestPost("/interface/bridge/port/set", payload);
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

    public String addInterfaceBridge(String name, boolean disabled) throws Exception {

        addNewInterfaceBridge novaInterfaceBridge = new addNewInterfaceBridge();
        novaInterfaceBridge.name = name;
        novaInterfaceBridge.disabled = disabled;

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

    public Integer deleteInterface(String endpoint) throws Exception {
        return sendRequestDelete(endpoint);
    }

    public String estadoInterface(String id, boolean state) throws Exception {
        boolean novoEstado = !state;
        String payload = "{ \".id\": \"" + id +"\",\n" +
                "\"disabled\": " + novoEstado + "}";

        return sendRequestPost("/interface/set", payload);
    }


    /// Route

    public List<Routes> getStaticRoute() throws Exception {
        String endpoint = sendRequestGet("/ip/route");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Routes>>(){}.getType();

        return gson.fromJson(endpoint, listType);
    }

    public String addStaticRoute() throws Exception {

        Routes novaRotaEstatica = new Routes();
        novaRotaEstatica.dst_address = "10.20.30.40./24";
        novaRotaEstatica.gateway = "10.20.30.41";
        novaRotaEstatica.routing_table = "main";

        Gson gson = new Gson();
        String payload = gson.toJson(novaRotaEstatica);

        return sendRequestPost("/ip/route/add", payload);
    }

    public Integer deleteRotaEstatica(String id) throws Exception {
        return sendRequestDelete("/ip/route/" + id);
    }

    public String StaticRouteState(String id, boolean disabled) throws Exception {

        Routes novoEstado = new Routes();
        novoEstado.disabled = disabled;

        Gson gson = new Gson();
        String payload = gson.toJson(novoEstado);

        return sendRequestPut("/ip/route/" + id, payload);
    }


}