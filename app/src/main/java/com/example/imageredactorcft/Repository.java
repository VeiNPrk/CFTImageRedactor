package com.example.imageredactorcft;

import java.util.List;

public interface Repository {
    void savePicture(PictureClass item);
    long deleteAll();
	long delete(/*PictureClass item*/long dateTime);
    List<PictureClass> getData();
}
