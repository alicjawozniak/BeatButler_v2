package beatbutler;

import beatbutler.player.Player;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * @author Tomasz Wójcik
 */
public class Launcher {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Player player = new Player();
                JFrame frame = new Window(player);
                frame.setVisible(true);
            }
        });
    }
}
