package com.example.dropshipping.model;


public  class ShippingAddress {
    private long id;
    private String addressLine;
    private String region;
    private String city;
    private String brgy;
    private String postalCode;

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }


    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getBrgy() { return brgy; }
    public void setBrgy(String brgy) { this.brgy = brgy; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}