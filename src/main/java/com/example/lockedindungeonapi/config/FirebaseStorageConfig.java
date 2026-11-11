package com.example.lockedindungeonapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseStorageConfig {


    @Bean
    public FirebaseApp initializeFirebase(){
        try {
            FileInputStream serviceAccount = new FileInputStream("secrets/locked-in-dungeon-firebase-adminsdk-fbsvc-b7a00bc2e8.json");

            FirebaseOptions options = null;
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
