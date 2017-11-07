package fuji.antibot.antibot;

import fuji.antibot.commands.VerifyCommand;
import fuji.antibot.main.AntiBot;
import fuji.antibot.util.AdminLog;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class CaptchaTimer extends BukkitRunnable {

    private final JavaPlugin plugin;

    int count;
    Player player;

    public CaptchaTimer(JavaPlugin plugin, int count, Player player) {
        this.plugin = plugin;

        if (count <= 0) {
            throw new IndexOutOfBoundsException("Timer cannot be 0 or less.");
        } else if (player == null) {
            throw new NullPointerException("Player equals null.");
        } else {
            this.count = count;
            this.player = player;
        }
    }

    @Override
    public void run() {
        if (count <= 0) {
            cancel();
            if (VerifyCommand.hasTask(player.getUniqueId())) {
                VerifyCommand.removeTask(player.getUniqueId());
            }

            if (VerifyCommand.hasID(player.getUniqueId())) {
                VerifyCommand.removeID(player.getUniqueId());
            }
            AdminLog.playerDeniedVerificationUnknownID(player);
            String kickMessage = AntiBot.getAntiBotLangFiles().get().getString("Captcha.kickMessage");
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage.replace("%player%", player.getName())));
        } else {
            count--;
        }
    }

    public boolean playerHasCaptchaTimer(Player player) {
        if (this.player.getUniqueId().toString().equals(player.getUniqueId().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public int getCount() {
        return count;
    }
}
