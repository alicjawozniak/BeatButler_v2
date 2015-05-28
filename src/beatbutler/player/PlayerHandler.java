package beatbutler.player;

import beatbutler.playlist.song.Song;

/**
 * @author Tomasz Wójcik
 */
public interface PlayerHandler {
    void timeChanged(double value, double duration);

    void playing();

    void paused();

    void stopped();

    void songSelected(Song song);
}
