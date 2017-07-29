package minigame.kings.minigamecmds.cmds;

import minigame.kings.Main;
import minigame.kings.kingsarena.Arena;
import minigame.kings.minigamecmds.SubCommand;
import minigame.kings.permissions.PermissionSettings;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;


public class LeaveGameCMD extends SubCommand {

    public LeaveGameCMD() {
        super("Leave the game.", "", "/kings leave", "leave", "leavegame");
    }

    @Override
    protected void onPlayerExecute(Player sender, String... args) {
        if (!(args.length == 1)) {
            Main.getInstance().sendMessage(PermissionSettings.INVALID_ARGUMENTS, sender);
        }

        sender.teleport(sender.getWorld().getSpawnLocation());
        Arena.playersIngame.remove(sender);
        Arena.playersInLobby.remove(sender);
        Arena.spectators.remove(sender);

        sender.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        sender.setGameMode(GameMode.SURVIVAL);

        for (ItemStack itemStack : sender.getInventory()) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                sender.getInventory().removeItem(itemStack);
            }
        }

        sender.getInventory().setHelmet(null);
        sender.getInventory().setChestplate(null);
        sender.getInventory().setLeggings(null);
        sender.getInventory().setBoots(null);

    }

    @Override
    protected void onConsoleExecute(ConsoleCommandSender sender, String... args) {
        sender.sendMessage(PermissionSettings.INVALID_COMMAND_SENDER);
    }
}
