package com.proj.limtick.Model;

public class Routes
{
    private String routename,price,description,image;
    public Routes()
    {

    }

    public Routes(String routename, String price, String description, String image) {
        this.routename = routename;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
