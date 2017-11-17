package fuji.antibot.main;

import fuji.antibot.commands.AntiBotCommand;
import fuji.antibot.commands.VerifyCommand;
import fuji.antibot.events.Events;
import fuji.antibot.files.AntiBotLangFiles;
import fuji.antibot.files.AntiBotSettingsFiles;
import fuji.antibot.settings.AntiBotLang;
import fuji.antibot.settings.AntiBotSettings;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class AntiBot extends JavaPlugin {

    static AntiBotSettingsFiles antiBotSettingsFiles;
    static AntiBotLangFiles antiBotLangFiles;
    static AntiBotSettings antiBotSettings;
    static AntiBotLang antiBotLang;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("verify").setExecutor(new VerifyCommand());
        getCommand("ab").setExecutor(new AntiBotCommand());

        antiBotSettingsFiles = new AntiBotSettingsFiles(this);
        antiBotLangFiles = new AntiBotLangFiles(this);
        antiBotSettings = new AntiBotSettings(this);
        antiBotLang = new AntiBotLang(this);

        antiBotSettingsFiles.create();
        antiBotLangFiles.create();

        try {
            if (antiBotLangFiles.get().getConfigurationSection("Captcha") == null) {
                antiBotLangFiles.get().set("Captcha.kickMessage", "&6Captcha was not verified.");

                String border = "&9&m                                                    ";

                List<String> verifyMessage = antiBotLangFiles.get().getStringList("Captcha.verify");
                verifyMessage.add(border);
                verifyMessage.add(" &6&lYou are verified. Thank you. ID: %id%");
                verifyMessage.add(border);

                List<String> captchaMessage = antiBotLangFiles.get().getStringList("Captcha.message");
                captchaMessage.add(border);
                captchaMessage.add(" ");
                captchaMessage.add(" &c&lWARNING:");
                captchaMessage.add(" &6This message was triggered by: &c%trigger%");
                captchaMessage.add(" &6Please verify that you are not a bot. You have &c" + "%seconds%" + " &6seconds.");
                captchaMessage.add(" ");
                captchaMessage.add(" %verify%");
                captchaMessage.add(" ");
                captchaMessage.add(border);

                antiBotLangFiles.get().set("Captcha.message", captchaMessage);
                antiBotLangFiles.get().set("Captcha.verify.message", verifyMessage);
                antiBotLangFiles.get().set("Captcha.verify.button.display", "&a&l[VERIFY]");
                antiBotLangFiles.get().set("Captcha.verify.button.hover", "&9Verify your player.");
                antiBotLangFiles.get().set("Captcha.guessWrong", "&c&lYou are not allowed to guess your verifiable ID.");

                antiBotLangFiles.save();
            }

            if (antiBotLangFiles.get().getConfigurationSection("AntiBot") == null) {
                antiBotLangFiles.get().set("AntiBot.prefix", "&8[&cAntiBot&8] &6");
                antiBotLangFiles.get().set("AntiBot.noPermission", "&cYou do not have permission to use this command.");
                antiBotLangFiles.get().set("AntiBot.configReloaded", "&aAll files have been reloaded.");
                antiBotLangFiles.get().set("AntiBot.AdminLog.playerAcceptVerification", "&c%player% &6has accepted the verification. (ID: %id%)");
                antiBotLangFiles.get().set("AntiBot.AdminLog.playerDeniedVerification", "&c%player% &6has denied the verification. (ID: %id%)");
                antiBotLangFiles.get().set("AntiBot.AdminLog.playerDeniedVerificationUnknownID", "&c%player% &6has denied the verification. (ID: Unknown)");
                antiBotLangFiles.get().set("AntiBot.AdminLog.playerTriggered", "&c%player% &6has triggered %trigger%. (ID: %id%)");
                antiBotLangFiles.get().set("AntiBot.AdminLog.disabled", "&cAdminLog has been disabled.");
                antiBotLangFiles.get().set("AntiBot.AdminLog.enabled", "&aAdminLog has been enabled.");
                antiBotLangFiles.get().set("AntiBot.AdminLog.noPermission", "&cYou do not have permission to use this command.");
                antiBotLangFiles.save();
            }

            if (antiBotSettingsFiles.get().getConfigurationSection("Captcha") == null) {
                antiBotSettingsFiles.get().set("Captcha.time", 20);
                antiBotSettingsFiles.get().set("Triggers.chest-access.count", 5);
                antiBotSettingsFiles.get().set("Triggers.chest-access.enabled", true);
                antiBotSettingsFiles.get().set("Triggers.sign-interact.count", 5);
                antiBotSettingsFiles.get().set("Triggers.sign-interact.enabled", true);
                antiBotSettingsFiles.get().set("Triggers.teleport.count", 5);
                antiBotSettingsFiles.get().set("Triggers.teleport.enabled", true);
                antiBotSettingsFiles.get().set("Triggers.chat.enabled", true);
                antiBotSettingsFiles.get().set("Triggers.chat.minMessageDelay", 0.5);
                antiBotSettingsFiles.get().set("AdminLog.permission", "antibot.adminlog");
                antiBotSettingsFiles.get().set("AntiBot.permission", "antibot.admin");
                antiBotSettingsFiles.save();
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error occurred while trying to load lang.yml or settings.yml. [AntiBot]");
            antiBotLangFiles.save();
            antiBotSettingsFiles.save();
        }


    }

    @Override
    public void onDisable() {

    }

    public static AntiBotSettingsFiles getAntiBotSettingsFiles() {
        return antiBotSettingsFiles;
    }

    public static AntiBotLangFiles getAntiBotLangFiles() {
        return antiBotLangFiles;
    }

    public static AntiBotSettings getAntiBotSettings() {
        return antiBotSettings;
    }

    public static AntiBotLang getAntiBotLang() {
        return antiBotLang;
    }

}
