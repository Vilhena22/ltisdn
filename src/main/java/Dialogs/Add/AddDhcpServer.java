package Dialogs.Add;

import ApiClient.ApiClient;
import Models.Dhcp.Servers.DhcpServer;
import Models.Interfaces.getAllInterfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class AddDhcpServer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JComboBox interComboBox;
    private JCheckBox disabledCheckBox;
    private JLabel interLabel;
    private JLabel nameLabel;

    public AddDhcpServer(Frame owner) {
        super(owner,"Add Dhcp Server",true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        Font font = owner.getFont();
        contentPane.setFont(font);
        buttonOK.setFont(font);
        buttonCancel.setFont(font);
        nameTextField.setFont(font);
        interComboBox.setFont(font);
        disabledCheckBox.setFont(font);
        nameLabel.setFont(font);
        interLabel.setFont(font);


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


        ApiClient apiClient = new ApiClient();
        try {
            for (getAllInterfaces inter :apiClient.getAllInterfaces()) {
                interComboBox.addItem(inter.name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void onOK() {

        try {
            DhcpServer dhcpServer = new DhcpServer();
            dhcpServer.name = nameTextField.getText();
            dhcpServer.interfaceName = Objects.requireNonNull(interComboBox.getSelectedItem()).toString();
            dhcpServer.disabled = Boolean.parseBoolean(String.valueOf(disabledCheckBox.isSelected()));
            System.out.println(new ApiClient().postDhcpServer(dhcpServer));
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
