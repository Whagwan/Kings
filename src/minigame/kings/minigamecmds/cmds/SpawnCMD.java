package minigame.kings.minigamecmds.cmds;

import minigame.kings.Main;
import minigame.kings.kingsarena.kings.EvilWither;
import minigame.kings.minigamecmds.SubCommand;
import minigame.kings.permissions.PermissionSettings;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class SpawnCMD extends SubCommand {

    public SpawnCMD() {
        super("SpawnCMD in kings.", PermissionSettings.PERMISSION_SPAWN, "/kings evilwither", "wither", "s");
    }

    @Override
    protected void onPlayerExecute(Player sender, String... args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("evilwither")) {
                Main.getInstance().sendMessage("&eSpawning the EvilWither!", sender);
                EvilWither.spawn(sender, 1);
            }
        } else {
            Main.getInstance().sendMessage(PermissionSettings.INVALID_ARGUMENTS, sender);
        }
    }

    @Override
    protected void onConsoleExecute(ConsoleCommandSender sender, String... args) {
        sender.sendMessage("You cannot use this command in the console!");
    }
}