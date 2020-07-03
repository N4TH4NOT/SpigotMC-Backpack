package me.N4TH4NOT.Backpack;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class Settings
    implements Reloadable {

    private ItemStack backpack;
    private int slot;
    private boolean drop;
    private String title;

    Settings() {
        a();
    }

    public ItemStack getBackpackIcon() {
        return this.backpack.clone();
    }

    public int getSlot4Backpack() {
        return this.slot;
    }

    public String getBackpackTitle(HumanEntity var) {
        String out = this.title;
        if (var != null)
            return out.replace("{name}",var.getName()).replace("{nick}",var.getCustomName() == null? var.getName() : var.getCustomName());
        return out;
    }

    public boolean canDropContentOnDeath() {
        return this.drop;
    }

    @Override
    public void reload() {
        Main.ins.saveDefaultConfig();
        Main.ins.reloadConfig();
        a();
    }

    private void a() {
        Material var0 = Material.getMaterial(Main.ins.getConfig().getString("linked-backpack.button.material","CHEST"));
        this.backpack = new ItemStack(var0 == null ? Material.CHEST : var0);
        ItemMeta var1 = this.backpack.getItemMeta();
        if (var1 != null) {
            var1.setDisplayName(Main.ins.getConfig().getString("linked_backpack.button.display-name"));
            var1.setLore(Main.ins.getConfig().getStringList("linked-backpack.button.lore"));
            var1.getPersistentDataContainer().set(NamespacedKey.minecraft("id"), PersistentDataType.STRING,"linked_backpack");
        }
        this.slot = Main.ins.getConfig().getInt("linked-backpack.slot",9);
        this.drop = Main.ins.getConfig().getBoolean("linked-backpack.drop-on-death");
        this.title = Main.ins.getConfig().getString("linked-backpack.title","&3My Backpack").replace('&','§');
    }
}
