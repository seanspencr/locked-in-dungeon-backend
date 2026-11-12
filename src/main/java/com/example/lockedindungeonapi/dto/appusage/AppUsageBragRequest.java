package com.example.lockedindungeonapi.dto.appusage;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record AppUsageBragRequest(
        Long targetUserid,
        @JsonUnwrapped AppUsageBragContent content
        ) {
}
