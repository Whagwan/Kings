package minigame.kings.gametimers;

import minigame.kings.Main;
import minigame.kings.kingsarena.Arena;
import minigame.kings.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class LobbyTimer {

    private Scoreboard scoreboard;
    private Player player;
    private  boolean wait = false;

    public LobbyTimer() {
    }

    public LobbyTimer(final Player player) {

        this.player = player;
        this.scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");

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
        authorTitle.setScore(8);
        final Score break1 = objective.getScore(Utilities.color("  "));
        break1.setScore(7);
        Score timerTitle = objective.getScore(Utilities.color("&eStarting in: "));
        timerTitle.setScore(6);
        Score break2 = objective.getScore(Utilities.color("   "));
        break2.setScore(5);
        Score ipTitle = objective.getScore(Utilities.color(" &e&lPLAY.MCPZ.NET"));
        ipTitle.setScore(4);

        Team timerValue = scoreboard.registerNewTeam("timer");
        timerValue.addEntry(Utilities.color("&eStarting in: "));
        timerValue.setPrefix("");
        timerValue.setSuffix(ChatColor.RED + "10");

        objective.getScore(Utilities.color("&eStarting in: ")).setScore(6);

        new BukkitRunnable() {
            int timer = 10;

            @Override

            public void run() {

                if (!wait) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                        @Override
                        public void run() {
                            colorTitle(objective);
                        }
                    }, 20);
                    wait = true;
                }

                if (!Arena.playersIngame.isEmpty() || Arena.playersIngame.size() > 0) {
                    if (Arena.gameStarted || Arena.playersIngame.size() >= 3) {

                        if (timer == 10) {
                            Main.getInstance().sendMessage("&eThe game will begin in " + timer + " seconds!", player);
                        }

                        //Scoreboard scoreboard = player.getScoreboard();

                        scoreboard.getTeam("timer").setSuffix(String.valueOf(ChatColor.RED) + timer);

                        if (timer < 6) {
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                            if (!(timer < 4)) {
                                Utilities.sendTitle(player, "", "&eStarting in &4&l" + timer + " &c&lseconds!", 1);
                            } else {
                                switch (timer) {
                                    case 3:
                                        Utilities.sendTitle(player, "&e❸", "&eStarting in &4&l" + timer + " &c&lseconds!", 1);
                                        break;
                                    case 2:
                                        Utilities.sendTitle(player, "&6❷", "&eStarting in &4&l" + timer + " &c&lseconds!", 1);
                                        break;
                                    case 1:
                                        Utilities.sendTitle(player, "&c❶", "&eStarting in &4&l" + timer + " &c&lseconds!", 1);
                                        break;
                                }
                            }
                        }

                        timer--;

                        if (timer < 0) {
                            cancel();
                            timer = 30;

                            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

                            Arena.playersInLobby.remove(player);

                            if (Arena.tick == 0) {

                                player.getWorld().setTime(13000);
                                Utilities.spawnRandomKings(player);

                                Arena.gameover = false;

                                Arena.tick++;
                            }

                            setTitleValue(2, player, "&c&l><");
                            setTitleValue(4, player, "&c&l>  <");
                            setTitleValue(6, player, "&c&l>  &e&lF  &c&l<");
                            setTitleValue(8, player, "&c&l>  &e&lFi  &c&l<");
                            setTitleValue(10, player, "&c&l>  &e&lFig  &c&l<");
                            setTitleValue(12, player, "&c&l>  &e&lFigh  &c&l<");
                            setTitleValue(14, player, "&c&l>  &e&lFight &c&l<");
                            setTitleValue(16, player, "&c&l>  &e&lFight!  &c&l<");

                            GameTimer timer = new GameTimer(player);
                            timer.setScoreboard();

                        }
                    }
                }
            }

        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    private void colorTitle(Objective objective) {
        setTitleValue(2, objective, "&e&lKings");
        setTitleValue(4, objective, "&6&lK&e&lings");
        setTitleValue(6, objective, "&f&lK&6&li&e&lngs");
        setTitleValue(8, objective, "&f&lKi&6&ln&e&lgs");
        setTitleValue(10, objective, "&f&lKin&6&lg&e&ls");
        setTitleValue(12, objective, "&f&lKing&6&ls&e&l");
        setTitleValue(14, objective, "&f&lKing&6&ls");
        setTitleValue(16, objective, "&f&lKings");
        setTitleValue(18, objective, "&e&lKings");

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

    private static void setTitleValue(int delay, final Player player, final String title) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                Utilities.sendTitle(player, title, "", 1);
            }
        }, delay);
    }

    public void setScoreboard() {
        player.setScoreboard(scoreboard);
    }
}