package Dialogs.Add;

import ApiClient.ApiClient;
import Models.ApiResponse;
import Models.Wireguard.InterfaceWG;
import Models.Wireguard.Peer;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

public class AddWireguardPeer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JComboBox interfacesComboBox;
    private JFormattedTextField endpointADRFormattedText;
    private JSlider slider1;
    private JFormattedTextField listenPortFormattedTextField;
    private JRadioButton noneRadioButton;
    private JRadioButton autoRadioButton;
    private JFormattedTextField allowedADRFormattedText;
    private final JFrame owner;
    private final ApiClient apiClient;
    private final ButtonGroup preshareBG;


    private static final String IPV4_REGEX =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

    private static final String CIDR_REGEX =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)/(3[0-2]|[12]?\\d)$";

    public AddWireguardPeer(JFrame owner, ApiClient apiClient) {
        super(owner, "Add Wireguard Peer", true);
        this.apiClient = apiClient;
        this.owner = owner;
        setContentPane(contentPane);
        SwingUtilities.updateComponentTreeUI(owner);
        getRootPane().setDefaultButton(buttonOK);

        preshareBG = new ButtonGroup();
        preshareBG.add(noneRadioButton);
        preshareBG.add(autoRadioButton);

        noneRadioButton.setSelected(true);
        noneRadioButton.setActionCommand("none");
        autoRadioButton.setActionCommand("auto");


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

        endpointADRFormattedText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String text = endpointADRFormattedText.getText().trim();

                if (!text.matches(IPV4_REGEX)) {
                    JOptionPane.showMessageDialog(null, "Endpoint inválido (IPv4). Ex: 203.0.112.10");
                    endpointADRFormattedText.setText("");
                }
            }
        });

        allowedADRFormattedText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String text = allowedADRFormattedText.getText().trim();

                if (text.isEmpty()) return;

                String[] parts = text.split(",");

                for (String part : parts) {
                    String cidr = part.trim();

                    if (!cidr.matches(CIDR_REGEX)) {
                        JOptionPane.showMessageDialog(null,
                                "CIDR inválido: " + cidr + "\nEx: 192.168.56.0/24");
                        allowedADRFormattedText.setText("");
                        return;
                    }
                }
            }
        });

        try {
            for (InterfaceWG interfaceWG : apiClient.getInterfacesWireGuard()){
                interfacesComboBox.addItem(interfaceWG.name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        listenPortFormattedTextField.setValue(slider1.getValue());
    }

    private void onOK() {
        if (nameTextField.getText().isEmpty() || endpointADRFormattedText.getText().isEmpty() || allowedADRFormattedText.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fill all the fields!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        try {
            Peer newPeer = new Peer();
            newPeer.name = nameTextField.getText();
            newPeer.inter = Objects.requireNonNull(interfacesComboBox.getSelectedItem()).toString();
            newPeer.endpointAddress = endpointADRFormattedText.getText();
            newPeer.endpointPort = slider1.getValue();
            newPeer.allowedAddress = allowedADRFormattedText.getText();
            newPeer.presharedKey = preshareBG.getSelection().getActionCommand();
            newPeer.privateKey = "auto";
            ApiResponse response = apiClient.postWireguardPeer(newPeer);
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
