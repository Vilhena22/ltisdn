package Forms;

import ApiClient.ApiClient;
import Models.Dhcp.Clients.DhcpClient;
import Models.Dns.DnsCache;
import Models.Dns.DnsRecord;
import Models.System.SystemResources;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePage {
    private JFrame owner;
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
    private JScrollPane cacheScroll;
    private JScrollPane recordScroll;
    private JButton deleteRecord;
    private JPanel dhcpPanel;
    private ApiClient apiClient;


    public HomePage(Boolean isThemeDark, JFrame owner) throws Exception{

        this.owner = owner;
        createUIComponents();

        if (isThemeDark) {
            setColors(new Color(60, 63, 65), Color.WHITE);
        }else {
            setColors(Color.WHITE, new Color(60, 63, 65));
        }

        setDashboardValues();
        if (isThemeDark) {
            buildChart(new Color(60, 63, 65), Color.WHITE);
        }else {
            buildChart(Color.WHITE, new Color(60, 63, 65));
        }

        homeButton.addActionListener(this::btnHomeButton);
        dnsButton.addActionListener(this::btnDnsButton);
        dhcpButton.addActionListener(this::btnDhcpButton);
        clearCacheButton.addActionListener(this::btnClearCahce);
        addRecordbutton.addActionListener(this::btnAddRecord);
        deleteRecord.addActionListener(this::btnDeleteRecord);

    }

    private void btnDhcpButton(ActionEvent actionEvent)  {
        dnsPanel.setVisible(false);
        cpuPanel.setVisible(false);
        statsPanel.setVisible(false);
        System.out.println(contentPanel.getComponents().toString());
        try {
            DhcpPanel dhcpPanel = new DhcpPanel(owner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //contentPanel.add(dhcpPanel);

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
        fillTable();
    }

    private void btnAddRecord(ActionEvent actionEvent) {
        AddDnsRecord addDnsRecord = new AddDnsRecord(owner);
        addDnsRecord.pack();
        addDnsRecord.setLocationRelativeTo(owner);
        addDnsRecord.setVisible(true);
        fillTable();
    }

    private void fillTable() {
        String[] columNames = {"ID","Data","Name","Static","TTL","Type"};

        DefaultTableModel model = new DefaultTableModel(columNames, 0);
        List<DnsCache> cacheList = null;
        List<DnsRecord> recordList = null;
        try {
            cacheList = apiClient.getCacheDns();
            recordList = apiClient.getDnsRecord();
            for (DnsCache dnsCache : cacheList) {
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
            cacheTable.setModel(model);
            columNames = new String[]{"ID", "Data", "Name", "Dynamic","Disabled", "TTL", "Type"};
            model = new DefaultTableModel(columNames, 0);
            for (DnsRecord dnsRecord : recordList) {
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
            recordsTable.setModel(model);
            recordsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            recordsTable.getSelectionModel().addListSelectionListener(e -> {
                if (e.getValueIsAdjusting()) {
                    boolean visible = recordsTable.getSelectedRowCount() > 0;
                    deleteRecord.setEnabled(visible);
                }
            });
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            for (int i = 0; i < cacheTable.getColumnCount(); i++) {
                cacheTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            for (int i = 0; i < recordsTable.getColumnCount(); i++) {
                recordsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

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

        apiClient = new ApiClient();
        try {
            fillTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnHomeButton(ActionEvent actionEvent) {
        try {
            statsPanel.setVisible(true);
            cpuPanel.setVisible(true);
            dnsPanel.setVisible(false);
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
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void setDashboardValues() throws Exception{
        apiClient = new ApiClient();
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
        progressBarLoad.setString(Integer.toString(load) +"%");
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
        apiClient = new ApiClient();
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




    }


}
