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

//getExtra
//String childAccount, childId; noReported


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reported_bullying_list);

//        Intent iin= getIntent();
//        Bundle bun = iin.getExtras();
//
//        if(bun!=null)
//        {
//            childAccount =(String) bun.get("childAccount");
//            childId =(String) bun.get("childId");

     //   }
        textView = (TextView) findViewById(R.id.noReported);

        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef.keepSynced(true);

        adapter = new ViewReportedBullyingAdapter(this, reportList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                System.out.println("OnItemClick");

                Report lr = reportList.get(pos);
                String com_id = lr.getComment_id();
                System.out.println("com_id"+com_id);
                String rep_id = lr.getReport_id();
                String sender_id = lr.getSender_id();
                String receiver_id = lr.getReceiver_id();
                System.out.println("receiver_id"+receiver_id);


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

        userID = "";
        userID="rTTwi9dfa9W425lPa2A6MiU93yz1";

               /*
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            userID = user.getUid();
        } else {
            // No user is signed in
        }*/


        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reportList.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Report rep = messageSnapshot.getValue(Report.class);
                        //The sent report
                        if (rep.getSender_id().equals(userID)){ //if (rep.getUser_id().equals(userID))
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
                    String user_id = findRep.getSender_id(); //String user_id = findRep.getUser_id();
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





//for toolbar
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

//toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.home:
//                Intent intent = new Intent(ViewReportedBullyingMain.this, ChildHome.class);
//
//                startActivity(intent);
//                return true;
//
//            case R.id.back:
//                Intent intent1 = new Intent(ViewReportedBullyingMain.this, ChildHome.class);
//                startActivity(intent1);
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }
//});




    }
}