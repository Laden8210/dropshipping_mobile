package com.example.dropshipping.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Messenger {

    private static ProgressDialog progressDialog;

    public static MaterialAlertDialogBuilder showAlertDialog(
            Context context,
            String title,
            String message,
            String positiveButtonTitle
    ) {
        return new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonTitle, (dialog, which) -> {
                });
    }


    public static MaterialAlertDialogBuilder showAlertDialog(
            Context context,
            String title,
            String message,
            String positive,
            String negative,
            DialogInterface.OnClickListener positiveAction,
            DialogInterface.OnClickListener negativeAction
    ) {
        return new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive, positiveAction)
                .setNegativeButton(negative, negativeAction);
    }

    public static void showProgressDialog(Context context, String message) {
        dismissProgressDialog();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}