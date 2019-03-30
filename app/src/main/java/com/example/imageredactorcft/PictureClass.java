package com.example.imageredactorcft;

import java.util.Date;

public class PictureClass {

    private long dateTime = 0;
    private String path = "";

    public PictureClass(String _path){
        path=_path;
        dateTime = new Date().getTime();
    }

    public void setPath(String _path) {
        path=_path;
        dateTime = new Date().getTime();
    }

    public String getPath() {
        return path;
    }

    public long getDateTimeMils(){
        return dateTime;
    }

    public void setDateTimeMils(long dateTime){
        dateTime = dateTime;
    }

}
