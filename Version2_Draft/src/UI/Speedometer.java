package UI;

public class Speedometer extends Gauge {

    public Speedometer(int maxValue) {
        super(maxValue);
    }

    @Override
    public void update(int newValue) {

        this.value = Math.min(newValue, maxValue);
    }

    @Override
    public void display() {
        System.out.print("\r\n\n\n\n\n\n\nSPEED: " + value + " " + bar(value, maxValue) + "\n");
    }

    private String bar(int value, int maxValue) // bara ascii simulare speedometer
    {
        int barLen = 20;
        int filled = (int) ((double) value / maxValue * barLen);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLen; ++i) {
            if (i < filled)
                bar.append("█");
            else
                bar.append("░");
        }
        bar.append("]");
        return bar.toString();
    }
}