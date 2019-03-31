package com.example.imageredactorcft;

import java.util.List;

public interface Repository {
    void savePicture(PictureClass item);
    void deleteAll();
    List<PictureClass> getData();
}
