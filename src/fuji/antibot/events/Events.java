package fuji.antibot.events;

import fuji.antibot.antibot.Captcha;
import fuji.antibot.antibot.CaptchaTimer;
import fuji.antibot.commands.VerifyCommand;
import fuji.antibot.main.AntiBot;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Events implements Listener {

    private HashMap<UUID, Integer> chestCount = new HashMap<>();
    private HashMap<UUID, Integer> signCount = new HashMap<>();
    private HashMap<UUID, Integer> teleportCount = new HashMap<>();

    private HashMap<UUID, Long> oldMessage = new HashMap<>();
    private HashMap<UUID, Long> newMessage = new HashMap<>();

    @EventHandler
    public void onChestOpen(InventoryOpenEvent e) {
        final Player player = (Player) e.getPlayer();
        InventoryHolder ih = e.getInventory().getHolder();
        if (AntiBot.getAntiBotSettingsFiles().get().getBoolean("Triggers.chest-access.enabled")) {
            if (ih instanceof Chest || ih instanceof DoubleChest) {
                if (!VerifyCommand.hasTask(player.getUniqueId())) {
                    if (!chestCount.containsKey(player.getUniqueId())) {
                        chestCount.put(player.getUniqueId(), 1);
                    } else {
                        int i = chestCount.get(player.getUniqueId());
                        i++;
                        chestCount.remove(player.getUniqueId());
                        chestCount.put(player.getUniqueId(), i);
                        if (i >= AntiBot.getAntiBotSettingsFiles().get().getInt("Triggers.chest-access.count")) {
                            chestCount.remove(player.getUniqueId());
                            createCaptcha(player, "chest-access", AntiBot.getAntiBotSettingsFiles().get().getInt("Captcha.time"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (AntiBot.getAntiBotSettingsFiles().get().getBoolean("Triggers.sign-interact.enabled")) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() != null) {
                    if (e.getClickedBlock().getType().equals(Material.ACACIA_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.ACACIA_WALL_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.BIRCH_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.BIRCH_WALL_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.DARK_OAK_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.DARK_OAK_WALL_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.JUNGLE_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.JUNGLE_WALL_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.OAK_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.SPRUCE_SIGN) ||
                            e.getClickedBlock().getType().equals(Material.SPRUCE_WALL_SIGN)) {
                        Sign sign = (Sign) e.getClickedBlock().getState();
                        String[] lines = {sign.getLine(0), sign.getLine(1), sign.getLine(2), sign.getLine(3)};
                        if (lines[0].contains("[Buy]") || lines[0].contains("[Sell]") || lines[0].contains("§1[Buy]") || lines[0].equals("§1[Sell]")) {
                            if (!VerifyCommand.hasTask(player.getUniqueId())) {
                                if (!signCount.containsKey(player.getUniqueId())) {
                                    signCount.put(player.getUniqueId(), 1);
                                } else {
                                    int i = signCount.get(player.getUniqueId());
                                    i++;
                                    signCount.remove(player.getUniqueId());
                                    signCount.put(player.getUniqueId(), i);
                                    if (i >= AntiBot.getAntiBotSettingsFiles().get().getInt("Triggers.sign-interact.count")) {
                                        signCount.remove(player.getUniqueId());
                                        createCaptcha(player, "sign-interact", AntiBot.getAntiBotSettingsFiles().get().getInt("Captcha.time"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        final Player player = e.getPlayer();
        if (AntiBot.getAntiBotSettingsFiles().get().getBoolean("Triggers.teleport.enabled")) {
            if (!VerifyCommand.hasTask(player.getUniqueId())) {
                if (!teleportCount.containsKey(player.getUniqueId())) {
                    teleportCount.put(player.getUniqueId(), 1);
                } else {
                    int i = teleportCount.get(player.getUniqueId());
                    i++;
                    teleportCount.remove(player.getUniqueId());
                    teleportCount.put(player.getUniqueId(), i);
                    if (i >= AntiBot.getAntiBotSettingsFiles().get().getInt("Triggers.teleport.count")) {
                        teleportCount.remove(player.getUniqueId());
                        createCaptcha(player, "teleport", AntiBot.getAntiBotSettingsFiles().get().getInt("Captcha.time"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        if (AntiBot.getAntiBotSettingsFiles().get().getBoolean("Triggers.chat.enabled")) {
            if (!VerifyCommand.hasTask(player.getUniqueId())) {
                if (oldMessage != null && newMessage != null) {
                    Calendar firstMessage;
                    Calendar secondMessage;

                    if (!newMessage.containsKey(player.getUniqueId())) {
                        firstMessage = Calendar.getInstance();
                        newMessage.put(player.getUniqueId(), firstMessage.getTimeInMillis());
                    } else {
                        if (newMessage.containsKey(player.getUniqueId())) {
                            oldMessage.put(player.getUniqueId(), newMessage.get(player.getUniqueId()));

                            secondMessage = Calendar.getInstance();
                            newMessage.put(player.getUniqueId(), secondMessage.getTimeInMillis());
                        }
                    }
                    if (oldMessage.containsKey(player.getUniqueId())) {
                        if (newMessage.containsKey(player.getUniqueId())) {
                            long time = newMessage.get(player.getUniqueId()) - oldMessage.get(player.getUniqueId());
                            if (AntiBot.getAntiBotSettingsFiles().get().get("Triggers.chat.minMessageDelay") != null) {
                                if (time / 1000.00 < AntiBot.getAntiBotSettingsFiles().get().getDouble("Triggers.chat.minMessageDelay")) {
                                    createCaptcha(player, "chat-spam", AntiBot.getAntiBotSettingsFiles().get().getInt("Captcha.time"));
                                }
                            }
                        }
                    }
                }
            }
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
