package beatbutler.player;

import beatbutler.playlist.Playlist;
import beatbutler.playlist.iterator.PlaylistIterator;
import beatbutler.playlist.song.Song;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author Tomasz Wójcik
 */
public class Player {
    private final Playlist playlist;
    private double volume;
    private boolean repeat;
    private PlaylistIterator iterator;
    private MediaPlayer mediaPlayer;
    private Song selectedSong;
    private PlayerHandler handler = new DefaultPlayerHandler();
    private State state;


    public Player() {
        state = State.STOPPED;
        playlist = new Playlist();
        iterator = playlist.orderedIterator();

        playlist.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                updateIterator();
            }
        });
    }

    public void toggle() {
        switch (state) {
            case PLAYING:
                setState(State.PAUSED);
                break;
            case PAUSED:
                setState(State.PLAYING);
                break;
            case STOPPED:
                if (iterator.hasNext()) {
                    setSong(iterator.next());
                    setState(State.PLAYING);
                } else {
                    setState(State.STOPPED);
                }
                break;
        }
    }

    public void previous() {
        switch (state) {
            case PLAYING:
            case PAUSED:
                if (iterator.hasPrevious()) {
                    setSong(iterator.previous());
                    setState(state);
                } else {
                    if (repeat) {
                        iterator.reset();
                        if (iterator.canJump(playlist.getLast())) {
                            setSong(iterator.jump(playlist.getLast()));
                            setState(state);
                        } else {
                            setSong(null);
                            setState(State.STOPPED);
                        }
                    } else {
                        setSong(null);
                        setState(State.STOPPED);
                    }
                }
                break;
            case STOPPED:
                break;
        }
    }

    public void next() {
        switch (state) {
            case PLAYING:
            case PAUSED:
                if (iterator.hasNext()) {
                    setSong(iterator.next());
                    setState(state);
                } else {
                    if (repeat) {
                        iterator.reset();
                        if (iterator.hasNext()) {
                            setSong(iterator.next());
                            setState(state);
                        } else {
                            setSong(null);
                            setState(State.STOPPED);
                        }
                    } else {
                        setSong(null);
                        setState(State.STOPPED);
                    }
                }
                break;
            case STOPPED:
                break;
        }
    }

    public void jumpTo(Song song) {
        iterator.reset();
        if (iterator.canJump(song)) {
            setSong(iterator.jump(song));
            setState(State.PLAYING);
        } else {
            setSong(null);
            setState(State.STOPPED);
        }
    }

    public void seek(double v) {
        if (state == State.PAUSED) {
            mediaPlayer.seek(Duration.millis(v));
        }
    }

    public void setSeeking(boolean b) {
        if (state != State.STOPPED) {
            setState(b ? State.PAUSED : State.PLAYING);
        }
    }

    private void setState(State state) {
        this.state = state;
        switch (state) {
            case PLAYING:
                mediaPlayer.play();
                handler.playing();
                break;
            case STOPPED:
                iterator.reset();
                if (mediaPlayer != null) {
                    mediaPlayer.dispose();
                    mediaPlayer = null;
                }
                handler.stopped();
                break;
            case PAUSED:
                mediaPlayer.pause();
                handler.paused();
                break;
        }
    }

    private void setSong(Song song) {
        selectedSong = song;

        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        if (song == null) {
            return;
        }

        // restore settings
        mediaPlayer = song.getPlayer();
        mediaPlayer.setVolume(volume);
        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                handler.timeChanged(
                        mediaPlayer.getCurrentTime().toMillis(),
                        mediaPlayer.getTotalDuration().toMillis()
                );
            }
        });
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                next();
            }
        });

        handler.songSelected(song);
    }

    public void setHandler(PlayerHandler handler) {
        this.handler = handler;
        setSong(null);
        setState(State.STOPPED);
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
        this.volume = volume;
    }

    public void setShuffle(boolean shuffle) {
        iterator = shuffle ? playlist.shuffleIterator() : playlist.orderedIterator();
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    private void updateIterator() {
        iterator.reset();
        if (iterator.canJump(selectedSong)) {
            iterator.jump(selectedSong);
            handler.songSelected(selectedSong);
        } else {
            setSong(null);
            setState(State.STOPPED);
        }
    }
}
