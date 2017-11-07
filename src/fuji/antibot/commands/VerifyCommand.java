package fuji.antibot.commands;

import fuji.antibot.main.AntiBot;
import fuji.antibot.util.AdminLog;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/9/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class VerifyCommand implements CommandExecutor, Listener {

    private static HashMap<UUID, BukkitTask> timer = new HashMap<>();
    private static HashMap<UUID, Integer> ids = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 0) {
                player.sendMessage("Unknown command. Type \"/help\" for help.");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase(getIDs().get(player.getUniqueId()) + "")) {
                    if (hasTask(player.getUniqueId())) {
                        AdminLog.playerAcceptedVerification(player, getIDs().get(player.getUniqueId()));
                        BukkitTask task = timer.get(player.getUniqueId());
                        task.cancel();

                        if (AntiBot.getAntiBotLangFiles().get().getStringList("Captcha.verify.message") != null) {
                            for (int i = 0; i < AntiBot.getAntiBotLangFiles().get().getStringList("Captcha.verify.message").size(); i++) {
                                String message = AntiBot.getAntiBotLangFiles().get().getStringList("Captcha.verify.message")
                                        .get(i);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%id%", ids.get(player.getUniqueId()) + "")));
                            }
                        }
                        removeTask(player.getUniqueId());
                        removeID(player.getUniqueId());


                    } else {
                        player.sendMessage("Unknown command. Type \"/help\" for help.");
                    }
                } else {
                    if (hasTask(player.getUniqueId())) {
                        AdminLog.playerDeniedVerification(player, getIDs().get(player.getUniqueId()));
                        BukkitTask task = timer.get(player.getUniqueId());
                        task.cancel();
                        removeTask(player.getUniqueId());
                        removeID(player.getUniqueId());
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', AntiBot.getAntiBotLangFiles().get().getString("Captcha.guessWrong")));

                    }
                }
            }
        }
        return true;
    }

    public static void addTask(UUID uuid, BukkitTask task) {
        timer.put(uuid, task);
    }

    public static void removeTask(UUID uuid) {
        timer.remove(uuid);
    }

    public static HashMap<UUID, BukkitTask> getMap() {
        return timer;
    }

    public static boolean hasTask(UUID uuid) {
        if (timer.containsKey(uuid)) {
            return true;
        } else {
            return false;
        }
    }


    public static void addID(UUID uuid, int id) {
        ids.put(uuid, id);
    }

    public static void removeID(UUID uuid) {
        ids.remove(uuid);
    }

    public static HashMap<UUID, Integer> getIDs() {
        return ids;
    }

    public static boolean hasID(UUID uuid) {
        if (ids.containsKey(uuid)) {
            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (hasTask(player.getUniqueId())) {
            removeTask(player.getUniqueId());
        }

        if (hasID(player.getUniqueId())) {
            removeID(player.getUniqueId());
        }
    }
}
