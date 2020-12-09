package me.n4th4not.backpacks;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class Settings {

    private ItemStack backpack;
    private int slot;
    private String title;

    Settings() {
        a();
    }

    public ItemStack getBackpackIcon() {
        return this.backpack.clone();
    }

    public int getSlotLb() {
        return this.slot;
    }

    public String getBackpackTitle(HumanEntity var0) {
        return Utilities.Placeholder._a(Utilities.Text.c(this.title,"%nick%'s linked backpack"),var0);
    }

    public void a() {
        Main.ins.saveDefaultConfig();
        Main.ins.reloadConfig();
        this.backpack = a("linked-backpack.button");
        ItemMeta var0 = this.backpack.getItemMeta();
        var0.getPersistentDataContainer().set(Utilities.Item.A,PersistentDataType.BYTE,(byte) 1);
        this.backpack.setItemMeta(var0);
        this.slot = Math.max(Math.min(Main.ins.getConfig().getInt("linked-backpack.slot",9), InventoryType.PLAYER.getDefaultSize() -1),9);
        this.title = Utilities.Text.a(Main.ins.getConfig().getString("linked-backpack.title","&3My Backpack"));
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