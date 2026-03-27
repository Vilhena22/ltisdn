package Forms;

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

    public addInterfaceWiFi() {
        setContentPane(contentPane);
        setModal(true);
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
        // add your code here
        ApiClient api = new ApiClient();
        try {
            api.AddInterfaceWiFi(name.getText(),master_Interface.getText(), mode.getText(), ssid.getText(), band.getText(), channel_width.getText(), Boolean.parseBoolean(comboBoxDisabled.getSelectedItem().toString()));
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
