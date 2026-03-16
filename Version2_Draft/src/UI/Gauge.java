package UI;

public abstract class Gauge
{
    protected int value;
    protected int maxValue;

    public Gauge()
    {}

    public Gauge(int maxValue)
    {
        this.maxValue = maxValue;
        this.value = 0;
    }

    public abstract void update(int newValue);

    public abstract void display();
}
