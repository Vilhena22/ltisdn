package Forms;

import ApiClient.ApiClient;
import Models.Interfaces.getAllInterfaces;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class UpdateIP extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField campoIPMask;
    private JLabel interfaceName;
    private boolean ipValid;
    private String addr;

    public UpdateIP(String ip,String inter) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        addr = ip;

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


        // regex para IPv4
        Pattern ipPattern = Pattern.compile(
                "^(?!(0\\.0\\.0\\.0|255\\.255\\.255\\.255))" +          // exclusões
                        "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                        "(25[0-4]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
                        "/(3[0-2]|[12]?\\d)$"
        );
        // InputVerifier para validar quando o foco sai do campo
        campoIPMask.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JFormattedTextField) input).getText().trim();
                ipValid = ipPattern.matcher(text).matches();
                return ipValid;
            }
        });

        interfaceName.setText(inter);
    }

    private void onOK() {
        if (!ipValid) {
            JOptionPane.showMessageDialog(campoIPMask, "Invalid IP!\nExample: 192.168.1.5/24", "Erro", JOptionPane.ERROR_MESSAGE);
        }else {
            ApiClient api = new ApiClient();
            try {
                api.UpdateAddress(addr,campoIPMask.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
