package com.app.WeatherInfo;

public class IncorrectZipCodeFormatException
        extends ZipCodeException {
    public IncorrectZipCodeFormatException(String errorMessage) {
        super(errorMessage);
    }
}
