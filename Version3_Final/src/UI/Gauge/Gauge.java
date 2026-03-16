package UI.Gauge;

import javax.swing.*;
import java.awt.*;

public abstract class Gauge extends JPanel
{
    protected double value;
    protected double maxValue;
    protected Image background;


    public Gauge(double maxValue)
    {
        this.maxValue = maxValue;
        this.value = 0;
        setOpaque(false);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        display(g2);
    }

    public void update(double newValue)
    {
        this.value = Math.max(0, Math.min(newValue, maxValue));
        repaint();
    }

    public abstract void display(Graphics2D g2);
}
