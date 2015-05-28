package beatbutler;

import beatbutler.player.Player;
import beatbutler.player.PlayerHandler;
import beatbutler.playlist.AudioFileChooser;
import beatbutler.playlist.Playlist;
import beatbutler.playlist.PlaylistCellRenderer;
import beatbutler.playlist.song.ConcreteSong;
import beatbutler.playlist.song.Song;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Tomasz Wójcik
 */
public class Window extends JFrame {
    private final Player player;
    private final Playlist playlist;
    private final Icon playIcon;
    private final Icon pauseIcon;
    private JPanel mainPanel;
    private JList<Song> playlistJList;
    private JButton addButton;
    private JButton removeButton;
    private JButton previousButton;
    private JButton stateButton;
    private JButton nextButton;
    private JSlider progressSlider;
    private JLabel songInfoLabel;
    private JSlider volumeSlider;
    private JLabel progressLabel;
    private JLabel durationLabel;
    private JToggleButton repeatToggleButton;
    private JToggleButton shuffleToggleButton;
    private JButton showButton;
    private JButton sortButton;

    public Window(Player p) {
        player = p;
        playlist = player.getPlaylist();

        playIcon = new ImageIcon(getClass().getResource("/play.png"));
        pauseIcon = new ImageIcon(getClass().getResource("/pause.png"));

        // hack
        new JFXPanel();

        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        // playlist
        playlistJList.setModel(playlist);
        playlistJList.setCellRenderer(new PlaylistCellRenderer());
        playlistJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = playlistJList.locationToIndex(e.getPoint());
                    if (0 <= index && index < playlist.getSize()) {
                        player.jumpTo(playlist.getElementAt(index));
                    }
                }
            }
        });

        // buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new AudioFileChooser();
                if (fc.showDialog(Window.this, "Add") == JFileChooser.APPROVE_OPTION) {
                    List<File> files = Arrays.asList(fc.getSelectedFiles());
                    for (File file : files) {
                        playlist.add(new ConcreteSong(file));
                    }
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Song song : playlistJList.getSelectedValuesList()) {
                    playlist.remove(song);
                }
            }
        });

        stateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.toggle();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.next();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.previous();
            }
        });

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playlistJList.setSelectedValue(player.getSelectedSong(), true);
            }
        });

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playlist.sort();
            }
        });

        // toggle buttons
        repeatToggleButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        player.setRepeat(true);
                        break;
                    case ItemEvent.DESELECTED:
                        player.setRepeat(false);
                        break;
                }
            }
        });

        shuffleToggleButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        player.setShuffle(true);
                        break;
                    case ItemEvent.DESELECTED:
                        player.setShuffle(false);
                        break;
                }
            }
        });

        // sliders
        progressSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (progressSlider.getValueIsAdjusting()) {
                    player.seek(progressSlider.getValue());
                }

                progressLabel.setText(String.format(
                        "%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(progressSlider.getValue()),
                        TimeUnit.MILLISECONDS.toSeconds(progressSlider.getValue()) % 60
                ));
                durationLabel.setText(String.format(
                        "%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(progressSlider.getMaximum()),
                        TimeUnit.MILLISECONDS.toSeconds(progressSlider.getMaximum()) % 60
                ));
            }
        });

        progressSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    player.setSeeking(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    player.setSeeking(false);
                }
            }
        });

        progressSlider.setValue(0);
        progressSlider.setMaximum(0);

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int min = volumeSlider.getMinimum();
                int val = volumeSlider.getValue();
                int max = volumeSlider.getMaximum();
                player.setVolume((double) (val - min) / (max - min));
            }
        });

        volumeSlider.setValue(25);
        volumeSlider.setMaximum(100);

        // model handler
        player.setHandler(new PlayerHandler() {
            @Override
            public void songSelected(Song song) {
                playlistJList.setSelectedValue(song, false);
                if (song.hasTags()) {
                    songInfoLabel.setText(String.format(
                            "%s - %s",
                            song.getTag("artist"),
                            song.getTag("title")
                    ));
                } else {
                    songInfoLabel.setText(song.getTag("filename"));
                }
            }

            @Override
            public void timeChanged(double value, double duration) {
                if (!progressSlider.getValueIsAdjusting()) {
                    progressSlider.setMaximum((int) duration);
                    progressSlider.setValue((int) value);
                }
            }

            @Override
            public void playing() {
                stateButton.setIcon(pauseIcon);
            }

            @Override
            public void paused() {
                stateButton.setIcon(playIcon);
            }

            @Override
            public void stopped() {
                playlistJList.clearSelection();
                stateButton.setIcon(playIcon);
                songInfoLabel.setText(null);
            }
        });
    }
}
