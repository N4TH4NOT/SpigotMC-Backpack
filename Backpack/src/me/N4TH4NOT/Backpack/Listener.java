package me.N4TH4NOT.Backpack;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Listener
    implements org.bukkit.event.Listener {

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        if (b(e.getCurrentItem()) || e.getAction() == InventoryAction.HOTBAR_SWAP && b(e.getWhoClicked().getInventory().getItem(e.getHotbarButton())))
            e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(EntityDropItemEvent e) {
        if (b(e.getItemDrop().getItemStack())) {
            if (e.getEntity() instanceof Player)
                e.setCancelled(true);
            else
                e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().removeIf(Listener::b);
        if (Main.getSettings().canDropContentOnDeath()) {
            try {
                Inventory var0 = Main.getLinkedBackpack(e.getEntity());
                for (ItemStack var1 : var0.getContents()) {
                    if (var1 != null)
                        e.getDrops().add(var1);
                }
                var0.clear();
            } catch (RuntimeException ignored) {}
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskLater(Main.ins,() -> {
            b_(e.getPlayer().getInventory());
            e.getPlayer().sendMessage("Backpack button gived ! (" + Main.getSettings().getBackpackIcon().getType().name() + ")");
        },5L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Main.a(e.getPlayer());
    }

    @EventHandler
    public void onSave(WorldSaveEvent e) {
        if (Bukkit.getWorlds().get(0).equals(e.getWorld()))
            Main.b();
    }

    static String a(ItemStack var0) {
        ItemMeta var1 = var0 == null? null : var0.getItemMeta();
        if (var1 != null)
            return var1.getPersistentDataContainer().getOrDefault(NamespacedKey.minecraft("id"), PersistentDataType.STRING,"");
        return "";
    }

    static boolean b(ItemStack var0) {
        return a(var0).equals("linked_backpack");
    }

    static void b_(Inventory var0) {
        for (int var1 = 0; var1 < var0.getSize(); var1++) {
            if (b(var0.getItem(var1))) {
                if (Main.getSettings().getSlot4Backpack() == var1)
                    var0.setItem(var1,Main.getSettings().getBackpackIcon());
                else
                    var0.clear(var1);
            }
        }
    }
}