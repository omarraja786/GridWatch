

import javax.swing.*;
import java.awt.*;

class Square extends JPanel {
    private Color colour;

    Square(Color colour) {
        this.colour = colour;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(colour);
        g2.fillRect(0, 0, 16, 16);
    }
}
