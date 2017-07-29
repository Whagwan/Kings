package minigame.kings.kingsarena.kings;

import minigame.kings.Main;
import minigame.kings.kingsarena.Arena;
import minigame.kings.kingsarena.kings.armour.KingsGear;
import minigame.kings.utilities.MathUtils;
import minigame.kings.utilities.ParticleEffect;
import minigame.kings.utilities.Utilities;
import org.bukkit.*;
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


public class EvilWither implements Listener {

    private static boolean web;
    private static boolean slam;

    public static void spawn(Player spawner, int players) {

        Arena.attackers.add(spawner);
        Arena.health = 400 * players;
        Arena.maxHealth = 400 * players;

        Arena.damage.put(spawner, 0);
        web = true;

        Arena.king_entity = spawner.getWorld().spawnEntity(Arena.arenaLocation, EntityType.WITHER);

        Arena.phase2 = false;

        init();
    }

    protected static void init() {

        Utilities.bodyParts(Arena.king_entity, "FLAME", false);

        for (Player player : Arena.playersIngame) {
            Main.getInstance().sendMessage("&e&m-----------------------------------", player);
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("  &6The &4&lEvilWither&6 has been spawned!", player);
            Main.getInstance().sendMessage("", player);
            Main.getInstance().sendMessage("&e&m-----------------------------------", player);
            player.getInventory().addItem(KingsGear.bow());
        }

        Arena.king_entity.getWorld().strikeLightningEffect(Arena.king_entity.getLocation());

        Arena.king_entity.setCustomName(Utilities.color("&4&lEvilWither &a" + Arena.health + " &d❤"));
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
                            Main.getInstance().sendMessage("&eThe &4&lEvilWither&e has spawned his MiniWithers!", player);
                            Main.getInstance().sendMessage("", player);
                            Main.getInstance().sendMessage("&eTIP: &a&oHis MiniWithers will heal her - Kill them before they heal too much!", player);
                        }

                    }

                    if (Arena.spawnedEntities.size() < 10) {
                        for (int i = 1; i < 8; i++) {
                            Skeleton skeleton = (Skeleton) Arena.king_entity.getWorld().spawnEntity
                                    (Arena.king_entity.getLocation(), EntityType.WITHER);
                            skeleton.setCustomName(Utilities.color("&cMiniWither"));
                            skeleton.setCustomNameVisible(true);
                            skeleton.setHealth(2);

                            Random random1 = new Random();

                            skeleton.setTarget(Arena.playersIngame.get(random1.nextInt(Arena.playersIngame.size())));
                            Arena.spawnedEntities.add(skeleton);
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
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            Main.getInstance().sendMessage("&eThe &4&lEvilWither&e has trapped you in his web!", player);
                            player.playSound(player.getLocation(), Sound.HURT_FLESH, 1, 1);
                            for (final Block block : Utilities.getNearbyBlocks(player.getLocation(), 1)) {
                                if (MathUtils.probability(50)) {
                                    if (block.getType().equals(Material.AIR)) {
                                        block.setType(Material.WEB);

                                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                block.setType(Material.AIR);
                                            }
                                        }, 80);
                                    }
                                }
                            }
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack3() {

        Random random = new Random();

        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            Main.getInstance().sendMessage("&eThe &4&lEvilWither&e flings you up into the air!", player);
                            player.playSound(player.getLocation(), Sound.HURT_FLESH, 1, 1);
                            player.setVelocity(new Vector(0, 1.2, 0));
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack4() {

        Random random = new Random();
        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            if (MathUtils.probability(75)) {
                                Main.getInstance().sendMessage("&4&lThe EvilWither&e poisoned you!", player);
                                player.playSound(player.getLocation(), Sound.HURT_FLESH, 1, 1);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                            }
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack5() {

        Random random = new Random();
        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            if (MathUtils.probability(75)) {
                                Main.getInstance().sendMessage("&4&lThe EvilWither&e blinded you with her webs!", player);
                                player.playSound(player.getLocation(), Sound.HURT_FLESH, 1, 1);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 3));
                                ((Wither) Arena.king_entity).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                            }
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack6() {

        Random random = new Random();
        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            if (MathUtils.probability(75)) {
                                Main.getInstance().sendMessage("&4&lThe EvilWither&e blinded you with her webs!", player);
                                player.playSound(player.getLocation(), Sound.HURT_FLESH, 1, 1);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 3));
                                ((Wither) Arena.king_entity).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                            }
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack7() {

        Random random = new Random();
        int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getLocation().distance(Arena.king_entity.getLocation()) < 15
                                && !Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            if (MathUtils.probability(75)) {
                                Main.getInstance().sendMessage("&4&lThe EvilWither's&e rage sped her up!", player);
                                player.playSound(player.getLocation(), Sound.HURT_FLESH, 1, 1);
                                ((Wither) Arena.king_entity).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 4));
                            }
                        }
                    }
                    selectAttack();
                }
            }
        }, i * 20);
    }

    protected static void attack8() {

        Random random = new Random();
        final int i = min + random.nextInt(max);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
                if (!Arena.king_entity.isDead()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getLocation().distance(Arena.king_entity.getLocation()) < 15
                                && !Arena.spectators.contains(player)
                                && Arena.playersIngame.contains(player)) {
                            Main.getInstance().sendMessage("&cThe &4&lEvilWither&c is charging her slam attack! &c&oGet back!", player);
                        }
                    }

                    int delay = (i-5) * 20;

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {

                        @Override
                        public void run() {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getLocation().distance(Arena.king_entity.getLocation()) < 6
                                        && !Arena.spectators.contains(player)
                                        && Arena.playersIngame.contains(player)) {
                                    Main.getInstance().sendMessage("&cThe &4&lEvilWither&c slammed you!", player);
                                    if (player.getHealth() - 10 <= 0) {
                                        player.setHealth(0);
                                    } else {
                                        player.setHealth(player.getHealth() - 10);
                                    }
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5));
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5));
                                    ParticleEffect.EXPLOSION_HUGE.display(0.8F, 0.8F, 0.8F, 1F, 1, Arena.king_entity.getLocation(), 10.0D);
                                } else {
                                    Main.getInstance().sendMessage("&cYou dodged the &4&lEvilWither's&c slam!", player);
                                }
                            }
                            slam = false;
                            selectAttack();
                        }
                    }, delay);
                }
            }
        }, i * 20);
    }

    private static void schedule(int delay, final int secondsleft) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Arena.playersIngame) {
                    player.sendMessage(Utilities.color("&c&l&o" + secondsleft + "..."));
                }
            }
        }, delay);
    }

    private static void selectAttack() {

        Random attack = new Random();

        final int x = 1 + attack.nextInt(8);

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
        } else if (x == 7) {
            attack7();
        } else if (x == 8) {
            if (!slam) {
                attack8();
                slam = true;
            }
        }

        if (web) {
            web = false;

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    web = true;
                }
            }, 35 * 20);
        }
    }

    //############################### EVENTS ##############################

    //HOW MUCH DAMAGE BROOD AND MINIWITHERS DO:
    @EventHandler
    void onPlayerDamage(EntityDamageByEntityEvent event) {

        if (Arena.king != null && Arena.king.contains("EvilWither")) {
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
                    event.setDamage(event.getDamage() * 25);
                } else {
                    event.setDamage(event.getDamage() * 15);
                }
            }

            if (Arena.spawnedEntities.contains(event.getDamager())) {
                event.setDamage(event.getDamage() * 3);
            }
        }
    }

    @EventHandler
    void onKill(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player
                && Arena.spawnedEntities.contains(event.getEntity())) {
            if (Arena.king.contains("EvilWither")) {
                Skeleton skeleton = (Skeleton) event.getEntity();
                if (skeleton.getHealth() - event.getDamage() <= 0) {
                    Arena.spawnedEntities.remove(skeleton);
                    event.getDamager().sendMessage(Utilities.color("&7&oKilling the MiniWither gave you some health back!"));
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
            if (Arena.king.contains("EvilWither")) {

                Arrow arrow = (Arrow) event.getDamager();

                if (arrow.getShooter() instanceof Player
                        && Arena.playersIngame.contains(arrow.getShooter())) {

                    Monster WITHER = (Monster) event.getEntity();
                    if (WITHER.getHealth() - event.getDamage() <= 0) {
                        Arena.spawnedEntities.remove(WITHER);
                        Player player = (Player) arrow.getShooter();
                        player.sendMessage(Utilities.color("&7&oKilling the MiniWither gave you some health back!"));
                        if (player.getHealth() + 1 <= player.getMaxHealth()) {
                            player.setHealth(player.getHealth() + 1);
                        }
                    }
                }
            }
        }
    }
}
