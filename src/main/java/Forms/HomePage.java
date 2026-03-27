package Forms;

import ApiClient.ApiClient;
import Models.Address.GetAddress;
import Models.Dhcp.Clients.DhcpClient;
import Models.Dhcp.Leases.DhcpLease;
import Models.Dhcp.Pools.DhcpPool;
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
    private JLabel hostip;
    private JLabel username;
    private JPanel topBar;
    private JButton dhcpButton;
    private JButton interfaceButton;
    private JButton addressButton;
    private JButton homeButton;
    private JPanel contentPanel;
    private JButton dnsButton;
    private JLabel hoslLabel;
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
    private JButton addRecordbutton;
    private JTabbedPane tabbedPane1;
    private JTable recordsTable;
    private JButton deleteRecord;
    private JPanel dhcpPanel;
    private JTabbedPane tabbedDhcp;
    private JTable dhcpPoolTable;
    private JTable dhcpClientsTable;
    private JTable dhcpServerTable;
    private JTable dhcpLeasesTable;
    private JButton deleteButton;
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
    private final ApiClient apiClient;


    public HomePage(Boolean isThemeDark, JFrame owner) throws Exception{

        this.owner = owner;

        createUIComponents();

        if (isThemeDark) {
            setColors(new Color(60, 63, 65), Color.WHITE);
        }else {
            setColors(Color.WHITE, new Color(60, 63, 65));
        }
        this.apiClient = new ApiClient();

        setDashboardValues();
//        if (isThemeDark) {
//            buildChart(new Color(60, 63, 65), Color.WHITE);
//        }else {
//            buildChart(Color.WHITE, new Color(60, 63, 65));
//        }
        comboBoxInterfaces.addActionListener(e -> interfaceTable());

        homeButton.addActionListener(this::btnHomeButton);
        dnsButton.addActionListener(this::btnDnsButton);
        dhcpButton.addActionListener(this::btnDhcpButton);
        clearCacheButton.addActionListener(this::btnClearCahce);
        addRecordbutton.addActionListener(this::btnAddRecord);
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
    }

    private void btnDeleteButton(ActionEvent actionEvent) {

    }

    private void btnDhcpButton(ActionEvent actionEvent)  {
        dnsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        statsPanel.setVisible(false);

        fillDhcpTable();

        dhcpPanel.setVisible(true);

    }

    private void fillDhcpTable() {
        String[] columNames = {"ID","Address","Caps-Manager","Dhcp-Option","Dns-Derver","Dynamic", "Gateway","Ntp-Server","Wins-Server" };
        DefaultTableModel model = new DefaultTableModel(columNames, 0);

        try {
            for (DhcpPool dhcpPool : apiClient.getDhcpPools()) {
                Object[] row = {
                        dhcpPool.id,
                        dhcpPool.address,
                        dhcpPool.capsManager,
                        dhcpPool.dhcpOption,
                        dhcpPool.dnsServer,
                        dhcpPool.dynamic,
                        dhcpPool.gateway,
                        dhcpPool.ntpServer,
                        dhcpPool.winsServer
                };
                model.addRow(row);
            }
            formatTable(dhcpPoolTable, model);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            columNames = new String[] {"ID","Address","Caps-Manager","Dhcp-Option","Dns-Derver","Dynamic", "Gateway","Ntp-Server","Wins-Server" };
            model = new DefaultTableModel(columNames, 0);
            for (DhcpClient dhcpClient : apiClient.getDhcpClients()) {
                Object[] row = {
                        dhcpClient.id,
                        dhcpClient.interfaceName,
                        dhcpClient.defaultRouteTables,
                        dhcpClient.dhcpOption
                };
                model.addRow(row);
            }

            formatTable(dhcpClientsTable, model);

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        try {
            columNames = new String[] {"ID","Address","Blocked","Client-ID","Disabled","Dynamic", "Last-Seen","Server","Status" };
            model = new DefaultTableModel(columNames, 0);
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

            dhcpPanel.setVisible(true);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

    private void formatTable(JTable table,DefaultTableModel model) {
        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getSelectionModel().addListSelectionListener(e2 -> {
            if (e2.getValueIsAdjusting()) {
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
        int[] selectedRows = recordsTable.getSelectedRows();
        for (int row : selectedRows) {
            String id  = recordsTable.getValueAt(row, 0).toString();
            try {
                apiClient.deleteDnsRecord(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        fillDnsTable();
    }

    private void btnAddRecord(ActionEvent actionEvent) {
        AddDnsRecord addDnsRecord = new AddDnsRecord(owner);
        addDnsRecord.pack();
        addDnsRecord.setLocationRelativeTo(owner);
        addDnsRecord.setVisible(true);
        fillDnsTable();
    }


    private void fillDnsTable() {
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
            formatTable(cacheTable, model);

            columNames = new String[]{"ID", "Data", "Name", "Dynamic","Disabled", "TTL", "Type"};
            model = new DefaultTableModel(columNames, 0);
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

    private void btnClearCahce(ActionEvent actionEvent) {
        try {
            apiClient.postClearDnsCache();
            btnDnsButton(actionEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnDnsButton(ActionEvent actionEvent) {
        statsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        dhcpPanel.setVisible(false);
        try {
            fillDnsTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        //MainPanel
        Color buttonColor = new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(),15);
        mainPanel.setBackground(backColor);
        contentPanel.setBackground(backColor);
        cpuPanel.setBackground(backColor);
        statsPanel.setBackground(backColor);
        navBar.setBackground(backColor);
        topBar.setBackground(backColor);

        homeButton.setBackground(backColor);
        dnsButton.setBackground(backColor);
        routeButton.setBackground(backColor);
        addressButton.setBackground(backColor);
        dhcpButton.setBackground(backColor);
        interfaceButton.setBackground(backColor);
        divider.setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(),50));
        progressBarLoad.setForeground(Color.GREEN);
        progressBarMem.setForeground(Color.GREEN);



        hoslLabel.setForeground(textColor);
        userLabel.setForeground(textColor);
        hostip.setForeground(textColor);
        username.setForeground(textColor);
        homeButton.setForeground(textColor);
        dnsButton.setForeground(textColor);
        routeButton.setForeground(textColor);
        dhcpButton.setForeground(textColor);
        interfaceButton.setForeground(textColor);
        addressButton.setForeground(textColor);
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


        Image home = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/home.png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        homeButton.setIcon(new ImageIcon(home));

        Image dns = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dns.png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        dnsButton.setIcon(new ImageIcon(dns));

        Image dhcp = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/dhcp.png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        dhcpButton.setIcon(new ImageIcon(dhcp));

        Image route = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/routes.png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        routeButton.setIcon(new ImageIcon(route));

        Image inter = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/interface.png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        interfaceButton.setIcon(new ImageIcon(inter));

        Image addresses = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("./icons/address.png"))).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        addressButton.setIcon(new ImageIcon(addresses));



        //DNS Panel
        dnsPanel.setBackground(backColor);
        clearCacheButton.setBackground(buttonColor);
        clearCacheButton.setForeground(textColor);

        addRecordbutton.setBackground(buttonColor);
        addRecordbutton.setForeground(textColor);

        deleteRecord.setBackground(buttonColor);
        deleteRecord.setForeground(textColor);

        tabbedPane1.setBackground(buttonColor);
        tabbedPane1.setForeground(textColor);

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

        tabbedDhcp.setBackground(backColor);
        tabbedDhcp.setForeground(textColor);

        dhcpServerTable.setBackground(backColor);
        dhcpServerTable.setForeground(textColor);
        dhcpPoolTable.setBackground(backColor);
        dhcpPoolTable.setForeground(textColor);
        dhcpClientsTable.setBackground(backColor);
        dhcpClientsTable.setForeground(textColor);
        dhcpLeasesTable.setBackground(backColor);
        dhcpLeasesTable.setForeground(textColor);

        dhcpPanel.setVisible(false);

        tabbedPaneGeral.setVisible(false);

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

        for ( DhcpClient iface : apiClient.getDhcpClients()) {
            dataset.addValue(Double.parseDouble(iface.getDefaultRouteDistance()), "RX Drop", iface.getDefaultRouteDistance());
            dataset.addValue(Double.parseDouble(iface.getDefaultRouteDistance()), "TX Drop", iface.getDefaultRouteDistance());
        }
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
        statsPanel.setVisible(true);
    }

    private void createUIComponents(){
        Font font = new Font("JetBrains Mono", Font.PLAIN,14);
        //MainPanel
        navBar.setFont(font);
        topBar.setFont(font);
        contentPanel.setFont(font);
        hoslLabel.setFont(font);
        hostip.setFont(font);
        userLabel.setFont(font);
        username.setFont(font);
        routeButton.setFont(font);
        dhcpButton.setFont(font);
        interfaceButton.setFont(font);
        addressButton.setFont(font);
        homeButton.setFont(font);
        dnsButton.setFont(font);
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
        dnsButton.setFont(font);

        progressBarLoad.setFont(font);
        progressBarMem.setFont(font);
        progressBarHdd.setFont(font);

        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);
        dnsButton.setFocusPainted(false);
        dnsButton.setBorderPainted(false);
        routeButton.setFocusPainted(false);
        routeButton.setBorderPainted(false);
        dhcpButton.setFocusPainted(false);
        dhcpButton.setBorderPainted(false);
        interfaceButton.setFocusPainted(false);
        interfaceButton.setBorderPainted(false);
        addressButton.setFocusPainted(false);
        addressButton.setBorderPainted(false);

        //InterfacesPanel
        interfaceTable.setFont(font);


        //DNS Panel
        clearCacheButton.setFont(font);
        clearCacheButton.setFocusPainted(false);
        clearCacheButton.setBorderPainted(false);
        addRecordbutton.setFont(font);
        addRecordbutton.setFocusPainted(false);
        addRecordbutton.setBorderPainted(false);
        deleteRecord.setFont(font);
        deleteRecord.setFocusPainted(false);
        deleteRecord.setBorderPainted(false);
        deleteRecord.setEnabled(false);


        cacheTable.setFont(font);
        recordsTable.setFont(font);
        tabbedPane1.setFont(font);

        //DHCP Panel

        deleteButton.setFont(font);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setEnabled(false);
        dhcpLeasesTable.setFont(font);
        dhcpClientsTable.setFont(font);
        dhcpPoolTable.setFont(font);
        dhcpServerTable.setFont(font);


    }

}
