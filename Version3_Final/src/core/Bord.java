package core;

import UI.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class Bord extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private Dashboard dashboardPanel;


    public Bord() {
        setTitle("Dashboard");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        menuPanel = new MenuPanel(this);
        dashboardPanel = new Dashboard(this);

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(dashboardPanel, "dashboard");

        add(mainPanel);
        setVisible(true);
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "menu");
    }

    public void showDashboard(VehicleType type) {
        dashboardPanel.setVehicleType(type);
        cardLayout.show(mainPanel, "dashboard");
        dashboardPanel.requestFocusInWindow();
    }



    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(Bord::new);
        }
    }

