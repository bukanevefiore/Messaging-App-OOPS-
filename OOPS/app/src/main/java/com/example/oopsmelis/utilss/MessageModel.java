package com.example.oopsmelis.utilss;

public class MessageModel {


    String  type, time, text,from;
    Boolean seen;

    public MessageModel() {
    }

    public MessageModel(String from, Boolean seen, String text,String time, String type) {

        this.type = type;
        this.time = time;
        this.text = text;
        this.seen = seen;
        this.from=from;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
