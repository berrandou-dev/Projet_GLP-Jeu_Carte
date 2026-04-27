package gui.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Background music player.
 */
public final class MusicPlayer {

    private MusicPlayer() {}

    private static final String MUSIC_FILE = "/resources/casino_music.wav";
    private static Clip clip = null;
    private static boolean muted = false;

    public static void play() {
        if (clip != null && clip.isRunning()) return;
        
        try {
            URL url = MusicPlayer.class.getResource(MUSIC_FILE);
            if (url == null) {
                System.err.println("[MusicPlayer] Fichier introuvable : " + MUSIC_FILE);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            System.err.println("[MusicPlayer] Erreur : " + e.getMessage());
        }
    }

    public static void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    public static void toggleMute() {
        if (clip == null) return;
        
        if (muted) {
            clip.start();
            muted = false;
        } else {
            clip.stop();
            muted = true;
        }
    }

    public static boolean isMuted() {
        return muted;
    }
}
