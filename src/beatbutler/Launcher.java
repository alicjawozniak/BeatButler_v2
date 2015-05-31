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

        final Player player = new Player();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Window window = new Window();
                window.buildGUI(player);
                window.setVisible(true);
            }
        });
    }
}
