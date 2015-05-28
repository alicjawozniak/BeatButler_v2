package beatbutler.playlist.iterator;

import beatbutler.playlist.song.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @author Tomasz Wójcik
 */
public class ShuffleIterator implements PlaylistIterator {
    private final List<Song> list;
    private final Random random;

    public ShuffleIterator(List<Song> list) {
        this.list = list;
        random = new Random(System.nanoTime());
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
        ArrayList<Song> left = new ArrayList<>();
        for (Song song : list) {
            if (!song.isPlayed()) {
                left.add(song);
            }
        }

        if (left.size() < 1) {
            throw new NoSuchElementException();
        }

        Song song = left.get(random.nextInt(left.size()));
        song.setPlayed(true);
        return song;
    }

    @Override
    public boolean hasPrevious() {
        for (Song song : list) {
            if (song.isPlayed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Song previous() {
        ArrayList<Song> played = new ArrayList<>();
        for (Song song : list) {
            if (song.isPlayed()) {
                played.add(song);
            }
        }

        if (played.size() < 1) {
            throw new NoSuchElementException();
        }

        return played.get(random.nextInt(played.size()));
    }

    @Override
    public boolean canJump(Song destination) {
        return list.contains(destination);
    }

    @Override
    public Song jump(Song destination) {
        if (!list.contains(destination)) {
            throw new NoSuchElementException();
        }

        for (Song song : list) {
            song.setPlayed(false);
        }

        destination.setPlayed(true);
        return destination;
    }

    @Override
    public void reset() {
        for (Song song : list) {
            song.setPlayed(false);
        }
    }
}
