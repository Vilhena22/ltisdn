package Dialogs;

import ApiClient.ApiClient;
import Models.ApiResponse;
import Models.Wireguard.ClientConfig;
import Models.Wireguard.Peer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class SetupWireguardConnection extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton noneRadioButton;
    private JRadioButton autoRadioButton;
    private JTextField peerNameTextField;
    private JRadioButton yesConfig;
    private JRadioButton noConfig;
    private final JFrame owner;
    private final ApiClient apiClient;
    private final ButtonGroup preshareBG;


    public SetupWireguardConnection(JFrame owner, ApiClient apiClient) {
        super(owner,"Setup Wireguard Connection",true);
        setContentPane(contentPane);
        SwingUtilities.updateComponentTreeUI(owner);
        getRootPane().setDefaultButton(buttonOK);
        this.owner = owner;
        this.apiClient = apiClient;

        preshareBG = new ButtonGroup();
        preshareBG.add(noneRadioButton);
        preshareBG.add(autoRadioButton);

        noneRadioButton.setSelected(true);
        noneRadioButton.setActionCommand("none");
        autoRadioButton.setActionCommand("auto");

        ButtonGroup configBG = new ButtonGroup();
        configBG.add(noConfig);
        configBG.add(yesConfig);
        noConfig.setSelected(true);

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

    private boolean checkAllFields() {

        if (peerNameTextField.getText().isBlank()) {
            JOptionPane.showMessageDialog(owner, "Please enter a peer name.","Warning",JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void onOK() {
        try {
            if (checkAllFields()) {
                Peer newPeer = new Peer();
                newPeer.name = peerNameTextField.getText();
                newPeer.inter = "wg1";
                LinkedList<String> ipUsed = new LinkedList<>();
                for (Peer peer : apiClient.getPeersWireGuard()){
                    String[] ip = peer.allowedAddress.split("/");
                    ipUsed.add(ip[0]);
                }
                int min = 2;
                int max = 254;
                int newIp ;
                do {
                    newIp = ThreadLocalRandom.current().nextInt(min, max );
                }while (ipUsed.contains("192.168.100."+newIp));
                newPeer.allowedAddress = "192.168.100."+newIp;
                newPeer.presharedKey = preshareBG.getSelection().getActionCommand();
                newPeer.privateKey = "auto";
                newPeer.disabled = false;
                newPeer.clientEndpoint = apiClient.getInterfaceByName("ether1"); //TEM DE SER ALTERADo
                newPeer.clientDns = newPeer.clientEndpoint;
                newPeer.clientAddress = newPeer.allowedAddress;
                ApiResponse response =  apiClient.postWireguardPeer(newPeer);
                if( response.ret != null){
                    if (yesConfig.isSelected()) {
                        ArrayList<ClientConfig> clientConfig = apiClient.getWireguardClinentConfig(response.ret);
                        GenerateClientConfig gcc = new GenerateClientConfig(clientConfig.getFirst(),owner);
                        gcc.pack();
                        gcc.setLocationRelativeTo(owner);
                        gcc.setVisible(true);
                    }
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(owner,"Error: " + response.detail,"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
