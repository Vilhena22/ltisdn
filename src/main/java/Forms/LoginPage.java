package Forms;

import ApiClient.ApiClient;
import Models.Router;
import Models.RouterDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginPage {
    private JPanel panel1;
    private JPanel loginPanel;
    private JPasswordField passwordField;
    private JFormattedTextField hostFormattedTextField;
    private JTextField userTextField;
    private JButton loginButton;
    private JTable deviceTable;
    private JButton clearFieldsButton;
    private JLabel logoLabel;
    private JScrollPane scrollPanel;
    private JPanel hostPanel;
    private JPanel userPanel;
    private JPanel passPanel;
    private JLabel singLabel;
    private JFrame owner;
    private final boolean isThemeDark;
    private ApiClient apiClient;

    private final Pattern ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
    );

    public LoginPage(boolean isThemeDark, JFrame frame) {

        this.owner = frame;
        this.isThemeDark = isThemeDark;
        owner.getRootPane().setDefaultButton(loginButton);
        if (isThemeDark) {
            setStyle(new Color(60, 63, 65), Color.WHITE);
        }else {
            setStyle(Color.WHITE, new Color(60, 63, 65));
        }

        loginButton.addActionListener(this::login);
        clearFieldsButton.addActionListener(this::clearFields);

        hostFormattedTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hostFormattedTextField.setBackground(owner.getBackground());
            }
        });

        userTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                userTextField.setBackground(owner.getBackground());
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                passwordField.setBackground(owner.getBackground());
            }
        });


        try {
            RouterDAO dao = new RouterDAO();
            String [] columNames = {"ID", "Address", "Username"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Router router : dao.getAllRouters()) {
                Object[] row = {
                        router.id,
                        router.host,
                        router.username,

                };
                model.addRow(row);
            }

            deviceTable.setModel(model);
            deviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            deviceTable.getTableHeader().setReorderingAllowed(false);
            deviceTable.getSelectionModel().addListSelectionListener(e -> {
                if (e.getValueIsAdjusting()) {
                    deviceTable.getSelectedRow();
                    hostFormattedTextField.setText(deviceTable.getValueAt(deviceTable.getSelectedRow(), 1).toString());
                    userTextField.setText(deviceTable.getValueAt(deviceTable.getSelectedRow(), 2).toString());

                }
            });
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            for (int i = 0; i < deviceTable.getColumnCount(); i++) {
                deviceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            deviceTable.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void clearFields(ActionEvent actionEvent) {
        userTextField.setText("");
        passwordField.setText("");
        hostFormattedTextField.setText("");

        userTextField.setBackground(owner.getBackground());
        passwordField.setBackground(owner.getBackground());
        hostFormattedTextField.setBackground(owner.getBackground());

    }

    private void login(ActionEvent actionEvent) {
        userTextField.setText("admin");
        passwordField.setText("microtik");
        hostFormattedTextField.setText("192.168.1.72");
        String username = userTextField.getText();
        String password = passwordField.getText();
        String host = hostFormattedTextField.getText();

        if (host.isEmpty() || !isValidAddress(host)) {
            JOptionPane.showMessageDialog(owner,
                    "Host Address Invalid",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            hostFormattedTextField.setBackground(new Color(241, 0, 15, 15));
            return;
        }

        if (username.isEmpty() ||  password.isEmpty()) {
            JOptionPane.showMessageDialog(owner,
                    "Fill Username and Password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            userTextField.setBackground(new Color(241, 0, 15, 15));
            passwordField.setBackground(new Color(241, 0, 15, 15));
            return;
        }

        try {
            apiClient = new ApiClient(username,password,host);
            String msg = apiClient.checkCredentials();
             if (msg.compareTo("Unauthorized") == 0){
                 JOptionPane.showMessageDialog(owner,
                         "Invalid Credentials",
                         "Error",
                         JOptionPane.ERROR_MESSAGE);
                 userTextField.setBackground(new Color(241, 0, 15, 15));
                 passwordField.setBackground(new Color(241, 0, 15, 15));
                 return;
             }
             SwingUtilities.invokeLater(() -> {
                try {

                    Router router = new Router();
                    router.host = host;
                    router.username = username;
                    router.password = password;
                    RouterDAO dao  = new RouterDAO().getInstance();
                    if (!dao.checkRouter(router)) {
                        dao.saveRouter(router);
                    }

                    HomePage homePage = new HomePage(isThemeDark,owner,apiClient,username,host);
                    owner.setContentPane(homePage.getMainPanel());
                    owner.revalidate();
                    owner.repaint();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (java.net.UnknownHostException e) {

            JOptionPane.showMessageDialog(owner,
                    "Invalid Host",
                    "Invalid Host",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(owner,
                    "Connection Error:\n" + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public JPanel getMainPanel() {
        return panel1;
    }


    private void setStyle(Color backColor, Color textColor) {
        //Define Font style
        Font font = new Font("JetBrains Mono", Font.PLAIN,14);

        //Set Background Colors
        //Panels Style
        Color buttonColor = new Color(255 - backColor.getRed(), 255 - backColor.getGreen(), 255 - backColor.getBlue(), 15);

        // hoverColor = 10~15% mais claro ou escuro dependendo da luminosidade
        float[] hsb = Color.RGBtoHSB(backColor.getRed(), backColor.getGreen(), backColor.getBlue(), null);
        float brightness = Math.min(hsb[2] * 1.15f, 1.0f); // +15% brilho
        Color hoverColor = Color.getHSBColor(hsb[0], hsb[1], brightness);

        JComponent[] components = {
            loginPanel,deviceTable,scrollPanel,panel1,hostFormattedTextField,userTextField,passwordField,
                userPanel,hostPanel,passPanel,singLabel

        };

        for (JComponent component : components){
            component.setBackground(backColor);
            component.setForeground(textColor);
            component.setFont(font);
        }



        // --- Botões ---
        AbstractButton[] buttons = {
                loginButton,clearFieldsButton
        };

        for (AbstractButton btn : buttons) {
            btn.setBackground(buttonColor);
            btn.setForeground(textColor);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setFont(font);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Aplica hover automático
            applyHoverEffect(btn, buttonColor, hoverColor);

            Image logo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/logo.png"))).getImage().getScaledInstance(173, 133, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(logo));
        }
        TitledBorder border = (TitledBorder) Objects.requireNonNull(scrollPanel).getBorder();
        border.setTitleColor(textColor);
        border.setTitleFont(new Font("JetBrains Mono", Font.PLAIN,25));
        singLabel.setFont(new Font("JetBrains Mono", Font.PLAIN,25));


    }

    private void applyHoverEffect(AbstractButton button, Color normal, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }

    private boolean isValidAddress(String text) {
        return ADDRESS_PATTERN.matcher(text.trim()).matches();
    }
}
