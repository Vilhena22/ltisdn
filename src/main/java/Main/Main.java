package Main;

import ApiClient.ApiClient;
import Models.SystemResources;

public class Main {
    public static void main(String[] args) throws Exception {
        /*List<Dns> dnsCaches = new ApiClient().getDnsConfig();
        for (Dns cache : dnsCaches){
            System.out.println(cache.cacheSize);
        }*/

        SystemResources dns = new ApiClient().getSystemResources();
        System.out.println(dns.cpu);
    }
}
