package beatbutler.playlist;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Tomasz Wójcik
 */
public class AudioFileChooser extends JFileChooser {
    private final static String[] EXTENSIONS = {"mp3", "aac", "wav"};

    public AudioFileChooser() {
        super();
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setMultiSelectionEnabled(true);
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                for (String ext : EXTENSIONS) {
                    if (f.getName().endsWith("." + ext)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Audio files";
            }
        });
    }
}
