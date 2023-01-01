package jp.cron.sample.profile;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Service
public class ProfileReader {
    public Gson gson;

    @Bean
    public Profile profile() throws FileNotFoundException {
        String profileName = System.getenv("PROFILE_NAME");
        if (profileName==null )
            profileName = "default";
        Profile p = loadFromFile(profileName);
        validate(p);
        return p;
    }

    @Autowired
    public ProfileReader(Gson gson) {
        this.gson = gson;
    }

    public Profile loadFromFile(String profileName) throws FileNotFoundException {
        File configFolder = new File("profiles");
        if (!configFolder.exists() || !configFolder.isDirectory()) {
            configFolder.mkdir();
        }
        File configFile = new File("profiles/"+profileName+".json");

        try {
            return gson.fromJson(new FileReader(configFile), Profile.class);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Profile file not found: "+configFile.getAbsolutePath());
        }
    }

    public void validate(Profile profile) {
        if (profile.ownersId.length < 1) {
            throw new RuntimeException("Owners ID not found");
        }
    }

}
