package fuji.antibot.antibot;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static sun.audio.AudioPlayer.player;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/15/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class HourlyCheckTimer extends BukkitRunnable {

    private final JavaPlugin plugin;

    int count;

    public HourlyCheckTimer(JavaPlugin plugin, int count) {
        this.plugin = plugin;

        if (count <= 0) {
            throw new IndexOutOfBoundsException("Timer cannot be 0 or less.");
        } else if (player == null) {
            throw new NullPointerException("Player equals null.");
        } else {
            this.count = count;
        }
    }

    @Override
    public void run() {
        if (count <= 0) {
            cancel();

        } else {
            count--;
        }
    }

}
