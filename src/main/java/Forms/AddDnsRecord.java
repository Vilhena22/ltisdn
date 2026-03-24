package Forms;

import ApiClient.ApiClient;
import Models.Dns.DnsRecord;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.regex.Pattern;

public class AddDnsRecord extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JFormattedTextField textFieldAddres;
    private JFormattedTextField textFieldTTL;
    private JCheckBox checkBox;
    private JLabel nameLabel;
    private JLabel addressLabel;
    private JLabel ttlLabel;
    private JSpinner spinnerTTL;

    public AddDnsRecord(Frame owner) {
        super(owner,"Add DNS Record",true);
        SwingUtilities.updateComponentTreeUI(owner);
        setContentPane(contentPane);
        setModal(true);
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



        //Formata o campo de address com o formato de IP

        // regex para IPv4
        Pattern ipPattern = Pattern.compile(
                "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                        "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
        );
        // InputVerifier para validar quando o foco sai do campo
        textFieldAddres.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JFormattedTextField) input).getText().trim();
                boolean valid = ipPattern.matcher(text).matches();
                if (!valid) {
                    JOptionPane.showMessageDialog(input, "Invalid IP!\nMust be 0.0.0.0", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                return valid;
            }
        });

        //Formata o spiner para aceitar apenas numeros
        JSpinner.NumberEditor numbersEditor = new JSpinner.NumberEditor(spinnerTTL);
        spinnerTTL.setEditor(numbersEditor);
        spinnerTTL.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        JFormattedTextField textField = numbersEditor.getTextField();
        NumberFormatter formatter = (NumberFormatter) textField.getFormatter();
        formatter.setAllowsInvalid(false);

    }

    private void onOK() {
        // add your code here
        ApiClient apiClient = new ApiClient();
        try {
            DnsRecord dnsRecord = new DnsRecord();
            dnsRecord.name = textFieldName.getText();
            dnsRecord.address = textFieldAddres.getText();
            dnsRecord.ttl = spinnerTTL.getValue().toString();
            dnsRecord.disabled =checkBox.isSelected();

            apiClient.postDnsRecord(dnsRecord);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
/*
    public static void main(String[] args) {
        AddDnsRecord dialog = new AddDnsRecord();
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
        System.exit(0);
    }*/
}
