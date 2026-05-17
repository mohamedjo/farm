package com.shabic.farm.domain.model.valueobject;

public record GeoLocation(Double latitude, Double longitude) {

	public static GeoLocation ofNullable(Double latitude, Double longitude) {
		if (latitude == null && longitude == null) {
			return null;
		}
		if (latitude == null || longitude == null) {
			throw new IllegalArgumentException("location requires both latitude and longitude");
		}
		if (latitude < -90 || latitude > 90) {
			throw new IllegalArgumentException("latitude must be between -90 and 90");
		}
		if (longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException("longitude must be between -180 and 180");
		}
		return new GeoLocation(latitude, longitude);
	}
}
