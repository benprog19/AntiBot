package fuji.antibot.antibot;

import fuji.antibot.commands.VerifyCommand;
import fuji.antibot.main.AntiBot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static sun.audio.AudioPlayer.player; // TODO Check what import this is

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/15/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class CheckTimer {

    private final JavaPlugin plugin;
    BukkitTask runnable;

    int count;

    public CheckTimer(JavaPlugin plugin, int count) {
        this.plugin = plugin;

        if (count <= 0) {
            throw new IndexOutOfBoundsException("Timer cannot be 0 or less.");
        } else if (player == null) {
            throw new NullPointerException("Player equals null.");
        } else {
            this.count = count;
        }
    }

    public void start() {
        if (count != 0) {
            runnable = new BukkitRunnable() {

                @Override
                public void run() {
                    //System.out.print("T - " + count);
                    if (count <= 0) {
                        cancel();
                        for (Player pls : Bukkit.getOnlinePlayers()) {
                            if (AntiBot.getAntiBotSettingsFiles().get().getBoolean("Triggers.timelycheck.ignoreops")) {
                                if (!pls.isOp()) {
                                    if (!VerifyCommand.hasID(pls.getUniqueId())) {
                                        createCaptcha(pls, "timely-check", AntiBot.getAntiBotSettingsFiles().get().getInt("Captcha.time"));
                                    }
                                }
                            } else {
                                if (!VerifyCommand.hasID(pls.getUniqueId())) {
                                    createCaptcha(pls, "timely-check", AntiBot.getAntiBotSettingsFiles().get().getInt("Captcha.time"));
                                }
                            }

                        }
                    } else {
                        count--;
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Cannot start timelycheck due to time being equal to 0.");
        }
    }

    public void restart() {
        runnable.cancel();
        if (AntiBot.getAntiBotSettingsFiles().get().getBoolean("Triggers.timelycheck.enabled")) {
            count = AntiBot.getAntiBotSettingsFiles().get().getInt("Triggers.timelycheck.countMin");
            start();
        }
    }

    private void createCaptcha(Player player, String triggername, int time) {
        BukkitTask task = new CaptchaTimer(JavaPlugin.getPlugin(AntiBot.class), time, player)
                .runTaskTimer(JavaPlugin.getPlugin(AntiBot.class), 0L, 20L);
        Captcha captcha = new Captcha(triggername, player, task, time);
        captcha.printMessage();
        VerifyCommand.addTask(player.getUniqueId(), task);
        VerifyCommand.addID(player.getUniqueId(), captcha.getId());
    }

}
