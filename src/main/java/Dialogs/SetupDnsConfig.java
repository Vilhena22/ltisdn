package Dialogs;

import ApiClient.ApiClient;
import Models.ApiResponse;
import Models.Dns.Dns;
import Models.Dns.Vrf;
import Models.Interfaces.getAllInterfaces;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.regex.Pattern;

public class SetupDnsConfig extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField serversFormattedTextField;
    private JComboBox repInterfacescComboBox;
    private JComboBox vrfComboBox;
    private JFormattedTextField maxUdpFormattedTextField;
    private JCheckBox allowRemoteRequestsCheckBox;
    private JFormattedTextField querryServerTimeoutFormattedTextField;
    private JFormattedTextField querryTotalTimeoutFormattedTextField;
    private JFormattedTextField maxConcurrentQueriesFormattedTextField;
    private JFormattedTextField maxTcpSessionsFormattedTextField;
    private JFormattedTextField cacheSizeFormattedTextField;
    private JFormattedTextField cacheMaxTTLFormattedTextField;
    private JLabel cacheUsedValue;
    private JCheckBox verifyDOHCertCheckBox;

    private final Pattern ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
                    "(\\s*,\\s*((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d))*$"
    );


    private final ApiClient apiClient;
    private final JFrame owner;
    public SetupDnsConfig(JFrame owner, ApiClient apiClient) {
        super(owner,"Setup DNS Server",true);
        setContentPane(contentPane);
        SwingUtilities.updateComponentTreeUI(owner);
        getRootPane().setDefaultButton(buttonOK);
        this.apiClient = apiClient;
        this.owner = owner;
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

        try {
            for (Vrf vrf : apiClient.getVrfTables()) {
                vrfComboBox.addItem(vrf.name);
            }
            for (getAllInterfaces interf :  apiClient.getAllInterfaces()) {
                repInterfacescComboBox.addItem(interf.name);
            }
            vrfComboBox.addItem("");
            repInterfacescComboBox.addItem("");

            Dns dns = apiClient.getDnsConfig();

            serversFormattedTextField.setValue(dns.servers);
            repInterfacescComboBox.setSelectedItem(dns.mdnsRepeatIfaces);
            maxUdpFormattedTextField.setText(dns.maxUdpPacketSize);
            querryServerTimeoutFormattedTextField.setText(dns.queryServerTimeout);
            querryTotalTimeoutFormattedTextField.setText(dns.queryTotalTimeout);
            maxConcurrentQueriesFormattedTextField.setText(dns.maxConcurrentQueries);
            maxTcpSessionsFormattedTextField.setText(dns.maxConcurrentTcpSessions);
            cacheSizeFormattedTextField.setText(dns.cacheSize);
            cacheMaxTTLFormattedTextField.setText(dns.cacheMaxTtl);
            cacheUsedValue.setText(dns.cacheUsed);
            allowRemoteRequestsCheckBox.setSelected(Boolean.parseBoolean(dns.allowRemoteRequests));
            verifyDOHCertCheckBox.setSelected(dns.verifyDohCert);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void onOK() {
        String servers = serversFormattedTextField.getText();
        if (servers.compareTo("") !=0 &&  !isValidAddress(servers)){
            JOptionPane.showMessageDialog(owner, "Invalid server address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Dns dns = new Dns();
        dns.servers = servers;
        dns.mdnsRepeatIfaces = Objects.requireNonNull(repInterfacescComboBox.getSelectedItem()).toString();
        dns.maxUdpPacketSize = maxUdpFormattedTextField.getText();
        dns.queryServerTimeout = querryServerTimeoutFormattedTextField.getText();
        dns.queryTotalTimeout = querryTotalTimeoutFormattedTextField.getText();
        dns.maxConcurrentQueries = maxConcurrentQueriesFormattedTextField.getText();
        dns.maxConcurrentTcpSessions = maxTcpSessionsFormattedTextField.getText();
        dns.cacheSize = cacheSizeFormattedTextField.getText();
        dns.cacheMaxTtl = cacheMaxTTLFormattedTextField.getText();
        dns.verifyDohCert = verifyDOHCertCheckBox.isSelected();
        dns.allowRemoteRequests = String.valueOf(allowRemoteRequestsCheckBox.isSelected());
        dns.vrf = Objects.requireNonNull(vrfComboBox.getSelectedItem()).toString();

        try {
            ApiResponse response = apiClient.postDnsConfig(dns);
            System.out.println(response);
            if (response.error != 200){
                JOptionPane.showMessageDialog(owner, "Error: " + response.detail, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private boolean isValidAddress(String text) {
        return ADDRESS_PATTERN.matcher(text.trim()).matches();
    }

}
