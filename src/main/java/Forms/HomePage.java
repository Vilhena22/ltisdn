package Forms;

import ApiClient.ApiClient;
import Dialogs.Add.*;
import Dialogs.Edit.UpdateIP;
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
import Models.Interfaces.wifi.interfaces.GetInterfacesWiFi;
import Models.Interfaces.wifi.securityProfiles.GetProfiles;
import Models.ApiResponse;
import Models.Route.Routes;
import Models.System.SystemResources;
import Models.System.SystemVersion;
import Models.TablesTypes;
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
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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
    private JButton deleteButton;
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
    private JLabel instaLabel;
    private JLabel latestLabel;
    private JLabel statusLabel;
    private JLabel status;
    private JLabel latest;
    private JLabel instaled;
    private JLabel chanel;
    private JButton logoutButton;
    private JToggleButton editBridgeButton;
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
            if (isThemeDark) {
                buildChart(new Color(60, 63, 65), Color.WHITE);
                setStyle(new Color(60, 63, 65),Color.WHITE);
            } else {
                buildChart(Color.WHITE, new Color(60, 63, 65));
                setStyle(Color.WHITE,new Color(60, 63, 65));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        logoutButton.addActionListener(this::btnLogout);
        homeButton.addActionListener(this::btnShowHomePanel);
        dnsButton.addActionListener(this::btnShowDnsPanel);
        dhcpButton.addActionListener(this::btnShowDhcpPanel);
        clearCacheButton.addActionListener(this::btnClearCache);
        addRecordButton.addActionListener(this::btnAddRecord);
        editRecordsToggleButton.addActionListener(this::btnEditRecord);
        deleteRecordButton.addActionListener(this::btnDeleteRecord);
        deleteButton.addActionListener(this::btnDeleteButton);
        addressButton.addActionListener(this::btnShowAddressPanel);
        addIPButton.addActionListener(this::btnAddIP);
        ableDisableAddrButton.addActionListener(this::btnAbleDisableAddr);
        removeIPButton.addActionListener(this::btnRemoveIP);
        UpdateIPButton.addActionListener(this::btnUpdateIP);
        interfaceButton.addActionListener(this::btnShowInterfacePanel);
        ableDisableInterfaceButton.addActionListener(this::btnAbleDisableInterface);
        deleteInterfaceButton.addActionListener(this::btnDeleteInterface);
        addInterfaceButton.addActionListener(this::btnAddInterface);
        editDhcpButton.addActionListener(this::btnEditDhcpButton);
        addNetworkButton.addActionListener(this::btnAddPool);
        addLeaseButton.addActionListener(this::btnAddLease);
        addClientButton.addActionListener(this::btnAddClient);
        addServerButton.addActionListener(this::btnAddServer);
        routeButton.addActionListener(this::btnRoute);
        deleteStaticRouteButton.addActionListener(this::btnRouteDelete);
        addStaticRouteButton.addActionListener(this::btnRouteAdd);
        ableDisableStaticRouteButton.addActionListener(this::btnRouteAbleDisabled);
        editBridgeButton.addActionListener(this::btnEditBridgeButton);

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

    //Listner para trocar os botoes consoante a tabela selecionada
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

    //Listner para trocar os botoes consoante a tabela selecionada
        tabbedDhcp.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked (MouseEvent e){
            super.mouseClicked(e);
            editDhcpButton.setSelected(false);
            editDhcpButton.setText("Enable Edit");
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

        //Cria Listneers para quando tiver itens selecionados o botao delete ativa

        addrTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = addrTable.getSelectedRow() != -1;
            UpdateIPButton.setEnabled(selected);
            ableDisableAddrButton.setEnabled(selected);
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
                deleteButton.setEnabled(isSelected);


            }
        });

        dhcpLeasesTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpLeasesTable.getSelectedRow() != -1;
                deleteButton.setEnabled(isSelected);


            }
        });

        dhcpClientsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpClientsTable.getSelectedRow() != -1;
                deleteButton.setEnabled(isSelected);


            }
        });

        dhcpServerTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpServerTable.getSelectedRow() != -1;
                deleteButton.setEnabled(isSelected);


            }
        });

        statsPanel.setVisible(true);
        cpuPanel.setVisible(true);
        dnsPanel.setVisible(false);
        dhcpPanel.setVisible(false);
        interfacePanel.setVisible(false);
        addrPanel.setVisible(false);
        routePanel.setVisible(false);
        updatePanel.setVisible(false);

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
                                instaled.setText(systemVersion.installedVersion);
                                chanel.setText(systemVersion.channel);
                                if (systemVersion.latestVersion != null) {
                                    latest.setText(systemVersion.latestVersion);
                                    status.setText(systemVersion.status);
                                    updateButton.setVisible(true);
                                }
                                checkUpdateButton.setText("Close");
                                updatePanel.setVisible(true);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }).start();
                }else {
                    checkUpdateButton.setText("Check Updates");
                    updatePanel.setVisible(false);
                }
            }
        });
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
            getStaticRoutes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnRouteAdd(ActionEvent actionEvent) {
        try {
            addStaticRoute rota = new addStaticRoute();
            rota.pack();
            rota.setLocationRelativeTo(owner);
            rota.setVisible(true);
            getStaticRoutes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnRouteDelete(ActionEvent actionEvent) {
        int selected = tableRoute.getSelectedRow();
        String id = tableRoute.getValueAt(selected, 0).toString();

        try {
            apiClient.deleteRotaEstatica(id);
            getStaticRoutes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnRoute(ActionEvent actionEvent) {
        try {
            routePanel.setVisible(true);
            cpuPanel.setVisible(false);
            addrPanel.setVisible(false);
            dnsPanel.setVisible(false);
            dhcpPanel.setVisible(false);
            interfacePanel.setVisible(false);
            statsPanel.setVisible(false);

            getStaticRoutes();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void getStaticRoutes() {
        Set<Integer> editableColuns = new java.util.HashSet<>(Set.of());
        boolean isEditing = false;
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
    }


    private boolean isValidAddress(String text) {
        return ADDRESS_PATTERN.matcher(text.trim()).matches();
    }

    private boolean isValidNetwork(String text) {
        return NETWORK_PATTERN.matcher(text.trim()).matches();
    }


    private void btnAddInterface(ActionEvent actionEvent) {
         String selected = (String) comboBoxInterfaces.getSelectedItem();
         if (selected.compareTo("Wi-Fi") == 0){
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

        switch (comboBoxValue) {
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
            apiClient.deleteInterface(id);
            System.out.println(apiClient.deleteInterface(endpoint));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        interfaceTable();
    }

    private void btnAbleDisableInterface(ActionEvent actionEvent) {
        int selectedRow;
        int lastCol;

        switch ((String) comboBoxInterfaces.getSelectedItem()) {
//            case "All interfaces":
//                selectedRow = interfaceTable.getSelectedRow();
//                lastCol = interfaceTable.getColumnCount() - 1;
//                try {
//                    apiClient.estadoInterface(interfaceTable.getValueAt(selectedRow, 0).toString(), (Boolean) interfaceTable.getValueAt(selectedRow, lastCol));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                break;
            case "Wi-Fi":
                if (tabbedPaneBridge.getTitleAt(tabbedPaneBridge.getSelectedIndex()).compareTo("Interfaces") == 0) {
                    selectedRow = tableInterfacesWiFi.getSelectedRow();
                    lastCol = tableInterfacesWiFi.getColumnCount() - 1;
                    try {
                        apiClient.estadoInterface(tableInterfacesWiFi.getValueAt(selectedRow, 0).toString(), (Boolean) tableInterfacesWiFi.getValueAt(selectedRow, lastCol));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    ableDisableInterfaceButton.setVisible(false);
                }
                break;
            case "Bridge":
                if (tabbedPaneBridge.getTitleAt(tabbedPaneBridge.getSelectedIndex()).compareTo("Interfaces") == 0) {
                    selectedRow = tableInterfacesBridge.getSelectedRow();
                    lastCol = tableInterfacesBridge.getColumnCount() - 1;
                    try {
                        System.out.println(apiClient.estadoInterface(tableInterfacesBridge.getValueAt(selectedRow, 0).toString(), (Boolean) tableInterfacesBridge.getValueAt(selectedRow, lastCol)));
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

        switch (comboBoxValue) {
            case "All interfaces":
                addInterfaceButton.setVisible(false);
                deleteInterfaceButton.setVisible(false);
                ableDisableInterfaceButton.setVisible(false);
                interfacesAllPanel.setVisible(true);
                tabbedPaneWiFi.setVisible(false);
                tabbedPaneBridge.setVisible(false);
                editBridgeButton.setVisible(false);
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
                interfacesAllPanel.setVisible(false);
                if (tabbedPaneWiFi.getTitleAt(tabbedPaneWiFi.getSelectedIndex()).compareTo("Interfaces") == 0) {
                    try {
                        String[] colsWiFi = {"ID", "Name", "Master Interface", "Mode", "SSID", "Band", "Channel Width", "Disabled"};
                        model = new DefaultTableModel(colsWiFi, 0); // ← colunas próprias para WiFi
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
                }else {
                    ableDisableInterfaceButton.setVisible(false);
                    try {
                        String[] colsWiFi = {"ID", "Name", "Disabled"};
                        model = new DefaultTableModel(colsWiFi, 0); // ← colunas próprias para WiFi
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
                }
                break;

            case "Bridge":
                addInterfaceButton.setVisible(true);
                deleteInterfaceButton.setVisible(true);
                ableDisableInterfaceButton.setVisible(true);
                editBridgeButton.setVisible(true);
                interfacesAllPanel.setVisible(false);
                if (tabbedPaneBridge.getSelectedIndex() == 0) {
                    fillBridgeInterfacesTable(false);
                }else {
                    fillBridgePortsTable(false);
                }
                break;
        }

    }

    private void btnEditBridgeButton(ActionEvent actionEvent) {
        clearTableSelection(TablesTypes.BRIDGE);
        boolean isEdited = editBridgeButton.isSelected();

        //System.out.println(isEdited);
        if (isEdited) {
            editBridgeButton.setText("Disable Edit");
        }else {
            editBridgeButton.setText("Enable Edit");
        }
        fillBridgeInterfacesTable(isEdited);
        clearTableSelection(TablesTypes.BRIDGE);
    }

    private void btnUpdateIP(ActionEvent actionEvent) {
        if (addrTable.getSelectedRow() > 0) {
            int selectedRow = addrTable.getSelectedRow();
            UpdateIP update = new UpdateIP(addrTable.getValueAt(selectedRow, 0).toString(), (String) addrTable.getValueAt(selectedRow, 1),owner,apiClient);
            update.pack();
            update.setLocationRelativeTo(owner);
            update.setVisible(true);
            AddressTable();
        }
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
        AddressTable();
    }

    private void btnAbleDisableAddr(ActionEvent actionEvent) {
        int selectedRow = addrTable.getSelectedRow();
        try {
            apiClient.EstadoIPAddress(addrTable.getValueAt(selectedRow, 0).toString(), (Boolean) addrTable.getValueAt(selectedRow, 3));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        AddressTable();
    }

    private void btnAddIP(ActionEvent actionEvent) {
        addAddress novoIP = new addAddress(owner,apiClient);
        novoIP.pack();
        novoIP.setLocationRelativeTo(owner);
        novoIP.setVisible(true);
        AddressTable();
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
        AddressTable();
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
        System.out.println(isEdited);
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

    private void AddressTable() {
        String[] columNames = {"ID","Actual Interface","Address","Disabled"};
        DefaultTableModel model = new DefaultTableModel(columNames, 0);

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
            formatTable(tablePortsBridge, model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("false");
        comboBox.addItem("true");
        tableInterfacesBridge.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));

        JComboBox<String> comboBoxInterfaces = new JComboBox<>();
        try {
            List<GetPorts> interfacesAtuais = apiClient.getBridgePorts();
            for (GetPorts ports : interfacesAtuais){
                comboBoxInterfaces.addItem(ports.interfaceAtual);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableInterfacesBridge.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxInterfaces));

        //Este listener é o responsavel por esta à espera que cliques no enter ou fora da box
        tablePortsBridge.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) return;

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
            //tabbedPaneWiFi.setVisible(false);
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
                boolean state = Boolean.valueOf(tableInterfacesBridge.getValueAt(row, 3).toString());

                //faz a chamada à API para atualizar o DHCP Network editado
                try {
                    System.out.println(apiClient.editInterfaceBridge(interf.id, interf.name, state));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
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
            DefaultTableModel model = getDefaultTableModel(editableColuns);
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

    private DefaultTableModel getDefaultTableModel(Set<Integer> editableColuns) throws Exception {
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
        return model;
    }

    private void formatTable(JTable table,DefaultTableModel model) {
        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean visible = table.getSelectedRowCount() > 0;
                deleteButton.setEnabled(visible);
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
        /*if (dnsTabbed.getSelectedIndex() == 1) {
            int selectedRow = recordsTable.getSelectedRow();
            String id = recordsTable.getValueAt(selectedRow, 0).toString();
            String name = recordsTable.getValueAt(selectedRow, 2).toString();
            String address =  recordsTable.getValueAt(selectedRow, 1).toString();
            boolean disabled = (boolean) recordsTable.getValueAt(selectedRow, 4);

            EditDnsRecord dialog = new EditDnsRecord(owner,id,name,address,disabled);
            dialog.pack();
            dialog.setLocationRelativeTo(owner);
            dialog.setVisible(true);
            clearTableSelection(TablesTypes.DNS);
            fillDnsRecordsTable();
        }*/
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

        fillDhcpLeaseTable(false);

    }

    private void setStyle(Color backColor, Color textColor) {
        //Define Font style
        Font font = new Font("JetBrains Mono", Font.PLAIN,14);
/*
        //Set Background Colors
        //Panels Style
        Color buttonColor = new Color(12, 61, 101);
        //Color buttonColor = new Color(255 - backColor.getRed(), 255 - backColor.getGreen(), 255 - backColor.getBlue(), 15);;

        // hoverColor = 10~15% mais claro ou escuro dependendo da luminosidade
        float[] hsb = Color.RGBtoHSB(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), null);
        float brightness = Math.min(hsb[2] * 1.15f, 1.0f); // +15% brilho
        Color hoverColor = Color.getHSBColor(hsb[0], hsb[1], brightness);*/

        JComponent[] components = {
                //Pannels
                mainPanel,contentPanel, navBar, topBar,cpuPanel,statsPanel,dhcpPanel,interfacePanel,addrPanel,dnsPanel,interfacesAllPanel,routePanel,updatePanel,
                //Top barLabels
                hostLabel,userLabel, hostIpText, usernameText,
                //VersionPanel
                status,statusLabel,chanel,chanelLabel,instaLabel,instaled,latestLabel,latest,
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
                //Rout Panel
                tableRoute,

        };

        for (JComponent component : components){
            component.setBackground(backColor);
            component.setForeground(textColor);
            component.setFont(font);
        }


        divider.setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(),50));


        // --- Botões ---
        AbstractButton[] buttons = {
                logoutButton,homeButton, dnsButton, routeButton, addressButton,
                dhcpButton, interfaceButton,
                addRecordButton, deleteRecordButton,updateButton,checkUpdateButton,
                deleteButton, editDhcpButton,
                addClientButton, addServerButton, addNetworkButton, addLeaseButton, editRecordsToggleButton,
                UpdateIPButton, removeIPButton, ableDisableAddrButton, addIPButton,
                deleteInterfaceButton, ableDisableInterfaceButton, addInterfaceButton,clearCacheButton
        };

        Color buttonColor = new Color(16, 83, 138);
        Color hoverColor = new Color(102, 210, 170);
        Color borderColor = new Color(10, 52, 86,60);

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
        }

        String iconColor = "white";
        if (textColor.getBlue() ==255 && textColor.getRed() ==255 && textColor.getGreen() ==255) {
                iconColor = "white";
        }
        Image home = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/home_" + iconColor+ ".png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        homeButton.setIcon(new ImageIcon(home));

        Image dns = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dns_" + iconColor+ ".png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        dnsButton.setIcon(new ImageIcon(dns));

        Image dhcp = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dhcp_" + iconColor+ ".png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        dhcpButton.setIcon(new ImageIcon(dhcp));

        Image route = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/routes_" + iconColor+ ".png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        routeButton.setIcon(new ImageIcon(route));

        Image inter = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/interface_" + iconColor+ ".png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        interfaceButton.setIcon(new ImageIcon(inter));

        Image addresses = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/address_" + iconColor+ ".png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        addressButton.setIcon(new ImageIcon(addresses));

        Image check = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/refresh_" + iconColor+ ".png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        checkUpdateButton.setIcon(new ImageIcon(check));
        UpdateIPButton.setIcon(new ImageIcon(check));
        ableDisableInterfaceButton.setIcon(new ImageIcon(check));
        ableDisableAddrButton.setIcon(new ImageIcon(check));
        ableDisableStaticRouteButton.setIcon(new ImageIcon(check));

        Image remove = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/delete_" + iconColor+ ".png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        removeIPButton.setIcon(new ImageIcon(remove));
        deleteButton.setIcon(new ImageIcon(remove));
        deleteInterfaceButton.setIcon(new ImageIcon(remove));
        deleteRecordButton.setIcon(new ImageIcon(remove));
        deleteStaticRouteButton.setIcon(new ImageIcon(remove));
        clearCacheButton.setIcon(new ImageIcon(remove));

        Image edit = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/edit_" + iconColor+ ".png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        editDhcpButton.setIcon(new ImageIcon(edit));
        editRecordsToggleButton.setIcon(new ImageIcon(edit));

        Image add = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/add_" + iconColor+ ".png"))).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        addInterfaceButton.setIcon(new ImageIcon(add));
        addIPButton.setIcon(new ImageIcon(add));
        addServerButton.setIcon(new ImageIcon(add));
        addClientButton.setIcon(new ImageIcon(add));
        addLeaseButton.setIcon(new ImageIcon(add));
        addRecordButton.setIcon(new ImageIcon(add));
        addLeaseButton.setIcon(new ImageIcon(add));
        addNetworkButton.setIcon(new ImageIcon(add));
        addStaticRouteButton.setIcon(new ImageIcon(add));


        progressBarLoad.setForeground(Color.GREEN);
        progressBarMem.setForeground(Color.GREEN);
        progressBarLoad.setFont(font);
        progressBarMem.setFont(font);
        progressBarHdd.setFont(font);

        //Set visible components
        addRecordButton.setVisible(false);
        updateButton.setVisible(false);

        deleteRecordButton.setVisible(false);
        deleteRecordButton.setEnabled(false);

        editRecordsToggleButton.setVisible(false);

        deleteButton.setEnabled(false);
        addNetworkButton.setVisible(false);

        addServerButton.setVisible(false);

        addClientButton.setVisible(false);

        addLeaseButton.setVisible(true);

    }

    private void applyHoverEffect(AbstractButton button, Color normal, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
                button.setBorder(BorderFactory.createLineBorder(new Color(73, 180, 128,60), 5,true));
                button.setForeground(Color.DARK_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
                button.setBorder(BorderFactory.createLineBorder(new Color(10, 52, 86,60), 5,true));
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
