
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;



public class PieChart extends JPanel{


    public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.red);
            g.fillRect(20,20,400,400);
            System.out.println("hello");
            Graphics2D g2 = (Graphics2D) g;
            Arc2D.Float drawpie = new Arc2D.Float(Arc2D.PIE);
            drawpie.setFrame(800, 300, 500, 500);



            drawpie.setAngleStart(0);
            drawpie.setAngleExtent(80);
            g2.setColor(Color.gray);
            g2.draw(drawpie);
            g2.setColor(Color.red);
            g2.fill(drawpie);

            drawpie.setAngleStart(20);
            drawpie.setAngleExtent(80);
            g2.setColor(Color.gray);
            g2.draw(drawpie);
            g2.setColor(Color.pink);
            g2.fill(drawpie);

            drawpie.setAngleStart(30);
            drawpie.setAngleExtent(80);
            g2.setColor(Color.gray);
            g2.draw(drawpie);
            g2.setColor(Color.black);
            g2.fill(drawpie);


            drawpie.setAngleStart(50);
            drawpie.setAngleExtent(80);
            g2.setColor(Color.gray);
            g2.draw(drawpie);
            g2.setColor(Color.blue);
            g2.fill(drawpie);


            drawpie.setAngleStart(70);
            drawpie.setAngleExtent(100);
            g2.setColor(Color.gray);
            g2.draw(drawpie);
            g2.setColor(Color.green);
            g2.fill(drawpie);

            drawpie.setAngleStart(60);
            drawpie.setAngleExtent(50);
            g2.setColor(Color.gray);
            g2.draw(drawpie);
            g2.setColor(Color.yellow);
            g2.fill(drawpie);

            drawpie.setAngleStart(80);
            drawpie.setAngleExtent(70);
            g2.setColor(Color.gray);
            g2.draw(drawpie);
            g2.setColor(Color.cyan);
            g2.fill(drawpie);
        }
}
