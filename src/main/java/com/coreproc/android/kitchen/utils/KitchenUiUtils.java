package com.coreproc.android.kitchen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Kael on 11/10/2016.
 */

public class KitchenUiUtils {

    public static  void showAlertDialog(AlertDialog alertDialog) {

        dismissAlertDialog(alertDialog);
        alertDialog.show();
    }

    public static  void showAlertDialog(Context context, String title, String message, boolean cancelable) {
        if (context == null) {
            return;
        }

        if (((Activity) context).isFinishing()) {
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                .create();
        showAlertDialog(alertDialog);
    }

    public static  void showAlertDialog(final Context context, String title, String message,
                                        boolean isCancelable, final boolean finishActivity) {
        if (context == null) {
            return;
        }

        if (((Activity) context).isFinishing()) {
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(isCancelable)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((Activity) context).finish();
                            }
                        })
                .create();
        showAlertDialog(alertDialog);
    }

    public static  void showAlertDialog(Context context, String title, String message, boolean cancelable, DialogInterface.OnClickListener onOkClickListener) {
        if (context == null) {
            return;
        }

        if (((Activity) context).isFinishing()) {
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton("OK", onOkClickListener)
                .create();
        showAlertDialog(alertDialog);
    }

    public static  void showAlertDialog(Context context, String title, String message, boolean cancelable,
                                        String positiveText, DialogInterface.OnClickListener onPositiveClickListener,
                                        String negativeText, DialogInterface.OnClickListener onNegativeClickListener) {
        if (context == null) {
            return;
        }

        if (((Activity) context).isFinishing()) {
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .create();
        showAlertDialog(alertDialog);
    }

    public static  void showAlertDialog(Context context, String title, String message, boolean cancelable,
                                        String positiveText, DialogInterface.OnClickListener onPositiveClickListener,
                                        String negativeText, DialogInterface.OnClickListener onNegativeClickListener,
                                        String neutralText, DialogInterface.OnClickListener onNeutralClickListener) {
        if (context == null) {
            return;
        }

        if (((Activity) context).isFinishing()) {
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .setNeutralButton(neutralText, onNeutralClickListener)
                .create();
        showAlertDialog(alertDialog);
    }

    public static  void showAlertDialog(Context context, String title, String message) {
        showAlertDialog(context, title, message, false);
    }

    public static  void showAlertDialog(Context context, String message) {
        showAlertDialog(context, "", message, false);
    }

    public static void dismissAlertDialog(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        alertDialog = null;
    }

    public static void showItemsSelectionDialog(Context context, String[] items, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setItems(items, onClickListener);
        alertDialog.create().show();
    }

}
