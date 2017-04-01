package com.inbaytech.oauth2.client.demo;

import com.inbaytech.oauth2.client.demo.idq.IdqClient;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public final class Utils {

    public Utils() {

    }

    public IdqClient initialIdqClient() throws IOException {
        Properties prop = new Properties();
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream input;
        input = new FileInputStream(classLoader.getResource("oauth2.properties").getFile());
        // Load a properties file
        prop.load(input);
        // Obtain OAuth 2 parameters from the properties file
        String clientID = prop.getProperty("clientID");
        String clientSecret = prop.getProperty("clientSecret");
        String redirectUrl = prop.getProperty("redirectUrl");
        String baesUrl = prop.getProperty("baseUrl");

        IdqClient idqClient = new IdqClient();
        idqClient.initOauthClient(clientID, clientSecret, redirectUrl);
        idqClient.initOauthServer(baesUrl);
        return idqClient;
    }

}
