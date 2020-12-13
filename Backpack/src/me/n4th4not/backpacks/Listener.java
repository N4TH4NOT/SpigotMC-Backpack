package me.n4th4not.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Listener
    implements org.bukkit.event.Listener {

    //block action in the current inventory
    static final List<UUID> A = new ArrayList<>();
    //fail to give the button
    static final List<UUID> B = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void a(InventoryClickEvent e) {
        if (A.contains(e.getWhoClicked().getUniqueId())) e.setCancelled(true);
        else if (Utilities.Item.a(e.getCurrentItem())
                || (e.getHotbarButton() != -1 && Utilities.Item.a(e.getView().getItem(e.getHotbarButton())))) {
            e.setCancelled(true);
            switch (e.getClick()) {
                case DOUBLE_CLICK:
                case LEFT:
                    B.remove(e.getWhoClicked().getUniqueId());
                    Main.openBackpack(e.getWhoClicked(), e.getWhoClicked());
                default:
            }
        }
    }

    @EventHandler
    public void a(EntityDropItemEvent e) {
        if (Utilities.Item.a(e.getItemDrop().getItemStack())) {
            if (e.getEntityType() == EntityType.PLAYER) e.setCancelled(true);
            else e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void a(PlayerDeathEvent e) {
        e.getDrops().removeIf(Utilities.Item::a);
        if (Main.getSettings().isWhitelisted(e.getEntity().getWorld()) && !(e.getKeepInventory()
                || e.getEntity().hasPermission(Permission.LINKED_BACKPACK_IGNORE_DEATH.perm)
                || e.getEntity().hasPermission(Permission.LINKED_BACKPACK_LOCKDOWN.perm))
        && a(e.getEntity().getGameMode())) {
            Inventory var0 = Main.getLinkedBackpack(e.getEntity());
            try {Objects.requireNonNull(var0);}
            catch (NullPointerException ignored) {return;}
            for (ItemStack var1 : var0.getContents()) {
                if (var1 != null) e.getDrops().add(var1);
            }
            Main.ins.a(e.getEntity().getUniqueId(),6,
                    "Death in " + e.getEntity().getWorld().getName() + " at " + Utilities.Text.d(e.getEntity().getLocation()) + " at " + Utilities.Text.B.format(new Date()),
                    "Exp lost: " + e.getEntity().getExp(),
                    "Death message: " + e.getDeathMessage());
        }
    }

    @EventHandler
    public void a(PlayerRespawnEvent e) {
        a(e.getPlayer(),0);
    }

    @EventHandler
    public void a(PlayerGameModeChangeEvent e) {
        final boolean var0 = a(e.getNewGameMode());
        if (var0 != a(e.getPlayer().getGameMode())) {
            if (var0 && !e.getPlayer().hasPermission(Permission.NO_LINKED_BACKPACK_BUTTON.perm)
                    && (e.getPlayer().hasPermission(Permission.LINKED_BACKPACK_BYPASS_WHITELIST.perm)
                    || Main.getSettings().isWhitelisted(e.getPlayer().getWorld()))) a(e.getPlayer(),0x6);
            else a(e.getPlayer(),0xA);
        }
    }

    @EventHandler
    public void a(InventoryCloseEvent e) {
        Listener.A.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void a(PlayerChangedWorldEvent var0) {
        a(var0.getPlayer(),(Main.getSettings().isWhitelisted(var0.getPlayer().getWorld())? 0 : 8) | 2);
    }

    @EventHandler
    public void a(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.ins,() -> a(e.getPlayer(),0x1),20L);
    }

    @EventHandler
    public void a(PlayerQuitEvent e) {
        Main.ins.a(e.getPlayer().getUniqueId(),0);
        a(e.getPlayer(),0xC);
    }

    @EventHandler
    public void onSave(WorldSaveEvent e) {
        if (Bukkit.getWorlds().get(0).equals(e.getWorld())) {
            for (UUID var0 : Main.ins.linked_backpacks.keySet()) Main.ins.a(var0,0);
        }
    }

    // 0b 00 00 00
    //     ↑  ↑  ↑_ message (0 -> none, 1 -> chat, 2 -> title)
    //     |   `--- action (0 -> give, 1 -> force give, 2 -> clear slot, 3 -> clear all)
    //      `------ target (0 -> linked backpack)
    // return: -1=error, 0=no give, 1=ok
    static int a(Player var0, int var1) {
        if ((var1 >> 4 & 3) == 0) {
            switch (var1 >> 2 & 3) {
                case 0:
                    if (var0.hasPermission(Permission.NO_LINKED_BACKPACK_BUTTON.perm) || !a(var0.getGameMode())
                            || !Main.getSettings().isWhitelisted(var0.getWorld()))
                        return 0;
                case 1:
                    for (int var2 = 0; var2 < var0.getInventory().getSize(); var2++) {
                        if (var2 == Main.getSettings().getSlotLb()) {
                            ItemStack var3 = var0.getInventory().getItem(var2);
                            var0.getInventory().setItem(var2, Main.getSettings().getBackpackIcon());
                            if (!Utilities.Item.a(var3) && var3 != null && !var0.getInventory().addItem(var3).isEmpty()) {
                                var0.getInventory().setItem(var2, var3);
                                if ((var1 & 3) == 1) var0.sendMessage(Main.getMessage("0F"));
                                else if ((var1 & 3) == 2)
                                    var0.sendTitle(Main.getMessage("10"), Main.getMessage("11"), 10, 50, 20);
                                var0.getWorld().playSound(var0.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                                B.add(var0.getUniqueId());
                                return -1;
                            }
                        }
                        else if (Utilities.Item.a(var0.getInventory().getItem(var2))) var0.getInventory().clear(var2);
                    }
                    if ((var1 & 3) == 1) var0.sendMessage(Main.getMessage("12"));
                    else if ((var1 & 3) == 2)
                        var0.sendTitle(Main.getMessage("13"),Main.getMessage("14"),10,35,15);
                    break;
                case 2:
                    if (Utilities.Item.a(var0.getInventory().getItem(Main.getSettings().getSlotLb()))) {
                        var0.getInventory().clear(Main.getSettings().getSlotLb());
                        if ((var1 & 3) == 1) var0.sendMessage(Main.getMessage("15"));
                        else if ((var1 & 3) == 2) var0.sendTitle(Main.getMessage("16"), Main.getMessage("17"), 10, 35, 15);
                    }
                    break;
                case 3:
                    boolean var2 = false;
                    for (int var3 = 0; var3 < var0.getInventory().getSize(); var3++) {
                        if (Utilities.Item.a(var0.getInventory().getItem(var3))) {
                            var0.getInventory().clear(var3);
                            var2 = true;
                        }
                    }
                    if (var2) {
                        if ((var1 & 3) == 1) var0.sendMessage(Main.getMessage("15"));
                        else if ((var1 & 3) == 2) var0.sendTitle(Main.getMessage("16"), Main.getMessage("17"), 10, 35, 15);
                    }
            }
        }
        return 1;
    }

    public static boolean a(GameMode var0) {
        return var0 == GameMode.ADVENTURE || var0 == GameMode.SURVIVAL;
    }
}