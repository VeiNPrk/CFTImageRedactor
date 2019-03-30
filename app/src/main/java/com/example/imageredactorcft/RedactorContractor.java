package com.example.imageredactorcft;


import android.content.Intent;
import android.graphics.Bitmap;

public interface RedactorContractor {
    interface View {
		void updatePhotoView(Bitmap bitmap);
		void startActivityToResults(Intent intent, int requestCode);
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
		void onListItemRemoveClick();
		void onListItemSourceClick();
		void onLoadFromCameraClick();
		void onLoadFromGalleryClick();
		void onActivityResultFinish(int requestCode);
		void onDestroy();
		/*void onLoadFinished(Bundle bundle);
        void onDestroy();
        void dialogShowDone();*/
    }
}
