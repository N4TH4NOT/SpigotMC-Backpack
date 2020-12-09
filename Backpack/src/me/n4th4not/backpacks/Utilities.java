package me.n4th4not.backpacks;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Utilities {

    public static final class Text {
        public static final SimpleDateFormat A = new SimpleDateFormat("ssmmHH_ddMMyyyy");
        public static final SimpleDateFormat B = new SimpleDateFormat("dd/MM/yyyy HH'h'mm''ss");

        public static String a(String var0, char var1) {
            if (var0 == null) return null;
            StringBuilder var2 = new StringBuilder(var0);
            for (int var3 = var2.indexOf(String.valueOf(var1)); var3 != -1 && var3 < var2.length()-1; var3 = var2.indexOf(String.valueOf(var1),var3+1)) {
                if (var3 != 0 && var2.charAt(var3-1) == '\\') {
                    var2.deleteCharAt(var3 - 1);
                    var3--;
                }
                else if ("0123456789AaBbCcDdEeFfKkLlMmNnOoKk".indexOf(var2.charAt(var3+1)) != -1)
                    var2.setCharAt(var3,'§');
            }
            return var2.toString();

        }
        public static String a(String var0) {
            return a(var0,'&');
        }

        public static List<String> a(List<String> var0) {
            if (var0 == null) return null;
            List<String> var1 = new ArrayList<>(var0.size());
            for (String var2 : var0) var1.add(a(var2));
            return var1;
        }

        public static String b(String var0) {
            return var0.replace("&","\\&").replace("§","&");
        }

        public static List<String> b(List<String> var0) {
            if (var0 == null) return null;
            List<String> var1 = new ArrayList<>(var0.size());
            for (String var2 : var0) var1.add(b(var2));
            return var1;
        }
        public static String a(CharSequence var0,CharSequence... var1) {
            Objects.requireNonNull(var0,"Argument 1 cannot be null!");
            Objects.requireNonNull(var1,"Argument 2 cannot be null!");
            StringJoiner var2 = new StringJoiner(var0);
            for (CharSequence var3: var1) {
                if (var3 != null && var3.length() > 0) var2.add(var3);
            }
            return var2.toString();
        }
        public static String c(Object var0) {return var0 == null? "" : var0.toString();}
        public static String c(Object var0, String var1) {return var0 == null? var1 : var0.toString();}

        public static String d(Location var0) {
            World var1 = var0.getWorld();
            return  "x=" + var0.getBlockX() + ", y=" + var0.getBlockY() + ", z=" + var0.getBlockZ();
        }
    }
    public static final class Item {
        public static final NamespacedKey A = new NamespacedKey("backpacks","id");

        public static boolean a(ItemStack var0) {
            try {return var0.getItemMeta().getPersistentDataContainer().get(A, PersistentDataType.BYTE) == (byte) 1;}
            catch (Exception ex) {return false;}
        }
    }
    public static final class Dir {
        public static final File a = new File(Main.ins.getDataFolder(),"linked_backpack");
        public static final File b = new File(Main.ins.getDataFolder(),"backups" + File.separatorChar + "linked_backpack");
        public static final File c = new File(Main.ins.getDataFolder(),"messages.yml");

        public static boolean a(File var0, boolean var1) {
            Objects.requireNonNull(var0,"Argument 1 cannot be null!");
            File var2 = var1? var0.getParentFile() : var0;
            try {
                if (var2 != null && !(var2.mkdirs() || var2.isDirectory())) {
                    System.err.println("Impossible to create directories: " + var0.getParentFile().getPath());
                    return false;
                }
                if (var1 && !(var0.createNewFile() || var0.isFile())) {
                    System.err.println("A directory with this name already exists : " + var0.getPath());
                    return false;
                }
            }
            catch (IOException ex) {ex.printStackTrace();}
            return true;
        }
    }
    public static final class Objeci {
        public static <T> T[] a(T... var0) {
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; var2++) {
                if (var0[var2] == null) {
                    if (var2+1 < var1) System.arraycopy(var0,var2+1,var0,var2,var1-1);
                    var1--;
                }
            }
            return Arrays.copyOf(var0,var1);
        }
    }
    public static final class Placeholder {
        public static String a(String var0, GameMode var1) {
            return Main.getMessage(var0).replace("%gamemode%",Main.getMessage(var1));
        }
        public static String _a(String var0, Entity var1) {
            return var0.replace("%name%",var1.getName())
                    .replace("%nick%",Text.c(var1.getCustomName(),var1.getName()));
        }
        public static String a(String var0, Entity var1) {
            return _a(Main.getMessage(var0),var1);
        }
        public static String a(String var0, Object... var1) {
            StringJoiner var2 = new StringJoiner(" ,");
            for (Object var3: var1) {
                if (var3 != null) var2.add(var3.toString());
            }
            return Main.getMessage(var0).replace("%args%",var2.toString());
        }
    }
}