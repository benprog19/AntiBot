package fuji.antibot.files;

import fuji.antibot.main.AntiBot;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/10/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class AntiBotSettingsFiles {

    public AntiBot plugin;

    public AntiBotSettingsFiles(AntiBot pl) {
        plugin = pl;
    }

    File cfile;
    static FileConfiguration config;
    String location = "plugins/AntiBot";

    public void create() {
        if (!(getfile() == null))
            if (!plugin.getDataFolder().exists())
                plugin.getDataFolder().mkdir();
        if (!getfile().exists()) {
            try {
                getfile().createNewFile();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "Error creating " + getfile().getName() + "!  - " + e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public File getfile() {
        cfile = new File(location, "settings.yml");
        if (!(cfile == null)) {
            return cfile;
        } else {
            return null;
        }

    }

    public void load() {
        if (!(getfile() == null)) {
            config = YamlConfiguration.loadConfiguration(cfile);
        } else {
            Bukkit.broadcastMessage("§cFile does not exsist.  Cannot load file.");
        }
    }

    public FileConfiguration get() {
        return config;
    }

    public void save() {
        try {
            config.save(getfile());
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error saving " + getfile().getName() + "!");
        }
    }
}
