package demos.nehe.lesson30;

import demos.common.ResourceRetriever;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

class AudioSample {
    private final Clip clip;

    public AudioSample(InputStream inputStream) throws IOException {
        /* Load Sound*/
        try {
            // The inputstreams that we get by loading files from jars do not support
            // mark() and reset(), which are required by AudioSystem.getAudioInputStream().
            // To work around this problem, we first read the entire contents
            // of the inputstream to a byte array and wrap it in a ByteArrayInputStream.
            // This class does support mark() and reset().
            InputStream in = ensureMarkResetAvailable(inputStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
            AudioFormat audioFormat = audioInputStream.getFormat();

            int size = (int) (audioFormat.getFrameSize() * audioInputStream.getFrameLength());
            byte[] audio = new byte[size];
            audioInputStream.read(audio, 0, size);
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat, size);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(audioFormat, audio, 0, size);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream ensureMarkResetAvailable(InputStream inputStream) throws IOException {
        if (inputStream.markSupported()) {
            return inputStream;
        } else {
            return new ByteArrayInputStream(readEntireStream(inputStream));
        }
    }

    private static byte[] readEntireStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[8];
        byte[] data = null;
        int dataLength = 0;

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            data = append(buffer, bytesRead, data, dataLength);
            dataLength += bytesRead;
        }

        return trim(data, dataLength);
    }

    private static byte[] append(byte[] data, int amount, byte[] array, int offset) {
        if (array == null) {
            array = new byte[amount];
        }

        if (offset + amount >= array.length) {
            byte[] newArray = new byte[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, offset);
            array = newArray;
        }

        System.arraycopy(data, 0, array, offset, amount);
        return array;
    }

    private static byte[] trim(byte[] data, int amount) {
        if (data == null) {
            return new byte[amount];
        } else if (data.length == amount) {
            return data;
        } else {
            byte[] newArray = new byte[amount];
            System.arraycopy(data, 0, newArray, 0, amount);
            return newArray;
        }
    }

    public void play() {
        play(false, false);
    }

    public void play(boolean wait, boolean loop) {
        if (wait) {
            WaitUntilFinishedLineListener waitUntilFinishedLineListener = new WaitUntilFinishedLineListener();
            clip.addLineListener(waitUntilFinishedLineListener);
            synchronized (clip) {
                play(loop);
                try {
                    clip.wait();
                } catch (InterruptedException e) {
                }
            }
            clip.removeLineListener(waitUntilFinishedLineListener);
        } else {
            play(loop);
        }
    }

    private void play(boolean loop) {
        clip.stop();
        clip.setFramePosition(0);
        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
    }

    private class WaitUntilFinishedLineListener implements LineListener {
        public WaitUntilFinishedLineListener() {
        }

        public void update(LineEvent event) {
            if (event.getType().equals(LineEvent.Type.STOP) ||
                    event.getType().equals(LineEvent.Type.CLOSE)) {
                synchronized (clip) {
                    clip.notify();
                }
            }
        }
    }

    public void stop() {
        clip.stop();
    }
}
