package manu.apps.eclinic.classes;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import manu.apps.eclinic.R;

public class Config {

    public static void showSnackBar(Context context, String msg) {


        Snackbar snackbar = Snackbar.make(((Activity)context).getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_SHORT);
        /**Changing TextColor of the info in SnackBar*/
        View snackView = snackbar.getView();

        /**Setting background color to the snack bar*/
        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(15);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        snackbar.show();
    }
}
