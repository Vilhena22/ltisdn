package Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dns {
    @JsonProperty(".id")
    public String addressListExtraTime;
    @JsonProperty("allow-remote-requests")
    public String allowRemoteRequests;
    @JsonProperty("cache-max-ttl")
    public String cacheMaxTtl;
    @JsonProperty("cache-size")
    public String cacheSize;
    @JsonProperty("cache-used")
    public String cacheUsed;
    @JsonProperty("doh-max-concurrent-queries")
    public String dohMaxConcurrentQueries;
    @JsonProperty("doh-max-server-connections")
    public String dohMaxServerConnections;
    @JsonProperty("doh-timeout")
    public String dohTimeout;
    @JsonProperty("dynamic-servers")
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
    public String verifyDohCert;
    public String vrf;

}
