package Dialogs.Add;

import ApiClient.ApiClient;
import Models.Interfaces.wifi.interfaces.GetInterfacesWiFi;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

public class addInterfaceWiFi extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox channel_width;
    private JComboBox master_Interface;
    private JComboBox mode;
    private JTextField ssid;
    private JComboBox band;
    private JTextField name;
    private JCheckBox disabledCheckBox;
    private final JFrame owner;
    private final ApiClient apiClient;

    public addInterfaceWiFi(JFrame owner, ApiClient apiClient) {
        super(owner,"Add Interface WiFi",true);
        this.apiClient = apiClient;
        this.owner = owner;
        setContentPane(contentPane);
        SwingUtilities.updateComponentTreeUI(owner);
        getRootPane().setDefaultButton(buttonOK);

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
            for(GetInterfacesWiFi wifiInter : apiClient.GetInterfacesWiFi()){
                master_Interface.addItem(wifiInter.name);
            }

            band.addItem("2ghz-ax");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        band.addItem("2ghz-be");
        band.addItem("2ghz-g");
        band.addItem("2ghz-n");
        band.addItem("5ghz-a");
        band.addItem("5ghz-ac");
        band.addItem("5ghz-ax");
        band.addItem("5ghz-be");
        band.addItem("5ghz-n");

        channel_width.addItem("20/40/80+80mhz");
        channel_width.addItem("20/40/80/160mhz");
        channel_width.addItem("20/40mhz");
        channel_width.addItem("20/40mhz-eC");
        channel_width.addItem("20/40/80/160/320mhz");
        channel_width.addItem("20/40/80mhz");
        channel_width.addItem("20/40mhz-Ce");
        channel_width.addItem("20mhz");

        mode.addItem("ap");
        mode.addItem("ap-bridge");
        mode.addItem("station");
        mode.addItem("station-bridge");
        mode.addItem("station-pseudobridge");

    }

    private void onOK() {
        try {
            apiClient.AddInterfaceWiFi(name.getText(), Objects.requireNonNull(master_Interface.getSelectedItem()).toString(), Objects.requireNonNull(mode.getSelectedItem()).toString(), ssid.getText(), Objects.requireNonNull(band.getSelectedItem()).toString(), Objects.requireNonNull(channel_width.getSelectedItem()).toString(), disabledCheckBox.isSelected());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
