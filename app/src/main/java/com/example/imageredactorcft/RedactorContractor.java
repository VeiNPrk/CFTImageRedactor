package com.example.imageredactorcft;

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
		void showUrlDialog();
		void showProgressBar();
		void hideProgressBar();
		void updateProgress(int progress);
		void initDownloadLoader(Bundle bundle);
		void restartDownloadLoader();
    }

    interface Presenter {
        void onPickImageClick();
		void onRotateImageClick();
		void onMirrorImageClick();
		void onGrayImageClick();
		void onExifClick();
		void onListItemRemoveClick(PictureClass picture);
		void onListItemSourceClick(PictureClass picture);
		void onLoadFromCameraClick();
		void onLoadFromGalleryClick();
		void onDlgDownloadYesClick(String url);
		void onLoadFromUrlClick();
		void onLoadFinished(String data);
		void onActivityResultFinish(int requestCode, Intent data);
		void onDestroy();
		void onMessageEvent(int progress);
		void onInitViews(Bundle savedState);
		Bundle onSaveState(Bundle bundle);
    }
}
