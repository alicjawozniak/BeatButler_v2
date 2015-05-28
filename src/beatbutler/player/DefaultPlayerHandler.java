package beatbutler.player;

import beatbutler.playlist.song.Song;

/**
 * @author Tomasz Wójcik
 */
class DefaultPlayerHandler implements PlayerHandler {
    @Override
    public void songSelected(Song song) {
        complain();
    }

    @Override
    public void timeChanged(double value, double duration) {
        complain();
    }

    @Override
    public void playing() {
        complain();
    }

    @Override
    public void paused() {
        complain();
    }

    @Override
    public void stopped() {
        complain();
    }

    private void complain() {
        throw new UnsupportedOperationException("using default PlayerHandler");
    }
}
