package com.app.WeatherInfo;

public class NonexistentZipCodeException
        extends ZipCodeException {
    public NonexistentZipCodeException(String errorMessage) {
        super(errorMessage);
    }

}
