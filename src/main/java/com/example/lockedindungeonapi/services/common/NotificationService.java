package com.example.lockedindungeonapi.services.common;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class NotificationService {

    public String sendNotification(String targetFid, String title, String body,Map<String, String> messsageContents) throws FirebaseMessagingException {
        Message message = Message.builder().putAllData(messsageContents)
                .setNotification(
                        Notification.builder()
                                .setBody(body)
                                .setTitle(title)
                                .build())
                .setFid(targetFid)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}
