package Dialogs.Edit;

import ApiClient.ApiClient;
import Models.Dhcp.Servers.DhcpServer;
import Models.Interfaces.getAllInterfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class EditDhcpServer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JComboBox interComboBox;
    private JCheckBox disabledCheckBox;
    private JLabel interLabel;
    private JLabel nameLabel;
    private final String id;

    public EditDhcpServer(Frame owner,String id,String name,String interSelected, String disabled) {
        super(owner,"Edit Dhcp Server",true);
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

        ApiClient apiClient = new ApiClient();
        try {
            for (getAllInterfaces inter :apiClient.getAllInterfaces()) {
                interComboBox.addItem(inter.name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.id = id;
        nameTextField.setText(name);
        interComboBox.setSelectedItem(interSelected);
        disabledCheckBox.setSelected(Boolean.parseBoolean(disabled));
    }

    private void onOK() {

        try {
            DhcpServer dhcpServer = new DhcpServer();
            dhcpServer.id = id;
            dhcpServer.name = nameTextField.getText();
            dhcpServer.interfaceName = Objects.requireNonNull(interComboBox.getSelectedItem()).toString();
            dhcpServer.disabled = disabledCheckBox.isSelected();
            new ApiClient().postEditDhcpServer(dhcpServer);
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
