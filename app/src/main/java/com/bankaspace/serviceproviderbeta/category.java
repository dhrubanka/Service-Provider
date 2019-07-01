package com.bankaspace.serviceproviderbeta;

public class category {
    private int userid;
    private String name;
    private String category;
    private String price;
    private String locality;
    private String enq;


    public category(String enq, int id, String name, String category, String price,  String locality){
        this.userid=id;
        this.name=name;
        this.category=category;
        this.price=price;

        this.locality=locality;
        this.enq=enq;
    }

    public int getUserId() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getLocality() {
        return locality;
    }

  public String getEnq(){
        return enq;
  }
}
