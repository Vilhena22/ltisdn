package Main;

import ApiClient.ApiClient;

public class Main {
    public static void main(String[] args) throws Exception {
        new ApiClient().postDnsRecord();
    }
}
