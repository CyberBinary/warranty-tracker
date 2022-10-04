package com.example.warrantytracker;

public class dvice {
    private int id;
    private String name;
    private int dateOfWarranty;
    private String imageURL;

    public dvice(int id, String name, int dateOfWarranty, String imageURL) {
        this.id = id;
        this.name = name;
        this.dateOfWarranty = dateOfWarranty;
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "dvice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfWarranty=" + dateOfWarranty +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDateOfWarranty() {
        return dateOfWarranty;
    }

    public void setDateOfWarranty(int dateOfWarranty) {
        this.dateOfWarranty = dateOfWarranty;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
