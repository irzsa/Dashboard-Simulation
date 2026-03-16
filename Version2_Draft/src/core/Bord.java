package core;

import UI.Dashboard;
import UI.Gauge;
import UI.Speedometer;
import UI.Tachometer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class Bord {
    public static void main(String[] args) throws InterruptedException {
        Dashboard dash = new Dashboard();
        Sound sound = new Sound();
        sound.start();
        }
    }

