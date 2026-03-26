package Main;


import ApiClient.ApiClient;
import Forms.HomePage;
import Models.Route.Routes;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {

            boolean isDarkMode = ThemeDetect.isDarkMode();
            if (isDarkMode) {
                FlatDarkLaf.setup();;
            }else {
                FlatLightLaf.setup();
            }

            try {
                JFrame frame = new JFrame("SDN TL1 Mikrotik");
                HomePage homePage = new HomePage(isDarkMode,frame);
                frame.setContentPane(homePage.getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }
}
