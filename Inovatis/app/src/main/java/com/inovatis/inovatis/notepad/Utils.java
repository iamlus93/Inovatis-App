package com.inovatis.inovatis.notepad;

/**
 * Created by Lud' on 21/10/2016.
 */import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

//
//Summary: This class is a collection of utility methods that can be reused by any activity
//
public final class Utils
{
    //private constructor prevents instantiating the class
    private Utils() {}

    public static void ShowAboutDialog(Context context)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setTitle("Take Note v1.00");
        dlgAlert.setMessage("Author: Andrew Fee");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

}
