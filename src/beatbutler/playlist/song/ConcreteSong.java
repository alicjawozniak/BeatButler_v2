package beatbutler.playlist.song;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomasz Wójcik
 */
public class ConcreteSong implements Song {
    private final Map<String, String> tags;
    private final Media media;
    private boolean hasTags;
    private boolean isPlayed;

    public ConcreteSong(File file) {
        media = new Media(file.toURI().toString());
        tags = new HashMap<>();
        isPlayed = false;

        try {
            Tag tag = AudioFileIO.read(file).getTag();
            tags.put("album", tag.getFirst(FieldKey.ALBUM));
            tags.put("title", tag.getFirst(FieldKey.TITLE));
            tags.put("artist", tag.getFirst(FieldKey.ARTIST));
            hasTags = true;
        } catch (CannotReadException | IOException | ReadOnlyFileException | TagException | InvalidAudioFrameException e) {
            e.printStackTrace();
            tags.put("album", "unknown");
            tags.put("title", "unknown");
            tags.put("artist", "unknown");
            hasTags = false;
        }
        tags.put("filename", file.getName());
    }

    @Override
    public String getTag(String key) {
        if (tags.containsKey(key)) {
            return tags.get(key);
        } else {
            throw new IllegalArgumentException("key does not exist");
        }
    }

    @Override
    public MediaPlayer getPlayer() {
        return new MediaPlayer(media);
    }

    @Override
    public boolean hasTags() {
        return hasTags;
    }

    @Override
    public boolean isPlayed() {
        return isPlayed;
    }

    @Override
    public void setPlayed(boolean isPlayed) {
        this.isPlayed = isPlayed;
    }

    @Override
    public int compareTo(Song o) {
        int delta = Boolean.compare(hasTags(), o.hasTags());
        if (delta != 0) {
            return delta;
        }

        delta = compareTag(o, "artist");
        if (delta == 0) {
            delta = compareTag(o, "album");
            if (delta == 0) {
                delta = compareTag(o, "title");
            }
        }
        return delta;
    }

    private int compareTag(Song o, String tag) {
        return getTag(tag).compareTo(o.getTag(tag));
    }
}