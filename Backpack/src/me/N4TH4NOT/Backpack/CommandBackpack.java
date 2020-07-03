package me.N4TH4NOT.Backpack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CommandBackpack
    implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender s, Command com, String c, String[] a) {
        if (!(s instanceof Player))
            return false;
        if (a.length == 0) {
            try {((Player) s).openInventory(Main.getLinkedBackpack((HumanEntity) s));}
            catch (Exception ex) {return false;}
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command com, String c, String[] a) {
        return Collections.singletonList("");
    }
}