package fuji.antibot.commands;

import fuji.antibot.main.AntiBot;
import fuji.antibot.util.AdminLog;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/12/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class AntiBotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(AntiBot.getAntiBotSettingsFiles().get().getString("AntiBot.permission"))) {
                if (args.length == 0) {
                    String border = ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "                                                    ";
                    player.sendMessage(border);
                    player.sendMessage(" ");
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "  Anti - Bot");
                    player.sendMessage(ChatColor.GRAY + "   /ab reload - Reload config.");
                    player.sendMessage(ChatColor.GRAY + "   /ab adminlog - Activates admin mode.");
                    player.sendMessage(ChatColor.GRAY + "   More coming soon... Suggest a command at the spigot download site.");
                    player.sendMessage(" ");
                    player.sendMessage(border);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        AntiBot.getAntiBotLangFiles().load();
                        AntiBot.getAntiBotSettingsFiles().load();
                        AntiBot.getAntiBotLangFiles().save();
                        AntiBot.getAntiBotSettingsFiles().save();
                        AntiBot.getTimer().restart();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiBot.getAntiBotLangFiles().get().getString("AntiBot.prefix") + AntiBot.getAntiBotLangFiles().get().getString("AntiBot.configReloaded")));
                    } else if (args[0].equalsIgnoreCase("adminlog")) {
                        if (player.hasPermission(AntiBot.getAntiBotSettingsFiles().get().getString("AdminLog.permission"))) {
                            if (AdminLog.hasAdminLogEnabled(player)) {
                                AdminLog.removeAdminLog(player);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiBot.getAntiBotLangFiles().get().getString("AntiBot.prefix") + AntiBot.getAntiBotLangFiles().get().getString("AntiBot.AdminLog.disabled")));
                            } else {
                                AdminLog.addAdminLog(player);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiBot.getAntiBotLangFiles().get().getString("AntiBot.prefix") + AntiBot.getAntiBotLangFiles().get().getString("AntiBot.AdminLog.enabled")));
                            }
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiBot.getAntiBotLangFiles().get().getString("AntiBot.prefix") + AntiBot.getAntiBotLangFiles().get().getString("AntiBot.AdminLog.noPermission")));
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AntiBot.getAntiBotLangFiles().get().getString("AntiBot.prefix") + AntiBot.getAntiBotLangFiles().get().getString("AntiBot.noPermission")));
            }
        }
        return true;
    }
}
