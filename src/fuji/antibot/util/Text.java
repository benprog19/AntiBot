package fuji.antibot.util;

import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/12/2017.
 * Credit to: 567legodude.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */

public class Text {

    private static final String REGEX = "(?:" + ChatColor.COLOR_CHAR + "[A-Fa-fK-Ok-oRr0-9])+[^" + ChatColor.COLOR_CHAR + "]*(?:(?!" + ChatColor.COLOR_CHAR + "[A-Fa-fK-Ok-oRr0-9])[^" + ChatColor.COLOR_CHAR + "]*)*|[^" + ChatColor.COLOR_CHAR + "]*(?:" + ChatColor.COLOR_CHAR + "(?![A-Fa-fK-Ok-oRr0-9])[^" + ChatColor.COLOR_CHAR + "]*)*";

    private BaseComponent[] out;
    private List<TextPart> list = new ArrayList<>();

    public Text(String text) {
        int index = 0;
        Matcher m = BracketPart.PATTERN.matcher(text);
        while (m.find()) {
            int s = m.start();
            if (s > index) {
                addParts(text.substring(index, s));
            }
            List<TextPart> l = BracketPart.toTextParts(m.group());
            if (l != null) list.addAll(l);
            index = m.end();
        }
        if (index < text.length()) {
            addParts(text.substring(index));
        }
    }

    public Text(Text other) {
        list.addAll(other.list);
    }

    @Override
    public String toString() {
        if (out == null) return "";
        StringBuilder builder = new StringBuilder();
        for (BaseComponent component : out) {
            ClickEvent clickEvent = component.getClickEvent();
            HoverEvent hoverEvent = component.getHoverEvent();
            if (clickEvent != null) {
                builder.append("{");
                switch (clickEvent.getAction()) {
                    case OPEN_FILE:
                        builder.append("file");
                        break;
                    case OPEN_URL:
                        builder.append("url");
                        break;
                    case RUN_COMMAND:
                        builder.append("cmd");
                        break;
                    case SUGGEST_COMMAND:
                        builder.append("suggest");
                        break;
                    default:
                        break;
                }
                builder.append("=").append(clickEvent.getValue()).append("}");
            }
            if (hoverEvent != null) {
                builder.append("{hover=").append(Stream.of(hoverEvent.getValue()).map(component1 -> component1.toLegacyText()).collect(Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString))).append("}");
            }
            builder.append(component.toLegacyText());
            if (clickEvent != null || hoverEvent != null) builder.append("{/}");
        }
        return builder.toString();
    }

    public static Text of(String text) {
        return new Text(text);
    }

    public static BaseComponent[] parse(String input) {
        return Text.of(input).build();
    }

    private void addParts(String text) {
        //getParts(text)
        list.addAll(getParts(text));
    }

    private static List<TextPart> getParts(String input) {
        List<TextPart> l = new ArrayList<>();
        int index = 0;
        Matcher m = Pattern.compile(REGEX).matcher(input);
        while (m.find()) {
            if (!m.group().isEmpty()) {
                String group = input.substring(index, m.start()) + m.group();
                if (TextUtils.none(group).isEmpty()) continue;
                l.add(new TextPart(group));
                index = m.end();
            }
        }
        if (index < input.length()) l.add(new TextPart(input.substring(index)));
        return l;
    }

    private static BaseComponent[] build(List<TextPart> input) {
        ComponentBuilder c = null;
        for (TextPart part : input) {
            if (part == null) continue;
            TextComponent com = part.toComponent();
            if (c == null) c = new ComponentBuilder(com.getText());
            else c.append(com.getText(), ComponentBuilder.FormatRetention.NONE);
            c.color(com.getColor());
            c.bold(com.isBold());
            c.italic(com.isItalic());
            c.underlined(com.isUnderlined());
            c.obfuscated(com.isObfuscated());
            c.strikethrough(com.isStrikethrough());
            if (com.getClickEvent() != null) c.event(com.getClickEvent());
            if (com.getHoverEvent() != null) c.event(com.getHoverEvent());
        }
        if (c != null) return c.create();
        return null;
    }

    public BaseComponent[] build() {
        BaseComponent[] tout = build(list);
        if (tout == null) return null;
        out = tout;
        return out;
    }

    public void send(Player player) {
        if (out == null) build();
        player.spigot().sendMessage(out);
    }

    public void send(Collection<Player> coll) {
        for (Player player : coll) {
            send(player);
        }
    }

    public List<String> getParts() {
        List<String> l = new ArrayList<>();
        for (TextPart part : list) {
            l.add(part.getRaw());
        }
        return l;
    }

    private static class TextPart {
        private final String text;
        private ClickEvent click = null;
        private HoverEvent hover = null;

        TextPart(String t) {
            text = t;
        }

        String getRaw() {
            return text;
        }

        void setClickEvent(ClickEvent event) {
            click = event;
        }

        void setHoverEvent(HoverEvent event) {
            hover = event;
        }

        TextComponent toComponent() {
            List<ChatColor> colors = TextUtils.getColors(text);
            TextComponent c = new TextComponent(TextUtils.none(text));
            for (ChatColor color : colors) {
                if (color.isColor()) c.setColor(TextUtils.convert(color));
                else {
                    switch (color) {
                        case BOLD:
                            c.setBold(true);
                            break;
                        case ITALIC:
                            c.setItalic(true);
                            break;
                        case UNDERLINE:
                            c.setUnderlined(true);
                            break;
                        case MAGIC:
                            c.setObfuscated(true);
                            break;
                        case STRIKETHROUGH:
                            c.setStrikethrough(true);
                            break;
                        case RESET:
                            c.setColor(net.md_5.bungee.api.ChatColor.WHITE);
                            c.setBold(false);
                            c.setItalic(false);
                            c.setUnderlined(false);
                            c.setObfuscated(false);
                            c.setStrikethrough(false);
                            break;
                        default:
                            break;
                    }
                }
            }
            if (click != null) {
                c.setClickEvent(click);
            }
            if (hover != null) {
                c.setHoverEvent(hover);
            }
            return c;
        }
    }

    private static class BracketPart {
        static final Pattern PATTERN = Pattern.compile("\\{(.+?)=(.+?)}(.+?)\\{/}");
        static final Pattern BRACKET = Pattern.compile("\\{(.+?)=(.+?)}");

        static List<TextPart> toTextParts(String bracket) {
            if (!bracket.matches(PATTERN.pattern())) return null;
            List<TextPart> l = new ArrayList<>();
            String text = bracket.replaceAll("(?:\\{.+?=.+?})+(.+?)\\{/}", "$1");
            if (text.isEmpty()) return null;
            l.addAll(getParts(text));
            Matcher m = BRACKET.matcher(bracket);
            while (m.find()) {
                setEvents(l, m.group(1), m.group(2));
            }
            return l;
        }

        static void setEvents(List<TextPart> parts, String type, String data) {
            ClickEvent click = null;
            HoverEvent hover = null;
            if (type.equalsIgnoreCase("url")) {
                click = new ClickEvent(ClickEvent.Action.OPEN_URL, data);
            }
            else if (type.equalsIgnoreCase("file")) {
                click = new ClickEvent(ClickEvent.Action.OPEN_FILE, data);
            }
            else if (type.equalsIgnoreCase("cmd")) {
                click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, data);
            }
            else if (type.equalsIgnoreCase("suggest")) {
                click = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, data);
            }
            else if (type.equalsIgnoreCase("hover")) {
                hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, build(getParts(data)));
            }
            else return;
            for (TextPart part : parts) {
                if (click != null) part.setClickEvent(click);
                if (hover != null) part.setHoverEvent(hover);
            }
        }
    }

    private static class TextUtils {
        private static List<ChatColor> getColors(String input) {
            input = ChatColor.translateAlternateColorCodes('&', input);
            input = ChatColor.getLastColors(input);
            List<ChatColor> out = new ArrayList<>();
            Matcher m = Pattern.compile(ChatColor.COLOR_CHAR + "([A-Fa-fK-Ok-oRr0-9])").matcher(input);
            while (m.find()) {
                if (!m.group(1).isEmpty()) {
                    out.add(ChatColor.getByChar(m.group(1)));
                }
            }
            return out;
        }

        private static String color(String i) {
            return ChatColor.translateAlternateColorCodes('&', i);
        }

        private static String none(String i) {
            return ChatColor.stripColor(color(i));
        }

        private static net.md_5.bungee.api.ChatColor convert(ChatColor color) {
            return net.md_5.bungee.api.ChatColor.getByChar(color.getChar());
        }
    }

}
