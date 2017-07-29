package minigame.kings.utilities;

import minigame.kings.Main;
import minigame.kings.kingsarena.Arena;
import minigame.kings.kingsarena.kings.armour.KingsGear;
import minigame.kings.kingsarena.kings.EvilWither;
import minigame.kings.kingsarena.kings.SlimeKing;
import minigame.kings.gametimers.LobbyTimer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Utilities {

    private static int b;

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static String formatTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String disMinu = (minutes < 10 ? "0" : "") + minutes;
        String disSec = (seconds < 10 ? "0" : "") + seconds;
        return disMinu + ":" + disSec;
    }

    public static void spawnRandomKings(Player spawner) {

        Random random = new Random();

        int i = 1 + random.nextInt(1);

        switch (i) {
            case 1:
                EvilWither.spawn(spawner, Arena.playersIngame.size());
                Arena.king = "&4&lKingWither";
                return;
            case 2:
                SlimeKing.spawn(spawner, Arena.playersIngame.size());
                Arena.king = "&6&lKing Slime";
                return;
        }

        EvilWither.spawn(spawner, Arena.playersIngame.size());
        Arena.king = "&4&lKingWither";
    }

    private static void setTitleValue(int delay, final Player player, final String title) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                sendTitle(player, title, "", 1);
            }
        }, delay);
    }

    public static void sendPlayerToGame(Player sender) {

        sender.setHealth(20);
        sender.setSaturation(20);
        sender.setFoodLevel(20);

        sender.teleport(Arena.arenaLocation);
        Arena.playersIngame.add(sender);
        Arena.playersInLobby.add(sender);

        Arena.gameStarted = false;
        Arena.gameover = false;

        for (ItemStack item : sender.getInventory()) {
            if (item != null && !item.getType().equals(Material.AIR)) {
                sender.getInventory().removeItem(item);
            }
        }

        for (Player player : Arena.playersInLobby) {
            Main.getInstance().sendMessage("&c" + sender.getDisplayName() + " &ehas joined the game! &a#" + Arena.playersInLobby.size(), player);
        }

        sender.setGameMode(GameMode.ADVENTURE);

        KingsGear.giveGear(sender);

        sender.getInventory().setItem(0, KingsGear.kingssword());
        sender.getInventory().setItem(9, new ItemStack(Material.ARROW));

        for (int i = 2; i < 6; i++) {
            sender.getInventory().setItem(i, new ItemStack(Material.POTION, 1, (short) 16421));
        }

        LobbyTimer timer = new LobbyTimer(sender);
        timer.setScoreboard();

        setTitleValue(2, sender, "&6&l><");
        setTitleValue(4, sender, "&6&l>  <");
        setTitleValue(6, sender, "&6&l>  &e&lK &6&l<");
        setTitleValue(8, sender, "&6&l>  &e&lKi &6&l<");
        setTitleValue(10, sender, "&6&l>  &e&lKin &6&l<");
        setTitleValue(12, sender, "&6&l>  &e&lKing &6&l<");
        setTitleValue(14, sender, "&6&l>  &e&lKings &6&l<");
        setTitleValue(18, sender, "&6&l>>  &e&lKings &6&l<<");
    }

    public static String getOrbs(int number) {
        if (number < 2 && number > 0) {
            return Utilities.color("&c●&7●●●●●●●●●");
        } else if (number < 3 && number > 1) {
            return Utilities.color("&c●●&7●●●●●●●●");
        } else if (number < 4 && number > 2) {
            return Utilities.color("&c●●●&7●●●●●●●");
        } else if (number < 5 && number > 3) {
            return Utilities.color("&c●●●●&7●●●●●●");
        } else if (number < 6 && number > 4) {
            return Utilities.color("&c●●●●●&7●●●●●");
        } else if (number < 7 && number > 5) {
            return Utilities.color("&c●●●●●●&7●●●●");
        } else if (number < 8 && number > 6) {
            return Utilities.color("&c●●●●●●●&7●●●");
        } else if (number < 9 && number > 7) {
            return Utilities.color("&c●●●●●●●●&7●●");
        } else if (number < 10 && number > 8) {
            return Utilities.color("&c●●●●●●●●●&7●");
        } else if (number < 11 && number > 9) {
            return Utilities.color("&c●●●●●●●●●●");
        }
        return "";
    }

    public static void sendHotbarTitle(Player player, String message) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message.replaceAll("&", "§") + "\"}");
        PacketPlayOutChat chat = new PacketPlayOutChat(chatTitle, (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
    }

    public static Entity getLookedAtEntity(final Player player) {

        BlockIterator iterator = new BlockIterator(player.getWorld(), player
                .getLocation().toVector(), player.getEyeLocation()
                .getDirection(), 0, 100);
        while (iterator.hasNext()) {
            Block item = iterator.next();
            for (Entity entity : player.getNearbyEntities(100, 100, 100)) {
                int acc = 2;
                for (int x = -acc; x < acc; x++) {
                    for (int z = -acc; z < acc; z++) {
                        for (int y = -acc; y < acc; y++) {
                            if (entity.getLocation().getBlock()
                                    .getRelative(x, y, z).equals(item)) {
                                return entity;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void sendTitle(Player player, String message, String submessage, int duration) {

        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message.replaceAll("&", "§") + "\"}");
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(0, (duration * 20), 5);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);

        IChatBaseComponent chatTitle2 = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + submessage.replaceAll("&", "§") + "\"}");
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatTitle2);
        PacketPlayOutTitle length2 = new PacketPlayOutTitle(0, (duration * 20), 5);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length2);

    }

    public static void bodyParts(final Entity entity, final String particle, final Boolean color) {
        b = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            float step = 0.0F;

            public void run() {

                Location loc = entity.getLocation();

                if (color.booleanValue()) {
                    ParticleEffect.valueOf(particle).display(0.8F, 0.8F, 0.8F, 1F, 4, loc, 10.0D);
                } else {
                    ParticleEffect.valueOf(particle).display(0.8F, 0.8F, 0.8F, 0F, 4, loc, 10.0D);
                }

                ++this.step;
            }
        }, 0, 5).getTaskId();
    }

    public static void stopParts() {
        Bukkit.getServer().getScheduler().cancelTask(b);
    }

}