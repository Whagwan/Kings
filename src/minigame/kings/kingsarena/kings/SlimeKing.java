package minigame.kings.kingsarena.kings;

import minigame.kings.Main;
import minigame.kings.kingsarena.Arena;
import minigame.kings.kingsarena.kings.armour.KingsGear;
import minigame.kings.utilities.MathUtils;
import minigame.kings.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;


public class SlimeKing implements Listener {

    private static boolean web;

    public static void spawn(Player spawner, int players) {

        Arena.attackers.add(spawner);
        Arena.health = 400 * players;
        Arena.maxHealth = 400 * players;

        Arena.damage.put(spawner, 0);
        web = true;

        Arena.king_entity = spawner.getWorld().spawnEntity(Arena.arenaLocation, EntityType.SLIME);
        ((Slime) Arena.king_entity).setSize(15);
        ((Slime) Arena.king_entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1));

        Arena.phase2 = false;

        init();
    }

    protected static void init() {

        for (Player player : Arena.playersIngame) {
            Main.getInstance().sendMessage("&e&m-----------------------------------", player);
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("  &6The &6&lKing Slime&6 has been spawned!", player);
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("&e&m-----------------------------------", player);
            player.getInventory().addItem(KingsGear.slimeball());
            player.getInventory().addItem(KingsGear.bow());
        }

        Arena.king_entity.getWorld().strikeLightningEffect(Arena.king_entity.getLocation());

        Arena.king_entity.setCustomName(Utilities.color("&6&lKing Slime &a" + Arena.health + " &d❤"));
        Arena.king_entity.setCustomNameVisible(true);

        selectAttack();
    }

    public static int min = 5;
    public static int max = 10;

    protected static void attack1() {

        final Random random = new Random();

        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {

                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            Main.getInstance().sendMessage("&eThe &6&lKing Slime&e has split off part of its body!", player);
                            Main.getInstance().sendMessage("", player);
                            Main.getInstance().sendMessage("&eTIP: &a&oThe King Slimes chunks heal him! Kill them before they heal too much!", player);
                        }
                    }

                    ((Slime) Arena.king_entity).setSize(13);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                        @Override
                        public void run() {
                            ((Slime) Arena.king_entity).setSize(15);
                        }
                    }, 20 * 5);

                    if (Arena.spawnedEntities.size() < 3) {
                        for (int i = 1; i < 6; i++) {
                            Slime minion = (Slime) Arena.king_entity.getWorld().spawnEntity
                                    (Arena.king_entity.getLocation(), EntityType.SLIME);
                            minion.setCustomName(Utilities.color("&aKing Slime Chunk"));
                            minion.setSize(2);
                            minion.setCustomNameVisible(true);

                            Arena.spawnedEntities.add(minion);
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack2() {

        Random random = new Random();

        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Main.getInstance().sendMessage("&eThe &6&lKing Slime&e has changed state! He now takes no bow damage!", player);
                    }

                    Slime slime = (Slime) Arena.king_entity;

                    Arena.king_entity = slime.getWorld().spawnEntity(slime.getLocation(), EntityType.MAGMA_CUBE);
                    slime.remove();
                    ((MagmaCube) Arena.king_entity).setSize(15);
                    ((MagmaCube) Arena.king_entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1));
                    Arena.king_entity.setCustomName(Utilities.color(Arena.king + " &a" + Arena.health + " &d❤"));
                    Arena.king_entity.setCustomNameVisible(true);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!Arena.king_entity.isDead()) {
                                Entity magmacube = Arena.king_entity;
                                Arena.king_entity = magmacube.getWorld().spawnEntity(magmacube.getLocation(), EntityType.SLIME);
                                magmacube.remove();
                                ((Slime) Arena.king_entity).setSize(15);
                                ((Slime) Arena.king_entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1));
                                Arena.king_entity.setCustomName(Utilities.color(Arena.king + " &a" + Arena.health + " &d❤"));
                                Arena.king_entity.setCustomNameVisible(true);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    Main.getInstance().sendMessage("&eThe &6&lKing Slime&e is now vulnerable to bows again!", player);
                                }
                            }
                        }
                    }, 20 * 10);

                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack3() {

        final Random random = new Random();

        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            Main.getInstance().sendMessage("&eThe &6&lKing Slime&e drops shrapnel from above!", player);

                            TNTPrimed primed = player.getWorld().spawn(player.getLocation().add(0, 10, 0), TNTPrimed.class);
                            primed.setFuseTicks(25);
                            Arena.explosives.add(primed);
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack4() {

        final Random random = new Random();

        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            if (MathUtils.probability(50)) {
                                Main.getInstance().sendMessage("&eThe &6&lKing Slime&e trapped you!", player);

                                player.setWalkSpeed(0);

                                final Block bottom = player.getWorld().getBlockAt(player.getLocation());
                                final Block top = player.getWorld().getBlockAt(player.getLocation().add(0, 1, 0));

                                if (bottom.getType().equals(Material.AIR)) {
                                    bottom.setType(Material.STATIONARY_LAVA);
                                }
                                if (top.getType().equals(Material.AIR)) {
                                    top.setType(Material.STATIONARY_LAVA);
                                }

                                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (bottom.getType().equals(Material.STATIONARY_LAVA)) {
                                            bottom.setType(Material.AIR);
                                        }
                                        if (top.getType().equals(Material.STATIONARY_LAVA)) {
                                            top.setType(Material.AIR);
                                        }
                                        player.setWalkSpeed(0.2F);
                                    }
                                }, 20 * 3);
                            }
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack5() {

        final Random random = new Random();

        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            Main.getInstance().sendMessage("&eThe &6&lKing Slime&e stopped the ground!", player);
                            if (MathUtils.probability(70)) {
                                Main.getInstance().sendMessage("&eThe &a&oYou were slowed!", player);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 2));
                            }
                            if (MathUtils.probability(50)) {
                                Main.getInstance().sendMessage("&eThe &c&l&oYou were crippled!", player);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*5, 1));
                            }
                        }
                    }
                    Arena.king_entity.setVelocity(new Vector(0, 0.1, 0));
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack6() {

        final Random random = new Random();

        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            Main.getInstance().sendMessage("&eThe &6&lKing Slime&e fired slime at you!", player);
                            final Slime projectile = Arena.king_entity.getWorld().spawn(Arena.king_entity.getLocation(), Slime.class);
                            projectile.setSize(4);

                            Vector dir = player.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize();
                            projectile.setVelocity(dir.multiply(2));

                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player.getHealth() - 5 <= 0) {
                                        player.setHealth(0);
                                    } else {
                                        player.setHealth(player.getHealth() -5);
                                    }
                                    projectile.remove();
                                }
                            }, 20);
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    private static void selectAttack() {

        Random attack = new Random();

        final int x = 1 + attack.nextInt(6);

        if (x == 1) {
            attack1();
        } else if (x == 2) {
            attack2();
        } else if (x == 3) {
            attack3();
        } else if (x == 4) {
            attack4();
        } else if (x == 5) {
            attack5();
        } else if (x == 6) {
            attack6();
        }
    }

    //############################### EVENTS ##############################

    //HOW MUCH DAMAGE KINGS AND MINIONS DO:
    @EventHandler
    void onPlayerDamage(EntityDamageByEntityEvent event) {

        if (Arena.king != null && Arena.king.contains("King Slime")) {
            if (!(event.getEntity() instanceof Player)
                    || event.getDamager() instanceof Player) {
                return;
            }

            Player player = (Player) event.getEntity();

            if (!Arena.playersIngame.contains(player)) {
                return;
            }

            if (event.getDamager().equals(Arena.king_entity)) {
                if (Arena.phase2) {
                    event.setDamage(event.getDamage() * 2);
                } else {
                    event.setDamage(event.getDamage());
                }
            }

            if (Arena.spawnedEntities.contains(event.getDamager())) {
                event.setDamage(event.getDamage() * 2);
            }
        }
    }

    @EventHandler
    void onKill(EntityDamageByEntityEvent event) {

        if (Arena.king == null
                || Arena.king.equalsIgnoreCase("")) {
            return;
        }

        if (Arena.king.contains("King Slime")) {
            if (event.getDamager() instanceof Player
                    && Arena.spawnedEntities.contains(event.getEntity())) {
                Slime slime = (Slime) event.getEntity();
                if (slime.getHealth() - event.getDamage() <= 0) {
                    Arena.spawnedEntities.remove(slime);
                    event.getDamager().sendMessage(Utilities.color("&7&oKilling the Minion gave you some health back!"));
                    Player player = (Player) event.getDamager();
                    if (player.getHealth() + 1 <= player.getMaxHealth()) {
                        player.setHealth(player.getHealth() + 1);
                    }
                }
            }
        }
    }


    @EventHandler
    void onKillArrow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            if (Arena.king != null && Arena.king.contains("King Slime")) {
                if (event.getDamager() instanceof Arrow) {

                    Arrow arrow = (Arrow) event.getDamager();

                    if (arrow.getShooter() instanceof Player
                            && Arena.playersIngame.contains(arrow.getShooter())) {

                        Slime slime = (Slime) event.getEntity();
                        if (slime.getHealth() - event.getDamage() <= 0) {
                            Arena.spawnedEntities.remove(slime);
                            Player player = (Player) arrow.getShooter();
                            player.sendMessage(Utilities.color("&7&oKilling the Minion gave you some health back!"));
                            if (player.getHealth() + 1 <= player.getMaxHealth()) {
                                player.setHealth(player.getHealth() + 1);
                            }
                        }
                    }
                }
            }
        }
    }
}
