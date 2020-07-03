package me.N4TH4NOT.Backpack;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main
    extends JavaPlugin {

    static Main ins;
    final Map<UUID, Inventory> linked_backpacks = new HashMap<>(Bukkit.getMaxPlayers());
    private Settings set;
    private File dir_linkb;
    private File dir_backup_linkb;

    @Override
    public void onDisable() {
        b();
        Main.ins = null;
    }

    @Override
    public void onEnable() {
        Main.ins = this;
        saveDefaultConfig();
        this.set = new Settings();
        Main.ins.dir_linkb = new File(getDataFolder(),"/linked_backpack/");
        Main.ins.dir_linkb.mkdirs();
        Main.ins.dir_backup_linkb = new File(getDataFolder(),"/backups/linked_backpack/");
        CommandBackpack var0 = new CommandBackpack();
        getCommand("backpack").setTabCompleter(var0);
        getCommand("backpack").setExecutor(var0);
        getPluginLoader().createRegisteredListeners(new Listener(),this);
    }

    public static Inventory getLinkedBackpack(HumanEntity var0) {
        Inventory var1 = Main.ins.linked_backpacks.get(var0.getUniqueId());
        if (var1 == null) {
            var1 = _ini_LinkBac(var0,c1(var0));
            Main.ins.linked_backpacks.put(var0.getUniqueId(),var1);
            File var2 = new File(Main.ins.dir_linkb,var0.getUniqueId().toString() + ".yml");
            if (var2.exists()) {
                YamlConfiguration var3 = YamlConfiguration.loadConfiguration(var2);
                Collection<ItemStack> var4 = new ArrayList<>();
                for (String var5 : var3.getKeys(false)) {
                    int var6;
                    try {
                        var6 = Integer.parseInt(var5);
                        if (var6 < 0 || var6 >= 54)
                            throw new Exception();
                    } catch (Exception ex) {continue;}
                    ItemStack var7 = var3.getItemStack(var5);
                    if (var6 < var1.getSize() && var7 != null)
                        var1.setItem(var6,var7);
                    else if (var7 != null)
                        var4.add(var7);
                }
                if (!var4.isEmpty()) {
                    var4 = var1.addItem(var4.toArray(new ItemStack[0])).values();
                    if (!var4.isEmpty()) {
                        if (!Main.ins.dir_backup_linkb.exists())
                            Main.ins.dir_backup_linkb.mkdirs();
                        var2.renameTo(new File(Main.ins.dir_backup_linkb, var2.getName() + '.' + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
                        var4.forEach(var5 -> var0.getWorld().dropItem(var0.getLocation(),var5));
                    }
                }
            }
        } else {
            Inventory var2 = c(var0, var1);
            if (!var1.equals(var2))
                Main.ins.linked_backpacks.replace(var0.getUniqueId(),(var1 = var2));
        }
        return var1;
    }

    static Inventory c(HumanEntity var0, Inventory var1) {
        int var2 = c1(var0);
        if (var2 == cl(var1))
            return var1;
        Inventory var3 = _ini_LinkBac(var0,var2);
        var3.addItem(var1.getContents()).forEach((index,item) -> var0.getWorld().dropItem(var0.getLocation(),item));
        return var3;
    }

    private static int c1(HumanEntity var0) {
        if (var0.hasPermission("eliaze.linked_backpack.lv6"))
            return 6;
        else if (var0.hasPermission("eliaze.linked_backpack.lv5"))
            return 5;
        else if (var0.hasPermission("eliaze.linked_backpack.lv4"))
            return 4;
        else if (var0.hasPermission("eliaze.linked_backpack.lv3"))
            return 3;
        else if (var0.hasPermission("eliaze.linked_backpack.lv2"))
            return 2;
        else if (var0.hasPermission("eliaze.linked_backpack.lv1"))
            return 1;
        else if (var0.hasPermission("eliaze.linked_backpack.lv0"))
            return 0;
        return -1;
    }
    private static int cl(Inventory var0) {
        if (var0 != null) {
            if (var0.getType() == InventoryType.CHEST) {
                return var0.getSize()/9;
            } else if (var0.getType() == InventoryType.HOPPER) {
                return 0;
            }
        }
        return -1;
    }

    private static Inventory _ini_LinkBac(HumanEntity var0, int var1) {
        switch (var1) {
            case 0:
                return Bukkit.createInventory(var0, InventoryType.HOPPER,Main.ins.set.getBackpackTitle(var0));
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return Bukkit.createInventory(var0, var1*9,Main.ins.set.getBackpackTitle(var0));
        }
        throw new RuntimeException("Wrong level ! (needed: 0-6)");
    }

    public static Settings getSettings() {
        return Main.ins.set;
    }

    static void a(HumanEntity var0) {
        _ab(var0.getUniqueId(),Main.getLinkedBackpack(var0));
    }
    
    static void b() {
        Main.ins.linked_backpacks.forEach(Main::_ab);
    }

    private static void _ab(UUID var0, Inventory var1) {
        File var2 = new File(Main.ins.dir_linkb,var0.toString() + ".yml");
        try {var2.createNewFile();}
        catch (IOException e) {e.printStackTrace();}
        YamlConfiguration var3 = new YamlConfiguration();
        for (int var4 = 0; var4 < var1.getSize(); var4++) {
            ItemStack var5 = var1.getItem(var4);
            if (var5 != null)
                var3.set(String.valueOf(var4),var5);
        }
        try {var3.save(var2);}
        catch (IOException e) {e.printStackTrace();}
    }
}