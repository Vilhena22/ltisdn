package Forms;

import ApiClient.ApiClient;
import Dialogs.Add.*;
import Dialogs.Edit.*;
import Models.Address.GetAddress;
import Models.Dhcp.Clients.DhcpClient;
import Models.Dhcp.Leases.DhcpLease;
import Models.Dhcp.Networks.DhcpNetwork;
import Models.Dhcp.Servers.DhcpServer;
import Models.Dns.DnsCache;
import Models.Dns.DnsRecord;
import Models.Interfaces.bridge.interfaces.getInterfaceBridge;
import Models.Interfaces.getAllInterfaces;
import Models.Interfaces.wifi.interfaces.GetInterfacesWiFi;
import Models.System.SystemResources;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class HomePage {
    private final JFrame owner;
    private JPanel mainPanel;
    private JPanel navBar;
    private JButton routeButton;
    private JLabel hostIp;
    private JLabel username;
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
    private JButton deleteRecord;
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
    private JButton UpdateIP;
    private JButton removeIPButton;
    private JButton ableDisableAddrButton;
    private JButton addIPButton;
    private JPanel interfacePanel;
    private JButton ableDisableInterfaceButton;
    private JButton deleteInterfaceButton;
    private JTable interfaceTable;
    private JComboBox comboBoxInterfaces;
    private JButton addInterfaceButton;
    private JTabbedPane tabbedPaneGeral;
    private JTable tableInterfacesWiFi;
    private JTable tableSP;
    private JScrollPane interfacesAllPanel;
    private JButton addPoolButton;
    private JButton addLeaseButton;
    private JButton addClientButton;
    private JButton addServerButton;
    private JButton editButton;
    private final ApiClient apiClient;



    public HomePage(Boolean isThemeDark, JFrame owner){

        this.owner = owner;
        setFonts();

        if (isThemeDark) {
            setColors(new Color(60, 63, 65), Color.WHITE);
        }else {
            setColors(Color.WHITE, new Color(60, 63, 65));
        }
        this.apiClient = new ApiClient();

        comboBoxInterfaces.addActionListener(e -> interfaceTable());
        try {
            setDashboardValues();
            if (isThemeDark) {
                buildChart(new Color(60, 63, 65), Color.WHITE);
            }else {
                buildChart(Color.WHITE, new Color(60, 63, 65));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        homeButton.addActionListener(this::btnHomeButton);
        dnsButton.addActionListener(this::btnDnsButton);
        dhcpButton.addActionListener(this::btnDhcpButton);
        clearCacheButton.addActionListener(this::btnClearCache);
        addRecordButton.addActionListener(this::btnAddRecord);
        deleteRecord.addActionListener(this::btnDeleteRecord);
        deleteButton.addActionListener(this::btnDeleteButton);
        addressButton.addActionListener(this::btnAddress);
        addIPButton.addActionListener(this::btnAddIP);
        ableDisableAddrButton.addActionListener(this::btnAbleDisableAddr);
        removeIPButton.addActionListener(this::btnRemoveIP);
        UpdateIP.addActionListener(this::btnUpdateIP);
        interfaceButton.addActionListener(this::btnInterface);
        ableDisableInterfaceButton.addActionListener(this::btnAbleDisableInterface);
        deleteInterfaceButton.addActionListener(this::btnDeleteInterface);
        addInterfaceButton.addActionListener(this::btnAddInterface);
        editButton.addActionListener(this::btnEditButton);
        addNetworkButton.addActionListener(this::btnAddPool);
        addLeaseButton.addActionListener(this::btnAddLease);
        addClientButton.addActionListener(this::btnAddClient);
        addServerButton.addActionListener(this::btnAddServer);
    }

    private void btnAddInterface(ActionEvent actionEvent) {
         String selected = (String) comboBoxInterfaces.getSelectedItem();
         if (selected.compareTo("All interfaces") == 0) {
             JOptionPane.showMessageDialog(null, "Must be 'WiFi' or 'Bridge'!", "Erro", JOptionPane.ERROR_MESSAGE);
         }else {
             if (selected.compareTo("Wi-Fi") == 0){
                 addInterfaceWiFi novaInterfaceWiFi = new addInterfaceWiFi();
                 novaInterfaceWiFi.pack();
                 novaInterfaceWiFi.setLocationRelativeTo(owner);
                 novaInterfaceWiFi.setVisible(true);
             }else {
                 addInterfaceBridge novaInterfaceBridge = new addInterfaceBridge();
                 novaInterfaceBridge.pack();
                 novaInterfaceBridge.setLocationRelativeTo(owner);
                 novaInterfaceBridge.setVisible(true);
             }
         }
         interfaceTable();
    }

    private void btnDeleteInterface(ActionEvent actionEvent) {

        String comboBoxValue = (String) comboBoxInterfaces.getSelectedItem();
        int selected = interfaceTable.getSelectedRow();
        String id = interfaceTable.getValueAt(selected, 0).toString();

        switch (comboBoxValue) {
            case "All interfaces":
                try {
                    apiClient.deleteInterface(id);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                interfaceTable();
                break;

            case "Wi-Fi":
                try {
                    apiClient.DeleteInterfaceWiFi(id);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                interfaceTable();
                break;

            case "Bridge":
                try {
                    apiClient.deleteInterfaceBridge(id);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                interfaceTable();
                break;
        }
    }

    private void btnAbleDisableInterface(ActionEvent actionEvent) {
        int selectedRow = interfaceTable.getSelectedRow();
        int lastCol = interfaceTable.getColumnCount() -1;

        if (comboBoxInterfaces.getSelectedItem().toString().equals("Wi-Fi")) {
            if (tabbedPaneGeral.getTitleAt(tabbedPaneGeral.getSelectedIndex()).compareTo("Security Profiles") == 0) {
                ableDisableInterfaceButton.setEnabled(false);
            }
        }

        try {
            apiClient.estadoInterface(interfaceTable.getValueAt(selectedRow, 0).toString(), (Boolean) interfaceTable.getValueAt(selectedRow, lastCol));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        interfaceTable();
    }

    private void btnInterface(ActionEvent actionEvent) {
        interfacePanel.setVisible(true);
        addrPanel.setVisible(false);
        dnsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        statsPanel.setVisible(false);

        interfaceTable();
    }

    private void interfaceTable() {

        String comboBoxValue = (String) comboBoxInterfaces.getSelectedItem();
        DefaultTableModel model;

        switch (comboBoxValue) {
            case "All interfaces":
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
                    formatTable(interfaceTable, model);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

            case "Wi-Fi":
                interfacesAllPanel.setVisible(false);
                try {
                    String[] colsWiFi = {"ID", "Band", "ARP", "MAC Address", "Bridge Mode", "Channel Width", "Disabled"};
                    model = new DefaultTableModel(colsWiFi, 0); // ← colunas próprias para WiFi
                    for (GetInterfacesWiFi interf : apiClient.GetInterfacesWiFi()) {
                        Object[] row = {
                                interf.id,
                                interf.band,
                                interf.arp,
                                interf.mac_address,
                                interf.bridge_mode,
                                interf.channel_width,
                                interf.disabled
                        };
                        model.addRow(row);
                    }
                    tabbedPaneGeral.setVisible(true);
                    formatTable(tableInterfacesWiFi, model);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

            case "Bridge":
                try {
                    String[] colsBridge = {"ID", "Name", "Running", "Disabled"};
                    model = new DefaultTableModel(colsBridge, 0);
                    for (getInterfaceBridge interf : apiClient.getBridgeInterfaces()) {
                        Object[] row = {
                                interf.id,
                                interf.name,
                                interf.running,
                                interf.disabled
                        };
                        model.addRow(row);
                    }
                    formatTable(interfaceTable, model);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
        }

    }

    private void btnUpdateIP(ActionEvent actionEvent) {
        if (addrTable.getSelectedRow() > 0) {
            int selectedRow = addrTable.getSelectedRow();
            UpdateIP update = new UpdateIP(addrTable.getValueAt(selectedRow, 0).toString(), (String) addrTable.getValueAt(selectedRow, 1));
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
        addAddress novoIP = new addAddress();
        novoIP.pack();
        novoIP.setLocationRelativeTo(owner);
        novoIP.setVisible(true);
        AddressTable();
    }

    private void btnAddress(ActionEvent actionEvent) {
        addrPanel.setVisible(true);
        dnsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        statsPanel.setVisible(false);

        AddressTable();
    }

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



        //Listner para trocar os botoes consoante a tabela selecionada
        dnsTabbed.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (dnsTabbed.getSelectedIndex()) {
                    case 0:
                        fillDnsCacheTable();
                        addRecordButton.setVisible(false);
                        deleteRecord.setVisible(false);
                        clearCacheButton.setVisible(true);
                        break;
                    case 1:
                        fillDnsRecordsTable();
                        addRecordButton.setVisible(true);
                        deleteRecord.setVisible(true);
                        clearCacheButton.setVisible(false);
                        break;
                }
            }
        });

        //Listner para trocar os botoes consoante a tabela selecionada
        tabbedDhcp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (tabbedDhcp.getSelectedIndex()) {
                    case 0:
                        fillDhcpLeaseTable();
                        clearTableSelection();
                        addNetworkButton.setVisible(false);
                        addLeaseButton.setVisible(true);
                        addClientButton.setVisible(false);
                        addServerButton.setVisible(false);
                        break;
                    case 1:
                        fillDhcpServerTable();
                        clearTableSelection();
                        addNetworkButton.setVisible(false);
                        addLeaseButton.setVisible(false);
                        addClientButton.setVisible(false);
                        addServerButton.setVisible(true);
                        break;
                    case 2:
                        fillDhcpClientTable();
                        clearTableSelection();
                        addNetworkButton.setVisible(false);
                        addLeaseButton.setVisible(false);
                        addClientButton.setVisible(true);
                        addServerButton.setVisible(false);
                        break;
                    case 3:
                        fillDhcpNetworkTable();
                        clearTableSelection();
                        addNetworkButton.setVisible(true);
                        addLeaseButton.setVisible(false);
                        addClientButton.setVisible(false);
                        addServerButton.setVisible(false);
                        break;
                }
            }
        });


        //Cria Listneers para quando tiver itens selecionados o botao delete ativa
        recordsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = recordsTable.getSelectedRow() != -1;
                deleteRecord.setEnabled(isSelected);
            }
        });

        dhcpNetworkTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpNetworkTable.getSelectedRow() != -1;
                deleteButton.setEnabled(isSelected);
                editButton.setEnabled(isSelected);

            }
        });

        dhcpLeasesTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpLeasesTable.getSelectedRow() != -1;
                deleteButton.setEnabled(isSelected);
                editButton.setEnabled(isSelected);

            }
        });

        dhcpClientsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpClientsTable.getSelectedRow() != -1;
                deleteButton.setEnabled(isSelected);
                editButton.setEnabled(isSelected);

            }
        });

        dhcpServerTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                boolean isSelected = dhcpServerTable.getSelectedRow() != -1;
                deleteButton.setEnabled(isSelected);
                editButton.setEnabled(isSelected);

            }
        });


        statsPanel.setVisible(true);
        cpuPanel.setVisible(true);
        dnsPanel.setVisible(false);
        dhcpPanel.setVisible(false);

    }

    private void btnAddServer(ActionEvent actionEvent) {
        AddDhcpServer dialog = new AddDhcpServer(owner);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
        fillDhcpServerTable();
    }

    private void btnEditButton(ActionEvent actionEvent) {
        int selectedRow;
        String id;
        switch (tabbedDhcp.getTitleAt(tabbedDhcp.getSelectedIndex()).toLowerCase()) {
            case "leases":
                selectedRow = dhcpLeasesTable.getSelectedRow();
                id = dhcpLeasesTable.getValueAt(selectedRow, 0).toString();
                String address =  dhcpLeasesTable.getValueAt(selectedRow, 1).toString();
                String clientID =  dhcpLeasesTable.getValueAt(selectedRow, 3).toString();
                String server =  dhcpLeasesTable.getValueAt(selectedRow, 7).toString();
                EditDhcpLease dialogLease = new EditDhcpLease(owner,id,address,clientID,server);
                dialogLease.pack();
                dialogLease.setLocationRelativeTo(owner);
                dialogLease.setVisible(true);
                clearTableSelection();
                fillDhcpLeaseTable();

                break;
            case "clients":
                selectedRow = dhcpClientsTable.getSelectedRow();
                id = dhcpClientsTable.getValueAt(selectedRow, 0).toString();
                String intef =  dhcpClientsTable.getValueAt(selectedRow, 1).toString();
                String defaultRoute = dhcpClientsTable.getValueAt(selectedRow, 2).toString();
                String dns =  dhcpClientsTable.getValueAt(selectedRow, 3).toString();
                String ntp =  dhcpClientsTable.getValueAt(selectedRow, 4).toString();
                String disabled =  dhcpClientsTable.getValueAt(selectedRow, 5).toString();
                EditDhcpClient dialogClient = new EditDhcpClient(owner,id,intef,defaultRoute,dns,ntp,disabled);
                dialogClient.pack();
                dialogClient.setLocationRelativeTo(owner);
                dialogClient.setVisible(true);
                clearTableSelection();
                fillDhcpClientTable();
                break;
            case "server":
                selectedRow = dhcpServerTable.getSelectedRow();
                id = dhcpServerTable.getValueAt(selectedRow, 0).toString();
                String name =  dhcpServerTable.getValueAt(selectedRow, 1).toString();
                String interSelected =  dhcpServerTable.getValueAt(selectedRow, 2).toString();
                disabled =  dhcpServerTable.getValueAt(selectedRow, 4).toString();
                EditDhcpServer dialogServer = new EditDhcpServer(owner,id,name,interSelected,disabled);
                dialogServer.pack();
                dialogServer.setLocationRelativeTo(owner);
                dialogServer.setVisible(true);
                clearTableSelection();
                fillDhcpServerTable();
                break;
            case "networks":
                selectedRow = dhcpNetworkTable.getSelectedRow();
                id = dhcpNetworkTable.getValueAt(selectedRow, 0).toString();
                String network =  dhcpNetworkTable.getValueAt(selectedRow, 1).toString();
                dns =  dhcpNetworkTable.getValueAt(selectedRow, 2).toString();
                String gateway =  dhcpNetworkTable.getValueAt(selectedRow, 3).toString();
                EditDhcpNetwork dialogNetwork = new EditDhcpNetwork(owner,id,network,dns,gateway);
                dialogNetwork.pack();
                dialogNetwork.setLocationRelativeTo(owner);
                dialogNetwork.setVisible(true);
                clearTableSelection();
                fillDhcpNetworkTable();
                break;
        }
    }

    private void btnAddClient(ActionEvent actionEvent) {
        AddDhcpClient clientDialog = new AddDhcpClient(owner);
        clientDialog.pack();
        clientDialog.setLocationRelativeTo(owner);
        clientDialog.setVisible(true);
        fillDhcpClientTable();
    }

    private void clearTableSelection() {
        dhcpNetworkTable.clearSelection();
        dhcpServerTable.clearSelection();
        dhcpClientsTable.clearSelection();
        dhcpLeasesTable.clearSelection();
    }

    private void btnAddLease(ActionEvent actionEvent) {
        AddDhcpLease leaseDialog = new AddDhcpLease(owner);
        leaseDialog.pack();
        leaseDialog.setLocationRelativeTo(owner);
        leaseDialog.setVisible(true);
        fillDhcpLeaseTable();
    }

    private void btnAddPool(ActionEvent actionEvent) {
        AddDhcpNetwork poolDialog = new AddDhcpNetwork(owner);
        poolDialog.pack();
        poolDialog.setLocationRelativeTo(owner);
        poolDialog.setVisible(true);
        fillDhcpNetworkTable();

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
                clearTableSelection();
                fillDhcpLeaseTable();

                break;
            case "clients":
                for (int row : dhcpClientsTable.getSelectedRows()) {
                    try {
                        apiClient.deleteDhcpClients(dhcpClientsTable.getValueAt(row, 0).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection();
                fillDhcpClientTable();
                break;
            case "server":
                for (int row : dhcpServerTable.getSelectedRows()) {
                    try {
                        apiClient.deleteDhcpServer(dhcpServerTable.getValueAt(row, 0).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection();
                fillDhcpServerTable();
                break;
            case "networks":
                for (int row : dhcpNetworkTable.getSelectedRows()) {
                    try {
                        apiClient.deleteDhcpNetwork(dhcpNetworkTable.getValueAt(row, 0).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                clearTableSelection();
                fillDhcpNetworkTable();
                break;
        }
    }

    private void btnDhcpButton(ActionEvent actionEvent)  {
        dnsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        statsPanel.setVisible(false);
        fillDhcpLeaseTable();
        dhcpPanel.setVisible(true);

    }

    private void fillDhcpLeaseTable() {
        try {
            String [] columNames = new String[] {"ID","Address","Blocked","Client-ID","Disabled","Dynamic", "Last-Seen","Server","Status" };
            DefaultTableModel model = new DefaultTableModel(columNames, 0);
            for (DhcpLease lease : apiClient.getDhcpLeases()) {
                Object[] row = {
                        lease.id,
                        lease.address,
                        lease.blocked,
                        lease.clientId,
                        lease.disabled,
                        lease.dynamic,
                        lease.lastSeen,
                        lease.server,
                        lease.status
                };
                model.addRow(row);
            }
            formatTable(dhcpLeasesTable, model);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

    private void fillDhcpServerTable() {
        try {
            String [] columNames = new String[] {"ID","Name","Interface","Address Pool","Disabled","Dynamic"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0);
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

    }

    private void fillDhcpClientTable() {
        try {
            String[] columNames = {"ID","Inteface","Add Default Route","Dns-Server","Ntp-Server","Disabled"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0);
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
    }

    private void fillDhcpNetworkTable() {
        String[] columNames = {"ID","Network","Dns-Server", "Gateway","Dynamic","Dhcp-Option","Ntp-Server","Wins-Server" };
        DefaultTableModel model = new DefaultTableModel(columNames, 0);

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

    }

    private void formatTable(JTable table,DefaultTableModel model) {
        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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

    private void btnDeleteRecord(ActionEvent actionEvent) {
        for (int row : recordsTable.getSelectedRows()) {
            try {
                apiClient.deleteDnsRecord(recordsTable.getValueAt(row, 0).toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        recordsTable.clearSelection();
        fillDnsRecordsTable();
    }

    private void btnAddRecord(ActionEvent actionEvent) {
        AddDnsRecord addDnsRecord = new AddDnsRecord(owner);
        addDnsRecord.pack();
        addDnsRecord.setLocationRelativeTo(owner);
        addDnsRecord.setVisible(true);
        fillDnsRecordsTable();
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

    private void fillDnsRecordsTable() {

        try {
            String [] columNames = {"ID", "Data", "Name", "Dynamic","Disabled", "TTL", "Type"};
            DefaultTableModel model = new DefaultTableModel(columNames, 0);
            for (DnsRecord dnsRecord : apiClient.getDnsRecord()) {
                Object[] row = {
                        dnsRecord.id,
                        dnsRecord.address,
                        dnsRecord.name,
                        dnsRecord.dynamic,
                        dnsRecord.disabled,
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

    }

    private void btnClearCache(ActionEvent actionEvent) {
        try {
            apiClient.postClearDnsCache();
            fillDnsCacheTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnDnsButton(ActionEvent actionEvent) {
        statsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        dhcpPanel.setVisible(false);
        dnsPanel.setVisible(true);
    }

    private void btnHomeButton(ActionEvent actionEvent) {
        try {
            statsPanel.setVisible(true);
            cpuPanel.setVisible(true);
            dnsPanel.setVisible(false);
            dhcpPanel.setVisible(false);
            setDashboardValues();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setColors(Color backColor, Color textColor) {
        //Set Background Colors
        //Panels Style

        Color buttonColor = new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(),15);
        mainPanel.setBackground(backColor);
        contentPanel.setBackground(backColor);
        navBar.setBackground(backColor);
        topBar.setBackground(backColor);
        cpuPanel.setBackground(backColor);
        statsPanel.setBackground(backColor);
        statsPanel.setForeground(textColor);
        dhcpPanel.setBackground(backColor);
        dnsPanel.setBackground(backColor);
        divider.setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(),50));



        //NavBar Buttons Style
        homeButton.setBackground(backColor);
        homeButton.setForeground(textColor);

        dnsButton.setBackground(backColor);
        dnsButton.setForeground(textColor);

        routeButton.setBackground(backColor);
        routeButton.setForeground(textColor);

        addressButton.setBackground(backColor);
        addressButton.setForeground(textColor);

        dhcpButton.setBackground(backColor);
        dhcpButton.setForeground(textColor);

        interfaceButton.setBackground(backColor);
        interfaceButton.setForeground(textColor);

        String iconColor = "black";
        if (textColor.getBlue() ==255 && textColor.getRed() ==255 && textColor.getGreen() ==255) {
                iconColor = "white";
        }
        Image home = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/home_" + iconColor+ ".png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        homeButton.setIcon(new ImageIcon(home));

        Image dns = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dns_" + iconColor+ ".png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        dnsButton.setIcon(new ImageIcon(dns));

        Image dhcp = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dhcp_" + iconColor+ ".png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        dhcpButton.setIcon(new ImageIcon(dhcp));

        Image route = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/routes_" + iconColor+ ".png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        routeButton.setIcon(new ImageIcon(route));

        Image inter = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/interface_" + iconColor+ ".png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        interfaceButton.setIcon(new ImageIcon(inter));

        Image addresses = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/address_" + iconColor+ ".png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        addressButton.setIcon(new ImageIcon(addresses));




        //Top Bar Style
        hostLabel.setForeground(textColor);
        userLabel.setForeground(textColor);
        hostIp.setForeground(textColor);
        username.setForeground(textColor);


        //CPU Panel
        frequency.setForeground(textColor);
        freqLabel.setForeground(textColor);
        cores.setForeground(textColor);
        cpuLabel.setForeground(textColor);
        cpuName.setForeground(textColor);
        uptime.setForeground(textColor);
        uptimeLabel.setForeground(textColor);
        version.setForeground(textColor);
        versionLabel.setForeground(textColor);
        memoryLabel.setForeground(textColor);
        loadLabel.setForeground(textColor);
        coresLabel.setForeground(textColor);
        hddLabel.setForeground(textColor);
        progressBarLoad.setForeground(Color.GREEN);
        progressBarMem.setForeground(Color.GREEN);




        //DNS Panel
        clearCacheButton.setBackground(buttonColor);
        clearCacheButton.setForeground(textColor);

        addRecordButton.setBackground(buttonColor);
        addRecordButton.setForeground(textColor);

        deleteRecord.setBackground(buttonColor);
        deleteRecord.setForeground(textColor);

        dnsTabbed.setForeground(textColor);

        cacheTable.setBackground(backColor);
        cacheTable.setForeground(textColor);

        recordsTable.setBackground(backColor);
        recordsTable.setForeground(textColor);

        dnsPanel.setVisible(false);

        //InterfacesPanel
        interfaceTable.setBackground(buttonColor);
        interfaceTable.setForeground(textColor);

        // DHCP Panel

        dhcpPanel.setBackground(backColor);
        dhcpPanel.setForeground(textColor);

        deleteButton.setBackground(buttonColor);
        deleteButton.setForeground(textColor);

        editButton.setBackground(buttonColor);
        editButton.setForeground(textColor);

        tabbedDhcp.setForeground(textColor);

        dhcpServerTable.setBackground(backColor);
        dhcpServerTable.setForeground(textColor);

        dhcpNetworkTable.setBackground(backColor);
        dhcpNetworkTable.setForeground(textColor);

        dhcpClientsTable.setBackground(backColor);
        dhcpClientsTable.setForeground(textColor);

        dhcpLeasesTable.setBackground(backColor);
        dhcpLeasesTable.setForeground(textColor);

        dhcpPanel.setVisible(false);

        tabbedPaneGeral.setVisible(false);

        addClientButton.setBackground(buttonColor);
        addClientButton.setForeground(textColor);

        addNetworkButton.setBackground(buttonColor);
        addNetworkButton.setForeground(textColor);

        addServerButton.setBackground(buttonColor);
        addServerButton.setForeground(textColor);

        addLeaseButton.setBackground(buttonColor);
        addLeaseButton.setForeground(textColor);



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

        /*for ( getAllInterfaces interf : apiClient.getAllInterfaces()) {
            dataset.addValue(interf.rxDrop, "RX Drop", );
            dataset.addValue(interf.txDrop, "TX Drop", client.getDefaultRouteDistance());
        }*/
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "Interface",
                "Quantidade",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        barChart.setBackgroundPaint(backColor);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        chartPanel.setForeground(textColor);
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setMouseWheelEnabled(false);
        chartPanel.setBackground(textColor);

        // Adicionar ao JPanel existente no form (chartPanel1)
        statsPanel.setLayout(new BorderLayout());
        statsPanel.add(chartPanel, BorderLayout.CENTER);
        statsPanel.validate(); // força o layout a atualizar
    }

    private void setFonts(){
        Font font = new Font("JetBrains Mono", Font.PLAIN,14);
        //MainPanel
        navBar.setFont(font);
        topBar.setFont(font);
        contentPanel.setFont(font);


        //Top Panel
        hostLabel.setFont(font);
        hostIp.setFont(font);
        userLabel.setFont(font);
        username.setFont(font);

        //NavBar
        homeButton.setFont(font);
        addressButton.setFont(font);
        dnsButton.setFont(font);
        interfaceButton.setFont(font);
        dhcpButton.setFont(font);
        routeButton.setFont(font);
        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);
        addressButton.setFocusPainted(false);
        addressButton.setBorderPainted(false);
        dnsButton.setFocusPainted(false);
        dnsButton.setBorderPainted(false);
        interfaceButton.setFocusPainted(false);
        interfaceButton.setBorderPainted(false);
        dhcpButton.setFocusPainted(false);
        dhcpButton.setBorderPainted(false);
        routeButton.setFocusPainted(false);
        routeButton.setBorderPainted(false);


        //CPU Panel
        cpuLabel.setFont(font);
        cpuName.setFont(font);
        coresLabel.setFont(font);
        cores.setFont(font);
        freqLabel.setFont(font);
        frequency.setFont(font);
        loadLabel.setFont(font);
        uptimeLabel.setFont(font);
        versionLabel.setFont(font);
        uptime.setFont(font);
        memoryLabel.setFont(font);
        version.setFont(font);
        hddLabel.setFont(font);
        progressBarLoad.setFont(font);
        progressBarMem.setFont(font);
        progressBarHdd.setFont(font);


        //InterfacesPanel
        interfaceTable.setFont(font);


        //DNS Panel
        clearCacheButton.setFont(font);
        clearCacheButton.setFocusPainted(false);
        clearCacheButton.setBorderPainted(false);
        addRecordButton.setFont(font);
        addRecordButton.setFocusPainted(false);
        addRecordButton.setBorderPainted(false);
        addRecordButton.setVisible(false);
        deleteRecord.setFont(font);
        deleteRecord.setFocusPainted(false);
        deleteRecord.setBorderPainted(false);
        deleteRecord.setVisible(false);

        cacheTable.setFont(font);
        recordsTable.setFont(font);
        dnsTabbed.setFont(font);

        //DHCP Panel

        deleteButton.setFont(font);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setEnabled(false);

        editButton.setFont(font);
        editButton.setFocusPainted(false);
        editButton.setBorderPainted(false);
        editButton.setEnabled(false);

        tabbedDhcp.setFont(font);

        dhcpLeasesTable.setFont(font);
        dhcpClientsTable.setFont(font);
        dhcpNetworkTable.setFont(font);
        dhcpServerTable.setFont(font);

        addNetworkButton.setFont(font);
        addNetworkButton.setFocusPainted(false);
        addNetworkButton.setBorderPainted(false);
        addNetworkButton.setVisible(false);

        addServerButton.setFont(font);
        addServerButton.setFocusPainted(false);
        addServerButton.setBorderPainted(false);
        addServerButton.setVisible(false);

        addClientButton.setFont(font);
        addClientButton.setFocusPainted(false);
        addClientButton.setBorderPainted(false);
        addClientButton.setVisible(false);

        addLeaseButton.setFont(font);
        addLeaseButton.setFocusPainted(false);
        addLeaseButton.setBorderPainted(false);
        addLeaseButton.setVisible(true);

    }


}
