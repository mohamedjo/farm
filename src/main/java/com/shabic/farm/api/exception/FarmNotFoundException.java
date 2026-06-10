package com.shabic.farm.api.exception;

public class FarmNotFoundException extends RuntimeException {
	public FarmNotFoundException() {
		super("farm not found");
	}
}
