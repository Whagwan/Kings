package minigame.kings.minigamecmds.cmds;

import minigame.kings.Main;
import minigame.kings.minigamecmds.SubCommand;
import minigame.kings.permissions.PermissionSettings;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class ReloadCMD extends SubCommand {

    public ReloadCMD() {
        super("Reloads plugin configuration file.", PermissionSettings.PERMISSION_RELOAD, Main.plugin_command_label + " reload", "reload", "rl");
    }

    protected void onPlayerExecute(Player sender, String... args) {
        if (args.length == 1) {
            try {
                Main.getInstance().sendMessage("&6Loading configuration file...", sender);
                Main.getInstance().reloadConfig();
                Main.getInstance().setup_arena();
                Main.getInstance().sendMessage("&aReloaded configuration file successfully!", sender);
            } catch (Exception ex) {
                ex.printStackTrace();
                Main.getInstance().sendMessage("&cFailed to reload configuration file!", sender);
            }
        } else {
            Main.getInstance().sendMessage(PermissionSettings.INVALID_ARGUMENTS, sender);
        }
    }

    protected void onConsoleExecute(ConsoleCommandSender sender, String... args) {
        if (args.length == 1) {
            try {
                Main.getInstance().log("&6Loading configuration file...");
                Main.getInstance().reloadConfig();
                Main.getInstance().log("&aReloaded configuration file successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                Main.getInstance().log("&cFailed to reload configuration file!");
            }
        } else {
            sender.sendMessage(PermissionSettings.INVALID_ARGUMENTS.replace("&", "§"));
        }
    }
}

