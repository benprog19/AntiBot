package fuji.antibot.settings;

import fuji.antibot.main.AntiBot;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/10/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class AntiBotLang {

    AntiBot antiBot;
    FileConfiguration configuration;

    public AntiBotLang(AntiBot antiBot) {
        this.antiBot = antiBot;
        configuration = antiBot.getAntiBotLangFiles().get();
    }

    /*

    CAPTCHA MESSAGE

     */

    public void setCaptchaMessage(ArrayList<String> list) {
        List<String> captchaMessage = configuration.getStringList("Captcha.message");
        if (captchaMessage != null) {
            if (!captchaMessage.isEmpty()) {
                captchaMessage.clear();
            }
            for (int i = 0; i < list.size(); i++) {
                captchaMessage.add(list.get(i).toString());
            }
            antiBot.getAntiBotLangFiles().save();

        }
    }

    public List<String> getCaptchaMessage() {
        List<String> captchaMessage = configuration.getStringList("Captcha.message");
        if (captchaMessage != null) {
            return captchaMessage;
        }
        return null;
    }

    public void clearCaptchaMessage() {
        List<String> captchaMessage = configuration.getStringList("Captcha.message");
        if (captchaMessage != null) {
            captchaMessage.clear();
        }
    }

    /*

    VERIFY MESSAGE

     */

    public void setVerifyMessage(ArrayList<String> list) {
        List<String> verifyMessage = configuration.getStringList("Captcha.verify");
        if (verifyMessage != null) {
            if (!verifyMessage.isEmpty()) {
                verifyMessage.clear();
            }
            for (int i = 0; i < list.size(); i++) {
                verifyMessage.add(list.get(i).toString());
            }
            antiBot.getAntiBotLangFiles().save();

        }
    }

    public List<String> getVerifyMessage() {
        List<String> verifyMessage = configuration.getStringList("Captcha.verify");
        if (verifyMessage != null) {
            return verifyMessage;
        }
        return null;
    }

    public void clearVerifyMessage() {
        List<String> verifyMessage = configuration.getStringList("Captcha.verify");
        if (verifyMessage != null) {
            verifyMessage.clear();
        }
    }

    /*

    KICK MESSAGE

     */

    public void setKickMessage(String text) {
        configuration.set("Captcha.kickMessage", text);
        antiBot.getAntiBotLangFiles().save();
    }

    public String getKickMessage() {
        return configuration.getString("Captcha.kickMessage");
    }

    public void clearKickMessage() {
        configuration.set("Captcha.kickMessage", "");
        antiBot.getAntiBotLangFiles().save();
    }



}
