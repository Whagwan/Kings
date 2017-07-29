package minigame.kings.kingsarena.kings.armour;

import minigame.kings.utilities.Utilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class KingsGear {

    private static ItemStack helmet() {
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utilities.color("&4Kings Helmet"));
        item.setItemMeta(meta);
        item.addEnchantment(Enchantment.DURABILITY, 3);
        return item;
    }

    private static ItemStack chestplate() {
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utilities.color("&4Kings Chestplate"));
        item.setItemMeta(meta);
        item.addEnchantment(Enchantment.DURABILITY, 3);
        return item;
    }

    private static ItemStack leggings() {
        ItemStack item = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utilities.color("&4Kings Leggings"));
        item.setItemMeta(meta);
        item.addEnchantment(Enchantment.DURABILITY, 3);
        return item;
    }

    private static ItemStack boots() {
        ItemStack item = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utilities.color("&4Kings Boots"));
        item.setItemMeta(meta);
        item.addEnchantment(Enchantment.DURABILITY, 3);
        return item;
    }

    public static ItemStack kingssword() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Utilities.color("&6&lKing's Sword"));
        itemStack.setItemMeta(meta);
        itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        return itemStack;
    }



    public static ItemStack bow() {
        ItemStack itemStack = new ItemStack(Material.BOW);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Utilities.color("&4Kings Bow"));
        itemStack.setItemMeta(meta);
        itemStack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        itemStack.setDurability((short) 10);
        itemStack.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        return itemStack;
    }

    public static ItemStack slimeball() {
        ItemStack itemStack = new ItemStack(Material.SLIME_BALL);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Utilities.color("&dSlime ball"));
        itemStack.setItemMeta(meta);
        itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
        return itemStack;
    }

    public static void giveGear(Player player) {
        player.getInventory().setHelmet(helmet());
        player.getInventory().setChestplate(chestplate());
        player.getInventory().setLeggings(leggings());
        player.getInventory().setBoots(boots());
    }

}
