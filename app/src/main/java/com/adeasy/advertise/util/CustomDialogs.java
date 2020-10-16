package com.adeasy.advertise.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

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
        try {
            if (context != null) {
                String username = "user";

                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.manuka_custom_no_access_dialog);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                TextView title = dialog.findViewById(R.id.title);
                title.setText("Opps! Permission denied /404");
                TextView message = dialog.findViewById(R.id.message);
                message.setText("Dear " + username + " , We could not authorize you for the content that you requested. This may be due to requesting content which need administrative permissions, if you think this is by mistake please sign in again. If you have any issues please contact Ronicy Team for support. Thank you.");
                Button signOut = dialog.findViewById(R.id.button);
                signOut.setText("Sign In/Out");
                signOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        openNewActivity();
                    }
                });
                TextView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showVerifyEmail() {
        try {
            if (context != null) {
                String username = "user";

                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.manuka_custom_no_access_dialog);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                TextView title = dialog.findViewById(R.id.title);
                title.setText("Email Not Verified!");
                TextView message = dialog.findViewById(R.id.message);
                message.setText("Dear " + username + " in order to continue you need to verify your email. Click below to send an email to " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + ".");
                final Button signOut = dialog.findViewById(R.id.button);
                signOut.setText("Send verification email");
                signOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signOut.setText("Sending...");
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    signOut.setBackgroundResource(R.drawable.rount_sucess);
                                    signOut.setText("Email was sent");
                                } else {
                                    signOut.setBackgroundResource(R.drawable.round_danger);
                                    signOut.setText(task.getException().getMessage());
                                }
                            }
                        });
                    }
                });
                TextView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNoInternetDialog() {
        try {
            if (context != null) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.manuka_custom_no_access_dialog);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                TextView title = dialog.findViewById(R.id.title);
                title.setText("Opps! No Internet connection");
                TextView message = dialog.findViewById(R.id.message);
                message.setText("This device is not connected to internet. Stay connected to internet to explore Ronicy");
                final Button signOut = dialog.findViewById(R.id.button);
                signOut.setText("Turn on mobile data");
                signOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((AppCompatActivity) context).startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
                    }
                });
                TextView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((AppCompatActivity) context).finish();
                    }
                });
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openNewActivity() {
        if (context != null) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

    public void showCloudDialog(String header, String body, String image) {
        try {
            if (context != null) {
                String username = "user";

                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.manuka_cloud_message_dialog);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView title = dialog.findViewById(R.id.title);
                TextView message = dialog.findViewById(R.id.message);
                ImageView imageView = dialog.findViewById(R.id.image);

                if (image != null) {
                    Picasso.get().load(image).fit().into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                }

                if (header != null)
                    title.setText(header);
                else
                    title.setText("Team Ronicy.lk");

                message.setText("Dear " + username + " " + body);

                Button button = dialog.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                TextView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            View snackbarView = snackbar.getView();
            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setMaxLines(5);
            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();
        }
    }

    public void onDestroy() {
        this.context = null;
    }

}
