package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    private ArrayList<Report> reportList = new ArrayList();;
    private ViewReportedBullyingAdapter adapter;
    DatabaseReference reportRef, reportsRef;
    private String userID;
// هل احط سورت للريبورت ؟

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reported_bullying_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            userID = user.getUid();
        } else {

            Intent in = new Intent(ViewReportedBullyingMain.this, ParentLogin.class);
            startActivity(in);
        }

       // userID="rTTwi9dfa9W425lPa2A6MiU93yz1";


        textView = (TextView) findViewById(R.id.noReported);

        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef.keepSynced(true);

        adapter = new ViewReportedBullyingAdapter(this, reportList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Report lr = reportList.get(pos);
                String com_id = lr.getComment_id();
                String rep_id = lr.getReport_id();
                String sender_id = lr.getSender_id();
                String receiver_id = lr.getReceiver_id();
                String sta = lr.getStatus();


                Intent in = new Intent(ViewReportedBullyingMain.this, Report_info.class);

                in.putExtra("Comment_id", com_id);
                in.putExtra("Report_id", rep_id);
                in.putExtra("sender_id", sender_id);
                in.putExtra("receiver_id", receiver_id);
                in.putExtra("Status", sta);
                startActivity(in);
            }
        });

        recyclerView = findViewById(R.id.recyclerReported);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /*userID = "";
        userID="rTTwi9dfa9W425lPa2A6MiU93yz1";*/





        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reportList.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Report rep = messageSnapshot.getValue(Report.class);
                        //If this parent who send the report add this report to the list
                        if (rep.getSender_id().equals(userID)){
                            reportList.add(rep);
                        }
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