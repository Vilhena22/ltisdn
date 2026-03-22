package Forms;

import ApiClient.ApiClient;
import Models.Dhcp.Clients.DhcpClient;
import Models.Dhcp.Leases.DhcpLease;
import Models.System.SystemResources;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class HomePage {
    private JPanel mainPanel;
    private JPanel navBar;
    private JButton routeButton;
    private JLabel hostip;
    private JLabel username;
    private JPanel topBar;
    private JButton dhcpInterface;
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
    private ApiClient apiClient;


    public HomePage(Boolean isThemeDark) throws Exception{
        Font font = new Font("JetBrains Mono", Font.PLAIN,14);
        if (isThemeDark) {
            setColors(Color.DARK_GRAY, Color.WHITE);
        }else {
            setColors(Color.WHITE, Color.DARK_GRAY);
        }


        navBar.setFont(font);
        topBar.setFont(font);
        contentPanel.setFont(font);
        hoslLabel.setFont(font);
        hostip.setFont(font);
        userLabel.setFont(font);
        username.setFont(font);
        routeButton.setFont(font);
        dhcpInterface.setFont(font);
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

        progressBarLoad.setFont(font);
        progressBarMem.setFont(font);
        progressBarHdd.setFont(font);




        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);
        dnsButton.setFocusPainted(false);
        dnsButton.setBorderPainted(false);
        routeButton.setFocusPainted(false);
        routeButton.setBorderPainted(false);
        dhcpInterface.setFocusPainted(false);
        dhcpInterface.setBorderPainted(false);
        interfaceButton.setFocusPainted(false);
        interfaceButton.setBorderPainted(false);
        addressButton.setFocusPainted(false);
        addressButton.setBorderPainted(false);
        progressBarLoad.setForeground(Color.GREEN);
        progressBarMem.setForeground(Color.GREEN);

        createUIComponents();
        setDashboardValues();
        if (isThemeDark) {
            buildChart(Color.DARK_GRAY, Color.WHITE);
        }else {
            buildChart(Color.WHITE, Color.DARK_GRAY);
        }

    }

    private void setColors(Color backColor, Color textColor) {
        //Set Background Colors
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
        dhcpInterface.setBackground(backColor);
        interfaceButton.setBackground(backColor);
        divider.setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(),50));




        hoslLabel.setForeground(textColor);
        userLabel.setForeground(textColor);
        hostip.setForeground(textColor);
        username.setForeground(textColor);
        homeButton.setForeground(textColor);
        dnsButton.setForeground(textColor);
        routeButton.setForeground(textColor);
        dhcpInterface.setForeground(textColor);
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

    }


}
