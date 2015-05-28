package beatbutler.playlist;

import beatbutler.playlist.iterator.OrderedIterator;
import beatbutler.playlist.iterator.PlaylistIterator;
import beatbutler.playlist.iterator.ShuffleIterator;
import beatbutler.playlist.song.Song;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tomasz Wójcik
 */
public class Playlist extends AbstractListModel<Song> {
    private final List<Song> list;

    public Playlist() {
        list = new ArrayList<>();
    }

    public void add(Song song) {
        int index = list.size();
        list.add(song);
        fireIntervalAdded(this, index, index);
    }

    public void remove(Song song) {
        int index = list.indexOf(song);
        list.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Song getElementAt(int index) {
        return list.get(index);
    }

    public PlaylistIterator orderedIterator() {
        return new OrderedIterator(list);
    }

    public PlaylistIterator shuffleIterator() {
        return new ShuffleIterator(list);
    }

    public Song getLast() {
        return list.get(list.size() - 1);
    }

    public void sort() {
        list.sort(new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.compareTo(o2);
            }
        });
        fireContentsChanged(this, 0, list.size() - 1);
    }
}
