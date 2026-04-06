package Forms;

import ApiClient.ApiClient;
import Dialogs.Add.*;
import Dialogs.Edit.UpdateIP;
import Dialogs.GenerateClientConfig;
import Dialogs.SetupDnsConfig;
import Dialogs.SetupWireguardConnection;
import Models.Address.GetAddress;
import Models.Dhcp.Clients.DhcpClient;
import Models.Dhcp.Leases.DhcpLease;
import Models.Dhcp.Networks.DhcpNetwork;
import Models.Dhcp.Servers.DhcpServer;
import Models.Dns.DnsCache;
import Models.Dns.DnsRecord;
import Models.Interfaces.bridge.interfaces.addNewInterfaceBridge;
import Models.Interfaces.bridge.interfaces.getInterfaceBridge;
import Models.Interfaces.bridge.ports.AddBridgePort;
import Models.Interfaces.bridge.ports.GetPorts;
import Models.Interfaces.getAllInterfaces;
import Models.Interfaces.wifi.interfaces.AddInterfaceWiFi;
import Models.Interfaces.wifi.interfaces.GetInterfacesWiFi;
import Models.ApiResponse;
import Models.Interfaces.wifi.securityProfiles.AddProfile;
import Models.Interfaces.wifi.securityProfiles.GetProfiles;
import Models.Route.Routes;
import Models.System.SystemResources;
import Models.System.SystemVersion;
import Models.TablesTypes;
import Models.Wireguard.InterfaceWG;
import Models.Wireguard.Peer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class HomePage {
    private final JFrame owner;
    private JPanel mainPanel;
    private JPanel navBar;
    private JButton routeButton;
    private JLabel hostIpText;
    private JLabel usernameText;
    private JPanel topBar;
    private JButton dhcpButton;
    private JButton interfaceButton;
    private JButton addressButton;
    private JButton homeButton;
    private JPanel contentPanel;
    private JButton dnsButton;
    private JLabel hostLabel;
    private JLabel userLabel;
    private JLabel cpuLabel;
    private JLabel cpuName;
    private JLabel coresLabel;
    private JLabel cores;
    private JLabel freqLabel;
    private JLabel frequency;
    private JLabel loadLabel;
    private JProgressBar progressBarLoad;
    private JPanel cpuPanel;
    private JLabel uptimeLabel;
    private JLabel versionLabel;
    private JLabel uptime;
    private JLabel version;
    private JLabel memoryLabel;
    private JLabel hddLabel;
    private JProgressBar progressBarMem;
    private JProgressBar progressBarHdd;
    private JPanel statsPanel;
    private JSeparator divider;
    private JPanel dnsPanel;
    private JButton clearCacheButton;
    private JTable cacheTable;
    private JButton addRecordButton;
    private JTabbedPane dnsTabbed;
    private JTable recordsTable;
    private JButton deleteRecordButton;
    private JPanel dhcpPanel;
    private JTabbedPane tabbedDhcp;
    private JTable dhcpNetworkTable;
    private JTable dhcpClientsTable;
    private JTable dhcpServerTable;
    private JTable dhcpLeasesTable;
    private JButton deleteDhcpButton;
    private JButton addNetworkButton;
    private JPanel addrPanel;
    private JTable addrTable;
    private JButton UpdateIPButton;
    private JButton removeIPButton;
    private JButton ableDisableAddrButton;
    private JButton addIPButton;
    private JPanel interfacePanel;
    private JButton ableDisableInterfaceButton;
    private JButton deleteInterfaceButton;
    private JTable interfaceTable;
    private JComboBox comboBoxInterfaces;
    private JButton addInterfaceButton;
    private JTabbedPane tabbedPaneWiFi;
    private JTable tableInterfacesWiFi;
    private JTable tableSP;
    private JScrollPane interfacesAllPanel;
    private JButton addLeaseButton;
    private JButton addClientButton;
    private JButton addServerButton;
    private JToggleButton editDhcpButton;
    private JPanel routePanel;
    private JButton deleteStaticRouteButton;
    private JButton addStaticRouteButton;
    private JButton ableDisableStaticRouteButton;
    private JTable tableRoute;
    private JTabbedPane tabbedPaneBridge;
    private JTable tableInterfacesBridge;
    private JTable tablePortsBridge;
    private JToggleButton editRecordsToggleButton;
    private JToggleButton checkUpdateButton;
    private JPanel updatePanel;
    private JButton updateButton;
    private JLabel chanelLabel;
    private JLabel installedLabel;
    private JLabel latestLabel;
    private JLabel statusLabel;
    private JLabel status;
    private JLabel latest;
    private JLabel installed;
    private JLabel chanel;
    private JButton logoutButton;
    private JSeparator dividerCheck;
    private JPanel wireGuardPanel;
    private JTabbedPane wireguardTabbedPane;
    private JTable wireInterfacesTable;
    private JTable wirePeersTable;
    private JButton deleteWireButton;
    private JButton addWireguardButton;
    private JButton wireguardButton;
    private JToggleButton editWireGuardButton;
    private JButton disAbleWireguardButton;
    private JButton setupWireGuardButton;
    private JButton generateConfButton;
    private JToggleButton editInterfacesButton;
    private JToggleButton editRouteButton;
    private JButton setupDnsConfigButton;
    private JToggleButton editAddressButton;
    private final ApiClient apiClient;
    private final boolean isDarkMode;

    private final Pattern ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
    );

    private final Pattern NETWORK_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                    "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)/(3[0-2]|[12]?\\d)$"
    );


    public HomePage(Boolean isThemeDark, JFrame owner,ApiClient apiClient,String username,String host) {
        this.owner = owner;
        this.isDarkMode = isThemeDark;
        this.apiClient = apiClient;
        hostIpText.setText(host);
        usernameText.setText(username);
        owner.getRootPane().setDefaultButton(null);

        comboBoxInterfaces.addActionListener(e -> interfaceTable());

        try {
            setDashboardValues();
            if (isThemeDark) {          //Grey                        //White
                buildChart(new Color(60, 63, 65), new Color(242, 242, 242));
                setStyle(new Color(60, 63, 65),new Color(242, 242, 242));
            } else {
                buildChart(new Color(242, 242, 242), new Color(60, 63, 65));
                setStyle(new Color(242, 242, 242),new Color(60, 63, 65));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Navbar Buttons Functions
        logoutButton.addActionListener(this::btnLogout);
        homeButton.addActionListener(this::btnShowHomePanel);
        dnsButton.addActionListener(this::btnShowDnsPanel);
        dhcpButton.addActionListener(this::btnShowDhcpPanel);
        wireguardButton.addActionListener(this::btnShowWireguardPanel);
        addressButton.addActionListener(this::btnShowAddressPanel);
        interfaceButton.addActionListener(this::btnShowInterfacePanel);
        routeButton.addActionListener(this::btnShowRoutePanel);

        //Wireguard Buttons Functions
        editWireGuardButton.addActionListener(this::btnEditWireguard);
        addWireguardButton.addActionListener(this::btnAddWireguard);
        disAbleWireguardButton.addActionListener(this::btnDisAbleWireguard);
        setupWireGuardButton.addActionListener(this::btnSetupWireguardConnection);
        deleteWireButton.addActionListener(this::btnDeleteWireguard);
        generateConfButton.addActionListener(this::btnGenerateConfig);

        //DNS Buttons Functions
        clearCacheButton.addActionListener(this::btnClearCache);
        addRecordButton.addActionListener(this::btnAddRecord);
        editRecordsToggleButton.addActionListener(this::btnEditRecord);
        deleteRecordButton.addActionListener(this::btnDeleteRecord);
        deleteDhcpButton.addActionListener(this::btnDeleteButton);
        setupDnsConfigButton.addActionListener(this::btnConfigDns);

        //Address Buttons Functions
        addIPButton.addActionListener(this::btnAddIP);
        ableDisableAddrButton.addActionListener(this::btnAbleDisableAddr);
        removeIPButton.addActionListener(this::btnRemoveIP);
        UpdateIPButton.addActionListener(this::btnUpdateIP);
        editAddressButton.addActionListener(this::btnEditAddress);

        //Interface Buttons Functions

        ableDisableInterfaceButton.addActionListener(this::btnAbleDisableInterface);
        deleteInterfaceButton.addActionListener(this::btnDeleteInterface);
        addInterfaceButton.addActionListener(this::btnAddInterface);

        //DHCP Buttons Functions
        editDhcpButton.addActionListener(this::btnEditDhcpButton);
        addNetworkButton.addActionListener(this::btnAddPool);
        addLeaseButton.addActionListener(this::btnAddLease);
        addClientButton.addActionListener(this::btnAddClient);
        addServerButton.addActionListener(this::btnAddServer);

        //Route Buttons Functions

        deleteStaticRouteButton.addActionListener(this::btnRouteDelete);
        addStaticRouteButton.addActionListener(this::btnRouteAdd);
        ableDisableStaticRouteButton.addActionListener(this::btnRouteAbleDisabled);
        editInterfacesButton.addActionListener(this::btnEditInterfacesButton);
        editRouteButton.addActionListener(this::btnEditRouteButton);

        wireguardTabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e){
                super.mouseClicked(e);
                clearTableSelection(TablesTypes.WIREGUARD);
                switch (wireguardTabbedPane.getSelectedIndex()) {
                    case 0:
                        generateConfButton.setVisible(false);
                        generateConfButton.setEnabled(false);
                        deleteWireButton.setEnabled(false);
                        disAbleWireguardButton.setEnabled(false);
                        fillInterfacesWireGuardTable(false);
                        break;
                    case 1:
                        generateConfButton.setEnabled(false);
                        deleteWireButton.setEnabled(false);
                        disAbleWireguardButton.setEnabled(false);
                        generateConfButton.setVisible(true);
                        fillPeersWireGuardTable(false);
                        break;
                }

            }
        });

        tabbedPaneWiFi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                interfaceTable();
            }
        });
        tabbedPaneBridge.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                interfaceTable();
            }
        });

    //Listener para trocar os botoes consoante a tabela selecionada
        dnsTabbed.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked (MouseEvent e){
            super.mouseClicked(e);
            switch (dnsTabbed.getSelectedIndex()) {
                case 0:
                    fillDnsCacheTable();
                    addRecordButton.setVisible(false);
                    deleteRecordButton.setVisible(false);
                    clearCacheButton.setVisible(true);
                    editRecordsToggleButton.setVisible(false);
                    break;
                case 1:
                    editRecordsToggleButton.setSelected(false);
                    editRecordsToggleButton.setText("Enable Edit");
                    fillDnsRecordsTable(false);
                    addRecordButton.setVisible(true);
                    deleteRecordButton.setVisible(true);
                    editRecordsToggleButton.setVisible(true);
                    clearCacheButton.setVisible(false);
                    break;
            }

        }
    });

    //Listener para trocar os botoes consoante a tabela selecionada
        tabbedDhcp.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked (MouseEvent e){
            super.mouseClicked(e);
            switch (tabbedDhcp.getSelectedIndex()) {
                case 0:
                    fillDhcpLeaseTable(false);
                    addNetworkButton.setVisible(false);
                    addLeaseButton.setVisible(true);
                    addClientButton.setVisible(false);
                    addServerButton.setVisible(false);
                    break;
                case 1:
                    fillDhcpServerTable(false);
                    addNetworkButton.setVisible(false);
                    addLeaseButton.setVisible(false);
                    addClientButton.setVisible(false);
                    addServerButton.setVisible(true);
                    break;
                case 2:
                    fillDhcpClientTable(false);
                    addNetworkButton.setVisible(false);
                    addLeaseButton.setVisible(false);
                    addClientButton.setVisible(true);
                    addServerButton.setVisible(false);
                    break;
                case 3:
                    fillDhcpNetworkTable(false);
                    addNetworkButton.setVisible(true);
                    addLeaseButton.setVisible(false);
                    addClientButton.setVisible(false);
                    addServerButton.setVisible(false);
                    break;
            }
        }
    });

        //Cria Listeners para quando tiver itens selecionados o botao delete ativa
        tableInterfacesWiFi.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return; // evita eventos duplicados

            int row = tableInterfacesWiFi.getSelectedRow();
            if (row == -1) return; // evita exceção quando nada está selecionado

            int disableCol = tableInterfacesWiFi.getColumnCount() - 1;

            Boolean disabled = (Boolean) tableInterfacesWiFi.getValueAt(row, disableCol);

            if (Boolean.FALSE.equals(disabled)) {
                ableDisableInterfaceButton.setText("Disable");
            } else {
                ableDisableInterfaceButton.setText("Enable");
            }
        });

        tableSP.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return; // evita eventos duplicados

            int row = tableSP.getSelectedRow();
            if (row == -1) return; // evita exceção quando nada está selecionado

            int disableCol = tableSP.getColumnCount() - 1;

            Boolean disabled = (Boolean) tableSP.getValueAt(row, disableCol);

            if (Boolean.FALSE.equals(disabled)) {
                ableDisableInterfaceButton.setText("Disable");
            } else {
                ableDisableInterfaceButton.setText("Enable");
            }
        });

        tableInterfacesBridge.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return; // evita eventos duplicados

            int row = tableInterfacesBridge.getSelectedRow();
            if (row == -1) return; // evita erro quando nada está selecionado

            int disableCol = tableInterfacesBridge.getColumnCount() - 1;

            Boolean disabled = (Boolean) tableInterfacesBridge.getValueAt(row, disableCol);

            if (Boolean.FALSE.equals(disabled)) {
                ableDisableInterfaceButton.setText("Disable");
            } else {
                ableDisableInterfaceButton.setText("Enable");
            }
        });

        tablePortsBridge.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int row = tablePortsBridge.getSelectedRow();
            if (row == -1) return;

            int disableCol = tablePortsBridge.getColumnCount() - 1;

            Boolean disabled = (Boolean) tablePortsBridge.getValueAt(row, disableCol);

            if (disabled != null && !disabled) {
                ableDisableInterfaceButton.setText("Disable");
            } else {
                ableDisableInterfaceButton.setText("Enable");
            }
        });

        recordsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = recordsTable.getSelectedRow() != -1;
                deleteRecordButton.setEnabled(isSelected);
            }
        });

        dhcpNetworkTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpNetworkTable.getSelectedRow() != -1;
                deleteDhcpButton.setEnabled(isSelected);
                editDhcpButton.setEnabled(isSelected);
            }
        });

        dhcpLeasesTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpLeasesTable.getSelectedRow() != -1;
                deleteDhcpButton.setEnabled(isSelected);
                editDhcpButton.setEnabled(isSelected);
            }
        });

        dhcpClientsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpClientsTable.getSelectedRow() != -1;
                deleteDhcpButton.setEnabled(isSelected);
                editDhcpButton.setEnabled(isSelected);
            }
        });

        dhcpServerTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpServerTable.getSelectedRow() != -1;
                editDhcpButton.setEnabled(isSelected);
                deleteDhcpButton.setEnabled(isSelected);
            }
        });

        wirePeersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean isSelected = wirePeersTable.getSelectedRow() != -1;
                if (isSelected) {
                    String state = wirePeersTable.getValueAt(wirePeersTable.getSelectedRow(),5).toString();
                    if(state.compareTo("false") == 0){
                        disAbleWireguardButton.setText("Disable Peer");
                    }else{
                        disAbleWireguardButton.setText("Enable Peer");
                    }
                    disAbleWireguardButton.setEnabled(isSelected);
                    deleteWireButton.setEnabled(isSelected);
                    generateConfButton.setEnabled(isSelected);
                }
            }
        });

        wireInterfacesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean isSelected = wireInterfacesTable.getSelectedRow() != -1;
                if (isSelected) {
                    String state = wireInterfacesTable.getValueAt(wireInterfacesTable.getSelectedRow(),4).toString();
                    if(state.compareTo("false") == 0){
                        disAbleWireguardButton.setText("Disable Interface");
                    }else{
                        disAbleWireguardButton.setText("Enable Interface");
                    }
                    disAbleWireguardButton.setEnabled(isSelected);
                    deleteWireButton.setEnabled(isSelected);
                }

            }
        });

        checkUpdateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean isSelect = checkUpdateButton.isSelected();
                if (isSelect) {
                    new Thread(() -> {
                        try {
                            List<SystemVersion> list;
                            list = apiClient.getSystemVersion();
                            SystemVersion systemVersion;
                            if (!list.isEmpty()) {
                                systemVersion = list.getLast();
                                chanel.setText(systemVersion.channel);
                                installed.setText(systemVersion.installedVersion);
                                chanel.setText(systemVersion.channel);
                                if (systemVersion.latestVersion != null) {
                                    latest.setText(systemVersion.latestVersion);
                                    status.setText(systemVersion.status);
                                    updateButton.setVisible(true);
                                }
                                checkUpdateButton.setText("Close");
                                updatePanel.setVisible(true);
                                dividerCheck.setVisible(true);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }).start();
                }else {
                    checkUpdateButton.setText("Check Updates");
                    updatePanel.setVisible(false);
                    dividerCheck.setVisible(false);
                }
            }
        });

        addrTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean isSelected = addrTable.getSelectedRow() != -1;
                if (isSelected) {
                    String state = addrTable.getValueAt(addrTable.getSelectedRow(),3).toString();
                    if(state.compareTo("false") == 0){
                        ableDisableAddrButton.setText("Disable Address");
                    }else{
                        ableDisableAddrButton.setText("Enable Address");
                    }
                    UpdateIPButton.setEnabled(isSelected);
                    ableDisableAddrButton.setEnabled(isSelected);
                    ableDisableAddrButton.setEnabled(isSelected);
                }
            }
        });
        tableRoute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = tableRoute.getSelectedRow();
                int dis = tableRoute.getColumnCount() -1;
                if (tableRoute.getValueAt(row, dis).equals(false)){
                    ableDisableStaticRouteButton.setText("Disable");
                }else {
                    ableDisableStaticRouteButton.setText("Enable");
                }
            }
        });
    }

    private void btnConfigDns(ActionEvent actionEvent) {
        SetupDnsConfig dnsConfig = new SetupDnsConfig(owner,apiClient);
        dnsConfig.pack();
        dnsConfig.setLocationRelativeTo(owner);
        dnsConfig.setVisible(true);
    }

    private void btnEditRouteButton(ActionEvent actionEvent) {
        clearTableSelection(TablesTypes.ROUTE);
        boolean isEdited = editRouteButton.isSelected();

        //System.out.println(isEdited);
        if (isEdited) {
            editRouteButton.setText("Disable Edit");
        }else {
            editRouteButton.setText("Enable Edit");
        }
        fillStaticRouteTable(isEdited);
        clearTableSelection(TablesTypes.ROUTE);
    }

    private void btnDeleteWireguard(ActionEvent actionEvent) {
        switch (wireguardTabbedPane.getSelectedIndex()){
            case 0:
                for (int row : wireInterfacesTable.getSelectedRows() ){
                    try {
                        String id = wireInterfacesTable.getValueAt(row,0).toString();
                        apiClient.deleteWireguardInterface(id);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection(TablesTypes.WIREGUARD);
                fillInterfacesWireGuardTable(false);
                break;
            case 1:
                for (int row : wirePeersTable.getSelectedRows() ){
                    try {
                        String id = wirePeersTable.getValueAt(row,0).toString();
                        apiClient.deleteWireguardPeer(id);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection(TablesTypes.WIREGUARD);
                fillPeersWireGuardTable(false);
                break;
        }
    }

    private void btnGenerateConfig(ActionEvent actionEvent) {
        if (wireguardTabbedPane.getSelectedIndex() == 1){
            GenerateClientConfig gcc = new GenerateClientConfig(wirePeersTable.getValueAt(wirePeersTable.getSelectedRow(),0).toString(),owner,apiClient);
            gcc.pack();
            gcc.setLocationRelativeTo(owner);
            gcc.setVisible(true);
        }
    }

    private void btnSetupWireguardConnection(ActionEvent actionEvent) {
        SetupWireguardConnection connection = new SetupWireguardConnection(owner,apiClient);
        connection.pack();
        connection.setLocationRelativeTo(owner);
        connection.setVisible(true);
    }

    private void btnDisAbleWireguard(ActionEvent actionEvent) {
        switch (wireguardTabbedPane.getSelectedIndex()){
            case 0:
                try {
                    String id = wireInterfacesTable.getValueAt(wireInterfacesTable.getSelectedRow(),0).toString();
                    boolean state = Boolean.parseBoolean(wireInterfacesTable.getValueAt(wireInterfacesTable.getSelectedRow(),4).toString());
                    apiClient.dis_ableWireguarInterface(id,state);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                fillInterfacesWireGuardTable(false);
                break;
            case 1:
                try {
                    String id = wirePeersTable.getValueAt(wirePeersTable.getSelectedRow(),0).toString();
                    boolean state = Boolean.parseBoolean(wirePeersTable.getValueAt(wirePeersTable.getSelectedRow(),6).toString());
                    apiClient.dis_ableWireguarPeer(id,state);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                fillPeersWireGuardTable(false);
                break;
        }
    }

    private void btnAddWireguard(ActionEvent actionEvent) {
        switch (wireguardTabbedPane.getSelectedIndex()){
            case 0:
                AddWireguardInterface interWG = new AddWireguardInterface(owner,apiClient);
                interWG.pack();
                interWG.setLocationRelativeTo(owner);
                interWG.setVisible(true);
                fillInterfacesWireGuardTable(false);
                break;
            case 1:
                AddWireguardPeer peer = new AddWireguardPeer(owner,apiClient);
                peer.pack();
                peer.setLocationRelativeTo(owner);
                peer.setVisible(true);
                fillPeersWireGuardTable(false);
                break;
        }
    }

    private void btnEditWireguard(ActionEvent actionEvent) {
        boolean isEdited = editWireGuardButton.isSelected();
        System.out.println(isEdited);
        if (isEdited) {
            editWireGuardButton.setText("Disable Edit");
        }else {
            editWireGuardButton.setText("Enable Edit");
        }
        switch (wireguardTabbedPane.getSelectedIndex()) {
            case 0:
                fillInterfacesWireGuardTable(isEdited);
                break;
            case 1:
                fillPeersWireGuardTable(isEdited);
                break;
        }
        clearTableSelection(TablesTypes.WIREGUARD);

    }

    private void btnLogout(ActionEvent actionEvent) {
        owner.setContentPane(new LoginPage(isDarkMode,owner).getMainPanel());
        owner.revalidate();
        owner.repaint();

    }

    private void btnRouteAbleDisabled(ActionEvent actionEvent) {
        int selected = tableRoute.getSelectedRow();
        String id = tableRoute.getValueAt(selected, 0).toString();
        int lastCol = tableRoute.getColumnCount() - 1;

        boolean state = (boolean) tableRoute.getValueAt(selected, lastCol);

        try {
            apiClient.StaticRouteState(id, state);
            fillStaticRouteTable(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnRouteAdd(ActionEvent actionEvent) {
        try {
            addStaticRoute rota = new addStaticRoute(owner,apiClient);
            rota.pack();
            rota.setLocationRelativeTo(owner);
            rota.setVisible(true);
            fillStaticRouteTable(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnRouteDelete(ActionEvent actionEvent) {
        int selected = tableRoute.getSelectedRow();
        String id = tableRoute.getValueAt(selected, 0).toString();

        try {
            apiClient.deleteRotaEstatica(id);
            fillStaticRouteTable(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnShowWireguardPanel(ActionEvent actionEvent) {
        routePanel.setVisible(false);
        cpuPanel.setVisible(false);
        addrPanel.setVisible(false);
        dnsPanel.setVisible(false);
        dhcpPanel.setVisible(false);
        interfacePanel.setVisible(false);
        statsPanel.setVisible(false);
        wireGuardPanel.setVisible(true);
        updatePanel.setVisible(false);
        dividerCheck.setVisible(false);


        fillInterfacesWireGuardTable(false);
    }

    private void btnShowRoutePanel(ActionEvent actionEvent) {
        routePanel.setVisible(true);
        cpuPanel.setVisible(false);
        addrPanel.setVisible(false);
        dnsPanel.setVisible(false);
        dhcpPanel.setVisible(false);
        interfacePanel.setVisible(false);
        statsPanel.setVisible(false);
        wireGuardPanel.setVisible(false);
        updatePanel.setVisible(false);
        dividerCheck.setVisible(false);
        fillStaticRouteTable(false);

    }

    private void fillStaticRouteTable(boolean isEditing) {
        Set<Integer> editableColuns = new HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(3);
        }

        String[] columNames = {"ID", "Gateway", "Static", "Destination Address", "Routing table","Disabled"};
        DefaultTableModel model = new DefaultTableModel(columNames, 0);

        try {
            for (Routes route : apiClient.getStaticRoute()) {
                Object[] row = {
                        route.id,
                        route.gateway,
                        route._static,
                        route.dst_address,
                        route.routing_table,
                        route.disabled
                };
                model.addRow(row);
            }
            formatTable(tableRoute, model);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("false");
        comboBox.addItem("true");

        tableRoute.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBox));
        tableRoute.putClientProperty("terminateEditOnFocusLost", true);

        //Este listener é o responsavel por esta à espera que cliques no enter ou fora da box
        tableRoute.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;

                comboBox.setSelectedItem(tableRoute.getValueAt(row, col));

                Routes route = new Routes();
                route.dst_address = tableRoute.getValueAt(row, 3).toString();
                route.gateway = tableRoute.getValueAt(row, 1).toString();
                route.id = tableRoute.getValueAt(row, 0).toString();
                //boolean state = Boolean.valueOf(tableRoute.getValueAt(row, 5).toString());

                try {
                    apiClient.editStaticRoute(route.id, route.gateway, route.dst_address, route.disabled);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    private boolean isValidAddress(String text) {
        return ADDRESS_PATTERN.matcher(text.trim()).matches();
    }

    private boolean isValidNetwork(String text) {
        return NETWORK_PATTERN.matcher(text.trim()).matches();
    }


    private void btnAddInterface(ActionEvent actionEvent) {
         String selected = (String) comboBoxInterfaces.getSelectedItem();
         if (Objects.requireNonNull(selected).compareTo("Wi-Fi") == 0){
             if (tabbedPaneWiFi.getTitleAt(tabbedPaneWiFi.getSelectedIndex()).compareTo("Interfaces") == 0) {
                 addInterfaceWiFi novaInterfaceWiFi = new addInterfaceWiFi(owner,apiClient);
                 novaInterfaceWiFi.pack();
                 novaInterfaceWiFi.setLocationRelativeTo(owner);
                 novaInterfaceWiFi.setVisible(true);
             }
             if (tabbedPaneWiFi.getTitleAt(tabbedPaneWiFi.getSelectedIndex()).compareTo("Security Profiles") == 0){
                 addWiFiSP novoPerfil = new addWiFiSP(owner,apiClient);
                 novoPerfil.pack();
                 novoPerfil.setLocationRelativeTo(owner);
                 novoPerfil.setVisible(true);
             }
         }else {
             if (tabbedPaneBridge.getTitleAt(tabbedPaneBridge.getSelectedIndex()).compareTo("Interfaces") == 0){
                 addInterfaceBridge novaInterfaceBridge = new addInterfaceBridge(owner,apiClient);
                 novaInterfaceBridge.pack();
                 novaInterfaceBridge.setLocationRelativeTo(owner);
                 novaInterfaceBridge.setVisible(true);
                 fillBridgeInterfacesTable(false);
             }
             if (tabbedPaneBridge.getTitleAt(tabbedPaneBridge.getSelectedIndex()).compareTo("Ports") == 0){
                 addBridgePort newPort = new addBridgePort(owner,apiClient);
                 newPort.pack();
                 newPort.setLocationRelativeTo(owner);
                 newPort.setVisible(true);
             }
         }
         interfaceTable();
    }

    private void btnDeleteInterface(ActionEvent actionEvent) {

        String comboBoxValue = (String) comboBoxInterfaces.getSelectedItem();
        int selected;
        String id = "";
        String endpoint = "";

        switch (Objects.requireNonNull(comboBoxValue)) {
            case "Wi-Fi":
                if (tabbedPaneWiFi.getSelectedIndex() == 0) {
                    selected = tableInterfacesWiFi.getSelectedRow();
                    id = tableInterfacesWiFi.getValueAt(selected, 0).toString();
                    endpoint = "/interface/wifi/" + id;
                }else{
                    selected = tableSP.getSelectedRow();
                    id = tableSP.getValueAt(selected, 0).toString();
                    endpoint = "/interface/wifi/security/" + id;
                }
                break;
            case "Bridge":
                if (tabbedPaneBridge.getSelectedIndex() == 0) {
                    selected = tableInterfacesBridge.getSelectedRow();
                    id = tableInterfacesBridge.getValueAt(selected, 0).toString();
                    endpoint = "/interface/bridge/" + id;
                }else {
                    selected = tablePortsBridge.getSelectedRow();
                    id = tablePortsBridge.getValueAt(selected, 0).toString();
                    endpoint = "/interface/bridge/port/" + id;
                }
                break;
        }
        try {
            apiClient.deleteInterface(endpoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        interfaceTable();
    }

    private void btnAbleDisableInterface(ActionEvent actionEvent) {
        int selectedRow;
        int lastCol;

        switch ((String) Objects.requireNonNull(comboBoxInterfaces.getSelectedItem())) {
            case "Wi-Fi":
                if (tabbedPaneWiFi.getTitleAt(tabbedPaneWiFi.getSelectedIndex()).compareTo("Interfaces") == 0) {
                    selectedRow = tableInterfacesWiFi.getSelectedRow();
                    lastCol = tableInterfacesWiFi.getColumnCount() - 1;
                    try {
                        apiClient.estadoInterface(tableInterfacesWiFi.getValueAt(selectedRow, 0).toString(), (Boolean) tableInterfacesWiFi.getValueAt(selectedRow, lastCol));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    selectedRow = tableSP.getSelectedRow();
                    lastCol = tableSP.getColumnCount() - 1;
                    try {
                        apiClient.estadoInterface(tableSP.getValueAt(selectedRow, 0).toString(), (Boolean) tableSP.getValueAt(selectedRow, lastCol));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "Bridge":
                if (tabbedPaneBridge.getTitleAt(tabbedPaneBridge.getSelectedIndex()).compareTo("Interfaces") == 0) {
                    selectedRow = tableInterfacesBridge.getSelectedRow();
                    lastCol = tableInterfacesBridge.getColumnCount() - 1;
                    try {
                        apiClient.estadoInterface(tableInterfacesBridge.getValueAt(selectedRow, 0).toString(), (Boolean) tableInterfacesBridge.getValueAt(selectedRow, lastCol));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    selectedRow = tablePortsBridge.getSelectedRow();
                    lastCol = tablePortsBridge.getColumnCount() - 1;
                    try {
                        apiClient.bridgePortState(tablePortsBridge.getValueAt(selectedRow, 0).toString(), (Boolean) tablePortsBridge.getValueAt(selectedRow, lastCol));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
        }
        interfaceTable();
    }

    private void interfaceTable() {

        String comboBoxValue = (String) comboBoxInterfaces.getSelectedItem();
        DefaultTableModel model;

        switch (Objects.requireNonNull(comboBoxValue)) {
            case "All interfaces":
                addInterfaceButton.setVisible(false);
                deleteInterfaceButton.setVisible(false);
                ableDisableInterfaceButton.setVisible(false);
                interfacesAllPanel.setVisible(true);
                tabbedPaneWiFi.setVisible(false);
                tabbedPaneBridge.setVisible(false);
                editInterfacesButton.setVisible(false);
                try {
                    String[] colsAll = {"ID", "Name", "Running", "Disabled"};
                    model = new DefaultTableModel(colsAll, 0);
                    for (getAllInterfaces interf : apiClient.getAllInterfaces()) {
                        Object[] row = {
                                interf.id,
                                interf.name,
                                interf.running,
                                interf.disabled
                        };
                        model.addRow(row);
                    }
                    tabbedPaneWiFi.setVisible(false);
                    tabbedPaneBridge.setVisible(false);
                    formatTable(interfaceTable, model);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

            case "Wi-Fi":
                addInterfaceButton.setVisible(true);
                deleteInterfaceButton.setVisible(true);
                ableDisableInterfaceButton.setVisible(true);
                editInterfacesButton.setVisible(true);
                interfacesAllPanel.setVisible(false);
                if (tabbedPaneWiFi.getTitleAt(tabbedPaneWiFi.getSelectedIndex()).compareTo("Interfaces") == 0) {
                    fillWifiInterfacesTable(false);
                }else {
                    fillWifiSpTable(false);
                }
                break;

            case "Bridge":
                addInterfaceButton.setVisible(true);
                deleteInterfaceButton.setVisible(true);
                ableDisableInterfaceButton.setVisible(true);
                editInterfacesButton.setVisible(true);
                interfacesAllPanel.setVisible(false);
                if (tabbedPaneBridge.getSelectedIndex() == 0) {
                    fillBridgeInterfacesTable(false);
                }else {
                    fillBridgePortsTable(false);
                }
                break;
        }

    }

    private void btnEditInterfacesButton(ActionEvent actionEvent) {
        clearTableSelection(TablesTypes.BRIDGE);
        clearTableSelection(TablesTypes.WIFI);
        boolean isEdited = editInterfacesButton.isSelected();

        //System.out.println(isEdited);
        if (isEdited) {
            editInterfacesButton.setText("Disable Edit");
        }else {
            editInterfacesButton.setText("Enable Edit");
        }
        if (tabbedPaneBridge.getTitleAt(tabbedPaneBridge.getSelectedIndex()).compareTo("Interfaces") == 0){
            fillBridgeInterfacesTable(isEdited);
        }else {
            fillBridgePortsTable(isEdited);
        }
        if (tabbedPaneWiFi.getTitleAt(tabbedPaneWiFi.getSelectedIndex()).compareTo("Interfaces") == 0){
            fillWifiInterfacesTable(isEdited);
        }else {
            fillWifiSpTable(isEdited);
        }
        clearTableSelection(TablesTypes.BRIDGE);
        clearTableSelection(TablesTypes.WIFI);
    }

    private void btnUpdateIP(ActionEvent actionEvent) {
        if (addrTable.getSelectedRow() > 0) {
            int selectedRow = addrTable.getSelectedRow();
            UpdateIP update = new UpdateIP(addrTable.getValueAt(selectedRow, 0).toString(), (String) addrTable.getValueAt(selectedRow, 1),owner,apiClient);
            update.pack();
            update.setLocationRelativeTo(owner);
            update.setVisible(true);
            AddressTable(false);
        }
    }

    private void btnEditAddress(ActionEvent actionEvent) {
        clearTableSelection(TablesTypes.ADDRESS);
        boolean isEdited = editAddressButton.isSelected();
        if (isEdited) {
            editAddressButton.setText("Disable Edit");
        }else {
            editAddressButton.setText("Enable Edit");
        }
        AddressTable(isEdited);
    }

    private void btnRemoveIP(ActionEvent actionEvent) {
        int[] selectedRows = addrTable.getSelectedRows();
        for (int row : selectedRows) {
            String id  = addrTable.getValueAt(row, 0).toString();
            try {
                apiClient.DeleteAddress(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        AddressTable(false);
    }

    private void btnAbleDisableAddr(ActionEvent actionEvent) {
        int selectedRow = addrTable.getSelectedRow();
        try {
            System.out.println( apiClient.EstadoIPAddress(addrTable.getValueAt(selectedRow, 0).toString(), (Boolean) addrTable.getValueAt(selectedRow, 3)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        AddressTable(false);
    }

    private void btnAddIP(ActionEvent actionEvent) {
        addAddress novoIP = new addAddress(owner,apiClient);
        novoIP.pack();
        novoIP.setLocationRelativeTo(owner);
        novoIP.setVisible(true);
        AddressTable(false);
    }

    private void btnShowAddressPanel(ActionEvent actionEvent) {
        addrPanel.setVisible(true);
        routePanel.setVisible(false);
        dnsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        statsPanel.setVisible(false);
        interfacePanel.setVisible(false);
        dhcpPanel.setVisible(false);
        ableDisableAddrButton.setEnabled(false);
        UpdateIPButton.setEnabled(false);
        updatePanel.setVisible(false);
        wireGuardPanel.setVisible(false);
        dividerCheck.setVisible(false);
        AddressTable(false);
    }

    private void btnAddServer(ActionEvent actionEvent) {
        AddDhcpServer dialog = new AddDhcpServer(owner,apiClient);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
        fillDhcpServerTable(false);
    }

    private void btnEditDhcpButton(ActionEvent actionEvent) {
        clearTableSelection(TablesTypes.DHCP);
        boolean isEdited = editDhcpButton.isSelected();
        if (isEdited) {
            editDhcpButton.setText("Disable Edit");
        }else {
            editDhcpButton.setText("Enable Edit");
        }
        switch (tabbedDhcp.getTitleAt(tabbedDhcp.getSelectedIndex()).toLowerCase()) {
            case "leases":
                fillDhcpLeaseTable(isEdited);
                break;
            case "clients":
                fillDhcpClientTable(isEdited);
                break;
            case "server":
                fillDhcpServerTable(isEdited);
                break;
            case "networks":
                fillDhcpNetworkTable(isEdited);
                break;
        }
        clearTableSelection(TablesTypes.DHCP);
    }

    private void btnAddClient(ActionEvent actionEvent) {
        AddDhcpClient clientDialog = new AddDhcpClient(owner,apiClient);
        clientDialog.pack();
        clientDialog.setLocationRelativeTo(owner);
        clientDialog.setVisible(true);
        fillDhcpClientTable(false);
    }

    private void btnAddLease(ActionEvent actionEvent) {
        AddDhcpLease leaseDialog = new AddDhcpLease(owner,apiClient);
        leaseDialog.pack();
        leaseDialog.setLocationRelativeTo(owner);
        leaseDialog.setVisible(true);
        fillDhcpLeaseTable(false);
    }

    private void btnAddPool(ActionEvent actionEvent) {
        AddDhcpNetwork poolDialog = new AddDhcpNetwork(owner,apiClient);
        poolDialog.pack();
        poolDialog.setLocationRelativeTo(owner);
        poolDialog.setVisible(true);
        fillDhcpNetworkTable(false);

    }

    private void btnDeleteButton(ActionEvent actionEvent) {
        switch (tabbedDhcp.getTitleAt(tabbedDhcp.getSelectedIndex()).toLowerCase()) {
            case "leases":
                for (int row : dhcpLeasesTable.getSelectedRows()) {
                    try {
                        apiClient.deleteDhcpLease(dhcpLeasesTable.getValueAt(row, 0).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection(TablesTypes.DHCP);
                fillDhcpLeaseTable(false);
                break;
            case "clients":
                for (int row : dhcpClientsTable.getSelectedRows()) {
                    try {
                        apiClient.deleteDhcpClients(dhcpClientsTable.getValueAt(row, 0).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection(TablesTypes.DHCP);
                fillDhcpClientTable(false);
                break;
            case "server":
                for (int row : dhcpServerTable.getSelectedRows()) {
                    try {
                        apiClient.deleteDhcpServer(dhcpServerTable.getValueAt(row, 0).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection(TablesTypes.DHCP);
                fillDhcpServerTable(false);
                break;
            case "networks":
                for (int row : dhcpNetworkTable.getSelectedRows()) {
                    try {
                        apiClient.deleteDhcpNetwork(dhcpNetworkTable.getValueAt(row, 0).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection(TablesTypes.DHCP);
                fillDhcpNetworkTable(false);
                break;
        }

    }

    //Fill Tables functions

    private void AddressTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(2);
        }
        String[] columNames = {"ID","Actual Interface","Address","Disabled"};
        DefaultTableModel model = new DefaultTableModel(columNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return editableColuns.contains(column);
            }
        };

        try {
            for (GetAddress address : apiClient.GetAddress()) {
                Object[] row = {
                        address.id,
                        address.actual_interface,
                        address.address,
                        address.disabled
                };
                model.addRow(row);
            }
            formatTable(addrTable, model);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("true");
        comboBox.addItem("false");
        addrTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));

        addrTable.putClientProperty("terminateEditOnFocusLost", true);
        addrTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;
                if (col == 2 )  {
                    String newAddress = addrTable.getValueAt(row,col).toString();
                    if (!isValidNetwork(newAddress)) {
                        JOptionPane.showMessageDialog(owner,
                                "Invalid Address!\nMust be 10.10.10.1/24",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }else {
                        String id = addrTable.getValueAt(row,0).toString();
                        try {
                            apiClient.UpdateAddress(id, newAddress);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

            }
        });

    }

    private void fillBridgePortsTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(4);
        }
        try {
            String[] colsWiFi = {"ID", "Bridge", "Actual Interface", "Status", "Disabled"};
            DefaultTableModel model = new DefaultTableModel(colsWiFi, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (GetPorts interf : apiClient.getBridgePorts()) {
                Object[] row = {
                        interf.id,
                        interf.bridge,
                        interf.interfaceAtual,
                        interf.status,
                        interf.disabled
                };
                model.addRow(row);
            }
            tabbedPaneBridge.setVisible(true);
            tabbedPaneWiFi.setVisible(false);
            formatTable(tablePortsBridge, model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("false");
        comboBox.addItem("true");
        tablePortsBridge.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox));

        JComboBox<String> comboBoxBridge = new JComboBox<>();
        JComboBox<String> comboBoxInterfaces = new JComboBox<>();
        try {
            for (getInterfaceBridge interf : apiClient.getBridgeInterfaces()){
                comboBoxBridge.addItem(interf.name);
            }
            for (GetPorts port : apiClient.getBridgePorts()){
                comboBoxInterfaces.addItem(port.interfaceAtual);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tablePortsBridge.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBoxBridge));
        tablePortsBridge.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxInterfaces));


        tablePortsBridge.putClientProperty("terminateEditOnFocusLost", true);
        //Este listener é o responsavel por esta à espera que cliques no enter ou fora da box
        tablePortsBridge.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;

                comboBox.setSelectedItem(tablePortsBridge.getValueAt(row, col));
                comboBoxBridge.setSelectedItem(tablePortsBridge.getValueAt(row, col));
                comboBoxInterfaces.setSelectedItem(tablePortsBridge.getValueAt(row, col));

                AddBridgePort interf = new AddBridgePort();
                interf.bridge = tablePortsBridge.getValueAt(row, 1).toString();
                interf.id = tablePortsBridge.getValueAt(row, 0).toString();
                interf.interfaceAtual = tablePortsBridge.getValueAt(row, 2).toString();
                boolean state = (boolean) tablePortsBridge.getValueAt(row, 4);

                //faz a chamada à API para atualizar o DHCP Network editado
                try {
                    apiClient.editBridgePort(interf.id, interf.bridge, interf.interfaceAtual, state);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void fillWifiSpTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
        }
        try {
            String[] colsWiFi = {"ID", "Name", "Disabled"};
            DefaultTableModel model = new DefaultTableModel(colsWiFi, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (GetProfiles interf : apiClient.GetProfiles()) {
                Object[] row = {
                        interf.id,
                        interf.name,
                        interf.disabled
                };
                model.addRow(row);
            }
            tabbedPaneWiFi.setVisible(true);
            tabbedPaneBridge.setVisible(false);
            formatTable(tableSP, model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JComboBox comboBoxDisable = new JComboBox<>();
        comboBoxDisable.addItem(false);
        comboBoxDisable.addItem(true);

        tableSP.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxDisable));

        //Este listener é o responsavel por esta à espera que cliques no enter ou fora da box
        tableSP.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;

                comboBoxDisable.setSelectedItem(tableSP.getValueAt(row, col));

                AddProfile interf = new AddProfile();
                interf.name = tableSP.getValueAt(row, 1).toString();
                interf.id = tableSP.getValueAt(row, 0).toString();
                boolean state = Boolean.valueOf(tableSP.getValueAt(row, 2).toString());

                //faz a chamada à API para atualizar o DHCP Network editado
                try {
                    apiClient.editWifiSP(interf.id, interf.name, state);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    private void fillWifiInterfacesTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(4);
            editableColuns.add(7);
        }
        try {
            String[] colsWiFi = {"ID", "Name", "Master Interface", "Mode", "SSID", "Band", "Channel Width", "Disabled"};
            DefaultTableModel model = new DefaultTableModel(colsWiFi, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (GetInterfacesWiFi interf : apiClient.GetInterfacesWiFi()) {
                Object[] row = {
                        interf.id,
                        interf.name,
                        interf.master_interface,
                        interf.mode,
                        interf.ssid,
                        interf.band,
                        interf.channel_width,
                        interf.disabled
                };
                model.addRow(row);
            }
            tabbedPaneWiFi.setVisible(true);
            tabbedPaneBridge.setVisible(false);
            formatTable(tableInterfacesWiFi, model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JComboBox comboBoxDisable = new JComboBox<>();
        comboBoxDisable.addItem(false);
        comboBoxDisable.addItem(true);

        tableInterfacesWiFi.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(comboBoxDisable));

        //Este listener é o responsavel por esta à espera que cliques no enter ou fora da box
        tableInterfacesWiFi.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;

                comboBoxDisable.setSelectedItem(tableInterfacesWiFi.getValueAt(row, col));

                AddInterfaceWiFi interf = new AddInterfaceWiFi();
                interf.ssid = tableInterfacesWiFi.getValueAt(row, 4).toString();
                interf.name = tableInterfacesWiFi.getValueAt(row, 1).toString();
                interf.id = tableInterfacesWiFi.getValueAt(row, 0).toString();
                boolean state = Boolean.valueOf(tableInterfacesWiFi.getValueAt(row, 7).toString());

                //faz a chamada à API para atualizar o DHCP Network editado
                try {
                    apiClient.editInterfaceWifi(interf.id, interf.name, interf.ssid, state);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void fillBridgeInterfacesTable(boolean isEditing){

        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(3);
        }
        try {
            String[] colsBridge = {"ID", "Name", "Running", "Disabled"};
            DefaultTableModel model = new DefaultTableModel(colsBridge, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (getInterfaceBridge interf : apiClient.getBridgeInterfaces()) {
                Object[] row = {
                        interf.id,
                        interf.name,
                        interf.running,
                        interf.disabled
                };
                model.addRow(row);
            }
            tabbedPaneBridge.setVisible(true);
            tabbedPaneWiFi.setVisible(false);
            formatTable(tableInterfacesBridge, model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("false");
        comboBox.addItem("true");

        tableInterfacesBridge.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));
        tableInterfacesBridge.putClientProperty("terminateEditOnFocusLost", true);

        //Este listener é o responsavel por esta à espera que cliques no enter ou fora da box
        tableInterfacesBridge.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;

                comboBox.setSelectedItem(tableInterfacesBridge.getValueAt(row, col));

                addNewInterfaceBridge interf = new addNewInterfaceBridge();
                interf.name = tableInterfacesBridge.getValueAt(row, 1).toString();
                interf.id = tableInterfacesBridge.getValueAt(row, 0).toString();
                //interf.running = Boolean.parseBoolean(tableInterfacesBridge.getValueAt(row,2).toString());
                boolean state = Boolean.parseBoolean(tableInterfacesBridge.getValueAt(row, 3).toString());

                //faz a chamada à API para atualizar o DHCP Network editado
                try {
                    apiClient.editInterfaceBridge(interf.id, interf.name, state);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void fillInterfacesWireGuardTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(4);

        }
        try {
            String[] columNames = new String[]{"ID", "Name", "Listen-Port", "Public-Key", "Disabled"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (InterfaceWG interfaceWG : apiClient.getInterfacesWireGuard()) {
                Object[] row = {
                        interfaceWG.id,
                        interfaceWG.name,
                        interfaceWG.listenPort,
                        interfaceWG.publicKey,
                        interfaceWG.disabled,
                };
                model.addRow(row);
            }
            wireguardTabbedPane.setVisible(true);
            formatTable(wireInterfacesTable, model);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }


        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("true");
        comboBox.addItem("false");
        wireInterfacesTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox));
        wireInterfacesTable.putClientProperty("terminateEditOnFocusLost", true);
        wireInterfacesTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;
                comboBox.setSelectedItem(wireInterfacesTable.getValueAt(row, col));
                if (col == 2) {
                    String text = wireInterfacesTable.getValueAt(row, col).toString();
                    // Verifica se contém apenas números
                    if (!text.matches("\\d+")) {
                        JOptionPane.showMessageDialog(owner, "Only numbers allowed.");
                        fillInterfacesWireGuardTable(isEditing);
                        return;
                    }

                    int value = Integer.parseInt(text);

                    // Verifica intervalo
                    if (value < 49152 || value >= 65555) {
                        JOptionPane.showMessageDialog(owner, "Value must be between 49152 and 65555.");
                        fillInterfacesWireGuardTable(isEditing);
                        return;
                    }
                }

                InterfaceWG interfaceWG = new InterfaceWG();
                interfaceWG.id = wireInterfacesTable.getValueAt(row, 0).toString();
                interfaceWG.name = wireInterfacesTable.getValueAt(row, 1).toString();
                interfaceWG.listenPort = Integer.parseInt(wireInterfacesTable.getValueAt(row,2).toString());
                interfaceWG.disabled = Boolean.parseBoolean(wireInterfacesTable.getValueAt(row, 4).toString());
                try {
                    apiClient.postEditWireguardInterface(interfaceWG);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }

    private void fillPeersWireGuardTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(3);
            editableColuns.add(4);
            editableColuns.add(6);

        }
        try {
            String [] columNames = new String[] {"ID","Name","Interface","Allowed-Address","Endpoint-Port","Public-Key","Disabled"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (Peer peer : apiClient.getPeersWireGuard()) {
                Object[] row = {
                        peer.id,
                        peer.name,
                        peer.inter,
                        peer.allowedAddress,
                        peer.endpointPort,
                        peer.publicKey,
                        peer.disabled,
                };
                model.addRow(row);
            }
            formatTable(wirePeersTable, model);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        JComboBox<String> comboBox = new JComboBox<>();
        try {
            for (getAllInterfaces interf :apiClient.getAllInterfaces()){
                comboBox.addItem(interf.name);
            }
            wirePeersTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JComboBox<String> comboBox2 = new JComboBox<>();
        comboBox2.addItem("true");
        comboBox2.addItem("false");
        wirePeersTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox2));
        wirePeersTable.putClientProperty("terminateEditOnFocusLost", true);
        wirePeersTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;
                comboBox.setSelectedItem(wirePeersTable.getValueAt(row,col));
                comboBox2.setSelectedItem(wirePeersTable.getValueAt(row,col));

                if (col == 1 )  {
                    String newAddress = wirePeersTable.getValueAt(row,col).toString();
                    if (!isValidAddress(newAddress)) {
                        JOptionPane.showMessageDialog(owner,
                                "Invalid Address!\nMust be 0.0.0.0",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }else {
                        Peer peer =new Peer();
                        peer.id = wirePeersTable.getValueAt(row, 0).toString();
                        peer.name = wirePeersTable.getValueAt(row, 1).toString();
                        peer.inter = wirePeersTable.getValueAt(row, 2).toString();
                        peer.allowedAddress = wirePeersTable.getValueAt(row, 3).toString();
                        peer.endpointPort = Integer.parseInt(wirePeersTable.getValueAt(row, 4).toString());
                        peer.disabled = Boolean.parseBoolean(wirePeersTable.getValueAt(row, 6).toString());
                        try {
                            apiClient.postEditWireguardPeer(peer);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }

            }
        });

    }

    private void fillDhcpLeaseTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(3);
            editableColuns.add(4);

        }
        try {
            String [] columNames = new String[] {"ID","Address","Server","Client-ID","Disabled","Dynamic", "Last-Seen","Blocked","Status" };
            DefaultTableModel model = new DefaultTableModel(columNames, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (DhcpLease lease : apiClient.getDhcpLeases()) {
                Object[] row = {
                        lease.id,
                        lease.address,
                        lease.server,
                        lease.clientId,
                        lease.disabled,
                        lease.dynamic,
                        lease.lastSeen,
                        lease.blocked,
                        lease.status
                };
                model.addRow(row);
            }
            formatTable(dhcpLeasesTable, model);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        JComboBox<String> comboBox = new JComboBox<>();
        try {
            for (DhcpServer server :apiClient.getDhcpServer()){
                comboBox.addItem(server.name);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        dhcpLeasesTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox));

        JComboBox<String> comboBox2 = new JComboBox<>();
        comboBox2.addItem("true");
        comboBox2.addItem("false");
        dhcpLeasesTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox2));
        dhcpLeasesTable.putClientProperty("terminateEditOnFocusLost", true);
        dhcpLeasesTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;
                comboBox.setSelectedItem(dhcpLeasesTable.getValueAt(row,col));
                comboBox2.setSelectedItem(dhcpLeasesTable.getValueAt(row,col));

                if (col == 1 )  {
                    String newAddress = dhcpLeasesTable.getValueAt(row,col).toString();
                    if (!isValidAddress(newAddress)) {
                        JOptionPane.showMessageDialog(owner,
                                "Invalid Address!\nMust be 0.0.0.0",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }else {
                        DhcpLease dhcpLeases =new DhcpLease();
                        dhcpLeases.id = dhcpLeasesTable.getValueAt(row, 0).toString();
                        dhcpLeases.address = dhcpLeasesTable.getValueAt(row, 1).toString();
                        dhcpLeases.server = dhcpLeasesTable.getValueAt(row, 2).toString();
                        dhcpLeases.clientId = dhcpLeasesTable.getValueAt(row, 3).toString();
                        dhcpLeases.disabled = Boolean.parseBoolean(dhcpLeasesTable.getValueAt(row, 4).toString());
                        try {
                            apiClient.postEditDhcpLease(dhcpLeases);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }

            }
        });

    }

    private void fillDhcpServerTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(4);

        }
        try {
            String [] columNames = new String[] {"ID","Name","Interface","Address Pool","Disabled","Dynamic"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (DhcpServer serverObject : apiClient.getDhcpServer()) {
                Object[] row = {
                        serverObject.id,
                        serverObject.name,
                        serverObject.interfaceName,
                        serverObject.addressPool,
                        serverObject.disabled,
                        serverObject.dynamic
                };
                model.addRow(row);
            }
            formatTable(dhcpServerTable, model);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        JComboBox<String> comboBox = new JComboBox<>();
        try {
            for (getAllInterfaces interf : apiClient.getAllInterfaces()) {
                comboBox.addItem(interf.name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        dhcpServerTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox));

        JComboBox<String> comboBox2 = new JComboBox<>();
        comboBox2.addItem("true");
        comboBox2.addItem("false");
        dhcpServerTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox2));
        dhcpServerTable.putClientProperty("terminateEditOnFocusLost", true);
        dhcpServerTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;
                comboBox.setSelectedItem(dhcpServerTable.getValueAt(row,col));
                comboBox2.setSelectedItem(dhcpServerTable.getValueAt(row,col));
                DhcpServer dhcpServer =new DhcpServer();
                dhcpServer.id = dhcpServerTable.getValueAt(row, 0).toString();
                dhcpServer.name = dhcpServerTable.getValueAt(row, 1).toString();
                dhcpServer.interfaceName = dhcpServerTable.getValueAt(row, 2).toString();
                dhcpServer.disabled = Boolean.parseBoolean(dhcpServerTable.getValueAt(row, 4).toString());
                try {
                    apiClient.postEditDhcpServer(dhcpServer);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

    }

    private void fillDhcpClientTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(3);
            editableColuns.add(4);
            editableColuns.add(5);

        }
        try {
            String[] columNames = {"ID","Inteface","Add Default Route","Dns-Server","Ntp-Server","Disabled"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };
            for (DhcpClient dhcpClient : apiClient.getDhcpClients()) {
                Object[] row = {
                        dhcpClient.id,
                        dhcpClient.interfaceName,
                        dhcpClient.addDefaultRoute,
                        dhcpClient.usePeerDns,
                        dhcpClient.usePeerNtp,
                        dhcpClient.disabled,
                        dhcpClient.dhcpOption

                };
                model.addRow(row);
            }

            formatTable(dhcpClientsTable, model);

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        JComboBox<String> comboBox = new JComboBox<>();
        try {
            for (getAllInterfaces interf : apiClient.getAllInterfaces()) {
                comboBox.addItem(interf.name);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        dhcpClientsTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));

        JComboBox<String> comboBox2 = new JComboBox<>();
        comboBox2.addItem("yes");
        comboBox2.addItem("no");
        dhcpClientsTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox2));
        JComboBox<String> comboBox3 = new JComboBox<>();
        comboBox3.addItem("true");
        comboBox3.addItem("false");
        dhcpClientsTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox3));
        dhcpClientsTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox3));
        dhcpClientsTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBox3));

        dhcpClientsTable.putClientProperty("terminateEditOnFocusLost", true);
        dhcpClientsTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;
                comboBox.setSelectedItem(dhcpClientsTable.getValueAt(row,col));
                comboBox2.setSelectedItem(dhcpClientsTable.getValueAt(row,col));
                comboBox3.setSelectedItem(dhcpClientsTable.getValueAt(row,col));

                DhcpClient dhcpClient =new DhcpClient();
                dhcpClient.id = dhcpClientsTable.getValueAt(row, 0).toString();
                dhcpClient.interfaceName = dhcpClientsTable.getValueAt(row, 1).toString();
                dhcpClient.addDefaultRoute = dhcpClientsTable.getValueAt(row, 2).toString();
                dhcpClient.usePeerNtp = dhcpClientsTable.getValueAt(row, 4).toString();
                dhcpClient.usePeerDns = dhcpClientsTable.getValueAt(row, 3).toString();
                dhcpClient.disabled = Boolean.parseBoolean(dhcpClientsTable.getValueAt(row, 5).toString());
                try {
                    apiClient.postEditDhcpClient(dhcpClient);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }

    private void fillDhcpNetworkTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(3);

        }

        String[] columNames = {"ID","Network","Dns-Server", "Gateway","Dynamic","Dhcp-Option","Ntp-Server","Wins-Server" };
        DefaultTableModel model = new DefaultTableModel(columNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return editableColuns.contains(column);
            }
        };

        try {
            for (DhcpNetwork dhcpNetwork : apiClient.getDhcpNetworks()) {
                Object[] row = {
                        dhcpNetwork.id,
                        dhcpNetwork.address,
                        dhcpNetwork.dnsServer,
                        dhcpNetwork.gateway,
                        dhcpNetwork.dynamic,
                        dhcpNetwork.dhcpOption,
                        dhcpNetwork.ntpServer,
                        dhcpNetwork.winsServer,
                };
                model.addRow(row);
            }
            formatTable(dhcpNetworkTable, model);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dhcpNetworkTable.putClientProperty("terminateEditOnFocusLost", true);
        dhcpNetworkTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;
                String newAddress = dhcpNetworkTable.getValueAt(row,col).toString();
                if (col == 1){
                    if (!isValidNetwork(newAddress)) {
                        JOptionPane.showMessageDialog(owner,
                                "Invalid Network!\nMust be 0.0.0.0",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (col == 2){
                    if (!isValidAddress(newAddress)) {
                        JOptionPane.showMessageDialog(owner,
                                "Invalid DNS!\nMust be 0.0.0.0",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (col == 3){
                    if (!isValidAddress(newAddress)) {
                        JOptionPane.showMessageDialog(owner,
                                "Invalid Gateway!\nMust be 0.0.0.0",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                DhcpNetwork dhcpNetwork =new DhcpNetwork();
                dhcpNetwork.id = dhcpNetworkTable.getValueAt(row, 0).toString();
                dhcpNetwork.address = dhcpNetworkTable.getValueAt(row, 1).toString();
                dhcpNetwork.dnsServer = dhcpNetworkTable.getValueAt(row, 2).toString();
                dhcpNetwork.gateway = dhcpNetworkTable.getValueAt(row, 3).toString();
                try {
                    ApiResponse response =apiClient.postEditDhcpNetwork(dhcpNetwork);
                    if (response.error != 200) {
                        JOptionPane.showMessageDialog(owner,
                                response.detail,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

    }

    private void fillDnsCacheTable() {
        String[] columNames = {"ID","Data","Name","Static","TTL","Type"};
        DefaultTableModel model = new DefaultTableModel(columNames, 0);

        try {
            for (DnsCache dnsCache : apiClient.getCacheDns()) {
                Object[] row = {
                        dnsCache.id,
                        dnsCache.data,
                        dnsCache.name,
                        dnsCache._static,
                        dnsCache.ttl,
                        dnsCache.type
                };
                model.addRow(row);
            }
            formatTable(dhcpNetworkTable, model);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void fillDnsRecordsTable(boolean isEditing) {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        if (isEditing) {
            editableColuns.add(1);
            editableColuns.add(2);
            editableColuns.add(3);
        }
        try {
            String [] columNames = {"ID", "Address", "Name", "Disabled","Dynamic", "TTL", "Type"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return editableColuns.contains(column);
                }
            };

            for (DnsRecord dnsRecord : apiClient.getDnsRecord()) {
                Object[] row = {
                        dnsRecord.id,
                        dnsRecord.address,
                        dnsRecord.name,
                        dnsRecord.disabled,
                        dnsRecord.dynamic,
                        dnsRecord.ttl,
                        dnsRecord.type
                };
                model.addRow(row);
            }
            formatTable(recordsTable, model);
            dnsPanel.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("true");
        comboBox.addItem("false");
        recordsTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));

        recordsTable.putClientProperty("terminateEditOnFocusLost", true);
        recordsTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;
                comboBox.setSelectedItem(recordsTable.getValueAt(row,col));
                if (col == 1 )  {
                    String newAddress = recordsTable.getValueAt(row,col).toString();
                    if (!isValidAddress(newAddress)) {
                        JOptionPane.showMessageDialog(owner,
                                "Invalid Address!\nMust be 0.0.0.0",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }else {
                        DnsRecord dnsRecord =new DnsRecord();
                        dnsRecord.id = recordsTable.getValueAt(row, 0).toString();
                        dnsRecord.address = newAddress;
                        dnsRecord.name = recordsTable.getValueAt(row, 2).toString();
                        dnsRecord.disabled = Boolean.valueOf(recordsTable.getValueAt(row, 3).toString());
                        try {
                            apiClient.postEditDnsRecord(dnsRecord);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }

            }
        });

    }

    private void formatTable(JTable table,DefaultTableModel model) {
        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean visible = table.getSelectedRowCount() > 0;
                deleteDhcpButton.setEnabled(visible);
            }
        });
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.setVisible(true);
    }

    private void clearTableSelection(TablesTypes tableType) {
        switch(tableType) {
            case DNS:
                recordsTable.clearSelection();
                cacheTable.clearSelection();
                break;
            case DHCP:
                dhcpNetworkTable.clearSelection();
                dhcpClientsTable.clearSelection();
                dhcpServerTable.clearSelection();
                dhcpLeasesTable.clearSelection();
                break;
            case BRIDGE:
                tableInterfacesBridge.clearSelection();
                tablePortsBridge.clearSelection();
                break;
            case ROUTE:
                tableRoute.clearSelection();
                break;
            case WIREGUARD:
                wirePeersTable.clearSelection();
                wireInterfacesTable.clearSelection();
                break;
            case ADDRESS:
                addrTable.clearSelection();
                break;
            default:
                break;
        }
    }

    private void btnDeleteRecord(ActionEvent actionEvent) {
        for (int row : recordsTable.getSelectedRows()) {
            try {
                apiClient.deleteDnsRecord(recordsTable.getValueAt(row, 0).toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            clearTableSelection(TablesTypes.DNS);
        }
        fillDnsRecordsTable(false);
    }

    private void btnAddRecord(ActionEvent actionEvent) {
        AddDnsRecord addDnsRecord = new AddDnsRecord(owner,apiClient);
        addDnsRecord.pack();
        addDnsRecord.setLocationRelativeTo(owner);
        addDnsRecord.setVisible(true);
        fillDnsRecordsTable(false);
    }

    private void btnEditRecord(ActionEvent actionEvent) {
        clearTableSelection(TablesTypes.DNS);
        boolean isEdited = editRecordsToggleButton.isSelected();
        System.out.println(isEdited);
        if (isEdited) {
            editRecordsToggleButton.setText("Disable Edit");
        }else {
            editRecordsToggleButton.setText("Enable Edit");
        }
        fillDnsRecordsTable(isEdited);
    }

    private void btnClearCache(ActionEvent actionEvent) {
        try {
            apiClient.postClearDnsCache();
            fillDnsCacheTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnShowDnsPanel(ActionEvent actionEvent) {
        statsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        dhcpPanel.setVisible(false);
        dnsPanel.setVisible(true);
        interfacePanel.setVisible(false);
        addrPanel.setVisible(false);
        routePanel.setVisible(false);
        wireGuardPanel.setVisible(false);
        updatePanel.setVisible(false);
        dividerCheck.setVisible(false);
    }

    private void btnShowHomePanel(ActionEvent actionEvent) {
        try {
            statsPanel.setVisible(true);
            cpuPanel.setVisible(true);
            dnsPanel.setVisible(false);
            dhcpPanel.setVisible(false);
            interfacePanel.setVisible(false);
            addrPanel.setVisible(false);
            routePanel.setVisible(false);
            wireGuardPanel.setVisible(false);
            dividerCheck.setVisible(false);
            setDashboardValues();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnShowInterfacePanel(ActionEvent actionEvent) {
        interfacePanel.setVisible(true);
        addrPanel.setVisible(false);
        dnsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        statsPanel.setVisible(false);
        dhcpPanel.setVisible(false);
        addrPanel.setVisible(false);
        routePanel.setVisible(false);
        updatePanel.setVisible(false);
        dividerCheck.setVisible(false);
        wireGuardPanel.setVisible(false);
        interfaceTable();
    }

    private void btnShowDhcpPanel(ActionEvent actionEvent)  {
        dnsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        statsPanel.setVisible(false);
        dhcpPanel.setVisible(true);
        interfacePanel.setVisible(false);
        addrPanel.setVisible(false);
        routePanel.setVisible(false);
        wireGuardPanel.setVisible(false);
        updatePanel.setVisible(false);
        dividerCheck.setVisible(false);

        fillDhcpLeaseTable(false);

    }

    private void setStyle(Color backColor, Color textColor) {
        //Define Font style
        Font font = new Font("JetBrains Mono", Font.PLAIN,14);

        JComponent[] components = {
                //Panels
                mainPanel,contentPanel, navBar, topBar,cpuPanel,statsPanel,dhcpPanel,interfacePanel,addrPanel,dnsPanel,interfacesAllPanel,routePanel,updatePanel,
                //Top barLabels
                hostLabel,userLabel, hostIpText, usernameText,
                //VersionPanel
                status,statusLabel,chanel,chanelLabel, installedLabel, installed,latestLabel,latest,
                //StatsPanel Labels
                frequency,freqLabel,cores,cpuLabel,cpuName,uptime,uptimeLabel,version,versionLabel,memoryLabel,loadLabel,coresLabel,hddLabel,
                //DNS Panel
                dnsTabbed,cacheTable,recordsTable,
                //InterfacesPanel
                interfaceTable,comboBoxInterfaces,interfacesAllPanel,tabbedPaneBridge,tabbedPaneWiFi,tableInterfacesWiFi,tabbedPaneBridge,
                tableSP,tablePortsBridge,
                //Dhcp Panel
                tabbedDhcp,dhcpServerTable,dhcpNetworkTable,dhcpClientsTable,dhcpLeasesTable,
                //Address Panel
                addrTable,
                //Route Panel
                tableRoute,
                //WireGuard
                wireguardTabbedPane,wireGuardPanel,wirePeersTable,wireInterfacesTable,

        };

        for (JComponent component : components){
            component.setBackground(backColor);
            component.setForeground(textColor);
            component.setFont(font);
        }


        divider.setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(),50));

        dividerCheck.setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(),50));
        dividerCheck.setVisible(false);


        // --- Botões ---
        AbstractButton[] buttons = {
                //Main Buttons
                logoutButton,homeButton, dnsButton, routeButton, addressButton,
                dhcpButton, interfaceButton,checkUpdateButton,updateButton,wireguardButton,
                //DNS Buttons
                addRecordButton, deleteRecordButton,clearCacheButton,editRecordsToggleButton,setupDnsConfigButton,
                //DHCP Buttons
                deleteDhcpButton, editDhcpButton, addClientButton, addServerButton, addNetworkButton, addLeaseButton,
                //Address Button
                UpdateIPButton, removeIPButton, ableDisableAddrButton, addIPButton,editAddressButton,
                //Interfaces Button
                deleteInterfaceButton, ableDisableInterfaceButton, addInterfaceButton,editInterfacesButton,
                //Routes
                addStaticRouteButton,ableDisableStaticRouteButton,deleteStaticRouteButton,editRouteButton,
                //WireGuard
                addWireguardButton,deleteWireButton,editWireGuardButton, disAbleWireguardButton,setupWireGuardButton,generateConfButton,
        };

        Color buttonColor = new Color(16, 83, 138);
        Color hoverColor = new Color(102, 210, 170);
        Color borderColor = new Color(10, 52, 86,60);

        for (AbstractButton btn : buttons) {
            btn.setBackground(buttonColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(false);
            btn.setFont(font);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Aplica hover automático
            applyHoverEffect(btn, buttonColor, hoverColor);
        }

        String iconColor = "white";
        if (textColor.getBlue() ==255 && textColor.getRed() ==255 && textColor.getGreen() ==255) {
                iconColor = "white";
        }
        Image home = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/home_white.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image homeHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/home_black.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        homeButton.setIcon(new ImageIcon(home));
        homeButton.setRolloverIcon(new ImageIcon(homeHover));

        Image dns = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dns_white.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image dnsHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dns_black.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        dnsButton.setIcon(new ImageIcon(dns));
        dnsButton.setRolloverIcon(new ImageIcon(dnsHover));

        Image dhcp = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dhcp_white.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image dhcpHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dhcp_black.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        dhcpButton.setIcon(new ImageIcon(dhcp));
        dhcpButton.setRolloverIcon(new ImageIcon(dhcpHover));

        Image route = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/routes_white.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image routeHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/routes_black.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        routeButton.setIcon(new ImageIcon(route));
        routeButton.setRolloverIcon(new ImageIcon(routeHover));

        Image inter = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/interface_white.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image interHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/interface_black.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        interfaceButton.setIcon(new ImageIcon(inter));
        interfaceButton.setRolloverIcon(new ImageIcon(interHover));


        Image addresses = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/address_white.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image addressesHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/address_black.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        addressButton.setIcon(new ImageIcon(addresses));
        addressButton.setRolloverIcon(new ImageIcon(addressesHover));



        Image check = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/refresh_white.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        Image checkHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/refresh_black.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);

        checkUpdateButton.setIcon(new ImageIcon(check));
        UpdateIPButton.setIcon(new ImageIcon(check));
        ableDisableInterfaceButton.setIcon(new ImageIcon(check));
        ableDisableAddrButton.setIcon(new ImageIcon(check));
        ableDisableStaticRouteButton.setIcon(new ImageIcon(check));
        disAbleWireguardButton.setIcon(new ImageIcon(check));


        checkUpdateButton.setRolloverIcon(new ImageIcon(checkHover));
        UpdateIPButton.setRolloverIcon(new ImageIcon(checkHover));
        ableDisableInterfaceButton.setRolloverIcon(new ImageIcon(checkHover));
        ableDisableAddrButton.setRolloverIcon(new ImageIcon(checkHover));
        ableDisableStaticRouteButton.setRolloverIcon(new ImageIcon(checkHover));
        disAbleWireguardButton.setRolloverIcon(new ImageIcon(checkHover));


        Image remove = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/delete_white.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        Image removeHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/delete_black.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);

        removeIPButton.setIcon(new ImageIcon(remove));
        deleteDhcpButton.setIcon(new ImageIcon(remove));
        deleteInterfaceButton.setIcon(new ImageIcon(remove));
        deleteRecordButton.setIcon(new ImageIcon(remove));
        deleteStaticRouteButton.setIcon(new ImageIcon(remove));
        clearCacheButton.setIcon(new ImageIcon(remove));
        deleteWireButton.setIcon(new ImageIcon(remove));

        removeIPButton.setRolloverIcon(new ImageIcon(removeHover));
        deleteDhcpButton.setRolloverIcon(new ImageIcon(removeHover));
        deleteInterfaceButton.setRolloverIcon(new ImageIcon(removeHover));
        deleteRecordButton.setRolloverIcon(new ImageIcon(removeHover));
        deleteStaticRouteButton.setRolloverIcon(new ImageIcon(removeHover));
        clearCacheButton.setRolloverIcon(new ImageIcon(removeHover));
        deleteWireButton.setRolloverIcon(new ImageIcon(removeHover));


        Image edit = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/edit_white.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        Image editHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/edit_black.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);

        editRecordsToggleButton.setIcon(new ImageIcon(edit));
        editRecordsToggleButton.setIcon(new ImageIcon(edit));

        editWireGuardButton.setIcon(new ImageIcon(edit));
        editWireGuardButton.setRolloverIcon(new ImageIcon(editHover));

        editDhcpButton.setIcon(new ImageIcon(edit));
        editDhcpButton.setRolloverIcon(new ImageIcon(editHover));

        editInterfacesButton.setIcon(new ImageIcon(edit));
        editInterfacesButton.setRolloverIcon(new ImageIcon(edit));

        editAddressButton.setIcon(new ImageIcon(edit));
        editAddressButton.setRolloverIcon(new ImageIcon(edit));

        editRouteButton.setIcon(new ImageIcon(edit));
        editRouteButton.setRolloverIcon(new ImageIcon(edit));


        Image add = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/add_white.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        Image addHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/add_black.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);

        addInterfaceButton.setIcon(new ImageIcon(add));
        addIPButton.setIcon(new ImageIcon(add));
        addServerButton.setIcon(new ImageIcon(add));
        addClientButton.setIcon(new ImageIcon(add));
        addLeaseButton.setIcon(new ImageIcon(add));
        addRecordButton.setIcon(new ImageIcon(add));
        addLeaseButton.setIcon(new ImageIcon(add));
        addNetworkButton.setIcon(new ImageIcon(add));
        addStaticRouteButton.setIcon(new ImageIcon(add));
        addWireguardButton.setIcon(new ImageIcon(add));


        addInterfaceButton.setRolloverIcon(new ImageIcon(addHover));
        addIPButton.setRolloverIcon(new ImageIcon(addHover));
        addServerButton.setRolloverIcon(new ImageIcon(addHover));
        addClientButton.setRolloverIcon(new ImageIcon(addHover));
        addLeaseButton.setRolloverIcon(new ImageIcon(addHover));
        addRecordButton.setRolloverIcon(new ImageIcon(addHover));
        addLeaseButton.setRolloverIcon(new ImageIcon(addHover));
        addNetworkButton.setRolloverIcon(new ImageIcon(addHover));
        addStaticRouteButton.setRolloverIcon(new ImageIcon(addHover));
        addWireguardButton.setRolloverIcon(new ImageIcon(addHover));

        Image wire = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/wireguard_white.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        Image wireHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/wireguard_black.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);

        wireguardButton.setIcon(new ImageIcon(wire));
        wireguardButton.setRolloverIcon(new ImageIcon(wireHover));

        Image setup = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/setup_white.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        Image setupHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/setup_black.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);

        setupDnsConfigButton.setIcon(new ImageIcon(setup));
        setupDnsConfigButton.setRolloverIcon(new ImageIcon(setupHover));
        setupWireGuardButton.setIcon(new ImageIcon(setup));
        setupWireGuardButton.setRolloverIcon(new ImageIcon(setupHover));

        Image signout = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/logout_white.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        Image signoutHover = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/logout_black.png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);

        logoutButton.setIcon(new ImageIcon(signout));
        logoutButton.setRolloverIcon(new ImageIcon(signoutHover));

        progressBarLoad.setForeground(Color.GREEN);
        progressBarMem.setForeground(Color.GREEN);
        progressBarLoad.setFont(font);
        progressBarMem.setFont(font);
        progressBarHdd.setFont(font);

        //Set visible components

        statsPanel.setVisible(true);
        cpuPanel.setVisible(true);
        dnsPanel.setVisible(false);
        dhcpPanel.setVisible(false);
        interfacePanel.setVisible(false);
        addrPanel.setVisible(false);
        routePanel.setVisible(false);
        updatePanel.setVisible(false);
        wireGuardPanel.setVisible(false);
        addRecordButton.setVisible(false);
        updateButton.setVisible(false);


        deleteRecordButton.setVisible(false);
        deleteRecordButton.setEnabled(false);
        editRecordsToggleButton.setVisible(false);

        deleteDhcpButton.setEnabled(false);
        addNetworkButton.setVisible(false);

        addServerButton.setVisible(false);

        addClientButton.setVisible(false);

        addLeaseButton.setVisible(true);

        deleteWireButton.setEnabled(false);
        disAbleWireguardButton.setEnabled(false);

        generateConfButton.setEnabled(false);
        generateConfButton.setVisible(false);

    }

    private void applyHoverEffect(AbstractButton button, Color normal, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
                button.setForeground(Color.BLACK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
                button.setForeground(Color.WHITE);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void setDashboardValues() throws Exception{
        SystemResources sys = apiClient.getSystemResources();
        cpuName.setText(sys.getCpu());
        cores.setText(sys.getCpuCount());
        frequency.setText(sys.getCpuFrequency());
        uptime.setText(sys.getUptime());
        version.setText(sys.getVersion());

        int load = Integer.parseInt(sys.getCpuLoad());
        float totalMem = Integer.parseInt(sys.getTotalMemory());
        float freeMem = Integer.parseInt(sys.getFreeMemory());
        float usedMem = ((totalMem - freeMem) / totalMem) * 100;
        usedMem = Math.round(usedMem);
        if (load > 90) {
            progressBarLoad.setForeground(Color.RED);
        } else if (load > 70) {
            progressBarLoad.setForeground(Color.ORANGE);
        }
        progressBarLoad.setValue(load);
        progressBarLoad.setString(load +"%");
        progressBarLoad.setStringPainted(true);

        if (usedMem > 90) {
            progressBarMem.setForeground(Color.RED);
        } else if (usedMem > 70) {
            progressBarMem.setForeground(Color.ORANGE);
        }
        progressBarMem.setValue((int)usedMem);
        progressBarMem.setString(usedMem+"%");
        progressBarMem.setStringPainted(true);

        long totalHdd = Long.parseLong(sys.getTotalHddSpace()) * 1024;
        long freeHdd = Long.parseLong(sys.getFreeHddSpace());

        long usedHdd = totalHdd - freeHdd;

        int percentUsed = (int) ((double) usedHdd / totalHdd * 100);
        if (percentUsed > 90) {
            progressBarHdd.setForeground(Color.RED);
        } else if (percentUsed > 70) {
            progressBarHdd.setForeground(Color.ORANGE);
        }

        progressBarHdd.setValue(percentUsed);
        progressBarHdd.setString(percentUsed + "%");
        progressBarHdd.setStringPainted(true);


    }

    private void buildChart(Color backColor, Color textColor) throws Exception{

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for ( getAllInterfaces interf : apiClient.getAllInterfaces()) {
            dataset.addValue(interf.rxDrop, "RX Drop", interf.name);
            dataset.addValue(interf.txDrop, "TX Drop", interf.name);
        }
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "Interface",
                "Quantidade",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        barChart.setBackgroundPaint(backColor);
        CategoryPlot plot = barChart.getCategoryPlot();

        plot.setBackgroundPaint(backColor);
        plot.setOutlinePaint(backColor);
        plot.setRangeGridlinePaint(textColor);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelPaint(textColor);
        domainAxis.setTickLabelPaint(textColor);

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelPaint(textColor);
        rangeAxis.setTickLabelPaint(textColor);

        LegendTitle legend = barChart.getLegend();

        legend.setItemPaint(textColor);          // texto
        legend.setBackgroundPaint(backColor);    // fundo

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        chartPanel.setForeground(textColor);

        chartPanel.setMouseZoomable(true, false);
        chartPanel.setMouseWheelEnabled(false);
        chartPanel.setBackground(backColor);

        // Adicionar ao JPanel existente no form (chartPanel1)
        statsPanel.setLayout(new BorderLayout());
        statsPanel.add(chartPanel, BorderLayout.CENTER);
        statsPanel.validate(); // força o layout a atualizar
    }



}
