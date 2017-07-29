package minigame.kings.kingsarena;

import minigame.kings.Main;
import minigame.kings.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;


public class ArenaManager {

    private static void method(Player sender) {
        setTitleValue(2, sender, "&e&l><");
        setTitleValue(4, sender, "&e&l>  <");
        setTitleValue(6, sender, "&e&l>  &a&lW  &e&l<");
        setTitleValue(8, sender, "&e&l>  &a&lWi  &e&l<");
        setTitleValue(10, sender, "&e&l>  &a&lWin  &e&l<");
    }

    private static void setTitleValue(int delay, final Player player, final String title) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                Utilities.sendTitle(player, title, "", 1);
            }
        }, delay);
    }

    public static void death() {

        for (Player player : Arena.playersIngame) {

            method(player);

            Main.getInstance().sendMessage("&e&m----------------------------------------", player);
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("  &6The " + Arena.king + "&6 has been defeated!", player);
            Main.getInstance().sendMessage("    &c&oThe " + Arena.king + "&c&o claimed " + Arena.deaths.size() + "&c lives!", player);
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("  &eThe " + Arena.king + "&e was defeated by:", player);
            int i = 0;
            while (i < Arena.attackers.size()) {
                for (Player attacker : Arena.attackers) {
                    Main.getInstance().sendMessage("&6• &a" + attacker.getDisplayName(), player);
                }
                i++;
            }
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("&e&m----------------------------------------", player);

            Utilities.stopParts();

            Utilities.sendHotbarTitle(player, "");
        }

        Arena.gameover = true;
        Arena.attackers.clear();
        Arena.deaths.clear();
        Arena.king_entity.remove();
        Arena.damage.clear();
        for (Player online : Bukkit.getOnlinePlayers()) {
            for (Player specs : Arena.spectators) {
                online.showPlayer(specs);
                specs.setAllowFlight(false);
                specs.setGameMode(GameMode.SURVIVAL);
            }
        }

        for (Player player : Arena.playersIngame) {
            player.teleport(player.getWorld().getSpawnLocation());
        }

        Arena.spectators.clear();
        Arena.playersIngame.clear();

        for (Entity entity : Arena.spawnedEntities) {
            if (!entity.isDead()) {
                entity.remove();
            }
        }

        for (ArmorStand stand : Arena.power_ups.keySet()) {
            stand.remove();
        }

        Arena.power_ups.clear();

        Arena.phase2 = false;
        Arena.spawnedEntities.clear();
        Arena.tick = 0;
    }

    public static void lose() {

        for (Player player : Arena.playersIngame) {

            Main.getInstance().sendMessage("&e&m----------------------------------", player);
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("    &6The " + Arena.king + "&6 was victorious!", player);
            Main.getInstance().sendMessage("    &c&oThe " + Arena.king + "&c&o claimed " + Arena.deaths.size() + "&c lives!", player);
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("&e&m----------------------------------", player);

            Utilities.sendHotbarTitle(player, "");
        }

        Utilities.stopParts();

        Arena.gameover = true;
        Arena.attackers.clear();
        Arena.deaths.clear();
        Arena.king_entity.remove();
        Arena.damage.clear();

        for (Player online : Bukkit.getOnlinePlayers()) {
            for (Player specs : Arena.spectators) {
                online.showPlayer(specs);
                specs.setAllowFlight(false);
                specs.setGameMode(GameMode.SURVIVAL);
                specs.teleport(specs.getWorld().getSpawnLocation());
            }
        }

        Arena.spectators.clear();
        Arena.playersIngame.clear();

        for (Entity entity : Arena.spawnedEntities) {
            if (!entity.isDead()) {
                entity.remove();
            }
        }

        for (ArmorStand stand : Arena.power_ups.keySet()) {
            stand.remove();
        }

        Arena.power_ups.clear();

        Arena.phase2 = false;
        Arena.spawnedEntities.clear();
        Arena.tick = 0;
    }

    public static void spawnPowerup() {
        ArrayList<Block> valid_spawns = new ArrayList<>();

        if (Arena.power_ups.size() >= 5) {
            return;
        }

        for (Block block : Arena.game_arena.getBlocks()) {
            if (block.getY() == Arena.game_arena.getLowerY()) {
                valid_spawns.add(block);
            }
        }

        Random random = new Random();
        int i = random.nextInt(valid_spawns.size()-1);
        int RPU = random.nextInt(PowerUps.values().length);

        PowerUps[] list = PowerUps.values();

        PowerUps powerUps = list[RPU];

        final ArmorStand stand = valid_spawns.get(i).getWorld().spawn(valid_spawns.get(i).getLocation(), ArmorStand.class);
        stand.setHelmet(powerUps.getHelmet());
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCustomName(Utilities.color(powerUps.label + " Boost"));
        stand.setCustomNameVisible(true);

        Arena.power_ups.put(stand, powerUps);

        for (Player player : Arena.playersIngame) {
            player.sendMessage(Utilities.color("&6&l>> &eA " + powerUps.label() + " &epower-up has spawned! &6&l<<"));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                Arena.power_ups.remove(stand);
                stand.remove();
            }
        }, 20*15);

    }

    public static void showTitleOnDamage(Player player) {
        int number = (int) (((double)Arena.health/Arena.maxHealth)*10);
        Utilities.sendTitle(player, Utilities.getOrbs(number), "&a&l" + Arena.health, 1);
    }

}