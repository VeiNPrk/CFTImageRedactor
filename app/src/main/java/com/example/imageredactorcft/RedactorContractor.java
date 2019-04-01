package com.example.imageredactorcft;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.List;

public interface RedactorContractor {
    interface View {
		void updatePhotoView(Bitmap bitmap);
		void startActivityToResults(Intent intent, int requestCode);
		void updateRvData(List<PictureClass> data);
		void showError(String error);
		void viewDialog();
		/*void onPickImageClick();
		void onRotateImageClick();
		void onMirrorImageClick();
		void onGrayImageClick();*/
        /*void isShowDialog(String lang);
        void isShowErrorDialog(String error);
		String getIdentText();
		void clearTvIdentText();
		void initIdentifyer(String text);*/
    }

    interface Presenter {
        void onPickImageClick();
		void onRotateImageClick();
		void onMirrorImageClick();
		void onGrayImageClick();
		void onListItemClick();
		void onListItemRemoveClick(PictureClass picture);
		void onListItemSourceClick(PictureClass picture);
		void onLoadFromCameraClick();
		void onLoadFromGalleryClick();
		void onActivityResultFinish(int requestCode, Intent data);
		void isBitmapNullError();
		void onDestroy();
		void onInitViews(Bundle savedState);
		Bundle onSaveState(Bundle bundle);
		//void onRestoreState();
		/*void onLoadFinished(Bundle bundle);
        void onDestroy();
        void dialogShowDone();*/
    }
}
