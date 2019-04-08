package com.example.imageredactorcft;

import android.util.Log;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.util.List;

public class DBClass  implements Repository{
    private final String TAG = getClass().getSimpleName();
    public DBClass(){
    }

    @Override
    public void savePicture(PictureClass item) {
        item.save();
    }

	@Override
    public long delete(long dateTime) {
        long countDelete;
        countDelete = SQLite.delete().from(PictureClass.class).where(PictureClass_Table.dateTime.eq(dateTime)).executeUpdateDelete();
        return countDelete;
    }

    @Override
    public List<PictureClass> getData() {
        List<PictureClass> results = null;
        try{
            results = (List<PictureClass>) SQLite.select().from(PictureClass.class).orderBy(PictureClass_Table.dateTime.getNameAlias(), false).queryList();

        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
        return results;
    }
}
