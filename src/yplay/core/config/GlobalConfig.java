package yplay.core.config;

import ca.szc.configparser.Ini;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GlobalConfig {
    protected static final GlobalConfig instance = new GlobalConfig();
    protected static final String INI_FILE = "config.ini";
    protected Ini ini;

    public static GlobalConfig getInstance() {
        return instance;
    }

    protected GlobalConfig() {
        ini = new Ini();
        try {
            Path p = Paths.get(INI_FILE);
            if (!p.toFile().exists()) {
                Files.createFile(p);
            }
            ini.read(p);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Ini getIni() {
        return ini;
    }

    public String getValue(String section, String attr, String defaultValue) {
        Map<String, String> iniSection = getIni().getSections().get(section);
        if (iniSection == null || !iniSection.containsKey(attr)) {
            return defaultValue;
        }
        String value = iniSection.get(attr);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public void putValue(String section, String attr, String value) {
        Map iniSection = getIni().getSections().get(section);
        if (iniSection == null) {
            iniSection = new HashMap<String, String>(1);
            iniSection.put(attr, value);
            getIni().getSections().put(section, iniSection);
        } else {
            iniSection.put(attr, value);
        }
        try {
            getIni().write(Paths.get(INI_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getValue(String section, String attr) {
        return this.getValue(section, attr, "");
    }
}
