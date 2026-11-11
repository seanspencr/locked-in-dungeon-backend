package com.example.lockedindungeonapi.dto.auth;

import java.util.List;

public record MeResponse(
        String username,
        List<String> role
) {

}
