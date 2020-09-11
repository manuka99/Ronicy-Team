package com.adeasy.advertise.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.home.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class CustomErrorDialogs {

    private Context context;

    public CustomErrorDialogs(Context context){
        this.context = context;
    }

    public void showPermissionDeniedStorage() {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.manuka_custom_no_access_dialog);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Opps! Permission denied /404");
        TextView message = (TextView) dialog.findViewById(R.id.message);
        message.setText("We could not authorize you, this is because your identity towards our server was updated or invalidated by Administrators. Please signIn and try again, If you have any issues please contact Ronicy Team for support. Thank you.");
        Button signOut = (Button) dialog.findViewById(R.id.button);
        signOut.setText("Sign Out");
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openNewActivity() {
        if(context != null){
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

    public void onDestroy(){
        this.context = null;
    }

}
