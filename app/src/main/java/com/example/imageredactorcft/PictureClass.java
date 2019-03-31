package com.example.imageredactorcft;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.util.Date;

@Table(database = AppDataBase.class)
public class PictureClass extends BaseModel {
    @PrimaryKey
    @Column
    private long dateTime = 0;
    @Column
    private String path = "";

    public PictureClass(String _path, long _dateTime){
        path=_path;
        dateTime = _dateTime;
    }

    public PictureClass(){
    }

    public void setPath(String _path) {
        path=_path;
        dateTime = new Date().getTime();
    }

    public String getPath() {
        return path;
    }

    public long getDateTime(){
        return dateTime;
    }

    public void setDateTime(long dateTime){
        dateTime = dateTime;
    }

}
