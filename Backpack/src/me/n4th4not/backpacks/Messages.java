package me.n4th4not.backpacks;

import me.n4th4not.backpacks.Utilities.Dir;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Messages {

    private final Map<String,String> a = new HashMap<>();

    Messages() {
        b();
    }

    public String getMessage(String var0) {
        String var1 = this.a.get(var0);
        if (var1 == null)
            Main.ins.getLogger().severe("Message '" + var0 + "' does not exists or it was misspelled.");
        return Utilities.Text.c(var1,"§cThis message should not appears, contact the staff!");
    }

    public void b() {
        if (!Dir.c.exists() && Dir.a(Dir.c,true)) {
        InputStream var1 = a();
            if (var1 != null) {
                try (FileOutputStream var2 = new FileOutputStream(Dir.c)) {
                    byte[] var3 = new byte[1048];
                    int var4;
                    while ((var4 = var1.read(var3)) != -1) var2.write(var3,0 , var4);
                    var2.flush();
                }
                catch (IOException ex) {
                    Main.ins.getLogger().warning("Unable to save extracted data to '" + Dir.c.getName() + "' ...");
                    ex.printStackTrace();
                }
            }
        }
        FileConfiguration var0 = YamlConfiguration.loadConfiguration(Dir.c);
        for (String var2 : var0.getKeys(false)) this.a.put(var2,Utilities.Text.a(var0.getString(var2)));
    }

    private InputStream a() {
        try {
            URL var1 = Messages.class.getResource("/" + Dir.c.getName());
            if (var1 == null) {
                Main.ins.getLogger().severe("'" + Dir.c.getName() + "' is not found into plugin jar, restore the file or re-download the plugin!");
                return null;
            }
            else {
                URLConnection var2 = var1.openConnection();
                var2.setUseCaches(false);
                return var2.getInputStream();
            }
        }
        catch (IOException ex) {
            Main.ins.getLogger().severe("An error was occurred when try to load '" + Dir.c.getName() + "' !");
            ex.printStackTrace();
            return null;
        }
    }
}