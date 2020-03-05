package de.jeter.spleef.utils;

import de.jeter.bukkitgamelib.utils.LocaleManager;
import de.jeter.bukkitgamelib.utils.Utils;
import de.jeter.spleef.Main;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Locales {

    HELP_CREATESPLEEF_1("Help.Createspleef.1", "Start the creator mode with the given arena name."),
    HELP_CREATESPLEEF_2("Help.Createspleef.2", "Sets the selection to the current worldEdit selection."),
    HELP_CREATESPLEEF_3("Help.Createspleef.3", "Sets the maxPlayer to <number>"),
    HELP_CREATESPLEEF_4("Help.Createspleef.4", "Sets the minPlayer to <number>"),
    HELP_CREATESPLEEF_5("Help.Createspleef.5", "Sets the sign"),
    HELP_CREATESPLEEF_6("Help.Createspleef.6", "Sets the lobby to your location"),
    HELP_CREATESPLEEF_7("Help.Createspleef.7", "Sets the spawn to your location"),
    HELP_CREATESPLEEF_8("Help.Createspleef.8", "Sets the spectator to your location"),
    HELP_CREATESPLEEF_9("Help.Createspleef.9", "Finishes the current arena"),
    HELP_SPLEEF_1("Help.Spleef.1", "Gets info about the spleef plugin");

    private Locales(String path, Object val) {
        this.path = path;
        this.value = val;
    }

    private final Object value;
    private final String path;
    private static YamlConfiguration cfg;
    private static final File f = new File(Main.getInstance().getDataFolder(), "locales.yml");
    private static final Map<String, String> help_texts = new HashMap<>();

    public String getPath() {
        return path;
    }

    public Object getDefaultValue() {
        return value;
    }

    public String getString() {
        return Utils.replaceColors(cfg.getString(path));
    }

    public static void load() {
        Main.getInstance().getDataFolder().mkdirs();
        help_texts.clear();
        reload(false);
        for (Locales c : values()) {
            if (!cfg.contains(c.getPath())) {
                c.set(c.getDefaultValue(), false);
            }
            if (c.toString().toLowerCase().contains("help")) {
                help_texts.put(c.toString(), c.getString());
            }
        }
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        LocaleManager.registerHelp(help_texts);
    }

    public void set(Object value, boolean save) {
        cfg.set(path, value);
        if (save) {
            try {
                cfg.save(f);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            reload(false);
        }
    }

    public static void reload(boolean complete) {
        if (!complete) {
            cfg = YamlConfiguration.loadConfiguration(f);
            return;
        }
        load();
    }
}
