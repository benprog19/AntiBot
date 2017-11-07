package fuji.antibot.settings;

import fuji.antibot.main.AntiBot;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/10/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class AntiBotSettings {

    AntiBot antiBot;
    FileConfiguration configuration;

    public AntiBotSettings(AntiBot antiBot) {
        this.antiBot = antiBot;
        configuration = antiBot.getAntiBotSettingsFiles().get();
    }

    /*

    CAPTCHA TIME

     */

    public void setCaptchaTime(int time) {
        configuration.set("Captcha.time", time);
        antiBot.getAntiBotSettingsFiles().save();
    }

    public int getCaptchaTime() {
        return configuration.getInt("Captcha.time");
    }

}
