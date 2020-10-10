package com.adeasy.advertise.ui.getintouch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//Creator-A.M.W.W.R.L.Wataketiya
//IT19014128
//ravinduwata@gmail.com


public class BugStatisticsActivity extends AppCompatActivity {

    TextView TVbugCount,TVAcknowledgedBugs,TVfixedBugs,TVunattendedBugs,TVtotalAckFix;
    DatabaseReference dbref;

    long bugCount, acknowledgedBugCount, fixedBugCount, unattendedBugCount,totalAckFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_bug_statistics);

        //acknowledgedBugCount=0;
        //fixedBugCount =0;
        //totalAckFixed = 0;

        TVbugCount = findViewById(R.id.TVbugCount);
        TVAcknowledgedBugs = findViewById(R.id.TVAcknowledgedBugs);
        TVfixedBugs = findViewById(R.id.TVfixedBugs);
        TVunattendedBugs = findViewById(R.id.TVunattendedBugs);
        //TVtotalAckFix = findViewById(R.id.TVtotalAckFix);

        dbref = FirebaseDatabase.getInstance().getReference().child("Bug");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bugCount = snapshot.getChildrenCount();
                TVbugCount.setText(String.valueOf(bugCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query = dbref.orderByChild("status").equalTo("Acknowledged");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final long acknowledgedBugCount = snapshot.getChildrenCount();
                TVAcknowledgedBugs.setText(String.valueOf(acknowledgedBugCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.i("mylog","acknowledged count");
        Log.i("mylog",String.valueOf(acknowledgedBugCount));

        query = dbref.orderByChild("status").equalTo("Fixed");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fixedBugCount = snapshot.getChildrenCount();
                TVfixedBugs.setText(String.valueOf(fixedBugCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        query = dbref.orderByChild("status").equalTo("Pending");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unattendedBugCount = snapshot.getChildrenCount();
                TVunattendedBugs.setText(String.valueOf(unattendedBugCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        totalAckFixed = bugCalc(fixedBugCount, acknowledgedBugCount);
        TVtotalAckFix.setText(String.valueOf(totalAckFixed));*/
    }

    public static long bugCalc(long fixedBugCount, long acknowledgedBugCount)
    {
        long retValue = fixedBugCount + acknowledgedBugCount+1;
        System.out.println(retValue);
        Log.i("mylog",String.valueOf(fixedBugCount));
        Log.i("mylog",String.valueOf(acknowledgedBugCount));
        Log.i("mylog",String.valueOf(retValue));
        return retValue;
    }
}