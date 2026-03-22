package Main;

import ApiClient.ApiClient;
import Forms.HomePage;
import Models.Dhcp.Clients.DhcpClient;
import Models.Dhcp.Leases.DhcpLease;
import Models.Dhcp.Servers.DhcpServer;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.util.prefs.Preferences;

public class Main {
    public static void main(String[] args) throws Exception {
        /*ApiClient apiClient = new ApiClient();
        for(DhcpServer pool : apiClient.getDhcpServer()){
            System.out.println(pool.id);
            System.out.println(pool.interfaceName);
            System.out.println(pool.dynamic);
            System.out.println(pool.disabled);
        };*/

        //System.out.println(apiClient.postDhcpServer());
        //apiClient.deleteDhcpServer("4");
        //apiClient.postDesActivateServer("4",true);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("HomePage");
            HomePage homePage;
            if (isDarkModeWindows() || isDarkModeMac()) {
                FlatLightLaf.setup();
                try {
                    homePage = new HomePage(false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }else {
                FlatDarkLaf.setup();
                try {
                    homePage = new HomePage(true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            frame.setContentPane(homePage.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }


    public static boolean isDarkModeWindows() {
        Preferences prefs = Preferences.userRoot().node("Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");
        return prefs.getInt("AppsUseLightTheme", 1) == 0;
    }

    public static boolean isDarkModeMac() {
        String theme = System.getProperty("apple.awt.application.appearance");
        return "NSAppearanceNameDarkAqua".equals(theme);
    }
}
