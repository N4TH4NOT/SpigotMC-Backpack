package me.n4th4not.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class Main
    extends JavaPlugin {

    static Main ins;
    final Map<UUID, Inventory> linked_backpacks = new HashMap<>(Bukkit.getMaxPlayers());
    private Settings set;
    private Messages msgs;

    public static Settings getSettings() {
        return Main.ins.set;
    }
    public static String getMessage(String var0) {return Main.ins.msgs.getMessage(var0);}
    public static String getMessage(Enum<?> var0) {return getMessage((var0.getClass().getSimpleName() + "_" + var0.ordinal()).toLowerCase());}


    @Override
    public void onEnable() {
        try {Class.forName("org.bukkit.persistence.PersistentDataContainer");}
        catch (ClassNotFoundException ex) {
            System.err.println(getName() + " v" + getDescription().getVersion() + " does not support this server version.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        Main.ins = this;
        saveDefaultConfig();
        this.set = new Settings();
        this.msgs = new Messages();
        Utilities.Dir.a(Utilities.Dir.a,false);
        CommandBackpack var0 = new CommandBackpack();
        getCommand("backpack").setTabCompleter(var0);
        getCommand("backpack").setExecutor(var0);
        Bukkit.getPluginManager().registerEvents(new Listener(),this);
        for (Player var1 : Bukkit.getOnlinePlayers()) Listener.a(var1,2);
    }

    @Override
    public void onDisable() {
        for (Player var0 : Bukkit.getOnlinePlayers()) {
            Listener.a(var0,0xE);
            a(var0.getUniqueId(),4);
        }
        Main.ins = null;
    }

    // notify all players : 1 -> in chat ; 2 -> with title
    public void reload(int var0) {
        this.set.a();
        this.msgs.b();
        for (Player var1 : Bukkit.getOnlinePlayers()) {
            Listener.a(var1,12);
            if (Listener.a(var1,0) == 1) {
                switch (var0) {
                    case 1:
                        var1.sendMessage(getMessage("19"));
                        break;
                    case 2: var1.sendTitle(getMessage("1A"),getMessage("1B"),10,35,15);
                    default:
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Inventory getLinkedBackpack(HumanEntity var0) {
        Inventory var1 = Main.ins.linked_backpacks.get(var0.getUniqueId());
        int var2 = 0;
        for (Permission.A<Integer> var3 : new Permission.A[]{
                Permission.LINKED_BACKPACK_LV6,Permission.LINKED_BACKPACK_LV5,
                Permission.LINKED_BACKPACK_LV4,Permission.LINKED_BACKPACK_LV3,
                Permission.LINKED_BACKPACK_LV2,Permission.LINKED_BACKPACK_LV1,
                Permission.LINKED_BACKPACK_LV0}) {
            if (var0.hasPermission(var3.perm)) {
                if (var3.value == 0) var2 = 5;
                else var2 = var3.value*9;
                break;
            }
        }
        if (var1 != null && var1.getSize() == var2) return var1;
        else if (var2 == 0) return null;
        Inventory var3 = (var2 == 5? Bukkit.createInventory(var0,InventoryType.HOPPER,getSettings().getBackpackTitle(var0)) : Bukkit.createInventory(var0,var2,getSettings().getBackpackTitle(var0)));
        Collection<ItemStack> var4 = new ArrayList<>(0x36-var2);
        if (var1 == null) {
            YamlConfiguration var5 = YamlConfiguration.loadConfiguration(new File(Utilities.Dir.a,var0.getUniqueId() + ".yml"));
            for (String var6 : var5.getKeys(false)) {
                ItemStack var7 = var5.getItemStack(var6);
                try {var3.setItem(new Integer(var6),var7);}
                catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {var4.add(var7);}
            }
            var4 = var3.addItem(var4.toArray(new ItemStack[0])).values();
        }
        else var4.addAll(var3.addItem(Utilities.Objeci.a(var1.getContents())).values());
        if (!var4.isEmpty()) {
            Collection<ItemStack> var5 = var0.getInventory().addItem(var4.toArray(new ItemStack[0])).values();
            if (!var5.isEmpty()) {
                var4.removeAll(var5);
                var0.getInventory().removeItem(var4.toArray(new ItemStack[0]));
                if (var1 == null) {
                    File var6 = new File(Utilities.Dir.a,var0.getUniqueId() + ".yml");
                    YamlConfiguration var7 = YamlConfiguration.loadConfiguration(var6);
                    var6.delete();
                    var7.options().header("Items overflow (" + var5.size() + " stacks remaining)");
                    try {var7.save(new File(Utilities.Dir.a,var0.getUniqueId() + ".yml." + Utilities.Text.A.format(new Date())));}
                    catch (IOException ex) {
                        Main.ins.getLogger().severe("Impossible to save the corrupted linked backpack of " + var0);
                        Main.ins.getLogger().severe("Data lost!");
                        ex.printStackTrace();
                    }
                }
                else Main.ins.a(var0.getUniqueId(),5,"Items overflow (" + var5.size() + " stacks remaining)");
                throw new RuntimeException("Items overflow cannot be moved to your inventory due to lack of space");
            }
        }
        Main.ins.linked_backpacks.put(var0.getUniqueId(),var3);
        return var3;
    }

    public static void openBackpack(HumanEntity var0, HumanEntity var1) {
        Objects.requireNonNull(var0,"Argument 1 cannot be null");
        Objects.requireNonNull(var1,"Argument 2 cannot be null");
        boolean var2 = var0.getUniqueId().equals(var1.getUniqueId());
        if (var2) {
            if (var0.hasPermission(Permission.LINKED_BACKPACK_LOCKDOWN.perm)) {
                var0.sendMessage(getMessage("00"));
                return;
            }
            else if (!(var0.hasPermission(Permission.LINKED_BACKPACK_BYPASS_WHITELIST.perm)
                    || Main.ins.set.isWhitelisted(var0.getWorld()))) {
                var0.sendMessage(getMessage("1C"));
                return;
            }
            else if (!Listener.a(var0.getGameMode())) {
                var0.sendMessage(Utilities.Placeholder.a("01",var0.getGameMode()));
                return;
            }
        }
        else {
            if (!var0.hasPermission(Permission.LINKED_BACKPACK_SEE_OTHERS.perm)) {
                var0.sendMessage(getMessage("02"));
                return;
            }
        }
        try {
            Inventory var3 = Main.getLinkedBackpack(var1);
            if (var3 == null) {
                if (var2) {var0.sendMessage(getMessage("03"));}
                else {var0.sendMessage(Utilities.Placeholder.a("04",var1));}
            }
            else var0.openInventory(var3);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            if (var2) {var0.sendMessage(getMessage("05"));}
            else {var0.sendMessage(Utilities.Placeholder.a("06",var1));}
            return;
        }
        if (!var2 &&
                (!var0.hasPermission(Permission.LINKED_BACKPACK_EDIT_OTHERS.perm) ||
                ((var2 = var1.hasPermission(Permission.LINKED_BACKPACK_LOCKDOWN.perm)) && !var0.hasPermission(Permission.GAMEMASTER.perm)))) {
            if (var2) var0.sendMessage(getMessage("07"));
            Listener.A.add(var0.getUniqueId());
        }
    }


    // 0b 0 00
    //    ↑ ↑_ 0 -> save ; 1 -> rename ; 2 -> backup
    //    `--- 0 -> get ; 1 -> remove
    boolean a(UUID var0, int var1, String... var2) {
        Inventory var3;
        if ((var1 >> 2 & 1) == 1) {
            var3 = this.linked_backpacks.remove(var0);
            try {var3.getViewers().forEach(HumanEntity::closeInventory);}
            catch (Exception ignored) {}
        }
        else var3 = this.linked_backpacks.get(var0);
        if (var3 != null) {
            File var4;
            switch (var1 & 3) {
                case 0:
                    var4 = new File(Utilities.Dir.a,var0 + ".yml");
                    break;
                case 1:
                    var4 = new File(Utilities.Dir.a,var0 + ".yml." + Utilities.Text.A.format(new Date()));
                    new File(Utilities.Dir.a,var0 + ".yml").delete();
                    break;
                case 2:
                    var4 = new File(Utilities.Dir.b,var0 + File.separator + Utilities.Text.A.format(new Date()) + ".yml");
                    new File(Utilities.Dir.a,var0 + ".yml").delete();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            Utilities.Dir.a(var4,true);
            YamlConfiguration var5 = new YamlConfiguration();
            if (var2 != null && var2.length > 0) var5.options().header(String.join("\n",var2));
            boolean var6 = false;
            for (int var7 = 0; var7 < var3.getSize(); var7++) {
                ItemStack var8 = var3.getItem(var7);
                if (var8 != null) {
                    var5.set(String.valueOf(var7),var8);
                    var6 = true;
                }
            }
            try {
                if (var6) var5.save(var4);
                return true;
            }
            catch (IOException ex) {
                getLogger().severe("An error was occurred while saving linked backpack of  " + var0);
                ex.printStackTrace();
            }
        }
        else getLogger().warning("Impossible to find the linked backpack for " + var0 + ", it maybe have not generated or this player do not have the permission to generate one.");
        return false;
    }
}