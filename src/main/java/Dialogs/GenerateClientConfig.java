package Dialogs;

import ApiClient.ApiClient;
import Models.Wireguard.ClientConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GenerateClientConfig extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    public GenerateClientConfig(ClientConfig config, JFrame owner) {
        super(owner,"Client Config",true);
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
        try {
            showClientConfig(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GenerateClientConfig(String peerID, JFrame owner,ApiClient apiClient) {
        super(owner,"Client Config",true);
        setContentPane(contentPane);
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
        try {
            showClientConfig(apiClient.getWireguardClinentConfig(peerID).getFirst());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void showClientConfig(ClientConfig config) throws Exception {
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

        contentPane.add(topPanel, BorderLayout.CENTER);
        contentPane.add(qrLabel, BorderLayout.EAST);

        contentPane.setVisible(true);
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
