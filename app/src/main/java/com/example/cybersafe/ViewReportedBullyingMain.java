package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cybersafe.Objects.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewReportedBullyingMain extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;
    private ArrayList<Report> reportList = new ArrayList();
    private ViewReportedBullyingAdapter adapter;
    DatabaseReference reportRef, reportsRef;
    private String userID, userType;
    ImageView back, home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reported_bullying_list);


        //To get the user id and type if user exist
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            userID = user.getUid();
            userType = getIntent().getStringExtra("userType");
        } else {
            // if user not log in go to parent login page
            Intent in = new Intent(ViewReportedBullyingMain.this, ParentLogin.class);
            Toast.makeText(ViewReportedBullyingMain.this, "You have to login first", Toast.LENGTH_LONG).show();
            startActivity(in);
        }


        //Toolbar
/*        back = (ImageView) findViewById(R.id.arrowVR);
        home = (ImageView) findViewById(R.id.homeVR);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/


        textView = (TextView) findViewById(R.id.noReported);

        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef.keepSynced(true);

        adapter = new ViewReportedBullyingAdapter(this, reportList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                //Get the information of what report the user click
                Report lr = reportList.get(pos);
                String com_id = lr.getComment_id();
                String rep_id = lr.getReport_id();
                String sender_id = lr.getSender_id();
                String receiver_id = lr.getReceiver_id();
                String sta = lr.getStatus();


                //Send to Report info page with the information of the report
                Intent in = new Intent(ViewReportedBullyingMain.this, Report_info_reported.class);

                in.putExtra("Comment_id", com_id);
                in.putExtra("Report_id", rep_id);
                in.putExtra("sender_id", sender_id);
                in.putExtra("receiver_id", receiver_id);
                in.putExtra("Status", sta);
                in.putExtra("userType", userType);
                startActivity(in);
            }
        });

        //Set the adapter
        recyclerView = findViewById(R.id.recyclerReported);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        //Get the Reported list for the current user
        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reportList.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Report rep = messageSnapshot.getValue(Report.class);
                        //If this parent who send the report add this report to the list
                        if (rep.getSender_id().equals(userID))
                            reportList.add(rep);

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("===", "No Data Was Found");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //To write if their no report
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childrf : snapshot.getChildren()) {
                    Report findRep = childrf.getValue(Report.class);
                    String user_id = findRep.getSender_id();
                    if (user_id.equals(userID)) {
                        textView.setText("");
                        break;
                    } else {
                        textView.setText("no reported comment");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}