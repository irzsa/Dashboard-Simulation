package UI;

import UI.Gauge.Speedometer;
import UI.Gauge.Tachometer;
import UI.Gauge.TempGauge;
import UI.Gauge.FuelGauge;
import UI.Gauge.Gauge;
import core.Bord;
import core.Engine;
import core.Sound;
import core.VehicleType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import core.VehicleDataReader;

public class Dashboard extends JPanel implements KeyListener {
    private Bord parent;
    private VehicleType currentType;
    private Engine engine;
    private boolean throttle = false;
    private boolean upShift = false;
    private boolean downShift = false;
    private Tachometer tachometer;
    private Speedometer speedometer;
    private TempGauge tempGauge;
    private FuelGauge fuelGauge;
    private long lastTime = System.nanoTime();
    private Sound sound;
    private int redline;
    private Timer timer;
    private JLayeredPane layeredPane;
    private JLabel gearLabel;

    public Dashboard(Bord parent)
    {
        this.parent = parent;
        setLayout(null);

        JButton backButton = new JButton("back");
        backButton.setBounds(20, 20, 80, 30);
        backButton.addActionListener((ActionEvent e) -> {
            stopSimulation();
            parent.showMenu();
        });
        add(backButton);
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

    }

    public void setVehicleType(VehicleType type) {
        this.currentType = type;
        int[] specs = VehicleDataReader.loadSpecs("src/vehicle_data.txt", type);
        stopSimulation();

        int idle = specs[0];
        int torque = specs[1];
        int hp = specs[2];
        redline = specs[3];
        int tachX = specs[4];
        int tachY = specs[5];
        int speedX = specs[6];
        int speedY = specs[7];
        int tempX = specs[8];
        int tempY = specs[9];
        int fuelX = specs[10];
        int fuelY = specs[11];


        engine = new Engine(idle, torque, hp, redline);

        sound = new Sound();
        sound.start();

        removeAll();

        setLayout(new BorderLayout());
        setSize(1000, 464);


        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1300, 600));

        JLabel dashPic = null;
        switch(type) {
            case CAR -> dashPic = new JLabel(new ImageIcon("src/dash.png"));
            case MOTORBIKE -> dashPic = new JLabel(new ImageIcon("src/dash.png"));
            case TRUCK -> dashPic = new JLabel(new ImageIcon("src/dashtruck.png"));


        }
        dashPic.setBounds(0, 0, 1000, 464);
        layeredPane.add(dashPic, Integer.valueOf(0));




        gearLabel = new JLabel("Gear: " + engine.getGear());
        gearLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gearLabel.setForeground(Color.WHITE);
        gearLabel.setBounds(460, 350, 150, 40);
        layeredPane.add(gearLabel, Integer.valueOf(2));


        fuelGauge = new FuelGauge(100);
        fuelGauge.setBounds(fuelX, fuelY, 150, 150);
        fuelGauge.setOpaque(false);
        layeredPane.add(fuelGauge, Integer.valueOf(1));

        tachometer = new Tachometer(redline);
        tachometer.setBounds(tachX, tachY, 200, 200);
        tachometer.setOpaque(false);
        layeredPane.add(tachometer, Integer.valueOf(1));

        tempGauge = new TempGauge(100);
        tempGauge.setBounds(tempX, tempY, 150, 150);
        tempGauge.setOpaque(false);
        layeredPane.add(tempGauge, Integer.valueOf(1));

        speedometer = new Speedometer(250);
        speedometer.setBounds(speedX, speedY, 200, 200);
        speedometer.setOpaque(false);
        layeredPane.add(speedometer, Integer.valueOf(1));

        JButton backButton = new JButton ("Back");
        backButton.setBounds(20, 20, 80, 30);
        backButton.addActionListener( e -> {
            stopSimulation();
            parent.showMenu();
        });



        layeredPane.add(backButton, Integer.valueOf(2));

        add(layeredPane, BorderLayout.CENTER);
        revalidate();
        repaint();

        timer = new Timer(16, e -> update());
        timer.start();
    }

    private void update()
    {
        long now = System.nanoTime();
        double deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        if (downShift && engine.getRpm() > redline - 1200)
        {
            JOptionPane.showMessageDialog(null, "Error: too many rpms for a down shift", "Bye bye piston nr 2", JOptionPane.ERROR_MESSAGE);
            throttle = false;
            downShift = false;
        }

        if (upShift && engine.getRpm() < redline - 1200)
        {
            JOptionPane.showMessageDialog(null, "Error: Not enough rpms for an up shift", "Bye bye gearbox", JOptionPane.ERROR_MESSAGE);
            throttle = false;
            upShift = false;
        }

        if (upShift) {
            engine.upShift();
            engine.revDown();
            throttle = false;
            upShift = false;
        }

        if (downShift) {
            engine.downShift();
            engine.revUp();
            throttle = false;
            downShift = false;
        }

        gearLabel.setText("Gear: " + engine.getGear());

        if (throttle)
            engine.revUp();
        else
            engine.revDown();

        engine.update(deltaTime);

        double freq = ((double)engine.getRpm() / 70) + engine.getGear() * 17;
        sound.setFrequency(freq);
        tachometer.update(engine.getRpm());
        speedometer.update(engine.getSpeed());
        tempGauge.updateValue(engine.getRpm(), throttle);
        fuelGauge.updateValue(engine.getRpm(), deltaTime);
        if (fuelGauge.getValue() <= 30) {
            stopSimulation();
            JOptionPane.showMessageDialog(null, "Error: Empty fuel tank!", "Pana prostului", JOptionPane.ERROR_MESSAGE);
        }

    }
    private void stopSimulation()
    {
        if (timer != null)
            timer.stop();
        if (sound != null)
            sound.stop();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W)
            throttle = true;
        else
            if (key == KeyEvent.VK_SHIFT)
                upShift = true;
            else
                if (key == KeyEvent.VK_CONTROL)
                    downShift = true;
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

        if(e.getKeyCode() == KeyEvent.VK_W)
            throttle = false;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {}





}
