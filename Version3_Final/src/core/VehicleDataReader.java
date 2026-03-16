package core;

import java.io.File;
import java.util.Scanner;

public class VehicleDataReader {

    public static int[] loadSpecs (String filename, VehicleType type) {
        int[] values = new int[12];
        try (Scanner sc = new Scanner(new File(filename))) {
            boolean inSection = false;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty())
                    continue;
                if (line.equals("[" + type + "]")) {
                    inSection = true;
                    continue;
                }

                if (line.startsWith("[") && inSection)
                    break;

                if (inSection && line.contains("=")) {
                    String[] parts = line.split("=");
                    int value = Integer.parseInt(parts[1].trim());
                    switch(parts[0].trim()){
                        case "idle" -> values[0] = value;
                        case "torque" -> values[1] = value;
                        case "hp" -> values[2] = value;
                        case "redline" -> values[3] = value;
                        case "tachometerX" -> values[4] = value;
                        case "tachometerY" -> values[5] = value;
                        case "speedometerX" -> values[6] = value;
                        case "speedometerY" -> values[7] = value;
                        case "tempX" -> values[8] = value;
                        case "tempY" -> values[9] = value;
                        case "fuelX" -> values[10] = value;
                        case "fuelY" -> values[11] = value;
                    }


                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
}
