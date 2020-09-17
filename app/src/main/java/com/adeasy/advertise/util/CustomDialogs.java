package com.adeasy.advertise.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.home.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class CustomDialogs {

    private Context context;

    public CustomDialogs(Context context) {
        this.context = context;
    }

    public void showPermissionDeniedStorage() {
        if (context != null) {
            String username = "user";

            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

            final Dialog dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.manuka_custom_no_access_dialog);
            TextView title = (TextView) dialog.findViewById(R.id.title);
            title.setText("Opps! Permission denied /404");
            TextView message = (TextView) dialog.findViewById(R.id.message);
            message.setText("Dear " + username + " , We could not authorize you for the content that you requested. This may be due to requesting content which need administrative permissions, if you think this is by mistake please sign in again. If you have any issues please contact Ronicy Team for support. Thank you.");
            Button signOut = (Button) dialog.findViewById(R.id.button);
            signOut.setText("Sign In/Out");
            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    openNewActivity();
                }
            });
            TextView close = (TextView) dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog.dismiss();
                    ((Activity) context).finish();
                }
            });
            dialog.show();
        }
    }

    private void openNewActivity() {
        if (context != null) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

    public void showErrorSnackbar(View snackbarLayout, String error) {
        if (context != null) {
            Snackbar snackbar = Snackbar.make(snackbarLayout, error, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            }).setActionTextColor(context.getResources().getColor(R.color.colorWhite));
            View snackbarView = snackbar.getView();
            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setMaxLines(5);
            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorError2));
            snackbar.show();
        }
    }

    public void showSuccessSnackbar(View snackbarLayout, String message) {
        if (context != null) {
            Snackbar snackbar = Snackbar.make(snackbarLayout, message, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            }).setActionTextColor(context.getResources().getColor(R.color.colorWhite));

            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();
        }
    }

    public void onDestroy() {
        this.context = null;
    }

}
