
import Features.Printer;
import Features.SendPDF;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFrame {

    public static void main(String args[]) {
        FilledFrame frame = new FilledFrame();
        frame.setVisible(true);
        frame.setTitle("Gridwatch");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.dataLoaded = false;
    }
}

class FilledFrame extends JFrame {
    public static JTextField enterFileName;
    JTabbedPane tabbedPane;
    JPanel tab1;
    JPanel tab2;
    JPanel tab3;
    JPanel tab4;
    JPanel tab5;
    JPanel butPanel;
    JPanel loadPanel;
    JButton butInfo;
    JButton butHealth;
    JButton butExport;
    JButton butPrint;
    JButton butLoad;
    JLabel labeltext;
    JLabel labeltext2;
    JTextArea labelinfo;
    boolean dataLoaded;

    JPanel general = new JPanel(new GridBagLayout());
    JPanel seasonalLineGraphs = new JPanel(new GridBagLayout());
    JPanel superLineGraph = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    JPanel barGraphs = new JPanel(new GridBagLayout());
    GridBagConstraints bar = new GridBagConstraints();
    JPanel pieChart = new JPanel(new GridBagLayout());
    GridBagConstraints pie = new GridBagConstraints();

    public FilledFrame() {
        tabbedPane = new JTabbedPane();

        JPanel textPanel = new JPanel();
        JPanel textPanel2 = new JPanel();
        labeltext2 = new JLabel("Enter the filename of the CSV file below and press load to view the data in graphical form.");
        labeltext2.setFont(labeltext2.getFont().deriveFont(30.0f));
        labeltext = new JLabel("Gridwatch");
        labeltext.setFont(labeltext.getFont().deriveFont(70.0f));
        textPanel2.add(labeltext2);
        textPanel.add(labeltext);


        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.SOUTH;
        c.gridx = 0;
        c.gridwidth = 4;
        c.gridy = 4;
        general.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (dataLoaded) {
                    labeltext.setText("Hover over a Graph to See the Details");
                    labeltext2.setText("DETAILS");

                }

                labelinfo.setText("");
            }
        });

        general.add(textPanel, c);

        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridwidth = 4;
        c.gridy = 5;
        c.anchor = GridBagConstraints.SOUTH;
        general.add(textPanel2, c);

        JPanel infoPanel = new JPanel();

        labelinfo = new JTextArea(3, 110);
        labelinfo.setOpaque(false);
        labelinfo.setLineWrap(true);
        labelinfo.setWrapStyleWord(true);
        labelinfo.setEditable(false);
        labelinfo.setFont(labelinfo.getFont().deriveFont(20.0f));
        infoPanel.add(labelinfo);

        c.weighty = 0.01;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.SOUTH;
        c.gridx = 0;
        c.gridwidth = 4;
        c.gridy = 6;

        general.add(infoPanel, c);

        tab1 = general;


        tabbedPane.addTab("General", null, tab1, "Recent Data");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        tab2 = barGraphs;
        tabbedPane.addTab("Bar Graph 2017", null, tab2, "Bar Chart Data");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        tab3 = seasonalLineGraphs;
        tabbedPane.addTab("Seasonal Line Graphs", null, tab3, "Seasonal Data");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        tab4 = superLineGraph;
        tabbedPane.addTab("Super Line Graph", null, tab4, "Enlarged Super Data");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        tab5 = pieChart;
        tabbedPane.addTab("Pie Graph", null, tab5, "Pie Chart Data");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);



        butInfo = new JButton("Info");
        butExport = new JButton("Export to PDF");
        butPrint = new JButton("Print");
        butLoad = new JButton("Load CSV File");
        butHealth = new JButton("Health and Safety Tips");
        butLoad.setBounds(getHeight(), getWidth(), 100, 50);

        enterFileName = new JTextField("Enter filename (Without extension)", 20);
        enterFileName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    butLoadPressed();
                }
            }
        });

        butPanel = new JPanel(); //Creates a new jpanel
        loadPanel = new JPanel();

        butPanel.add(butInfo);
        butPanel.add(butExport);
        butPanel.add(butPrint);
        butPanel.add(butHealth);
        loadPanel.add(butLoad);
        loadPanel.add(enterFileName);

        add(butPanel, BorderLayout.NORTH);
        add(loadPanel, BorderLayout.SOUTH);
        add(tabbedPane);

        ButtonHandler loadBH = new ButtonHandler(this, 1);
        ButtonHandler infoBH = new ButtonHandler(this, 2);
        ButtonHandler exportBH = new ButtonHandler(this, 3);
        ButtonHandler printBH = new ButtonHandler(this, 4);
        ButtonHandler healthBH = new ButtonHandler(this, 5);


        butLoad.addActionListener(loadBH);
        butInfo.addActionListener(infoBH);
        butExport.addActionListener(exportBH);
        butPrint.addActionListener(printBH);
        butHealth.addActionListener(healthBH);
    }


    protected Component makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new BorderLayout());
        panel.add(filler);
        return panel;
    }

    private void setLoaded() {
        dataLoaded = true;
    }

    private void setNotLoaded() {
        dataLoaded = false;
    }

    private void exportForPDF() {

        BufferedImage image = new BufferedImage(general.getWidth(), general.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        general.printAll(g);
        g.dispose();
        try {
            ImageIO.write(image, "jpg", new File("src/recent.jpg"));

        } catch (IOException exp) {
            exp.printStackTrace();
        }

        BufferedImage image_2 = new BufferedImage(seasonalLineGraphs.getWidth(), seasonalLineGraphs.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image_2.createGraphics();
        seasonalLineGraphs.printAll(g2);
        g.dispose();
        try {
            ImageIO.write(image_2, "jpg", new File("src/average.jpg"));

        } catch (IOException exp) {
            exp.printStackTrace();
        }
        BufferedImage image_3 = new BufferedImage(superLineGraph.getWidth(), superLineGraph.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g3 = image_3.createGraphics();
        superLineGraph.printAll(g3);
        g.dispose();
        try {
            ImageIO.write(image_3, "jpg", new File("src/super.jpg"));

        } catch (IOException exp) {
            exp.printStackTrace();
        }
        BufferedImage image_4 = new BufferedImage(tab2.getWidth(), tab2.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g4 = image_4.createGraphics();
        tab2.printAll(g4);
        g.dispose();
        try {
            ImageIO.write(image_4, "jpg", new File("src/barchart.jpg"));

        } catch (IOException exp) {
            exp.printStackTrace();
        }
        BufferedImage image_5 = new BufferedImage(pieChart.getWidth(), pieChart.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g5 = image_5.createGraphics();
        pieChart.printAll(g5);
        g.dispose();
        try {
            ImageIO.write(image_5, "jpg", new File("src/piechart.jpg"));

        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    private void butLoadPressed() {
        new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, "CSV File Successfully Loaded!", "Loading CSV File...",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
        GraphData myData = new GraphData();
        setLoaded();
        enterFileName.setText("");

        Map<String, Double> annualAveragesList = new HashMap<>();

        // making small graphs for each variable to compare between seasons
        ArrayList<String> seasonalLabelsFull = new ArrayList<>();
        int labelCount = 0;
        for (String year : myData.yearList) {
            for (String season : myData.seasonList) {
                String myYear = year.substring(2, 4);
                String mySeason = season.substring(0, 3);
                seasonalLabelsFull.add(labelCount, mySeason + myYear);
                labelCount++;
            }
        }

        ArrayList<String> seasonalLabels = new ArrayList<>();
        labelCount = 0;
        for (int i = seasonalLabelsFull.size() - 8; i < seasonalLabelsFull.size(); i++) {
            seasonalLabels.add(labelCount, seasonalLabelsFull.get(i));
            labelCount++;
        }

        ArrayList<Double> seasonalDemandListFull = new ArrayList<>();
        System.out.println("Processing seasonal demand list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Demand";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.demandList, year, season);
                seasonalDemandListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalDemandList = new ArrayList<>();
        int variableCount = 0;
        for (int i = seasonalDemandListFull.size() - 8; i < seasonalDemandListFull.size(); i++) {
            seasonalDemandList.add(variableCount, seasonalDemandListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalDemandSuperList = new ArrayList<>();
        seasonalDemandSuperList.add(0, seasonalDemandList);

        ArrayList<Double> seasonalFrequencyListFull = new ArrayList<>();
        System.out.println("Processing seasonal frequency list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Frequency";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.frequencyList, year, season);
                seasonalFrequencyListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalFrequencyList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalFrequencyListFull.size() - 8; i < seasonalFrequencyListFull.size(); i++) {
            seasonalFrequencyList.add(variableCount, seasonalFrequencyListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalFrequencySuperList = new ArrayList<>();
        seasonalFrequencySuperList.add(0, seasonalFrequencyList);

        ArrayList<Double> seasonalCoalListFull = new ArrayList<>();
        System.out.println("Processing seasonal coal list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Coal";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.coalList, year, season);
                seasonalCoalListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalCoalList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalCoalListFull.size() - 8; i < seasonalCoalListFull.size(); i++) {
            seasonalCoalList.add(variableCount, seasonalCoalListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalCoalSuperList = new ArrayList<>();
        seasonalCoalSuperList.add(0, seasonalCoalList);

        ArrayList<Double> seasonalNuclearListFull = new ArrayList<>();
        System.out.println("Processing seasonal nuclear list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Nuclear";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.nuclearList, year, season);
                seasonalNuclearListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalNuclearList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalNuclearListFull.size() - 8; i < seasonalNuclearListFull.size(); i++) {
            seasonalNuclearList.add(variableCount, seasonalNuclearListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalNuclearSuperList = new ArrayList<>();
        seasonalNuclearSuperList.add(0, seasonalNuclearList);

        ArrayList<Double> seasonalCCGTListFull = new ArrayList<>();
        System.out.println("Processing seasonal ccgt list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "CCGT";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.ccgtList, year, season);
                seasonalCCGTListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalCCGTList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalCCGTListFull.size() - 8; i < seasonalCCGTListFull.size(); i++) {
            seasonalCCGTList.add(variableCount, seasonalCCGTListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalCCGTSuperList = new ArrayList<>();
        seasonalCCGTSuperList.add(0, seasonalCCGTList);

        ArrayList<Double> seasonalWindListFull = new ArrayList<>();
        System.out.println("Processing seasonal wind list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Wind";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.windList, year, season);
                seasonalWindListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalWindList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalWindListFull.size() - 8; i < seasonalWindListFull.size(); i++) {
            seasonalWindList.add(variableCount, seasonalWindListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalWindSuperList = new ArrayList<>();
        seasonalWindSuperList.add(0, seasonalWindList);

        ArrayList<Double> seasonalFrenchListFull = new ArrayList<>();
        System.out.println("Processing seasonal french list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "French";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.frenchList, year, season);
                seasonalFrenchListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalFrenchList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalFrenchListFull.size() - 8; i < seasonalFrenchListFull.size(); i++) {
            seasonalFrenchList.add(variableCount, seasonalFrenchListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalFrenchSuperList = new ArrayList<>();
        seasonalFrenchSuperList.add(0, seasonalFrenchList);

        ArrayList<Double> seasonalDutchListFull = new ArrayList<>();
        System.out.println("Processing seasonal dutch list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Dutch";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.dutchList, year, season);
                seasonalDutchListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalDutchList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalDutchListFull.size() - 8; i < seasonalDutchListFull.size(); i++) {
            seasonalDutchList.add(variableCount, seasonalDutchListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalDutchSuperList = new ArrayList<>();
        seasonalDutchSuperList.add(0, seasonalDutchList);

        ArrayList<Double> seasonalIrishListFull = new ArrayList<>();
        System.out.println("Processing seasonal irish list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Irish";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.irishList, year, season);
                seasonalIrishListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalIrishList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalIrishListFull.size() - 8; i < seasonalIrishListFull.size(); i++) {
            seasonalIrishList.add(variableCount, seasonalIrishListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalIrishSuperList = new ArrayList<>();
        seasonalIrishSuperList.add(0, seasonalIrishList);

        ArrayList<Double> seasonalEwListFull = new ArrayList<>();
        System.out.println("Processing seasonal ew list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Ew";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.ewList, year, season);
                seasonalEwListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalEwList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalEwListFull.size() - 8; i < seasonalEwListFull.size(); i++) {
            seasonalEwList.add(variableCount, seasonalEwListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalEwSuperList = new ArrayList<>();
        seasonalEwSuperList.add(0, seasonalEwList);

        ArrayList<Double> seasonalPumpedListFull = new ArrayList<>();
        System.out.println("Processing seasonal pumped list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Pumped";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.pumpedList, year, season);
                seasonalPumpedListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalPumpedList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalPumpedListFull.size() - 8; i < seasonalPumpedListFull.size(); i++) {
            seasonalPumpedList.add(variableCount, seasonalPumpedListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalPumpedSuperList = new ArrayList<>();
        seasonalPumpedSuperList.add(0, seasonalPumpedList);

        ArrayList<Double> seasonalHydroListFull = new ArrayList<>();
        System.out.println("Processing seasonal hydro list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Hydro";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.hydroList, year, season);
                seasonalHydroListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalHydroList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalHydroListFull.size() - 8; i < seasonalHydroListFull.size(); i++) {
            seasonalHydroList.add(variableCount, seasonalHydroListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalHydroSuperList = new ArrayList<>();
        seasonalHydroSuperList.add(0, seasonalHydroList);

        ArrayList<Double> seasonalOilListFull = new ArrayList<>();
        System.out.println("Processing seasonal oil list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Oil";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.oilList, year, season);
                seasonalOilListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalOilList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalOilListFull.size() - 8; i < seasonalOilListFull.size(); i++) {
            seasonalOilList.add(variableCount, seasonalOilListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalOilSuperList = new ArrayList<>();
        seasonalOilSuperList.add(0, seasonalOilList);

        ArrayList<Double> seasonalOCGTListFull = new ArrayList<>();
        System.out.println("Processing seasonal ocgt list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "OCGT";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.ocgtList, year, season);
                seasonalOCGTListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalOCGTList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalOCGTListFull.size() - 8; i < seasonalOCGTListFull.size(); i++) {
            seasonalOCGTList.add(variableCount, seasonalOCGTListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalOCGTSuperList = new ArrayList<>();
        seasonalOCGTSuperList.add(0, seasonalOCGTList);

        ArrayList<Double> seasonalOtherListFull = new ArrayList<>();
        System.out.println("Processing seasonal other list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Other";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.otherList, year, season);
                seasonalOtherListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalOtherList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalOtherListFull.size() - 8; i < seasonalOtherListFull.size(); i++) {
            seasonalOtherList.add(variableCount, seasonalOtherListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalOtherSuperList = new ArrayList<>();
        seasonalOtherSuperList.add(0, seasonalOtherList);

        ArrayList<Double> seasonalSolarListFull = new ArrayList<>();
        System.out.println("Processing seasonal solar list...");
        for (String year : myData.yearList) {
            String yearVariable = year + "Solar";
            for (String season : myData.seasonList) {
                GraphAverages average = new GraphAverages(myData, myData.solarList, year, season);
                seasonalSolarListFull.add(average.averageVariableSeason);
                if (!annualAveragesList.containsKey(yearVariable)) {
                    annualAveragesList.put(yearVariable, average.averageVariableYear);
                }
            }
        }
        ArrayList<Double> seasonalSolarList = new ArrayList<>();
        variableCount = 0;
        for (int i = seasonalSolarListFull.size() - 8; i < seasonalSolarListFull.size(); i++) {
            seasonalSolarList.add(variableCount, seasonalSolarListFull.get(i));
            variableCount++;
        }
        ArrayList<ArrayList<Double>> seasonalSolarSuperList = new ArrayList<>();
        seasonalSolarSuperList.add(0, seasonalSolarList);

        // with values now calculated, now creates a line graph for each variable comparing values at different seasons of each year. Also implements hover feature.
        LineGraph nuclearLineGraph = new LineGraph(seasonalNuclearSuperList, seasonalLabels, "Average Nuclear (W)");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 0;
        seasonalLineGraphs.add(nuclearLineGraph, c);

        nuclearLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("NUCLEAR");
                labelinfo.setText("Currently the UK has seven AGR designs and one relatively modern PWR. Nuclear power stations are run flat-out to maximise income. " +
                        "Since the cost of fuel is almost insignificant, it pays them to sell at any price they can get. Variations in output are generally signs that refuelling or maintenance is ongoing.");
            }
        });

        LineGraph coalLineGraph = new LineGraph(seasonalCoalSuperList, seasonalLabels, "Average Coal (W)");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        seasonalLineGraphs.add(coalLineGraph, c);

        coalLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("COAL");
                labelinfo.setText("Coal is no longer the largest contributor to the UK grid as gas prices are currently low, and legislation has forced closure of most plants. " +
                        "Drax also co-fires biomass with coal, which allows them to gain access to renewable subsidies. Coal plants are now restricted in running hours for emissions, " +
                        "so tend to run only in winter, when prices are higher.");
            }
        });

        LineGraph frequencyLineGraph = new LineGraph(seasonalFrequencySuperList, seasonalLabels, "Average Frequency (Hz)");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        seasonalLineGraphs.add(frequencyLineGraph, c);

        frequencyLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("FREQUENCY");
                labelinfo.setText("Grid frequency is controlled to be exactly 50Hz on  average/n" +
                        ", but varies slightly. A lower frequency corresponds to a higher potential demand than actual generating capacity: by allowing the frequency and voltage to go lower,/n " +
                        "the demand is reduced slightly to keep the balance, and vice versa.");
            }
        });

        LineGraph demandLineGraph = new LineGraph(seasonalDemandSuperList, seasonalLabels, "Average Demand (W)");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        seasonalLineGraphs.add(demandLineGraph, c);

        demandLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("DEMAND");
                labelinfo.setText("This is the total demand of the entire country (plus or minus exports) less any unmetered generating sources like wind but including an estimate for solar. " +
                        "The amber warning represents the demand level that cannot be reliably met by wood or fossil burning and nuclear generation, but must be augmented by imports, or unreliable intermittent" +
                        " 'renewable' energy.");
            }
        });

        LineGraph dutchLineGraph = new LineGraph(seasonalDutchSuperList, seasonalLabels, "Average Dutch Interconnector (W)");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 1;
        seasonalLineGraphs.add(dutchLineGraph, c);

        dutchLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("DUTCH INTERCONNECTOR (BRITNED)");
                labelinfo.setText("This is 1GW connector to Holland Its usage seems to reflect a surplus or a deficit of NW europe wind energy.");
            }
        });

        LineGraph frenchLineGraph = new LineGraph(seasonalFrenchSuperList, seasonalLabels, "Average French Interconnector (W)");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 1;
        seasonalLineGraphs.add(frenchLineGraph, c);

        frenchLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("FRENCH INTERCONNECTOR");
                labelinfo.setText("This is a 2GW bi-directional link to France which  is able to import up to 2GW of power from France - " +
                        "usually in summer when France has a nuclear power surplus - and export in winter, when the UK's excess of backup plant " +
                        "and coal  power can be profitably sold to meet continental shortfalls.");
            }
        });

        LineGraph windLineGraph = new LineGraph(seasonalWindSuperList, seasonalLabels, "Average Wind (W)");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        seasonalLineGraphs.add(windLineGraph, c);

        windLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("WIND");
                labelinfo.setText("This is the total contributed by metered wind farms. Wind power contributes about another 30% from embedded " +
                        "(or unmetered) wind turbines that shows only as a drop in demand. Wind like nuclear, will sell into any market price because turbines are expensive, " +
                        "wind is not and subsidies are always paid. The variability of wind leads to very high fluctuations in output.");
            }
        });

        LineGraph ccgtLineGraph = new LineGraph(seasonalCCGTSuperList, seasonalLabels, "Average Combined Cycle Gas Turbines (W)");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        seasonalLineGraphs.add(ccgtLineGraph, c);

        ccgtLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("COMBINED CYCLE GAS TURBINES");
                labelinfo.setText("Combined Cycle Gas Turbines are gas turbines whose hot exhausts are used to drive a boiler and steam turbine. " +
                        "This two stage process makes them very efficient in gas usage. They are also quite fast to get online - " +
                        "less than an hour in general, so they are used to cover (profitable) peak demand and to balance wind output.");
            }
        });

        LineGraph hydroLineGraph = new LineGraph(seasonalHydroSuperList, seasonalLabels, "Average Hydroelectric Power (W)");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 2;
        seasonalLineGraphs.add(hydroLineGraph, c);

        hydroLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("HYDROELECTRIC POWER");
                labelinfo.setText("The UK has no major hydroelectric power stations, but a collection of smaller ones, mainly in Scotland, " +
                        "that provide very useful power (if it's rained recently!). There would be a little more, but many stations deliberately " +
                        "reduce output to get the best renewable subsidy rates.");
            }
        });

        LineGraph pumpedLineGraph = new LineGraph(seasonalPumpedSuperList, seasonalLabels, "Average Pumped Storage (W)");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 2;
        seasonalLineGraphs.add(pumpedLineGraph, c);

        pumpedLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("PUMPED STORAGE");
                labelinfo.setText("These are small hydro-electric stations that can use overnight electricity to recharge their reservoirs. " +
                        "Mainly used to meet very short term peak demands (the water soon runs out). They represent the nearest thing to 'storage' " +
                        "that is attached to the grid.");
            }
        });

        LineGraph ewLineGraph = new LineGraph(seasonalEwSuperList, seasonalLabels, "Average East-West Interconnector (W)");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 2;
        seasonalLineGraphs.add(ewLineGraph, c);

        ewLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("EAST-WEST INTERCONNECTOR");
                labelinfo.setText("This is a new 500MW (0.5GW) bi-directional link between Wales and the Irish Republic, enabling access to the UK " +
                        "(and continental) grid, and prices, for the Irish consumers. On 8 September 2016, the interconnector developed a fault. " +
                        "The interconnector re-entered service on the 20 December 2016 with a fully rated 500 MW import, however exports to the UK " +
                        "are still limited to roughly 280MW");
            }
        });

        LineGraph irishLineGraph = new LineGraph(seasonalIrishSuperList, seasonalLabels, "Average Irish Interconnector (W)");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        seasonalLineGraphs.add(irishLineGraph, c);

        irishLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("IRISH INTERCONNECTOR (MOYLE)");
                labelinfo.setText("This is 1GW connector to Holland Its usage seems to reflect a surplus or a deficit of NW europe wind energy.");
            }
        });

        LineGraph solarLineGraph = new LineGraph(seasonalSolarSuperList, seasonalLabels, "Average Solar PV (W)");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 3;
        seasonalLineGraphs.add(solarLineGraph, c);

        solarLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("SOLAR PV");
                labelinfo.setText("As no solar PV to date is metered centrally, we cannot show accurate real time figures on solar PV power.");
            }
        });

        LineGraph otherLineGraph = new LineGraph(seasonalOtherSuperList, seasonalLabels, "Average Other (W)");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 3;
        seasonalLineGraphs.add(otherLineGraph, c);

        otherLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("OTHER");
                labelinfo.setText("Power generated by other means, usually via smaller or independent partners.");
            }
        });

        LineGraph ocgtLineGraph = new LineGraph(seasonalOCGTSuperList, seasonalLabels, "Average Open Cycle Gas Turbines (W)");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 3;
        seasonalLineGraphs.add(ocgtLineGraph, c);

        ocgtLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("OPEN CYCLE GAS TURBINES");
                labelinfo.setText("Open Cycle Gas Turbines, are gas turbines without steam plant to maximise their efficiency. " +
                        "They are cheap to build, but expensive to run, so are seldom used except in emergencies in winter, " +
                        "when very high market prices of electricity make them profitable.");
            }
        });

        LineGraph oilLineGraph = new LineGraph(seasonalOilSuperList, seasonalLabels, "Average Oil (W)");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        seasonalLineGraphs.add(oilLineGraph, c);

        oilLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("BIOMASS (OIL)");
                labelinfo.setText("These power stations are either (parts of) old coal plants that have been converted to run on " +
                        "imported timber - e.g. Drax 2 -  thus enabling them to qualify as 'renewable' and gain subsidies thereby, " +
                        "or purpose built biomass burners like Stevens Croft (40MW)  built to use sawmill waste.");
            }
        });


        // making the recent line graphs
        int size = myData.idList.size();

        ArrayList<String> recentTimestampsListRaw = new ArrayList<>();
        recentTimestampsListRaw.add(0, myData.timestampList.get(size - 4));
        recentTimestampsListRaw.add(1, myData.timestampList.get(size - 3));
        recentTimestampsListRaw.add(2, myData.timestampList.get(size - 2));
        recentTimestampsListRaw.add(3, myData.timestampList.get(size - 1));

        ArrayList<String> recentTimestampsList = new ArrayList<>();
        String day;
        String month;
        String year;
        String hour;
        String minute;
        int timeCount = 0;
        for (String timestamp : recentTimestampsListRaw) {
            day = Character.toString(timestamp.charAt(8)) + Character.toString(timestamp.charAt(9));
            month = Character.toString(timestamp.charAt(5)) + Character.toString(timestamp.charAt(6));
            year = Character.toString(timestamp.charAt(2)) + Character.toString(timestamp.charAt(3));
            hour = Character.toString(timestamp.charAt(10)) + Character.toString(timestamp.charAt(11));
            minute = Character.toString(timestamp.charAt(13)) + Character.toString(timestamp.charAt(14));
            recentTimestampsList.add(timeCount, day + "/" + month + "/" + year + " " + hour + ":" + minute);
            timeCount++;
        }

        ArrayList<Double> recentDemandList = new ArrayList<>();
        recentDemandList.add(0, myData.demandList.get(size - 4));
        recentDemandList.add(1, myData.demandList.get(size - 3));
        recentDemandList.add(2, myData.demandList.get(size - 2));
        recentDemandList.add(3, myData.demandList.get(size - 1));
        ArrayList<ArrayList<Double>> recentDemandSuperList = new ArrayList<>();
        recentDemandSuperList.add(0, recentDemandList);

        ArrayList<Double> recentFrequencyList = new ArrayList<>();
        recentFrequencyList.add(0, myData.frequencyList.get(size - 4));
        recentFrequencyList.add(1, myData.frequencyList.get(size - 3));
        recentFrequencyList.add(2, myData.frequencyList.get(size - 2));
        recentFrequencyList.add(3, myData.frequencyList.get(size - 1));
        ArrayList<ArrayList<Double>> recentFrequencySuperList = new ArrayList<>();
        recentFrequencySuperList.add(0, recentFrequencyList);

        ArrayList<Double> recentCoalList = new ArrayList<>();
        recentCoalList.add(0, myData.coalList.get(size - 4));
        recentCoalList.add(1, myData.coalList.get(size - 3));
        recentCoalList.add(2, myData.coalList.get(size - 2));
        recentCoalList.add(3, myData.coalList.get(size - 1));
        ArrayList<ArrayList<Double>> recentCoalSuperList = new ArrayList<>();
        recentCoalSuperList.add(0, recentCoalList);

        ArrayList<Double> recentNuclearList = new ArrayList<>();
        recentNuclearList.add(0, myData.nuclearList.get(size - 4));
        recentNuclearList.add(1, myData.nuclearList.get(size - 3));
        recentNuclearList.add(2, myData.nuclearList.get(size - 2));
        recentNuclearList.add(3, myData.nuclearList.get(size - 1));
        ArrayList<ArrayList<Double>> recentNuclearSuperList = new ArrayList<>();
        recentNuclearSuperList.add(0, recentNuclearList);

        ArrayList<Double> recentCCGTList = new ArrayList<>();
        recentCCGTList.add(0, myData.ccgtList.get(size - 4));
        recentCCGTList.add(1, myData.ccgtList.get(size - 3));
        recentCCGTList.add(2, myData.ccgtList.get(size - 2));
        recentCCGTList.add(3, myData.ccgtList.get(size - 1));
        ArrayList<ArrayList<Double>> recentCCGTSuperList = new ArrayList<>();
        recentCCGTSuperList.add(0, recentCCGTList);

        ArrayList<Double> recentWindList = new ArrayList<>();
        recentWindList.add(0, myData.windList.get(size - 4));
        recentWindList.add(1, myData.windList.get(size - 3));
        recentWindList.add(2, myData.windList.get(size - 2));
        recentWindList.add(3, myData.windList.get(size - 1));
        ArrayList<ArrayList<Double>> recentWindSuperList = new ArrayList<>();
        recentWindSuperList.add(0, recentWindList);

        ArrayList<Double> recentFrenchList = new ArrayList<>();
        recentFrenchList.add(0, myData.frenchList.get(size - 4));
        recentFrenchList.add(1, myData.frenchList.get(size - 3));
        recentFrenchList.add(2, myData.frenchList.get(size - 2));
        recentFrenchList.add(3, myData.frenchList.get(size - 1));
        ArrayList<ArrayList<Double>> recentFrenchSuperList = new ArrayList<>();
        recentFrenchSuperList.add(0, recentFrenchList);

        ArrayList<Double> recentDutchList = new ArrayList<>();
        recentDutchList.add(0, myData.dutchList.get(size - 4));
        recentDutchList.add(1, myData.dutchList.get(size - 3));
        recentDutchList.add(2, myData.dutchList.get(size - 2));
        recentDutchList.add(3, myData.dutchList.get(size - 1));
        ArrayList<ArrayList<Double>> recentDutchSuperList = new ArrayList<>();
        recentDutchSuperList.add(0, recentDutchList);

        ArrayList<Double> recentIrishList = new ArrayList<>();
        recentIrishList.add(0, myData.irishList.get(size - 4));
        recentIrishList.add(1, myData.irishList.get(size - 3));
        recentIrishList.add(2, myData.irishList.get(size - 2));
        recentIrishList.add(3, myData.irishList.get(size - 1));
        ArrayList<ArrayList<Double>> recentIrishSuperList = new ArrayList<>();
        recentIrishSuperList.add(0, recentIrishList);

        ArrayList<Double> recentEwList = new ArrayList<>();
        recentEwList.add(0, myData.ewList.get(size - 4));
        recentEwList.add(1, myData.ewList.get(size - 3));
        recentEwList.add(2, myData.ewList.get(size - 2));
        recentEwList.add(3, myData.ewList.get(size - 1));
        ArrayList<ArrayList<Double>> recentEwSuperList = new ArrayList<>();
        recentEwSuperList.add(0, recentEwList);

        ArrayList<Double> recentPumpedList = new ArrayList<>();
        recentPumpedList.add(0, myData.pumpedList.get(size - 4));
        recentPumpedList.add(1, myData.pumpedList.get(size - 3));
        recentPumpedList.add(2, myData.pumpedList.get(size - 2));
        recentPumpedList.add(3, myData.pumpedList.get(size - 1));
        ArrayList<ArrayList<Double>> recentPumpedSuperList = new ArrayList<>();
        recentPumpedSuperList.add(0, recentPumpedList);

        ArrayList<Double> recentHydroList = new ArrayList<>();
        recentHydroList.add(0, myData.hydroList.get(size - 4));
        recentHydroList.add(1, myData.hydroList.get(size - 3));
        recentHydroList.add(2, myData.hydroList.get(size - 2));
        recentHydroList.add(3, myData.hydroList.get(size - 1));
        ArrayList<ArrayList<Double>> recentHydroSuperList = new ArrayList<>();
        recentHydroSuperList.add(0, recentHydroList);

        ArrayList<Double> recentOilList = new ArrayList<>();
        recentOilList.add(0, myData.oilList.get(size - 4));
        recentOilList.add(1, myData.oilList.get(size - 3));
        recentOilList.add(2, myData.oilList.get(size - 2));
        recentOilList.add(3, myData.oilList.get(size - 1));
        ArrayList<ArrayList<Double>> recentOilSuperList = new ArrayList<>();
        recentOilSuperList.add(0, recentOilList);

        ArrayList<Double> recentOCGTList = new ArrayList<>();
        recentOCGTList.add(0, myData.ocgtList.get(size - 4));
        recentOCGTList.add(1, myData.ocgtList.get(size - 3));
        recentOCGTList.add(2, myData.ocgtList.get(size - 2));
        recentOCGTList.add(3, myData.ocgtList.get(size - 1));
        ArrayList<ArrayList<Double>> recentOCGTSuperList = new ArrayList<>();
        recentOCGTSuperList.add(0, recentOCGTList);

        ArrayList<Double> recentOtherList = new ArrayList<>();
        recentOtherList.add(0, myData.otherList.get(size - 4));
        recentOtherList.add(1, myData.otherList.get(size - 3));
        recentOtherList.add(2, myData.otherList.get(size - 2));
        recentOtherList.add(3, myData.otherList.get(size - 1));
        ArrayList<ArrayList<Double>> recentOtherSuperList = new ArrayList<>();
        recentOtherSuperList.add(0, recentOtherList);

        ArrayList<Double> recentSolarList = new ArrayList<>();
        recentSolarList.add(0, myData.solarList.get(size - 4));
        recentSolarList.add(1, myData.solarList.get(size - 3));
        recentSolarList.add(2, myData.solarList.get(size - 2));
        recentSolarList.add(3, myData.solarList.get(size - 1));
        ArrayList<ArrayList<Double>> recentSolarSuperList = new ArrayList<>();
        recentSolarSuperList.add(0, recentSolarList);

        LineGraph recentNuclearLineGraph = new LineGraph(recentNuclearSuperList, recentTimestampsList, "Recent Nuclear (W)");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 0;
        general.add(recentNuclearLineGraph, c);

        String dateAndTime = myData.timestampList.get(myData.timestampList.size() - 1);
        String dateDay = dateAndTime.substring(8, 10);
        String dateMonth = dateAndTime.substring(5, 7);
        String dateYear = dateAndTime.substring(0, 4);
        String time = dateAndTime.substring(10, 18);
        dateAndTime = dateDay + "/" + dateMonth + "/" + dateYear + " " + time;
        String finalDateAndTime = dateAndTime;

        recentNuclearLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("NUCLEAR");
                labeltext2.setText("Most Recent Value: " + myData.nuclearList.get(myData.nuclearList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("Currently the UK has seven AGR designs and one relatively modern PWR. Nuclear power stations are run flat-out to maximise income. " +
                        "Since the cost of fuel is almost insignificant, it pays them to sell at any price they can get. Variations in output are generally signs that refuelling or maintenance is ongoing.");
            }
        });

        LineGraph recentCoalLineGraph = new LineGraph(recentCoalSuperList, recentTimestampsList, "Recent Coal (W)");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        general.add(recentCoalLineGraph, c);

        recentCoalLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("COAL");
                labeltext2.setText("Most Recent Value: " + myData.coalList.get(myData.coalList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("Coal is no longer the largest contributor to the UK grid as gas prices are currently low, and legislation has forced closure of most plants. " +
                        "Drax also co-fires biomass with coal, which allows them to gain access to renewable subsidies. Coal plants are now restricted in running hours for emissions, " +
                        "so tend to run only in winter, when prices are higher.");
            }
        });

        LineGraph recentFrequencyLineGraph = new LineGraph(recentFrequencySuperList, recentTimestampsList, "Recent Frequency (Hz)");
        c.gridwidth = 1;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        general.add(recentFrequencyLineGraph, c);

        recentFrequencyLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("FREQUENCY");
                labeltext2.setText("Most Recent Value: " + myData.frequencyList.get(myData.frequencyList.size() - 1) + " (GHZ)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("Grid frequency is controlled to be exactly 50Hz on  average/n" +
                        ", but varies slightly. A lower frequency corresponds to a higher potential demand than actual generating capacity: by allowing the frequency and voltage to go lower,/n " +
                        "the demand is reduced slightly to keep the balance, and vice versa.");
            }
        });

        LineGraph recentDemandLineGraph = new LineGraph(recentDemandSuperList, recentTimestampsList, "Recent Demand (W)");
        c.gridwidth = 1;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        general.add(recentDemandLineGraph, c);

        recentDemandLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("DEMAND");
                labeltext2.setText("Most Recent Value: " + myData.demandList.get(myData.demandList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("This is the total demand of the entire country (plus or minus exports) less any unmetered generating sources like wind but including an estimate for solar. " +
                        "The amber warning represents the demand level that cannot be reliably met by wood or fossil burning and nuclear generation, but must be augmented by imports, or unreliable intermittent" +
                        " 'renewable' energy.");
            }
        });

        LineGraph recentDutchLineGraph = new LineGraph(recentDutchSuperList, recentTimestampsList, "Recent Dutch Interconnector (W)");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 1;
        general.add(recentDutchLineGraph, c);

        recentDutchLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("DUTCH INTERCONNECTOR (BRITNED)");
                labeltext2.setText("Most Recent Value: " + myData.dutchList.get(myData.dutchList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("This is 1GW connector to Holland Its usage seems to reflect a surplus or a deficit of NW europe wind energy.");
            }
        });

        LineGraph recentFrenchLineGraph = new LineGraph(recentFrenchSuperList, recentTimestampsList, "Recent French Interconnector (W)");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 1;
        general.add(recentFrenchLineGraph, c);

        recentFrenchLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("FRENCH INTERCONNECTOR");
                labeltext2.setText("Most Recent Value: " + myData.frenchList.get(myData.frenchList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("This is a 2GW bi-directional link to France which  is able to import up to 2GW of power from France - " +
                        "usually in summer when France has a nuclear power surplus - and export in winter, when the UK's excess of backup plant " +
                        "and coal  power can be profitably sold to meet continental shortfalls.");
            }
        });

        LineGraph recentWindLineGraph = new LineGraph(recentWindSuperList, recentTimestampsList, "Recent Wind (W)");
        c.gridwidth = 1;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        general.add(recentWindLineGraph, c);

        recentWindLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("WIND");
                labeltext2.setText("Most Recent Value: " + myData.windList.get(myData.windList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("This is the total contributed by metered wind farms. Wind power contributes about another 30% from embedded " +
                        "(or unmetered) wind turbines that shows only as a drop in demand. Wind like nuclear, will sell into any market price because turbines are expensive, " +
                        "wind is not and subsidies are always paid. The variability of wind leads to very high fluctuations in output.");
            }
        });

        LineGraph recentCCGTLineGraph = new LineGraph(recentCCGTSuperList, recentTimestampsList, "Recent Combined Cycle Gas Turbines (W)");
        c.gridwidth = 1;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        general.add(recentCCGTLineGraph, c);

        recentCCGTLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("COMBINED CYCLE GAS TURBINES");
                labeltext2.setText("Most Recent Value: " + myData.ccgtList.get(myData.ccgtList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("Combined Cycle Gas Turbines are gas turbines whose hot exhausts are used to drive a boiler and steam turbine. " +
                        "This two stage process makes them very efficient in gas usage. They are also quite fast to get online - " +
                        "less than an hour in general, so they are used to cover (profitable) peak demand and to balance wind output.");
            }
        });

        LineGraph recentHydroLineGraph = new LineGraph(recentHydroSuperList, recentTimestampsList, "Recent Hydroelectric Power (W)");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 2;
        general.add(recentHydroLineGraph, c);

        recentHydroLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("HYDROELECTRIC POWER");
                labeltext2.setText("Most Recent Value: " + myData.hydroList.get(myData.hydroList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("The UK has no major hydroelectric power stations, but a collection of smaller ones, mainly in Scotland, " +
                        "that provide very useful power (if it's rained recently!). There would be a little more, but many stations deliberately " +
                        "reduce output to get the best renewable subsidy rates.");
            }
        });

        LineGraph recentPumpedLineGraph = new LineGraph(recentPumpedSuperList, recentTimestampsList, "Recent Pumped Storage (W)");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 2;
        general.add(recentPumpedLineGraph, c);

        recentPumpedLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("PUMPED STORAGE");
                labeltext2.setText("Most Recent Value: " + myData.pumpedList.get(myData.pumpedList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("These are small hydro-electric stations that can use overnight electricity to recharge their reservoirs. " +
                        "Mainly used to meet very short term peak demands (the water soon runs out). They represent the nearest thing to 'storage' " +
                        "that is attached to the grid.");
            }
        });

        LineGraph recentEwLineGraph = new LineGraph(recentEwSuperList, recentTimestampsList, "Recent East-West Interconnector (W)");
        c.gridwidth = 1;

        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 2;
        general.add(recentEwLineGraph, c);

        recentEwLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("EAST-WEST INTERCONNECTOR");
                labeltext2.setText("Most Recent Value: " + myData.ewList.get(myData.ewList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("This is a new 500MW (0.5GW) bi-directional link between Wales and the Irish Republic, enabling access to the UK " +
                        "(and continental) grid, and prices, for the Irish consumers. On 8 September 2016, the interconnector developed a fault. " +
                        "The interconnector re-entered service on the 20 December 2016 with a fully rated 500 MW import, however exports to the UK " +
                        "are still limited to roughly 280MW");
            }
        });

        LineGraph recentIrishLineGraph = new LineGraph(recentIrishSuperList, recentTimestampsList, "Recent Irish Interconnector (W)");
        c.gridwidth = 1;

        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        general.add(recentIrishLineGraph, c);

        recentIrishLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("IRISH INTERCONNECTOR (MOYLE)");
                labeltext2.setText("Most Recent Value: " + myData.irishList.get(myData.irishList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("This is 1GW connector to Holland Its usage seems to reflect a surplus or a deficit of NW europe wind energy.");
            }
        });

        LineGraph recentSolarLineGraph = new LineGraph(recentSolarSuperList, recentTimestampsList, "Recent Solar PV (W)");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 3;
        general.add(recentSolarLineGraph, c);

        recentSolarLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("SOLAR PV");
                labeltext2.setText("Most Recent Value: " + myData.solarList.get(myData.solarList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("As no solar PV to date is metered centrally, we cannot show accurate real time figures on solar PV power.");
            }
        });

        LineGraph recentOtherLineGraph = new LineGraph(recentOtherSuperList, recentTimestampsList, "Recent Other (W)");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 3;
        general.add(recentOtherLineGraph, c);

        recentOtherLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("OTHER");
                labeltext2.setText("Most Recent Value: " + myData.otherList.get(myData.otherList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("Power generated by other means, usually via smaller or independent partners.");
            }
        });

        LineGraph recentOCGTLineGraph = new LineGraph(recentOCGTSuperList, recentTimestampsList, "Recent Open Cycle Gas Turbines (W)");
        c.gridwidth = 1;

        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 3;
        general.add(recentOCGTLineGraph, c);

        recentOCGTLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("OPEN CYCLE GAS TURBINES");
                labeltext2.setText("Most Recent Value: " + myData.ocgtList.get(myData.ocgtList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("Open Cycle Gas Turbines, are gas turbines without steam plant to maximise their efficiency. " +
                        "They are cheap to build, but expensive to run, so are seldom used except in emergencies in winter, " +
                        "when very high market prices of electricity make them profitable.");
            }
        });

        LineGraph recentOilLineGraph = new LineGraph(recentOilSuperList, recentTimestampsList, "Recent Oil (W)");
        c.gridwidth = 1;
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        general.add(recentOilLineGraph, c);

        recentOilLineGraph.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                labeltext.setText("BIOMASS (OIL)");
                labeltext2.setText("Most Recent Value: " + myData.oilList.get(myData.oilList.size() - 1) + " (GW)           " + "Date & Time: " + finalDateAndTime);
                labelinfo.setText("These power stations are either (parts of) old coal plants that have been converted to run on " +
                        "imported timber - e.g. Drax 2 -  thus enabling them to qualify as 'renewable' and gain subsidies thereby, " +
                        "or purpose built biomass burners like Stevens Croft (40MW)  built to use sawmill waste.");
            }
        });

        // making the super line graph
        ArrayList<ArrayList<Double>> superArrayList = new ArrayList<>();
        superArrayList.add(0, seasonalCCGTList);
        superArrayList.add(1, seasonalCoalList);
        superArrayList.add(2, seasonalDutchList);
        superArrayList.add(3, seasonalEwList);
        superArrayList.add(4, seasonalFrenchList);
        superArrayList.add(5, seasonalHydroList);
        superArrayList.add(6, seasonalIrishList);
        superArrayList.add(7, seasonalNuclearList);
        superArrayList.add(8, seasonalOCGTList);
        superArrayList.add(9, seasonalOilList);
        superArrayList.add(10, seasonalOtherList);
        superArrayList.add(11, seasonalPumpedList);
        superArrayList.add(12, seasonalSolarList);
        superArrayList.add(13, seasonalWindList);

        ArrayList<String> superLabels = new ArrayList<>();
        superLabels.add(0, "Winter 2016");
        superLabels.add(1, "Spring 2016");
        superLabels.add(2, "Summer 2016");
        superLabels.add(3, "Autumn 2016");
        superLabels.add(4, "Winter 2017");
        superLabels.add(5, "Spring 2017");
        superLabels.add(6, "Summer 2017");
        superLabels.add(7, "Autumn 2017");

        LineGraph mySuperLineGraph = new LineGraph(superArrayList, superLabels, "");

        // creating the Graph Legend (colour keys)
        c.gridwidth = 14;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        superLineGraph.add(mySuperLineGraph, c);

        JLabel graphLegend = new JLabel("Graph Legend:");
        c.gridwidth = 14;
        c.weightx = 0;
        c.weighty = 0.02;
        c.gridx = 1;
        c.gridy = 1;
        superLineGraph.add(graphLegend, c);

        Square ccgtSquare = new Square(Color.BLACK);
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 0;
        c.gridy = 2;
        superLineGraph.add(ccgtSquare, c);

        JLabel ccgt = new JLabel(" = Combined Cycle Gas Turbines");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 1;
        c.gridy = 2;
        superLineGraph.add(ccgt, c);

        Square coalSquare = new Square(new Color(0, 0, 255));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 2;
        c.gridy = 2;
        superLineGraph.add(coalSquare, c);

        JLabel coal = new JLabel(" = Coal");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 3;
        c.gridy = 2;
        superLineGraph.add(coal, c);

        Square dutchSquare = new Square(new Color(0, 255, 0));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 4;
        c.gridy = 2;
        superLineGraph.add(dutchSquare, c);

        JLabel dutch = new JLabel(" = Dutch Interconnector");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 5;
        c.gridy = 2;
        superLineGraph.add(dutch, c);

        Square ewSquare = new Square(new Color(255, 0, 0));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 6;
        c.gridy = 2;
        superLineGraph.add(ewSquare, c);

        JLabel ew = new JLabel(" = East-West Interconnector");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 7;
        c.gridy = 2;
        superLineGraph.add(ew, c);

        Square frenchSquare = new Square(new Color(0, 255, 255));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 8;
        c.gridy = 2;
        superLineGraph.add(frenchSquare, c);

        JLabel french = new JLabel(" = French Interconnector");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 9;
        c.gridy = 2;
        superLineGraph.add(french, c);

        Square hydroSquare = new Square(new Color(255, 0, 255));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 10;
        c.gridy = 2;
        superLineGraph.add(hydroSquare, c);

        JLabel hydro = new JLabel(" = Hydroelectric Power");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 11;
        c.gridy = 2;
        superLineGraph.add(hydro, c);

        Square irishSquare = new Square(new Color(255, 130, 0));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 12;
        c.gridy = 2;
        superLineGraph.add(irishSquare, c);

        JLabel irish = new JLabel(" = Irish Interconnector");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 13;
        c.gridy = 2;
        superLineGraph.add(irish, c);

        Square nuclearSquare = new Square(new Color(130, 0, 255));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 0;
        c.gridy = 3;
        superLineGraph.add(nuclearSquare, c);

        JLabel nuclear = new JLabel(" = Nuclear");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 1;
        c.gridy = 3;
        superLineGraph.add(nuclear, c);

        Square ocgtSquare = new Square(new Color(0, 160, 0));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 2;
        c.gridy = 3;
        superLineGraph.add(ocgtSquare, c);

        JLabel ocgt = new JLabel(" = Open Cycle Gas Turbines");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 3;
        c.gridy = 3;
        superLineGraph.add(ocgt, c);

        Square oilSquare = new Square(new Color(180, 100, 0));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 4;
        c.gridy = 3;
        superLineGraph.add(oilSquare, c);

        JLabel oil = new JLabel(" = Biomass (Oil)");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 5;
        c.gridy = 3;
        superLineGraph.add(oil, c);

        Square otherSquare = new Square(new Color(0, 0, 160));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 6;
        c.gridy = 3;
        superLineGraph.add(otherSquare, c);

        JLabel other = new JLabel(" = Other");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 7;
        c.gridy = 3;
        superLineGraph.add(other, c);

        Square pumpedSquare = new Square(new Color(130, 0, 130));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 8;
        c.gridy = 3;
        superLineGraph.add(pumpedSquare, c);

        JLabel pumped = new JLabel(" = Pumped Storage");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 9;
        c.gridy = 3;
        superLineGraph.add(pumped, c);

        Square solarSquare = new Square(new Color(0, 130, 130));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 10;
        c.gridy = 3;
        superLineGraph.add(solarSquare, c);

        JLabel solar = new JLabel(" = Solar");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 11;
        c.gridy = 3;
        superLineGraph.add(solar, c);

        Square windSquare = new Square(new Color(80, 80, 80));
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0.01;
        c.gridx = 12;
        c.gridy = 3;
        superLineGraph.add(windSquare, c);

        JLabel wind = new JLabel(" = Wind");
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.weighty = 0.01;
        c.gridx = 13;
        c.gridy = 3;
        superLineGraph.add(wind, c);

        JPanel barPanel = new JPanel(new GridLayout());
        barPanel.setSize(1000, 1000);

        ArrayList<String> names = new ArrayList<>();
        names.add("CCGT (W)");
        names.add("Coal");
        names.add("Dutch Int (W)");
        names.add("E-W Int (W)");
        names.add("French Int (W)");
        names.add("Hydro (W)");
        names.add("Irish Int (W)");
        names.add("Nuclear");
        names.add("OCGT");
        names.add("Oil");
        names.add("Gas");
        names.add("Piped Storage (W)");
        names.add("Solar PV");
        names.add("Wind");

        ArrayList<Double> values = new ArrayList<>();
        values.add(0, annualAveragesList.get("2017CCGT"));
        values.add(1, annualAveragesList.get("2017Coal"));
        values.add(2, annualAveragesList.get("2017Dutch"));
        values.add(3, annualAveragesList.get("2017Ew"));
        values.add(4, annualAveragesList.get("2017French"));
        values.add(5, annualAveragesList.get("2017Hydro"));
        values.add(6, annualAveragesList.get("2017Irish"));
        values.add(7, annualAveragesList.get("2017Nuclear"));
        values.add(8, annualAveragesList.get("2017OCGT"));
        values.add(9, annualAveragesList.get("2017Oil"));
        values.add(10, annualAveragesList.get("2017Other"));
        values.add(11, annualAveragesList.get("2017Pumped"));
        values.add(12, annualAveragesList.get("2017Solar"));
        values.add(13, annualAveragesList.get("2017Wind"));

        BarChartPanel barChart = new BarChartPanel(values, names, "2017 average energy values (W)");

        bar.gridwidth = 1;
        bar.gridx = 0;
        bar.gridy = 0;
        bar.weighty = 0.5;
        bar.weightx = 0.5;
        bar.fill = GridBagConstraints.BOTH;
        barGraphs.add(barChart, bar);

        PieGraph panel = new PieGraph();
        tab5.add(panel);
        pie.gridwidth = 1;
        pie.gridx = 0;
        pie.gridy = 0;
        pie.weighty = 0.5;
        pie.weightx = 0.5;
        pie.fill = GridBagConstraints.BOTH;
        pieChart.add(panel, pie);


        repaint();
    }


    class ButtonHandler implements ActionListener {
        FilledFrame theApp;
        int action;

        public ButtonHandler(FilledFrame theApp, int action) {
            this.theApp = theApp;
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == theApp.butLoad) {
                butLoadPressed();

            } else if (e.getSource() == theApp.butInfo) {
                String s = "What is this program? \n This program will display in a graphical form, different sources of power usage of the UK over a selected time period." +
                        "\n\n How do I use the program? \n To use the program start by clicking the load csv button and enter the name of ur file (Without the file type extension only accept csv formats)." +
                        "\n From there you will be shown different graphs and tabs to view the graphs in more detail." +
                        "\n\n What does the export button do? \n The export button allows you to save a visual representation of the graph as a PDF for reference in the future." +
                        "\n\n What does the Print button do? \n The print button will allow you to directly print the graph on the screen if you have a printer installed to your pc.";

                JOptionPane.showMessageDialog(null, s, "Information/Help", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == theApp.butExport) {
                try {
                    exportForPDF();
                    SendPDF.createPdf(SendPDF.DEST);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (e.getSource() == theApp.butHealth) {
                String s = "Health and Safety Regulations Act 1992 \n The health and safety act states that the program has had risk assessment linked to any danger to the user." +
                        "\n Please use the program safely and take frequent breaks i.e. every 30 minutes to reduce eye strain. If you feel any eye stress that is not recovering" +
                        "\n then please book an eye test to ensure no further damage is done.";


                JOptionPane.showMessageDialog(null, s, "Health and Safety Tips", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == theApp.butPrint) {
                PrinterJob printJob = PrinterJob.getPrinterJob();
                PageFormat preFormat = printJob.defaultPage();
                preFormat.setOrientation(PageFormat.LANDSCAPE);
                PageFormat postFormat = printJob.pageDialog(preFormat);

                if (preFormat != postFormat) {
                    printJob.setPrintable(new Printer(theApp), postFormat);
                    if (printJob.printDialog()) {
                        try {
                            printJob.print();
                        } catch (PrinterException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}

