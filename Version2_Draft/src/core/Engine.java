package core;

public class Engine {
    private int rpm;
    private int targetRpm;
    private int idle;
    private int redline;
    private int gear;
    private int hp;
    private int torque;


    public Engine()
    {
        this.rpm = idle;
    }

    public int getSpeed()
    {
        double gearMaxSpeed = (double)getMaxSpeed() * ((double) gear / 6);

        double speed = gearMaxSpeed * (rpm / (double)redline);

        if (speed > gearMaxSpeed) speed = gearMaxSpeed;
        return (int)speed;
    }

    public Engine(int idle, int torque, int hp, int redline)
    {
        this.rpm = idle;
        this.idle = idle;
        this.redline = redline;
        this.gear = 1;
        this.hp = hp;
        this.torque = torque;
        this.targetRpm = idle;
    }

    public void revUp() // urcam turatiile, formula
    {
        int acc = (int)(0.009 * (double)((hp * torque) / gear)) + (int)(0.015 * (redline - rpm));
        targetRpm = Math.min(redline, targetRpm + acc);
    }

    public void revDown()
    {
        int dec = (int)(0.009 * (double)((hp * torque) / gear)) + (int)(0.015 * (redline - rpm));
        targetRpm = Math.max(idle, targetRpm - dec);
    }

    public void update(double deltaTime)
    {
        double rpmDif = targetRpm - rpm;
        double inertia = 8.0;
        rpm += rpmDif * deltaTime * inertia;

        if (rpm < idle)
            rpm = idle;
        if (rpm > redline)
            rpm = redline;
    }

    public int getRpm()
    {
        return rpm;
    }

    public int getMaxRpm()
    {
        return redline;
    }

    public int getMaxSpeed() //renuntam la formula asta ca ne am prins urechile
    {
        //return (int)(0.07 * (hp * torque) / redline);
        return 250;
    }

    public void upShift()
    {
        if (gear < 6)
            gear++;
        if (gear != 6) {
            if (rpm < 3000)
                rpm = idle;
            else
                rpm -= 1800;
        }

    }

    public void downShift()
    {
        if (gear > 1)
            gear--;
        if (gear != 1) {
            if (rpm + 1800 > redline)
                rpm = redline;
            else
                rpm += 1800;
        }
    }

    public int getGear()
    {
        return gear;
    }
}
