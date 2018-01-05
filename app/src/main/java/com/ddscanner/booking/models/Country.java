package com.ddscanner.booking.models;

public class Country implements Comparable<Country> {

    private String type;

    private String countryName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public int compareTo(Country country) {
        return this.getCountryName().compareTo(country.getCountryName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Country) {
            return this.type.equalsIgnoreCase(((Country) o).getType());
        } else {
            return false;
        }
    }
}
