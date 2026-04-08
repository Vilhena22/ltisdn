package Dialogs.Add;

import ApiClient.ApiClient;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

public class addWiFiSP extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private final JFrame owner;
    private final ApiClient apiClient;
    private JTextField profileName;
    private JCheckBox disabledCheckBox;

    public addWiFiSP(JFrame owner, ApiClient apiClient) {
        super(owner,"Add Interface WiFi",true);
        this.owner = owner;
        this.apiClient = apiClient;
        SwingUtilities.updateComponentTreeUI(owner);
        setContentPane(contentPane);
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
            apiClient.AddProfile(profileName.getText(), disabledCheckBox.isSelected());
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
