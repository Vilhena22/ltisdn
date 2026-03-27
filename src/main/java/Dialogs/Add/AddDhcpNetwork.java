package Dialogs.Add;

import ApiClient.ApiClient;
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
    private final Pattern ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
    );

    private final Pattern NETWORK_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)/(3[0-2]|[12]?\\d)$"
    );

    public AddDhcpNetwork(Frame owner) {
        super(owner,"Add DHCP Network",true);
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


    }

    private void onOK() {
        if (!isValidNetwork()) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Network!\nMust be 0.0.0.0/24",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            addressFormattedText.setBackground(new Color(241, 0, 0, 25));
        }

        if (!isValidDns()) {
            JOptionPane.showMessageDialog(this,
                    "Invalid DNS Server!\nMust be 0.0.0.0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dnsFormattedText.setBackground(new Color(241, 0, 0, 25));
        }

        if (!isValidGateway()) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Gateway Address!\nMust be 0.0.0.0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            gatewayFormattedText.setBackground(new Color(241, 0, 0, 25));
        }
        if (isValidDns() && isValidGateway() && isValidNetwork()) {
            // tudo válido
            try {
                DhcpNetwork network = new DhcpNetwork();
                network.address = addressFormattedText.getText();
                network.dnsServer = dnsFormattedText.getText();
                network.gateway = gatewayFormattedText.getText();
                System.out.println( new ApiClient().postDhcpNetwork(network));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private boolean isValidNetwork() {
        String text = addressFormattedText.getText().trim();
        return NETWORK_PATTERN.matcher(text).matches();
    }

    private boolean isValidDns() {
        String text = dnsFormattedText.getText().trim();
        return ADDRESS_PATTERN.matcher(text).matches();
    }

    private boolean isValidGateway() {
        String text = gatewayFormattedText.getText().trim();
        return ADDRESS_PATTERN.matcher(text).matches();
    }

}
