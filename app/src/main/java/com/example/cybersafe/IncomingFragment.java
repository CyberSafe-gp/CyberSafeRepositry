package com.example.cybersafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomingFragment extends Fragment {

    DatabaseReference reportRef, reportsRef, usersRef;
    private TextView textView;
    private RecyclerView recyclerView;
    private ArrayList<Report> reportList = new ArrayList();
    private IncomingReportAdapter adapter;
    private FirebaseUser user;
    private String userID, userType;
    ImageView back, home;


    public IncomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_incoming_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


            //To get the user id and type if user exist
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        System.out.println(userID);

            //ارجعها
           if (user != null) {
                userID = user.getUid();
//                userType = getIntent().getStringExtra("userType");
                userType=getActivity().getIntent().getExtras().getString("userType");
                System.out.println("userType "+userType);
            } else {
                // if user not log in go to Interface page
                Intent in = new Intent(getActivity(), Interface.class);
                Toast.makeText(getActivity(), "You have to login first", Toast.LENGTH_LONG).show();
                startActivity(in);
            }




            textView = (TextView) getView().findViewById(R.id.noReport);

            reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
            reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");
            reportRef.keepSynced(true);


            adapter = new IncomingReportAdapter(getActivity(), reportList, new OnItemClickListener() {
                @Override
                public void OnItemClick(View v, int pos) {
                    Report lr = reportList.get(pos);
                    String status = lr.getStatus();
                    //If the user type is School manager and the status of the report not confirm he should confirm the receiving first
                    if(userType.equals("Schoolmanager") && status.equals("Not confirm")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        // Setting Alert Dialog Title
                        alertDialogBuilder.setTitle("View report");
                        // Setting Alert Dialog Message
                        alertDialogBuilder.setMessage("To view the report please confirm receiving it?");
                        // Confirm receiving
                        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //confirm
                                reportInfo(lr);
                            }
                        });
                        //not confirm
                        alertDialogBuilder.setNegativeButton("Cancel", null).show();

                    } else { //Parent or School manager and confirm
                        reportInfo(lr);
                    }
                }
            });

            recyclerView = getView().findViewById(R.id.recyclerViewIncoming);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);


            //To get the list of the incoming report
            reportRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("onDataChange");
                    if (snapshot.exists()) {
                        reportList.clear();

                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                            Report rep = messageSnapshot.getValue(Report.class);
                            //The income report
                            if (rep.getReceiver_id().equals(userID)){
                                reportList.add(rep);
                                System.out.println("REEport");
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



            //To write if their no report
            reportsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childrf : snapshot.getChildren()) {
                        Report findRep = childrf.getValue(Report.class);
                        String user_id = findRep.getReceiver_id(); //String user_id = findRep.getUser_id();
                        if (user_id.equals(userID)) {
                            textView.setText("");
                            break;
                        } else {
                            textView.setText("No incoming report");

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



    }
    public void reportInfo(Report report){
        String status = report.getStatus();
        String report_id = report.getReport_id();
        if (status.equals("Not confirm"))
            reportRef.child(report_id).child("status").setValue("Confirm");


        String com_id = report.getComment_id();
        String rep_id = report.getReport_id();
        String sender_id = report.getSender_id();
        String receiver_id = report.getReceiver_id();
        String sta = report.getStatus();

        if(userType.equals("Parent")){

            //Go to Report info page
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = new Report_info_incomingFragment();
            Bundle bundleReport = new Bundle();
            bundleReport.putString("Comment_id", com_id);
            bundleReport.putString("Report_id", rep_id);
            bundleReport.putString("sender_id", sender_id);
            bundleReport.putString("receiver_id", receiver_id);
            bundleReport.putString("Status", sta);
            bundleReport.putString("userType", userType);

            fragment.setArguments(bundleReport);
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).addToBackStack(null).commit();

        } else {

            Intent in = new Intent(getActivity(), Report_info_reported.class);
            in.putExtra("Comment_id", com_id);
            in.putExtra("Report_id", rep_id);
            in.putExtra("sender_id", sender_id);
            in.putExtra("receiver_id", receiver_id);
            in.putExtra("Status", sta);
            in.putExtra("userType", userType);
            startActivity(in);}



    }
}
