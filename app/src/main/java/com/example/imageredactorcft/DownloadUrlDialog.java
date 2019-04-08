package com.example.imageredactorcft;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DownloadUrlDialog extends DialogFragment {
    public static final String TAG = "DownloadUrlDialog";
    DownloadDialogListener mListener;
    EditText etUrl = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mListener = (DownloadDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement DownloadDialogListener");
        }
    }

    public static DownloadUrlDialog newInstance() {
        DownloadUrlDialog downloadDialog = new DownloadUrlDialog();
        return downloadDialog;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.maket_download_dialog, null);
        etUrl = view.findViewById(R.id.et_url);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dlg_download_title))
                .setView(view)
                .setPositiveButton(getString(R.string.dlg_download_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = etUrl.getText().toString();
                        mListener.onYesDownloadClicked(url);
                    }
                })
                .setNegativeButton(getString(R.string.dlg_download_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return adb.create();
    }

    public interface DownloadDialogListener {
        public void onYesDownloadClicked(String data);
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(TAG, "onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(TAG, "onCancel");
    }
}
