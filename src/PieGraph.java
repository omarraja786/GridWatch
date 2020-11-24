

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;


public class PieGraph extends JPanel {


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.red);
        g.fillRect(20, 20, 400, 400);
        System.out.println("hello");
        Graphics2D g2 = (Graphics2D) g;
        Arc2D.Float drawpie = new Arc2D.Float(Arc2D.PIE);
        drawpie.setFrame(800, 300, 500, 500);


        drawpie.setAngleStart(-20);
        drawpie.setAngleExtent(80);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(224, 161, 26));
        g2.fill(drawpie);

        drawpie.setAngleStart(40);
        drawpie.setAngleExtent(50);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(186, 180, 167));
        g2.fill(drawpie);

        drawpie.setAngleStart(90);
        drawpie.setAngleExtent(9);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(126, 132, 41));
        g2.fill(drawpie);


        drawpie.setAngleStart(95);
        drawpie.setAngleExtent(10);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(194, 206, 24));
        g2.fill(drawpie);


        drawpie.setAngleStart(100);
        drawpie.setAngleExtent(10);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(204, 20, 69));
        g2.fill(drawpie);

        drawpie.setAngleStart(110);
        drawpie.setAngleExtent(9);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(21, 112, 204));
        g2.fill(drawpie);

        drawpie.setAngleStart(119);
        drawpie.setAngleExtent(30);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(21, 59, 96));
        g2.fill(drawpie);

        drawpie.setAngleStart(125);
        drawpie.setAngleExtent(40);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(153, 46, 19));
        g2.fill(drawpie);

        drawpie.setAngleStart(150);
        drawpie.setAngleExtent(205);
        g2.setColor(Color.gray);
        g2.draw(drawpie);
        g2.setColor(new Color(150, 94, 61));
        g2.fill(drawpie);
    }
}
