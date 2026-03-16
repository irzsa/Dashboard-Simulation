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
            line.open(format, 512);
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
        double currentFreq = frequency;
        double freqSmoothing = 0.05;
        byte[] buffer = new byte[512];
        byte[] buffA = new byte[512];
        byte[] buffB = new byte[512];
        boolean toggle = false;
        double phase = 0;
        double phase2 = 0;
        double filtered = 0;
        double filterCoefficient = 0.5;
        double lfoPhase = 0;
        double lfoFrequency = 2.0 + (currentFreq / 3);
        double outputFiltered = 0;
        double outputFilterCoefficient = 1.0;
        while (running) {
            currentFreq += (frequency - currentFreq) * freqSmoothing;
            byte[] buf = toggle ? buffA : buffB;;
            for (int i = 0; i < buffer.length; ++i) {

                buf = toggle ? buffA : buffB;
                toggle = !toggle;

                currentFreq += (frequency - currentFreq) * 0.001; // incrementam putin pe fiecare sample
                // avansam perioada
                phase += 2.0 * Math.PI * frequency / sampleRate;
                phase2 += 2.0 * Math.PI * (frequency * 2) / sampleRate;
                lfoPhase += 2.0 * Math.PI * lfoFrequency / sampleRate;


                // reglam perioada
                if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI;
                if (phase2 > 2.0 * Math.PI) phase2 -= 2.0 * Math.PI;
                if (lfoPhase > 2.0 * Math.PI) lfoPhase -= 2.0 * Math.PI;

                // sine wave + square wave
                double raw = Math.sin(phase) * 0.8 + Math.sin(phase2) * 0.2;

                // low pass filter
                filtered += filterCoefficient * (raw - filtered);

                double sidechain = (Math.sin(lfoPhase) + 1.0) / 2.0;
                double modulated = filtered * sidechain;

                double output = Math.max(-1.0, Math.min(1.0, modulated));

                // low pass filter
                outputFiltered += outputFilterCoefficient * (modulated - outputFiltered);

                buf[i] = (byte)(Math.max(-1.0, Math.min(1.0, outputFiltered)) * 127);

            }
            line.write(buf, 0, buf.length);

        }
    }
}
