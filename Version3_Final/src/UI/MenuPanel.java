package UI;

import core.Bord;
import core.VehicleType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuPanel extends JPanel {
    private Bord parent;

    public MenuPanel (Bord parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton carButton = new JButton("Car");
        JButton bikeButton = new JButton("Motorbike");
        JButton truckButton = new JButton("Truck");

        carButton.addActionListener(e -> parent.showDashboard(VehicleType.CAR));
        bikeButton.addActionListener(e -> parent.showDashboard(VehicleType.MOTORBIKE));
        truckButton.addActionListener(e -> parent.showDashboard(VehicleType.TRUCK));

        gbc.gridy = 0; add(carButton, gbc);
        gbc.gridy = 1; add(bikeButton, gbc);
        gbc.gridy = 2; add(truckButton, gbc);


    }


}
