package com.example.lockedindungeonapi.dto.appusage;

import com.fasterxml.jackson.core.JsonProcessingException;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

public record AppUsageBragContent(
        Map<String, Long> appUsageDurationMillis,
        Integer currentStreak,
        Optional<String> message
) {

    public AppUsageBragContent {
        if(message.isEmpty() || message.get().isBlank()){
          message = "Look at this? Can you beat me?".describeConstable();
        }
    }

    public Map<String, String> toFcmDataMap(ObjectMapper mapper) throws JsonProcessingException {
        Map<String, String> data = new LinkedHashMap<>();

        if (appUsageDurationMillis != null) {
            data.put("appUsageDurationMillis", mapper.writeValueAsString(appUsageDurationMillis));
        }
        if (currentStreak != null) {
            data.put("currentStreak", String.valueOf(currentStreak));
        }
        if (message != null && message.isPresent()) {
            data.put("message", message.get());
        }

        return data;
    }
}
