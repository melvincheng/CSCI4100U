package ca.tru.comp3160.galacticoverlord.GalacticOverlord.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;

public class AudioHandler {
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;

    private int gameOverId;
    private int explosionId;
    private int laserId;

    public AudioHandler(Context context) {
        // get the media player ready for the background music
        mediaPlayer = new MediaPlayer();

        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor musicFd = assetManager.openFd("Endless.mp3");
            mediaPlayer.setDataSource(musicFd.getFileDescriptor(),
                                      musicFd.getStartOffset(),
                                      musicFd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);

            // load the sound effects
            soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
            AssetFileDescriptor descriptor = assetManager.openFd("Explosion.mp3");
            this.explosionId = soundPool.load(descriptor, 1);

            descriptor = assetManager.openFd("LaserBlast.mp3");
            this.laserId = soundPool.load(descriptor, 1);

            descriptor = assetManager.openFd("GameOver.mp3");
            this.gameOverId = soundPool.load(descriptor, 1);
        } catch (IOException e) {
            //messages.setText("Error loading music" + e.getMessage());
            mediaPlayer = null;
        }
    }

    public void playGameOverSound() {
        soundPool.play(this.gameOverId, 1.0f, 1.0f, 0, 0, 1);
    }

    public void playExplosionSound() {
        soundPool.play(this.explosionId, 1.0f, 1.0f, 0, 0, 1);
    }

    public void playLaserBlastSound() {
        soundPool.play(this.laserId, 1.0f, 1.0f, 0, 0, 1);
    }

    public void pause(boolean finishing) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            if (finishing) {
                // we are on the way out
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}
