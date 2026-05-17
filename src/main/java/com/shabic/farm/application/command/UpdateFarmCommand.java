package com.shabic.farm.application.command;

import java.util.UUID;

public record UpdateFarmCommand(UUID farmId, RegisterFarmCommand details) {
}
