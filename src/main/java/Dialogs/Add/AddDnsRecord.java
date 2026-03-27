package Dialogs.Add;

import ApiClient.ApiClient;
import Models.Dns.DnsRecord;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class AddDnsRecord extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JFormattedTextField textFieldAddress;
    private JCheckBox checkBox;
    private JLabel nameLabel;
    private JLabel addressLabel;
    private JLabel ttlLabel;
    private JSpinner spinnerTTL;
    private final Pattern ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
    );


    public AddDnsRecord(Frame owner) {
        super(owner,"Add DNS Record",true);
        Font font = owner.getFont();
        SwingUtilities.updateComponentTreeUI(owner);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.setFont(font);
        buttonCancel.setFont(font);
        textFieldName.setFont(font);
        textFieldAddress.setFont(font);
        spinnerTTL.setFont(font);
        nameLabel.setFont(font);
        addressLabel.setFont(font);
        ttlLabel.setFont(font);
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



        //Format o campo de address com o formato de IP

        //Formata o spinner para aceitar apenas numeros
        JSpinner.NumberEditor numbersEditor = new JSpinner.NumberEditor(spinnerTTL);
        spinnerTTL.setEditor(numbersEditor);
        spinnerTTL.setModel(new javax.swing.SpinnerNumberModel(1, 1, 60000, 1));
        JFormattedTextField textField = numbersEditor.getTextField();
        NumberFormatter formatter = (NumberFormatter) textField.getFormatter();
        formatter.setAllowsInvalid(false);

    }

    private void onOK() {
        // add your code here
        if (!isValidAddress()) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Address!\nMust be 0.0.0.0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            textFieldAddress.setBackground(new Color(241, 0, 0, 25));
        }else {
            ApiClient apiClient = new ApiClient();
            try {
                DnsRecord dnsRecord = new DnsRecord();
                dnsRecord.name = textFieldName.getText();
                dnsRecord.address = textFieldAddress.getText();
                dnsRecord.ttl = spinnerTTL.getValue().toString();
                dnsRecord.disabled =checkBox.isSelected();

                apiClient.postDnsRecord(dnsRecord);

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

    private boolean isValidAddress() {
        String text = textFieldAddress.getText().trim();
        return ADDRESS_PATTERN.matcher(text).matches();
    }
}
