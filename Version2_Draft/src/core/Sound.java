package core;

import javax.sound.sampled.*;

public class Sound {
    private static final float sampleRate = 44100;
    private SourceDataLine line;
    private boolean running = false;
    private Thread soundThread;
    private double frequency = 100;

    public void start() // pregatim si pornim line (sunetul)
    {
        running = true;
        try {
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);
            line = AudioSystem.getSourceDataLine(format);
            line.open(format, (int) sampleRate);
            line.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        soundThread = new Thread(() -> playLoop());
        soundThread.start();
    }

    public void stop() // golim line
    {
        running = false;
        if (soundThread != null)
        {
            try { soundThread.join(); }
            catch (InterruptedException ignored)
            {}
        }
        if (line != null)
        {
            line.drain();
            line.close();
        }
    }

    public void setFrequency(double freq)
    {
        this.frequency = freq;
    }

    private void playLoop() // construirea sunetului
    {

        byte[] buffer = new byte[1024];
        double phase = 0;
        double phase2 = 0;
        double filtered = 0;
        double filterCoefficient = 0.01;
        double lfoPhase = 0;
        double lfoFrequency = 12.5;
        double outputFiltered = 0;
        double outputFilterCoefficient = 0.005;
        while (running) {
            for (int i = 0; i < buffer.length; ++i) {
                // avansam perioada
                phase += 2.0 * Math.PI * frequency / sampleRate;
                phase2 += 2.0 * Math.PI * (frequency * 2) / sampleRate;
                lfoPhase += 2.0 * Math.PI * lfoFrequency / sampleRate;


                // reglam perioada
                if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI;
                if (phase2 > 2.0 * Math.PI) phase2 -= 2.0 * Math.PI;
                if (lfoPhase > 2.0 * Math.PI) lfoPhase -= 2.0 * Math.PI;

                // sine wave + square wave
                double raw = Math.sin(phase) * 0.6 + Math.sin(phase2) * 0.4;

                // low pass filter
                filtered += filterCoefficient * (raw - filtered);

                double sidechain = (Math.sin(lfoPhase) + 1.0) / 2.0; // range 0–1
                double modulated = filtered * sidechain;

                double output = Math.max(-1.0, Math.min(1.0, modulated));

                // low pass filter
                outputFiltered += outputFilterCoefficient * (modulated - outputFiltered);

                buffer[i] = (byte)(Math.max(-1.0, Math.min(1.0, outputFiltered)) * 127);
            }
            line.write(buffer, 0, buffer.length);

        }
    }
}
