package Dialogs.Add;

import ApiClient.ApiClient;

import javax.swing.*;
import java.awt.event.*;

public class addInterfaceWiFi extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField channel_width;
    private JTextField master_Interface;
    private JTextField mode;
    private JTextField ssid;
    private JTextField band;
    private JTextField name;
    private JComboBox comboBoxDisabled;
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
    }

    private void onOK() {
        try {
            apiClient.AddInterfaceWiFi(name.getText(),master_Interface.getText(), mode.getText(), ssid.getText(), band.getText(), channel_width.getText(), Boolean.parseBoolean(comboBoxDisabled.getSelectedItem().toString()));
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
