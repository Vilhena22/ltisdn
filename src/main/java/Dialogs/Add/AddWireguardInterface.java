package Dialogs.Add;

import ApiClient.ApiClient;
import Models.ApiResponse;
import Models.Wireguard.InterfaceWG;

import javax.swing.*;
import java.awt.event.*;

public class AddWireguardInterface extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JSlider slider1;
    private JCheckBox disabledCheckBox;
    private JFormattedTextField listenPortFormattedTextField;
    private final ApiClient apiClient;
    private final JFrame owner;

    public AddWireguardInterface(JFrame owner, ApiClient apiClient) {
        super(owner,"Add Wireguard Interface",true);
        setContentPane(contentPane);
        SwingUtilities.updateComponentTreeUI(owner);
        getRootPane().setDefaultButton(buttonOK);
        this.owner = owner;
        this.apiClient = apiClient;

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


        slider1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                listenPortFormattedTextField.setValue(slider1.getValue());

            }
        });

        slider1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                listenPortFormattedTextField.setValue(slider1.getValue());

            }
        });


        listenPortFormattedTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String text = listenPortFormattedTextField.getText();
                // Verifica se contém apenas números
                if (!text.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Only numbers allowed.");
                    listenPortFormattedTextField.setText("51820");
                    return;
                }

                int value = Integer.parseInt(text);

                // Verifica intervalo
                if (value <= 49152 || value >= 65555) {
                    JOptionPane.showMessageDialog(null, "Value must be between 0 and 65555.");
                    listenPortFormattedTextField.setText("51820");
                    return;
                }



                slider1.setValue(Integer.parseInt(listenPortFormattedTextField.getText()));
            }
        });

        listenPortFormattedTextField.setValue(slider1.getValue());


    }

    private void onOK() {
        if (nameTextField.getText().equals("")) {
            JOptionPane.showMessageDialog(owner, "Please enter a name.","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            InterfaceWG interfaceWG = new InterfaceWG();
            interfaceWG.name = nameTextField.getText();
            interfaceWG.listenPort = slider1.getValue();
            interfaceWG.disabled = disabledCheckBox.isSelected();
            ApiResponse response = apiClient.postWireguardInterface(interfaceWG);
            if( response.ret != null){
                dispose();
            }else {
                JOptionPane.showMessageDialog(owner,"Error: " + response.detail,"Error",JOptionPane.ERROR_MESSAGE);
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
