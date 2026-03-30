package Dialogs.Edit;

import ApiClient.ApiClient;
import Models.Dhcp.Clients.DhcpClient;
import Models.Interfaces.getAllInterfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class EditDhcpClient extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox interComboBox;
    private JRadioButton yesRadioButton1;
    private JRadioButton noRadioButton1;
    private JRadioButton yesRadioButton2;
    private JRadioButton noRadioButton2;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private JCheckBox disabledCheck;
    private JLabel interLabel;
    private JLabel routeLabel;
    private JLabel dnsLabel;
    private JLabel ntpLabel;
    private final ButtonGroup defaultRouteBG;
    private final ButtonGroup dnsBG;
    private final ButtonGroup ntpBG;
    private final String interfaceID;


    public EditDhcpClient(Frame owner,String interfaceID , String inter,String defaultRoute, String dns, String ntp, String disabled) {
        super(owner,"Edit Dhcp Client",true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        Font font = owner.getFont();

        contentPane.setFont(font);
        buttonOK.setFont(font);
        buttonCancel.setFont(font);
        interComboBox.setFont(font);
        yesRadioButton1.setFont(font);
        noRadioButton1.setFont(font);
        yesRadioButton2.setFont(font);
        noRadioButton2.setFont(font);
        yesRadioButton.setFont(font);
        noRadioButton.setFont(font);
        disabledCheck.setFont(font);
        interLabel.setFont(font);
        routeLabel.setFont(font);
        dnsLabel.setFont(font);
        ntpLabel.setFont(font);


        yesRadioButton1.setActionCommand("yes");
        noRadioButton1.setActionCommand("no");

        yesRadioButton2.setActionCommand("yes");
        noRadioButton2.setActionCommand("no");

        yesRadioButton.setActionCommand("true");
        noRadioButton.setActionCommand("false");

        defaultRouteBG = new ButtonGroup();
        defaultRouteBG.add(yesRadioButton1);
        defaultRouteBG.add(noRadioButton1);

        dnsBG = new ButtonGroup();
        dnsBG.add(yesRadioButton2);
        dnsBG.add(noRadioButton2);

        ntpBG = new ButtonGroup();
        ntpBG.add(yesRadioButton);
        ntpBG.add(noRadioButton);



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

        try {
            for (getAllInterfaces intef : new ApiClient().getAllInterfaces()){
                interComboBox.addItem(intef.name);
            }
            interComboBox.setSelectedItem(inter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.interfaceID = interfaceID;
        defaultRouteBG.setSelected(yesRadioButton1.getModel(), Boolean.parseBoolean(defaultRoute));
        dnsBG.setSelected(yesRadioButton2.getModel(), Boolean.parseBoolean(dns));
        ntpBG.setSelected(yesRadioButton.getModel(), Boolean.parseBoolean(ntp));
        disabledCheck.setSelected(Boolean.parseBoolean(disabled));
    }

    private void onOK() {
        try {
            DhcpClient dhcpClient = new DhcpClient();
            dhcpClient.id = interfaceID;
            dhcpClient.interfaceName = Objects.requireNonNull(interComboBox.getSelectedItem()).toString();
            dhcpClient.addDefaultRoute = defaultRouteBG.getSelection().getActionCommand();
            dhcpClient.usePeerNtp= ntpBG.getSelection().getActionCommand();
            dhcpClient.usePeerDns=dnsBG.getSelection().getActionCommand();
            dhcpClient.disabled = Boolean.parseBoolean(String.valueOf(disabledCheck.isSelected()));
            new ApiClient().postEditDhcpClient(dhcpClient);
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
