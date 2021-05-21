package com.example.yourselfmusic;

public class AllOffer {
    public AllOffer() {
    }
    private String offer_name;
    private String offer_image;

    public AllOffer(String offer_name, String offer_image, String offer_price, String offer_link) {
        this.offer_name = offer_name;
        this.offer_image = offer_image;
        this.offer_price = offer_price;
        this.offer_link = offer_link;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public void setOffer_image(String offer_image) {
        this.offer_image = offer_image;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getOffer_link() {
        return offer_link;
    }

    public void setOffer_link(String offer_link) {
        this.offer_link = offer_link;
    }

    private String offer_price;
    private String offer_link;
}
