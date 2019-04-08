package com.example.imageredactorcft;

import java.util.List;

public interface Repository {
    void savePicture(PictureClass item);
	long delete(long dateTime);
    List<PictureClass> getData();
}
