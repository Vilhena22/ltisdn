package Forms;

import ApiClient.ApiClient;

import javax.swing.*;

public class DhcpPanel {
    private JSeparator divider;
    private JFrame owner;
    private JPanel navBar;
    private JButton routeButton;
    private JLabel hostip;
    private JLabel username;
    private JPanel topBar;
    private JButton dhcpButton;
    private JButton interfaceButton;
    private JButton addressButton;
    private JButton homeButton;
    private JButton dnsButton;
    private JLabel hoslLabel;
    private JLabel userLabel;

    public DhcpPanel(JFrame owner) throws Exception {
        this.owner = owner;

    }
}
