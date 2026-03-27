package Dialogs;

import ApiClient.ApiClient;
import Models.Dhcp.Leases.DhcpLease;
import Models.Dhcp.Servers.DhcpServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.regex.Pattern;

public class AddDhcpLease extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField addressFormattedText;
    private JFormattedTextField clientFormattedText;
    private JComboBox serverComboBox;
    private JLabel serverLabel;
    private JLabel clientLabel;
    private JLabel addressLabel;
    private final Pattern ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
    );
    private final Pattern ID_PATTERN = Pattern.compile(
            "^*[a-zA-Z0-9]+$"
    );

    public AddDhcpLease(Frame owner) {
        super(owner,"Add DHCP Lease",true);
        setContentPane(contentPane);
        SwingUtilities.updateComponentTreeUI(owner);
        getRootPane().setDefaultButton(buttonOK);

        Font font = owner.getFont();
        buttonOK.setFont(font);
        buttonCancel.setFont(font);
        addressFormattedText.setFont(font);
        clientFormattedText.setFont(font);
        serverComboBox.setFont(font);
        serverLabel.setFont(font);
        clientLabel.setFont(font);
        addressLabel.setFont(font);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //Preenche os valores da combobox com os servidores de DHCP existentes
        try {
            for (DhcpServer server :new ApiClient().getDhcpServer()){
                serverComboBox.addItem(server.name);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void onOK() {
        if (!isValidAddress()) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Server!\nMust be 0.0.0.0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            addressFormattedText.setBackground(new Color(241, 0, 0, 25));
        }
        if (!isValidID()) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Client ID!\nMust be *ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            addressFormattedText.setBackground(new Color(241, 0, 0, 25));
        }
        if (isValidID() && isValidAddress()) {
            try {
                DhcpLease dhcpLease = new DhcpLease();
                dhcpLease.address = addressFormattedText.getText();
                dhcpLease.server = Objects.requireNonNull(serverComboBox.getSelectedItem()).toString();
                dhcpLease.clientId = clientFormattedText.getText();
                new ApiClient().postDhcpLease(dhcpLease);

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

    private boolean isValidAddress() {
        String text = addressFormattedText.getText().trim();
        return ADDRESS_PATTERN.matcher(text).matches();
    }
    private boolean isValidID() {
        String text = clientFormattedText.getText().trim();
        return ID_PATTERN.matcher(text).matches();
    }

}
