package beatbutler.playlist.iterator;

import beatbutler.playlist.song.Song;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Tomasz Wójcik
 */
public class OrderedIterator implements PlaylistIterator {
    private final List<Song> list;
    private Song last;

    public OrderedIterator(List<Song> list) {
        this.list = list;
    }

    @Override
    public boolean hasPrevious() {
        for (int i = list.size() - 1; i >= 0; i--) {
            Song song = list.get(i);
            if (song.isPlayed() && song != last) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Song previous() {
        for (int i = list.size() - 1; i >= 0; i--) {
            Song song = list.get(i);
            if (song.isPlayed()) {
                if (song == last) {
                    song.setPlayed(false);
                } else {
                    last = song;
                    return song;
                }
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasNext() {
        for (Song song : list) {
            if (!song.isPlayed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Song next() {
        for (Song song : list) {
            if (!song.isPlayed()) {
                song.setPlayed(true);
                last = song;
                return song;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean canJump(Song destination) {
        return list.contains(destination);
    }

    @Override
    public Song jump(Song destination) {
        if (!canJump(destination)) {
            throw new NoSuchElementException();
        }

        int index = list.indexOf(destination);
        for (int i = 0; i <= index; i++) {
            list.get(i).setPlayed(true);
        }
        for (int i = index + 1; i < list.size(); i++) {
            list.get(i).setPlayed(false);
        }

        last = list.get(index);
        return last;
    }

    @Override
    public void reset() {
        for (Song song : list) {
            song.setPlayed(false);
        }
    }
}
