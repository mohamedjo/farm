package com.shabic.farm.application.command;

public record RegisterFarmCommand(
		String name,
		String region,
		String address,
		Double locationLatitude,
		Double locationLongitude,
		String email,
		String registerId,
		String phoneNumber
) {
}
