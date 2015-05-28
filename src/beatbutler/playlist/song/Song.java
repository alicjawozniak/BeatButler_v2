package beatbutler.playlist.song;

import javafx.scene.media.MediaPlayer;

/**
 * @author Tomasz Wójcik
 */
public interface Song extends Comparable<Song> {
    String getTag(String key);

    MediaPlayer getPlayer();

    boolean hasTags();

    boolean isPlayed();

    void setPlayed(boolean isPlayed);
}
