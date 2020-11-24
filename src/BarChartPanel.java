import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BarChartPanel extends JPanel {
    private ArrayList<Double> values;
    private ArrayList<String> names;
    private String title;
    private int padding = 17;
    private int labelPadding = 17;
    private int numberYDivisions = 10;
    private int pointWidth = 4;
    private Color gridColor = new Color(200, 200, 200, 200);

    public BarChartPanel(ArrayList<Double> v, ArrayList<String> n, String t) {
        names = n;
        values = v;
        title = t;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values == null || values.size() == 0)
            return;
        double minValue = 0;
        double maxValue = 0;

        for (int i = 0; i < values.size(); i++) {
            if (minValue > values.get(i))
                minValue = values.get(i);
            if (maxValue < values.get(i))
                maxValue = values.get(i);
        }

        Dimension d = getSize();
        int clientWidth = d.width - 5;
        int clientHeight = d.height;
        int barWidth = clientWidth / values.size();

        Font titleFont = new Font("SansSerif", Font.BOLD, 15);
        FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
        FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

        int titleWidth = titleFontMetrics.stringWidth(title);
        int y = titleFontMetrics.getAscent() - 5;
        int x = (clientWidth - titleWidth) / 2;
        g.setFont(titleFont);
        g.drawString(title, x, y);

        int top = titleFontMetrics.getHeight();
        int bottom = labelFontMetrics.getHeight();
        if (maxValue == minValue)
            return;
        double scale = (clientHeight - top - bottom) / (maxValue - minValue);
        y = clientHeight - labelFontMetrics.getDescent();
        g.setFont(labelFont);

        //* 100)) / 100.0

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 2; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight()
                    - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (values.size() > 0) {
                g.setColor(gridColor);
                g.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g.setColor(Color.BLACK);
                String yLabel = ((int) ((minValue
                        + (maxValue - minValue) * ((i * 1.0) / numberYDivisions)))) + "";
                FontMetrics metrics = g.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < values.size(); i++) {
            if (values.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (values.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((values.size() / 20.0)) + 1)) == 0) {
                    g.setColor(gridColor);
                    g.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g.setColor(Color.BLACK);

                }
                g.drawLine(x0, y0, x1, y1);
            }
        }


        // draw white background
        g.setColor(Color.WHITE);
        g.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
                getHeight() - 2 * padding - labelPadding);
        g.setColor(Color.BLACK);

        g.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding,
                getHeight() - padding - labelPadding);


        //g.drawLine(0, 0, 1000, 1000);
        //g.drawLine(getHeight(), getWidth(), getHeight(), getWidth());

        for (int i = 0; i < values.size(); i++) {
            int valueX = i * barWidth + 40;
            int valueY = top;
            int height = (int) (values.get(i) * scale);
            if (values.get(i) >= 0)
                valueY += (int) ((maxValue - values.get(i)) * scale);
            else {
                valueY += (int) (maxValue * scale);
                height = -height;
            }
            /*
            g.setColor(Color.red);
            g.fillRect(valueX, valueY, barWidth - 80, height);
            g.setColor(Color.BLACK);
            g.drawRect(valueX, valueY, barWidth - 80, height);
            */

            g.setColor(Color.blue);
            g.fillRect(valueX, valueY - 20, barWidth - 80, height);
            g.setColor(Color.BLACK);
            g.drawRect(valueX, valueY - 20, barWidth - 80, height);
            int labelWidth = labelFontMetrics.stringWidth(names.get(i));
            x = i * barWidth + (barWidth - labelWidth) / 2;
            g.drawString(names.get(i), x, y);

        }
    }
}
