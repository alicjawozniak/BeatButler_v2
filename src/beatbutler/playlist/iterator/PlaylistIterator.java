package beatbutler.playlist.iterator;

import beatbutler.playlist.song.Song;

/**
 * @author Tomasz Wójcik
 */
public interface PlaylistIterator {
    boolean hasNext();

    Song next();

    boolean hasPrevious();

    Song previous();

    boolean canJump(Song destination);

    Song jump(Song destination);

    void reset();
}
