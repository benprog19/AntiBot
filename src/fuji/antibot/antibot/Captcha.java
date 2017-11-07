package fuji.antibot.antibot;

import fuji.antibot.main.AntiBot;
import fuji.antibot.util.AdminLog;
import fuji.antibot.util.Text;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Captcha {

    String trigger;
    Player player;
    BukkitTask task;
    int count;
    int id;

    public Captcha(String trigger, Player player,  BukkitTask task, int count) {
        this.trigger = trigger;
        this.player = player;
        this.task = task;
        this.count = count;
        this.id = new Random().nextInt(10000);

        AdminLog.playerTriggered(player, trigger, id);
    }

    public void printMessage() {
        if (trigger != null
                && player != null
                && task != null) {

            List<String> list = AntiBot.getAntiBotLangFiles().get().getStringList("Captcha.message");
            for (int i = 0; i < list.size(); i++) {
                String message = list.get(i);
                translate(message).send(player);
            }
        }
    }

    private Text translate(String message) {
        String msg = ChatColor.translateAlternateColorCodes('&', message.replace("%verify%","{cmd=/verify " + id + "}{hover=" + AntiBot.getAntiBotLangFiles().get().getString("Captcha.verify.button.hover") + "}" + AntiBot.getAntiBotLangFiles().get().getString("Captcha.verify.button.display") + "{/}").replace("%trigger%", trigger).replace("%player%", player.getName()).replace("%seconds%", count + ""));
        Text text = new Text(msg);
        return text;
    }

    public int getId() {
        return id;
    }
}
