package Dialogs.Add;

import ApiClient.ApiClient;

import javax.swing.*;
import java.awt.event.*;

public class addBridgePort extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldBridgeName;
    private JTextField textFieldInterfaceName;
    private final JFrame owner;
    private final ApiClient apiClient;

    public addBridgePort(JFrame owner,ApiClient apiClient) {
        super(owner,"Add Bridge Port",true);
        this.owner = owner;
        this.apiClient = apiClient;
        setContentPane(contentPane);
        setModal(true);
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
            apiClient.addBridgePort(textFieldBridgeName.getText(), textFieldInterfaceName.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        };
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
