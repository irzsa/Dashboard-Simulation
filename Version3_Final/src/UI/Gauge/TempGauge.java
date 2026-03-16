package UI.Gauge;

import java.awt.*;

public class TempGauge extends Gauge {
    public TempGauge(double maxValue) {
        super(maxValue);
        value = 30;
    }

    public void updateValue(double rpm, boolean throttle) {
        double increaseRate = (rpm / 100000.0) + (throttle ? 0.05 : 0.01);

        value += increaseRate;
        if (value > maxValue * 0.5)
            value = maxValue * 0.5;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        display(g2);
    }

    @Override
    public void display(Graphics2D g2) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int needleLength = Math.min(getWidth(), getHeight() / 2 - 10);

        double minAngle = Math.toRadians(210);
        double maxAngle = Math.toRadians(-25);
        double ratio = Math.min(value / maxValue, 1.0);
        double angle = minAngle + ratio * (maxAngle - minAngle);

        int x2 = (int) (centerX + needleLength * Math.cos(angle));
        int y2 = (int) (centerY - needleLength * Math.sin(angle));

        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.WHITE);
        g2.drawLine(centerX, centerY, x2, y2);
        g2.fillOval(centerX - 5, centerY - 5, 10, 10);
    }

}