package Dialogs;

import ApiClient.ApiClient;
import Models.ApiResponse;
import Models.Wireguard.ClientConfig;
import Models.Wireguard.InterfaceWG;
import Models.Wireguard.Peer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SetupWireguardConnection extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider sliderInterfacePort;
    private JFormattedTextField interfaceListenPortFormattedTextField;
    private JTextField interfaceNameTextField;
    private JPanel interfacePanel;
    private JFormattedTextField endpointADRFormattedText;
    private JRadioButton noneRadioButton;
    private JRadioButton autoRadioButton;
    private JFormattedTextField allowedADRFormattedText;
    private JPanel peerPanel;
    private JLabel interfacePeer;
    private JSlider sliderPeerPort;
    private JSeparator divider;
    private JFormattedTextField peerListenPortFormattedTextField;
    private JTextField peerNameTextField;
    private JCheckBox disabledCheckBox;
    private JSeparator divider2;
    private JRadioButton yesConfig;
    private JRadioButton noConfig;
    private final JFrame owner;
    private final ApiClient apiClient;
    private final ButtonGroup preshareBG;
    private final ButtonGroup configBG;


    private static final String IPV4_REGEX =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

    private static final String CIDR_REGEX =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)/32$";

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

        configBG  = new ButtonGroup();
        configBG.add(noConfig);
        configBG.add(yesConfig);
        noConfig.setSelected(true);

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

        //Interface Setup
        sliderInterfacePort.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                interfaceListenPortFormattedTextField.setValue(sliderInterfacePort.getValue());

            }
        });

        sliderInterfacePort.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                interfaceListenPortFormattedTextField.setValue(sliderInterfacePort.getValue());

            }
        });

        interfaceListenPortFormattedTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String text = interfaceListenPortFormattedTextField.getText();
                // Verifica se contém apenas números
                if (!text.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Only numbers allowed.");
                    interfaceListenPortFormattedTextField.setText("51820");
                    return;
                }

                int value = Integer.parseInt(text);

                // Verifica intervalo
                if (value <= 49152 || value >= 65555) {
                    JOptionPane.showMessageDialog(null, "Value must be between 0 and 65555.");
                    interfaceListenPortFormattedTextField.setText("51820");
                    return;
                }



                sliderInterfacePort.setValue(Integer.parseInt(interfaceListenPortFormattedTextField.getText()));
            }
        });

        interfaceListenPortFormattedTextField.setValue(sliderInterfacePort.getValue());

        interfaceNameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                interfacePeer.setText(interfaceNameTextField.getText());
            }
        });

        //Peer Setup
        sliderPeerPort.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                peerListenPortFormattedTextField.setValue(sliderPeerPort.getValue());

            }
        });

        sliderPeerPort.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                peerListenPortFormattedTextField.setValue(sliderPeerPort.getValue());

            }
        });

        peerListenPortFormattedTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String text = peerListenPortFormattedTextField.getText();
                // Verifica se contém apenas números
                if (!text.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Only numbers allowed.");
                    peerListenPortFormattedTextField.setText("51820");
                    return;
                }

                int value = Integer.parseInt(text);

                // Verifica intervalo
                if (value <= 49152 || value >= 65555) {
                    JOptionPane.showMessageDialog(null, "Value must be between 0 and 65555.");
                    peerListenPortFormattedTextField.setText("51820");
                    return;
                }

                sliderPeerPort.setValue(Integer.parseInt(peerListenPortFormattedTextField.getText()));
            }
        });

        peerListenPortFormattedTextField.setValue(sliderPeerPort.getValue());

        endpointADRFormattedText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String text = endpointADRFormattedText.getText().trim();

                if (!text.matches(IPV4_REGEX)) {
                    JOptionPane.showMessageDialog(null, "Invalid Endpoint (IPv4). Ex: 203.0.112.10");
                    endpointADRFormattedText.setText("");
                }
            }
        });

        allowedADRFormattedText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String text = allowedADRFormattedText.getText().trim();

                if (text.isBlank()) return;

                String[] parts = text.split("/");

                if (parts.length != 2) {
                    showError("Formato inválido. Ex: 192.168.56.2/32");
                    return;
                }

                String ip = parts[0].trim();
                String cidr = parts[1].trim();

                // ✔ validar máscara
                if (!cidr.equals("32")) {
                    showError("Apenas /32 é permitido para peers WireGuard");
                    return;
                }

                // ✔ validar IP
                if (!ip.matches("^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                        "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$")) {
                    showError("Invalid Address. Ex: 192.168.56.2/32");
                    return;
                }
            }

            private void showError(String msg) {
                JOptionPane.showMessageDialog(owner, msg, "Error", JOptionPane.ERROR_MESSAGE);
                allowedADRFormattedText.setText("");
            }
        });
    }

    private boolean checkAllFields() {
        if (interfaceNameTextField.getText().isBlank()) {
            JOptionPane.showMessageDialog(owner, "Please enter a interface name.","Warning",JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (peerNameTextField.getText().isBlank()) {
            JOptionPane.showMessageDialog(owner, "Please enter a peer name.","Warning",JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (endpointADRFormattedText.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Please enter a endpoint!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (allowedADRFormattedText.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Please enter allowed address!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void onOK() {
        try {
            String interfaceID;
            if (checkAllFields()) {
                InterfaceWG interfaceWG = new InterfaceWG();
                interfaceWG.name = interfaceNameTextField.getText();
                interfaceWG.listenPort = sliderInterfacePort.getValue();
                interfaceWG.disabled = disabledCheckBox.isSelected();
                ApiResponse response = apiClient.postWireguardInterface(interfaceWG);
                if( response.ret != null){
                    interfaceID = response.ret;
                    Peer newPeer = new Peer();
                    newPeer.name = peerNameTextField.getText();
                    newPeer.inter = interfacePeer.getText();
                    newPeer.endpointAddress = endpointADRFormattedText.getText();
                    newPeer.endpointPort = sliderPeerPort.getValue();
                    newPeer.allowedAddress = allowedADRFormattedText.getText();
                    newPeer.presharedKey = preshareBG.getSelection().getActionCommand();
                    newPeer.privateKey = "auto";
                    newPeer.disabled = disabledCheckBox.isSelected();
                    response = apiClient.postWireguardPeer(newPeer);
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
                        apiClient.deleteWireguardInterface(interfaceID);
                        JOptionPane.showMessageDialog(owner,"Error: " + response.detail,"Error",JOptionPane.ERROR_MESSAGE);
                    }
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

    public void showClientConfig(ClientConfig config) throws Exception {
        JDialog dialog = new JDialog((Frame) null, "Client Config", true);
        dialog.setSize(600, 500);
        dialog.setLayout(new BorderLayout());

        // TEXT AREA (copiável)
        JTextArea textArea = new JTextArea(config.getConf());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);

        // BOTÃO COPY
        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(textArea.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(scrollPane, BorderLayout.CENTER);
        topPanel.add(copyButton, BorderLayout.SOUTH);

        // QR CODE (vamos tratar a seguir)
        JLabel qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        BufferedImage qrImage = generateQR(config.getConf());
        qrLabel.setIcon(new ImageIcon(qrImage));

        dialog.add(topPanel, BorderLayout.CENTER);
        dialog.add(qrLabel, BorderLayout.EAST);

        dialog.setVisible(true);
    }

    public BufferedImage generateQR(String text) throws Exception {
        int size = 300;

        BitMatrix matrix = new MultiFormatWriter().encode(
                text,
                BarcodeFormat.QR_CODE,
                size,
                size
        );

        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
