package UI;

import core.Engine;
import core.Sound;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Dashboard extends JFrame implements KeyListener {
    private Engine engine;
    private boolean throttle = false;
    private Tachometer tachometer;
    private Speedometer speedometer;
    private long lastTime = System.nanoTime();
    private Sound sound;

    public Dashboard()
    {
        engine = new Engine(850, 200, 110, 5500);
        tachometer = new Tachometer(5500);
        speedometer = new Speedometer(250);
        sound = new Sound();
        sound.start();


        setSize(1000, 464);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon dashPic = new ImageIcon("src/dash.jpg");
        add(new JLabel(dashPic));
        pack();
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);





        Timer timer = new Timer(16, e -> update());
        timer.start();
    }

    private void update()
    {
        long now = System.nanoTime();
        double deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        if (throttle)
            engine.revUp();
        else
            engine.revDown();

        engine.update(deltaTime);

        double freq = 50 + (double)engine.getRpm() / 10;
        sound.setFrequency(freq);
        tachometer.update(engine.getRpm());
        speedometer.update(engine.getSpeed());
        tachometer.display();
        speedometer.display();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W)
            throttle = true;
        else
            if (key == KeyEvent.VK_SHIFT)
                engine.upShift();
            else
                if (key == KeyEvent.VK_CONTROL)
                    engine.downShift();
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
