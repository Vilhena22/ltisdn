package Dialogs.Add;

import ApiClient.ApiClient;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class addStaticRoute extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField dst;
    private JTextField gt;
    private JTextField rt;
    private JTextField maskTextField;
    private final JFrame owner;
    private final ApiClient apiClient;


    public addStaticRoute(JFrame owner, ApiClient apiClient) {
        super(owner, "Add Static Route", true);
        this.apiClient = apiClient;
        this.owner = owner;
        SwingUtilities.updateComponentTreeUI(owner);
        setContentPane(contentPane);

        getRootPane().setDefaultButton(buttonOK);

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
    private final Pattern ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
    );

    private void onOK() {

        // Validate address
        String address = dst.getText().trim();
        if (!ADDRESS_PATTERN.matcher(address).matches()) {
            JOptionPane.showMessageDialog(owner,
                    "Invalid Address!\nExample: 192.168.1.1",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validate gateway
        String gateway = gt.getText().trim();
        if (!ADDRESS_PATTERN.matcher(gateway).matches()) {
            JOptionPane.showMessageDialog(owner,
                    "Invalid Gateway!\nExample: 192.168.1.1",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validate address with gateway
        if (address.equals(gateway)) {
            JOptionPane.showMessageDialog(owner,
                    "Destination address and gateway must not be the same!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validate mask
        int mask;
        try {
            mask = Integer.parseInt(maskTextField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(owner,
                    "Mask must be a valid integer.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (mask < 0 || mask > 32) {
            JOptionPane.showMessageDialog(owner,
                    "Invalid Mask!\nMust be between 0 and 32 (inclusive)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Only runs when input is valid
        try {
            apiClient.addStaticRoute(address, gt.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(owner,
                    "Failed to add static route:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        dispose(); // Close window ONLY when everything is valid
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
