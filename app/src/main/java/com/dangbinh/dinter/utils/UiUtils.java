package com.dangbinh.dinter.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.dangbinh.dinter.R;

/**
 * Created by dangbinh on 9/11/2020.
 */
public class UiUtils {

    private AlertDialog alertDialog;

    public UiUtils(final Context ctx, int message) {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setMessage(ctx.getString(message));
    }

    public void show() {
        alertDialog.show();
    }

    public void sendResetPass() {
        // Setting Dialog Title
        alertDialog.setTitle("email sent");
        // Setting Dialog Message
        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.password_button);
        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        // Showing Alert Message
        show();
    }

    public static void showProgress(View view, ProgressBar progressBar, final boolean show, int shortAnimTime) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            view.setVisibility(show ? View.GONE : View.VISIBLE);
            view.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void clearSharePref(Activity activity, String name) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static <T extends Activity> void startActivity(Context context, Class<T> des) {
        Intent i = new Intent(context, des);
        context.startActivity(i);
        ((Activity)context).finish();
    }
}

