package me.n4th4not.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Settings {

    private ItemStack a;
    private int b;
    private String c;
    private final List<UUID> d = new ArrayList<>();

    Settings() {
        a();
    }

    public ItemStack getBackpackIcon() {
        return this.a.clone();
    }

    public int getSlotLb() {
        return this.b;
    }

    public String getBackpackTitle(HumanEntity var0) {
        return Utilities.Placeholder._a(Utilities.Text.c(this.c,"%nick%'s linked backpack"),var0);
    }

    public boolean isWhitelisted(World var0) {
        return this.d.contains(var0.getUID());
    }

    public void a() {
        Main.ins.saveDefaultConfig();
        Main.ins.reloadConfig();
        this.d.clear();
        this.a = a("linked-backpack.button");
        ItemMeta var0 = this.a.getItemMeta();
        var0.getPersistentDataContainer().set(Utilities.Item.A,PersistentDataType.BYTE,(byte) 1);
        this.a.setItemMeta(var0);
        this.b = Math.max(Math.min(Main.ins.getConfig().getInt("linked-backpack.slot",9), InventoryType.PLAYER.getDefaultSize() -1),9);
        this.c = Utilities.Text.a(Main.ins.getConfig().getString("linked-backpack.title","&3My Backpack"));
        List<?> var1 = Main.ins.getConfig().getList("world-whitelist");
        if (var1 == null) Main.ins.getLogger().warning("The whitelist was not found so backpacks are allowed in all worlds !");
        else {
            for (Object var2 : var1) {
                World var3 = Bukkit.getWorld(Utilities.Text.c(var2));
                if (var3 != null) this.d.add(var3.getUID());
            }
        }
    }

    static ItemStack a(String var0) {
        ItemStack var1 = new ItemStack(Material.DIRT);
        Material var2 = Material.getMaterial(Main.ins.getConfig().getString(var0 + ".material","DIRT"));
        if (var2 != null) {
            var1.setType(var2);
            ItemMeta var3 = var1.getItemMeta();
            if (var3 != null) {
                var3.setDisplayName(Utilities.Text.a(Main.ins.getConfig().getString(var0 + ".display-name")));
                var3.setLore(Utilities.Text.a(Main.ins.getConfig().getStringList(var0 + ".lore")));
                if (Main.ins.getConfig().getBoolean(var0 + ".glowing")) {
                    var3.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
                    var3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                var1.setItemMeta(var3);
            }
        }
        return var1;
    }
}