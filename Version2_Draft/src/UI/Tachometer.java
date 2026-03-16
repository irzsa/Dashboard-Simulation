package UI;

public class Tachometer extends Gauge
{

    public Tachometer(int maxValue)
    {
        super(maxValue);
    }

    @Override
    public void update(int newValue)
    {

        this.value = Math.min(newValue, maxValue);
    }

    @Override
    public void display()
    {
        System.out.print("\rRPM: " + value + " " + bar(value, maxValue) + "\n");
    }

    private String bar(int value, int maxValue) // bara ascii simulare tahometru
    {
        int barLen = 20;
        int filled = (int)((double) value / maxValue * barLen);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLen; ++i)
        {
            if (i < filled)
                bar.append("█");
            else
                bar.append("░");
        }
        bar.append("]");
        return bar.toString();
    }

}