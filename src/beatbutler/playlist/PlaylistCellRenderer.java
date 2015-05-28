package beatbutler.playlist;

import beatbutler.playlist.song.Song;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author Tomasz Wójcik
 */
public class PlaylistCellRenderer implements ListCellRenderer<Song> {
    @Override
    public Component getListCellRendererComponent(JList<? extends Song> list, Song song, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = new JLabel();
        label.setBorder(new EmptyBorder(1, 5, 1, 5));

        if (song.hasTags()) {
            label.setText(String.format(
                    "%s - %s - %s",
                    song.getTag("artist"),
                    song.getTag("album"),
                    song.getTag("title")
            ));
        } else {
            label.setText(song.getTag("filename"));
        }

        if (isSelected) {
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);
        }
        return label;
    }
}
