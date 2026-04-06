package Models.Dns;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dns {
    @JsonProperty(value = ".id", access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private String id;
    @JsonProperty(value = "address-list-extra-time", access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    public String addressListExtraTime;
    @JsonProperty("allow-remote-requests")
    public String allowRemoteRequests;
    @JsonProperty("cache-max-ttl")
    public String cacheMaxTtl;
    @JsonProperty("cache-size")
    public String cacheSize;
    @JsonProperty(value = "cache-used", access =  JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    public String cacheUsed;
    @JsonProperty("doh-max-concurrent-queries")
    @JsonIgnore
    public Integer dohMaxConcurrentQueries;
    @JsonProperty("doh-max-server-connections")
    @JsonIgnore
    public Integer dohMaxServerConnections;
    @JsonProperty("doh-timeout")
    @JsonIgnore
    public String dohTimeout;
    @JsonProperty(value = "dynamic-servers", access =  JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    public String dynamicServers;
    @JsonProperty("max-concurrent-queries")
    public String maxConcurrentQueries;
    @JsonProperty("max-concurrent-tcp-sessions")
    public String maxConcurrentTcpSessions;
    @JsonProperty("max-udp-packet-size")
    public String maxUdpPacketSize;
    @JsonProperty("mdns-repeat-ifaces")
    public String mdnsRepeatIfaces;
    @JsonProperty("query-server-timeout")
    public String queryServerTimeout;
    @JsonProperty("query-total-timeout")
    public String queryTotalTimeout;
    public String servers;
    @JsonProperty("use-doh-server")
    public String useDohServer;
    @JsonProperty("verify-doh-cert")
    public boolean verifyDohCert;
    public String vrf;

}
