package gui.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Lecteur de musique de fond en boucle.
 * Utilise uniquement javax.sound.sampled (Java 8, aucune dépendance externe).
 *
 * Utilisation :
 *   MusicPlayer.play();   // démarre la musique en boucle
 *   MusicPlayer.stop();   // arrête la musique
 *   MusicPlayer.setVolume(0.7f); // volume entre 0.0 et 1.0
 */
public final class MusicPlayer {

    private MusicPlayer() {}

    private static final String MUSIC_FILE = "/resources/casino_music.wav";

    private static Clip   clip;
    private static boolean muted = false;

    /** Démarre la musique en boucle infinie. Sans effet si déjà en cours. */
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
            clip.loop(Clip.LOOP_CONTINUOUSLY);   // boucle infinie
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("[MusicPlayer] Erreur lecture audio : " + e.getMessage());
        }
    }

    /** Arrête et libère la musique. */
    public static void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    /** Met en pause / reprend. */
    public static void togglePause() {
        if (clip == null) return;
        if (clip.isRunning()) {
            clip.stop();
        } else {
            clip.start();
        }
    }

    /**
     * Règle le volume.
     * @param volume entre 0.0f (silence) et 1.0f (maximum)
     */
    public static void setVolume(float volume) {
        if (clip == null) return;
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;

        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        // Conversion volume linéaire → décibels
        float dB = (float) (Math.log10(Math.max(volume, 0.0001f)) * 20.0);
        dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
        gain.setValue(dB);
    }

    /** Retourne true si la musique est actuellement en lecture. */
    public static boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}
