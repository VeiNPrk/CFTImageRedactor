package com.example.imageredactorcft;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class ChoseLoadDialog extends DialogFragment {
    public static final String TAG = "TextDialogFragment";
    final String LOG_TAG = "myLogs";
    static String mMessageToDisplay="";
    static ChoseLoadDialogListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mListener = (ChoseLoadDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    public static ChoseLoadDialog newInstance(/*String message*/) {

        ChoseLoadDialog textDialog = new ChoseLoadDialog();
        //mMessageToDisplay = message;
        return textDialog;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dlg_chose_load_tittle)
                .setItems(R.array.chose_load_dlg_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                mListener.onGalleryClicked();
                                break;
                            case 1:
                                mListener.onCameraClicked();
                                break;
                            case 2:
                                mListener.onUrlClicked();
                                break;
                        }
                        Log.d("dlg","element"+which);
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });

        return adb.create();
    }

    public interface ChoseLoadDialogListener {
        public void onGalleryClicked();
        public void onCameraClicked();
        public void onUrlClicked();
    }

    /*public void onDismiss(DialogInterface dialog) {
        mListener.onYesClicked();
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "TextDialog : onDismiss");
    }*/

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "TextDialog : onCancel");
    }
}
