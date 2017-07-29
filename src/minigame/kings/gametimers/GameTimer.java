package minigame.kings.gametimers;

import minigame.kings.Main;
import minigame.kings.kingsarena.Arena;
import minigame.kings.kingsarena.ArenaManager;
import minigame.kings.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Random;

public class GameTimer {

    private Scoreboard scoreboard;
    private Player player;
    private Player p1;
    private Player p2;
    private Player p3;
    private boolean wait;

    public GameTimer(final Player player) {

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                Arena.power_up = true;
            }
        }, 20 * 10);

        if (Arena.playersInLobby.size() < 3) {
            for (Player target : Arena.damage.keySet()) {
                p1 = target;
                p2 = target;
                p3 = target;
            }
        } else if (Arena.playersInLobby.size() == 3) {
            Object[] players = Arena.damage.keySet().toArray();
            p1 = (Player) players[0];
            p2 = (Player) players[1];
            p3 = (Player) players[2];
        } else {
            int tick = 1;
            for (Player target : Arena.damage.keySet()) {
                if (tick < 4) {
                    switch (tick) {
                        case 1:
                            p1 = target;
                            break;
                        case 2:
                            p2 = target;
                            break;
                        case 3:
                            p3 = target;
                            break;
                    }
                    tick++;
                }
            }
        }

        this.player = player;
        this.scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("scoreboard2", "dummy");

        scoreboard.registerNewTeam("ingame").setPrefix(ChatColor.RED + "[KINGS] ");

        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            if (players.equals(player)) {
                continue;
            }
            players.getScoreboard().getTeam("ingame").addPlayer(player);
            Team team = scoreboard.getTeam(players.getScoreboard().getPlayerTeam(players).getName());
            team.addPlayer(players);
        }
        scoreboard.getTeam("ingame").addPlayer(player);

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Utilities.color("&e&lKings"));

        Score authorTitle = objective.getScore(Utilities.color("&cBy BadEntities"));
        authorTitle.setScore(12);
        Score break1 = objective.getScore(Utilities.color("  "));
        break1.setScore(11);
        Score KingTitle = objective.getScore(Utilities.color("&eKing: "));
        KingTitle.setScore(10);
        Score break2 = objective.getScore(Utilities.color("      "));
        break2.setScore(9);
        Score damageTitle = objective.getScore(Utilities.color("&6&nDamage:"));
        damageTitle.setScore(8);
        if (Arena.playersIngame.size() == 1) {
            Score r1 = objective.getScore(Utilities.color("&a❶: "));
            r1.setScore(7);
            Score timerTitle = objective.getScore(Utilities.color("&eElapsed Time: "));
            timerTitle.setScore(6);
            Score break4 = objective.getScore(Utilities.color("   "));
            break4.setScore(5);
            Score ipTitle = objective.getScore(Utilities.color("      &e&lPLAY.MCPZ.NET"));
            ipTitle.setScore(4);
        } else {
            Score r1 = objective.getScore(Utilities.color("&a❶: "));
            r1.setScore(7);
            Score r2 = objective.getScore(Utilities.color("&6❷: "));
            r2.setScore(6);
            Score r3 = objective.getScore(Utilities.color("&c❸: "));
            r3.setScore(5);
            Score break3 = objective.getScore(Utilities.color(" "));
            break3.setScore(4);
            Score timerTitle = objective.getScore(Utilities.color("&eElapsed Time: "));
            timerTitle.setScore(3);
            Score break4 = objective.getScore(Utilities.color("   "));
            break4.setScore(2);
            Score ipTitle = objective.getScore(Utilities.color("     &e&lPLAY.MCPZ.NET"));
            ipTitle.setScore(1);
        }

        Team timerValue = scoreboard.registerNewTeam("timer");
        timerValue.addEntry(Utilities.color("&eElapsed Time: "));
        timerValue.setPrefix("");
        timerValue.setSuffix(ChatColor.RED + "0");

        Team KingValue = scoreboard.registerNewTeam("King");
        KingValue.addEntry(Utilities.color("&eKing: "));
        KingValue.setPrefix("");
        KingValue.setSuffix(Utilities.color(Arena.king));

        Team p1value = scoreboard.registerNewTeam("p1");
        p1value.addEntry(Utilities.color("&a❶: "));
        p1value.setPrefix("");
        p1value.setSuffix(Utilities.color(""));

        if (Arena.playersIngame.size() != 1) {
            Team p2value = scoreboard.registerNewTeam("p2");
            p2value.addEntry(Utilities.color("&6❷: "));
            p2value.setPrefix("");
            p2value.setSuffix(Utilities.color(""));

            Team p3value = scoreboard.registerNewTeam("p3");
            p3value.addEntry(Utilities.color("&c❸: "));
            p3value.setPrefix("");
            p3value.setSuffix(Utilities.color(""));
        }

        if (Arena.playersIngame.size() == 1) {
            objective.getScore(Utilities.color("&eElapsed Time: ")).setScore(6);
            objective.getScore(Utilities.color("&a❶: ")).setScore(7);
        } else {
            objective.getScore(Utilities.color("&eElapsed Time: ")).setScore(3);
            objective.getScore(Utilities.color("&a❶: ")).setScore(7);
            objective.getScore(Utilities.color("&6❷: ")).setScore(6);
            objective.getScore(Utilities.color("&c❸: ")).setScore(5);
        }
        objective.getScore(Utilities.color("&eKing: ")).setScore(10);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (Arena.gameover) {
                    Utilities.sendHotbarTitle(player, "");
                    cancel();
                }

                if (Utilities.getLookedAtEntity(player) != null) {
                    if (Arena.king_entity != null
                            && Utilities.getLookedAtEntity(player) == Arena.king_entity) {
                        Utilities.sendHotbarTitle(player, "&bTarget: " + Arena.king + " &7| &eHealth: &a&l" + Arena.health);
                    } else if (!Arena.king_entity.isEmpty()
                            && Arena.spawnedEntities.contains(Utilities.getLookedAtEntity(player))) {
                        CaveSpider spider = (CaveSpider) Utilities.getLookedAtEntity(player);
                        assert spider != null;
                        Utilities.sendHotbarTitle(player, "&bTarget: &cMiniWither &7| &eHealth: &a&l"
                                + (int) spider.getHealth());
                    }
                } else {
                    if (!Arena.spectators.contains(player)) {
                        Utilities.sendHotbarTitle(player, "&bDamage Dealt: &c" + Arena.damage.get(player) + " &7| &eYour Health: &a&l"
                                + (int) player.getHealth());
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 20, 1);

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                if (scoreboard == null) {
                    cancel();
                    return;
                }

                if (!wait) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                        @Override
                        public void run() {
                            colorTitle(objective);
                        }
                    }, 20);
                    wait = true;
                }

                if (Arena.power_up) {
                    ArenaManager.spawnPowerup();
                    Arena.power_up = false;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                        @Override
                        public void run() {
                            Arena.power_up = true;
                        }
                    }, 20 * 25);
                }

                if (Arena.king.contains("EvilWither") || Arena.king.contains("King Slime")) {
                    if (Arena.spawnedEntities.size() != 0 || !Arena.spawnedEntities.isEmpty()) {
                        Random random = new Random();

                        int bound = Arena.spawnedEntities.size() + 5;

                        if (bound < 1) {
                            bound = 1;
                        }

                        int i = 1 + random.nextInt(bound);

                        if (Arena.health + i <= Arena.maxHealth) {
                            Arena.health += i;
                            player.sendMessage(Utilities.color(Arena.king + "&a&o +" + i + " health"));
                        }
                    }
                }

                if (Arena.gameover) {
                    player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                    cancel();
                }

                HashMap<Player, Integer> temp = Arena.damage;

                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        Player val = p1;
                        for (Player ingame : temp.keySet()) {
                            if (temp.size() != 0 && !temp.isEmpty()) {
                                if (Arena.damage.get(ingame) > Arena.damage.get(val)) {
                                    p1 = ingame;
                                    temp.remove(ingame);
                                }
                            } else {
                                p1 = null;
                            }
                        }
                    } else if (i == 1) {
                        Player val = p2;
                        for (Player ingame : temp.keySet()) {
                            if (temp.size() != 0 && !temp.isEmpty()) {
                                if (Arena.damage.get(ingame) > Arena.damage.get(val)) {
                                    p2 = ingame;
                                    temp.remove(ingame);
                                }
                            } else {
                                p2 = null;
                            }
                        }
                    } else if (i == 2) {
                        Player val = p3;
                        for (Player ingame : temp.keySet()) {
                            if (temp.size() != 0 && !temp.isEmpty()) {
                                if (Arena.damage.get(ingame) > Arena.damage.get(val)) {
                                    p3 = ingame;
                                    temp.remove(ingame);
                                }
                            } else {
                                p3 = null;
                            }
                        }
                    }
                }

                if (p1 != null) {
                    scoreboard.getTeam("p1").setSuffix(Utilities.color("&c" + p1.getName()));
                } else {
                    scoreboard.getTeam("p1").setSuffix("");
                }
                if (Arena.playersIngame.size() != 1) {
                    if (p2 != null) {
                        scoreboard.getTeam("p2").setSuffix(Utilities.color("&c" + p2.getName()));
                    } else {
                        scoreboard.getTeam("p2").setSuffix("");
                    }
                    if (p3 != null) {
                        scoreboard.getTeam("p3").setSuffix(Utilities.color("&c" + p3.getName()));
                    } else {
                        scoreboard.getTeam("p3").setSuffix("");
                    }
                }

                scoreboard.getTeam("timer").setSuffix(String.valueOf(ChatColor.RED) + Utilities.formatTime(timer));
                scoreboard.getTeam("King").setSuffix(String.valueOf(Utilities.color(Arena.king)));
                timer++;
            }

        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    private void colorTitle(Objective objective) {
        setTitleValue(2, objective, "&e&lKings");
        setTitleValue(4, objective, "&6&lK&e&lings");
        setTitleValue(6, objective, "&f&lK&6&li&e&lngs");
        setTitleValue(8, objective, "&f&lKi&6&ln&e&lgs");
        setTitleValue(10, objective, "&f&lKin&6&lg&e&ls");
        setTitleValue(14, objective, "&f&lKing&6&ls");
        setTitleValue(16, objective, "&f&lKings");
        setTitleValue(18, objective, "&e&lKings");
        setTitleValue(20, objective, "&f&lKings");
        setTitleValue(22, objective, "&e&lKings");
        setTitleValue(24, objective, "&f&lKings");
        setTitleValue(26, objective, "&e&lKings");

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                wait = false;
            }
        }, 126);
    }

    private void setTitleValue(int delay, final Objective objective, final String title) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                objective.setDisplayName(Utilities.color(title));
            }
        }, delay);
    }

    public void setScoreboard() {
        player.setScoreboard(scoreboard);
    }
}