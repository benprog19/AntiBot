package fuji.antibot.util;

import fuji.antibot.main.AntiBot;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/12/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class AdminLog {

    static String prefix = AntiBot.getAntiBotLangFiles().get().getString("AntiBot.prefix");

    static HashMap<UUID, Boolean> enabledAdminLog = new HashMap<>();

    public static boolean hasAdminLogEnabled(Player player) {
        if (enabledAdminLog.containsKey(player.getUniqueId())) {
            return enabledAdminLog.get(player.getUniqueId());
        }
        return false;
    }

    public static void addAdminLog(Player player) {
        if (!enabledAdminLog.containsKey(player.getUniqueId())) {
            enabledAdminLog.put(player.getUniqueId(), true);
        }
    }

    public static void removeAdminLog(Player player) {
        if (enabledAdminLog.containsKey(player.getUniqueId())) {
            enabledAdminLog.remove(player.getUniqueId());
        }
    }


    public static void playerAcceptedVerification(Player player, int id) {
        String message = AntiBot.getAntiBotLangFiles().get().getString("AntiBot.AdminLog.playerAcceptVerification").replace("%player%", player.getName()).replace("%id%", id + "");
        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (hasAdminLogEnabled(pls)) {
                pls.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static void playerDeniedVerification(Player player, int id) {
        String message = AntiBot.getAntiBotLangFiles().get().getString("AntiBot.AdminLog.playerDeniedVerification").replace("%player%", player.getName()).replace("%id%", id + "");
        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (hasAdminLogEnabled(pls)) {
                pls.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static void playerDeniedVerificationUnknownID(Player player) {
        String message = AntiBot.getAntiBotLangFiles().get().getString("AntiBot.AdminLog.playerDeniedVerificationUnknownID").replace("%player%", player.getName());
        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (hasAdminLogEnabled(pls)) {
                pls.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }



    public static void playerTriggered(Player player, String trigger, int id) {
        String message = AntiBot.getAntiBotLangFiles().get().getString("AntiBot.AdminLog.playerTriggered").replace("%player%", player.getName()).replace("%trigger%", trigger).replace("%id%", id + "");
        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (hasAdminLogEnabled(pls)) {
                pls.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

}
