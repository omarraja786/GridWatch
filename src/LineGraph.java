/*
  Draws a single line graph.
  Input:
  ArrayList of ArrayList of Doubles - Doubles are values for the y axis, each arraylist of doubles represent a single line on the graph.
  ArrayList of Strings - These will be the labels for the x-axis.
  String - Title for the graph to show which data group it represents.
 */

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// create new JPanel object and initialise variables that I would need to use in paintComponent
class LineGraph extends JPanel {
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private ArrayList<ArrayList<Double>> variablesAll;
    private ArrayList<String> labels;
    private String dataGroup;
    private ArrayList<Double> variables = new ArrayList<>();
    private boolean singleLine = true;

    // creates the LineGraph object
    LineGraph(ArrayList<ArrayList<Double>> variablesAll, ArrayList<String> labels, String dataGroup) {
        this.variablesAll = variablesAll;
        this.labels = labels;
        this.dataGroup = dataGroup;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (variablesAll.size() == 1) {
            variables = variablesAll.get(0);
        } else {
            singleLine = false;
            int variableCount = 0;
            for (ArrayList<Double> arrayList : variablesAll) {
                for (double value : arrayList) {
                    variables.add(variableCount, value);
                    variableCount++;
                }
            }
        }

        // xScale and yScale is used for plotting the points and labels in relation to the width and height of the component
        int sizeSingleArray = variablesAll.get(0).size();
        double xScale = ((double) getWidth() - 105) / (sizeSingleArray - 1);
        double yScale;
        if (Collections.max(variables).equals(Collections.min(variables))) {
            yScale = ((double) getHeight()); // this is in case all values are the same, it keeps the points on the graph.
        } else {
            yScale = ((double) getHeight() - 75) / (Collections.max(variables) - Collections.min(variables));
        }

        // creating a list of coordinates for the points of the graph.
        List<Point> graphPoints = new ArrayList<>();
        ArrayList<List<Point>> graphPointsList = new ArrayList<>();
        if (singleLine) {
            for (int i = 0; i < sizeSingleArray; i++) {
                int x = (int) (i * xScale + 65);
                int y = (int) ((Collections.max(variables) - variables.get(i)) * yScale + 25);
                graphPoints.add(new Point(x, y));
            }
        } else { //creates a list of arraylists of graph points for multiple lines on a single graph
            int graphPointsCount = 0;
            for (ArrayList<Double> array : variablesAll) {
                for (int i = 0; i < sizeSingleArray; i++) {
                    int x = (int) (i * xScale + 65);
                    int y = (int) ((Collections.max(variables) - array.get(i)) * yScale + 25);
                    graphPoints.add(new Point(x, y));
                }
                List<Point> newGraphPoints = new ArrayList<>();
                int newCount = 0;
                for (Point myPoint : graphPoints) {
                    newGraphPoints.add(newCount, myPoint);
                    newCount++;
                }
                graphPointsList.add(graphPointsCount, newGraphPoints);
                graphPointsCount++;
                graphPoints.clear();
            }
        }

        //this draws white background for the graph
        g2.setColor(Color.WHITE);
        g2.fillRect(65, 25, getWidth() - 105, getHeight() - 75);
        g2.setColor(Color.LIGHT_GRAY); // colour is gray for the graph's grid

        // draws the horizontal gray lines for the graph's grid and creates the label for each gray line on the y axis
        int j;
        if (singleLine) {
            j = 6;
        } else {
            j = 17;
        }
        for (int i = 0; i < j; i++) {
            int x0 = 65, x1 = getWidth() - 40;
            int y0 = getHeight() - ((i * (getHeight() - 75)) / (j - 1) + 50);
            String yLabel;
            g2.drawLine(x0, y0, x1, y0);
            double yLabelDouble = ((Collections.min(variables) + (Collections.max(variables) - Collections.min(variables)) * i / (j - 1))); // calculates the values for the labels on the y axis
            DecimalFormat df2 = new DecimalFormat("#.##");
            int yLabelInt = Math.round(Math.round(Math.ceil(yLabelDouble)));
            if (Collections.max(variables) >= 1000) {
                yLabel = yLabelInt + ""; // if the value for the y axis is large, set the string for the y label to the rounded value
            } else {
                yLabel = df2.format(yLabelDouble); // else, use the formatted value
            }
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            g2.setColor(Color.BLACK);
            if (!singleLine) {
                yLabel = yLabel + " W";
                g2.drawString(yLabel, x0 - labelWidth - 18, y0 + (metrics.getHeight() / 2) - 3);
            } else {
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.setColor(Color.LIGHT_GRAY);
        }

        // draws the vertical gray lines for the graph's grid and the labels from the previously acquired labels list for each gray line on the x axis
        for (int i = 0; i < sizeSingleArray; i++) {
            if (sizeSingleArray > 1) {
                int x0 = i * (getWidth() - 105) / (sizeSingleArray - 1) + 65;
                int y0 = getHeight() - 50, y1 = 25;
                g2.drawLine(x0, y0, x0, y1);
                String xLabel = labels.get(i);
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.setColor(Color.BLACK);
                g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                g2.setColor(Color.LIGHT_GRAY);
            }
        }

        // draws the title of the graph
        FontMetrics metrics = g2.getFontMetrics();
        g2.setColor(Color.BLACK);
        g2.drawString(dataGroup, getWidth() / 3, getHeight() - 30 + metrics.getHeight());

        // draw x & y axes
        g2.drawLine(65, getHeight() - 50, 65, 25);
        g2.drawLine(65, getHeight() - 50, getWidth() - 40, getHeight() - 50);

        // list of colours used for the graph legend and multiple lines on a single graph
        List<Color> lineColours = new ArrayList<>();
        lineColours.add(0, Color.BLACK);
        lineColours.add(1, new Color(0, 0, 255));
        lineColours.add(2, new Color(0, 255, 0));
        lineColours.add(3, new Color(255, 0, 0));
        lineColours.add(4, new Color(0, 255, 255));
        lineColours.add(5, new Color(255, 0, 255));
        lineColours.add(6, new Color(255, 130, 0));
        lineColours.add(7, new Color(130, 0, 255));
        lineColours.add(8, new Color(0, 160, 0));
        lineColours.add(9, new Color(180, 100, 0));
        lineColours.add(10, new Color(0, 0, 160));
        lineColours.add(11, new Color(130, 0, 130));
        lineColours.add(12, new Color(0, 130, 130));
        lineColours.add(13, new Color(80, 80, 80));
        lineColours.add(14, new Color(200, 160, 0));
        lineColours.add(15, new Color(255, 120, 200));

        // draws a line between each point in the list of graph points
        g2.setStroke(GRAPH_STROKE);
        if (singleLine) {
            for (int i = 0; i < graphPoints.size() - 1; i++) {
                int x1 = graphPoints.get(i).x;
                int y1 = graphPoints.get(i).y;
                int x2 = graphPoints.get(i + 1).x;
                int y2 = graphPoints.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
            }
        } else {
            int colourCount = 0;
            for (List<Point> pointsList : graphPointsList) {
                g2.setColor(lineColours.get(colourCount));
                for (int i = 0; i < pointsList.size() - 1; i++) {
                    int x1 = pointsList.get(i).x;
                    int y1 = pointsList.get(i).y;
                    int x2 = pointsList.get(i + 1).x;
                    int y2 = pointsList.get(i + 1).y;
                    g2.drawLine(x1, y1, x2, y2);
                }
                colourCount++;
            }
        }
    }
}
