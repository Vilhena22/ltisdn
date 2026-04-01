package Dialogs.Add;

import ApiClient.ApiClient;

import javax.swing.*;
import java.awt.event.*;

public class addInterfaceBridge extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBoxDisabled;
    private JTextField name;
    private final JFrame owner;
    private final ApiClient apiClient;

    public addInterfaceBridge(JFrame owner,ApiClient apiClient) {
        super(owner,"Add Interface Bridge",true );
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
            apiClient.addInterfaceBridge(name.getText(), Boolean.parseBoolean(comboBoxDisabled.getSelectedItem().toString()));
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
