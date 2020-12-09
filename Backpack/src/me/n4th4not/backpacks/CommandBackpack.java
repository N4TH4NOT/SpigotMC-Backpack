package me.n4th4not.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommandBackpack
    implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender var0, Command var1, String var2, String[] var3) {
        if (!(var0 instanceof Player))
            return false;
        if (var3.length == 0) {
            if (Listener.B.contains(((Player) var0).getUniqueId()) && Listener.a((Player) var0,2) == -1)
                return false;
            Main.openBackpack((HumanEntity) var0, (HumanEntity) var0);
        }
        else if (var3.length > 1) {
            Player var4 = Bukkit.getPlayer(var3[1]);
            if (var4 == null) var0.sendMessage(Main.getMessage("08").replace("%name%",var3[1]));
            else if (var3[0].equalsIgnoreCase("open")) Main.openBackpack((HumanEntity) var0,var4);
            else if (var0.hasPermission(Permission.LINKED_BACKPACK_EDIT_OTHERS.perm) && var3[0].equalsIgnoreCase("clear")) {
                if (Main.ins.a(var4.getUniqueId(), 0b110,
                        "Cleared by " + var0.getName() + " (" + ((Player) var0).getUniqueId() + ") on " + Utilities.Text.B.format(new Date()),
                        "Message: " + a(var3,2)))
                    var0.sendMessage(Utilities.Placeholder.a("09",var4));
                else var0.sendMessage(Main.getMessage("0A"));
            }
            else if (var0.hasPermission(Permission.LINKED_BACKPACK_SEE_OTHERS.perm) && var3[0].equalsIgnoreCase("backup")) {
                if (Main.ins.a(var4.getUniqueId(),2,
                        "Saved by " + var0.getName() + " (" + ((Player) var0).getUniqueId() + ") at " + Utilities.Text.B.format(new Date()),
                        "Message: " + a(var3,2)))
                    var0.sendMessage(Utilities.Placeholder.a("0B",var1));
                else var0.sendMessage(Main.getMessage("0A"));
            }
            else var0.sendMessage(Main.getMessage("0C"));
        }
        else if (var3[0].equalsIgnoreCase("help"))
            var0.sendMessage(Main.getMessage("0D").replace("%command%",var2));
        else if (var3[0].equalsIgnoreCase("reload") && var0.hasPermission(Permission.GAMEMASTER.perm)) {
            Main.ins.reload(2);
            var0.sendMessage(Main.getMessage("18"));
        }
        else var0.sendMessage(Main.getMessage("0E"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender var0, Command var1, String var2, String[] var3) {
        if (var3.length == 1) return Arrays.asList("open", "backup", "clear", "help", "reload");
        return null;
    }

    private String a(String[] var0, int var1) {
        return String.join(" ",(var0.length > var1? Arrays.copyOfRange(var0,var1,var0.length-1) : new String[0]));
    }
}