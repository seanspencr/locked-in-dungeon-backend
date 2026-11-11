package com.example.lockedindungeonapi.dto.settings;

import java.util.Optional;
import java.util.OptionalInt;

public record ModifySettingsRequest(
         Optional<Boolean> showBlockingList,
         Optional<Boolean> showStats
        ) {
}
