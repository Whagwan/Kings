package minigame.kings.kingsarena;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public enum PowerUps {

    REGEN("&d&lRegen", new ItemStack(Material.REDSTONE_BLOCK), new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 2)),
    HEALTH("&b&lHealth", new ItemStack(Material.DIAMOND_BLOCK), null),
    HEALTHBOOST("&a&lExtra Health", new ItemStack(Material.EMERALD_BLOCK), new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 5, 2)),
    DAMAGE("&5&lDamage", new ItemStack(Material.TNT), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 2)),
    SPEED("&e&lSpeed", new ItemStack(Material.GOLD_BLOCK), new PotionEffect(PotionEffectType.SPEED, 20 * 5, 2)),
    UPGRADE("&6&lUpgrade", new ItemStack(Material.GLOWSTONE), null),
    POTIONS("&a&lPotions", new ItemStack(Material.SEA_LANTERN), null);

    String label;
    ItemStack identifier;
    PotionEffect effect;

    PowerUps(String label, ItemStack identifier, PotionEffect effect) {
        this.label = label;
        this.identifier = identifier;
        this.effect = effect;
    }

    public ItemStack getHelmet() {
        return identifier;
    }

    public String label() {
        return label;
    }

    public PotionEffect getEffect() {
        return effect;
    }

}
