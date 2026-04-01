package Dialogs.Add;

import ApiClient.ApiClient;
import Models.ApiResponse;
import Models.Dhcp.Networks.DhcpNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class AddDhcpNetwork extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField addressFormattedText;
    private JFormattedTextField dnsFormattedText;
    private JFormattedTextField gatewayFormattedText;
    private JLabel netLabel;
    private JLabel dnsLabel;
    private JLabel gatewayLabel;
    private final Frame owner;
    private final ApiClient apiClient;
    private final Pattern ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
    );

    private final Pattern NETWORK_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)/(3[0-2]|[12]?\\d)$"
    );

    public AddDhcpNetwork(Frame owner,ApiClient apiClient) {
        super(owner,"Add DHCP Network",true);
        this.owner = owner;
        this.apiClient = apiClient;
        setContentPane(contentPane);
        SwingUtilities.updateComponentTreeUI(owner);
        getRootPane().setDefaultButton(buttonOK);

        Font font = owner.getFont();
        addressFormattedText.setFont(font);
        dnsFormattedText.setFont(font);
        gatewayFormattedText.setFont(font);
        netLabel.setFont(font);
        dnsLabel.setFont(font);
        gatewayLabel.setFont(font);
        buttonOK.setFont(font);
        buttonCancel.setFont(font);


        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addressFormattedText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                addressFormattedText.setBackground(owner.getBackground());
            }
        });
        dnsFormattedText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                dnsFormattedText.setBackground(owner.getBackground());
            }
        });
        gatewayFormattedText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                gatewayFormattedText.setBackground(owner.getBackground());
            }
        });


    }

    private void onOK() {
        if (!isValidNetwork(addressFormattedText.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Network!\nMust be 0.0.0.0/24",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            addressFormattedText.setBackground(new Color(241, 0, 15, 15));
            return;
        }

        if (!isValidDns(dnsFormattedText.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Invalid DNS Server!\nMust be 0.0.0.0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dnsFormattedText.setBackground(new Color(241, 0, 15, 15));
            return;
        }

        if (!isValidGateway(gatewayFormattedText.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Gateway Address!\nMust be 0.0.0.0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            gatewayFormattedText.setBackground(new Color(241, 0, 15, 15));
            return;
        }
        // tudo válido
        try {
            DhcpNetwork network = new DhcpNetwork();
            network.address = addressFormattedText.getText();
            network.dnsServer = dnsFormattedText.getText();
            network.gateway = gatewayFormattedText.getText();
            ApiResponse response = apiClient.postDhcpNetwork(network);
            if (response.error >= 400) {
                JOptionPane.showMessageDialog(owner,
                        response.detail,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }else {
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(owner,
                    "Unexpected Error!\nError: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private boolean isValidNetwork(String text) {
        return NETWORK_PATTERN.matcher(text.trim()).matches();
    }

    private boolean isValidDns(String text) {
        return ADDRESS_PATTERN.matcher(text.trim()).matches();
    }

    private boolean isValidGateway(String text) {
        return ADDRESS_PATTERN.matcher(text.trim()).matches();
    }

}
