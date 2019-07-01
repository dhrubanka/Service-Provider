package com.bankaspace.serviceproviderbeta;

public class Message {
    String name,email,phone,message,date,time,enqno;
    public Message(String name,String email,String phone,String message,String Date,String time){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.message=message;
        this.date=Date;
        this.time=time;

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getEnqno(){
        return enqno;
    }
}
