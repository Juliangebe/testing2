package Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigLoader {

    public static Properties loadConfig(String configFile){
        Properties prop = new Properties();

        try{
            prop.load(Files.newInputStream(Paths.get(configFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;

    }


}
