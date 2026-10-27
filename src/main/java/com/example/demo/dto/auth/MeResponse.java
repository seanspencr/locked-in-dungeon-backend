package com.example.demo.dto.auth;

import java.util.List;

public record MeResponse(
        String username,
        List<String> role
) {

}
