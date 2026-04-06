package Dialogs.Add;

import ApiClient.ApiClient;
import Models.Interfaces.bridge.interfaces.getInterfaceBridge;
import Models.Interfaces.bridge.ports.GetPorts;

import javax.swing.*;
import java.awt.event.*;
public class addBridgePort extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBoxBridgeName;
    private JComboBox comboBoxInterface;
    private final ApiClient apiClient;

    public addBridgePort(JFrame owner,ApiClient apiClient) {
        super(owner, "Add Bridge Port", true);
        this.apiClient = apiClient;
        setContentPane(contentPane);
        setModal(true);
        SwingUtilities.updateComponentTreeUI(owner);
        getRootPane().setDefaultButton(buttonOK);

        try {
            for (getInterfaceBridge interf : apiClient.getBridgeInterfaces()){
                comboBoxBridgeName.addItem(interf.name);
            }
            for (GetPorts port : apiClient.getBridgePorts()){
                comboBoxInterface.addItem(port.interfaceAtual);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    private void onOK() {
        try {
            apiClient.addBridgePort((String) comboBoxBridgeName.getSelectedItem(), (String) comboBoxInterface.getSelectedItem());
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
