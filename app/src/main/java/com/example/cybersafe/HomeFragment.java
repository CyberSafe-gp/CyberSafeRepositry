package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Child;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
public class HomeFragment extends Fragment {

    private TextView textView;
    //
    private RecyclerView recyclerView;
    //for the adapter
    private ArrayList<Child> childrenList = new ArrayList();
    //to list all the current parent children
    private ParentHomeAdapter adapter;
    DatabaseReference childRef, childrenRef;
    private String userID,userType1;
    FloatingActionButton actionButton;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_parent_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //get the current user
        if (user != null) {

            userID = user.getUid();
            //get and store current user id
            //userType1 = getIntent().getStringExtra("userType");
            userType1 = getActivity().getIntent().getExtras().getString("userType");
            //get and store userType1 is for storing  what is the type of the current user
        } else {
            System.out.println("EEEEELLLLLSSSEEEE");

            Intent in = new Intent(getActivity(), ParentLogin.class);
            startActivity(in);
        }

        //adapter
        textView = (TextView) getActivity().findViewById(R.id.noChild);
//adapter
        childrenRef = FirebaseDatabase.getInstance().getReference().child("Children");
        childRef = FirebaseDatabase.getInstance().getReference().child("Children");
        childRef.keepSynced(true);
        //click child
        adapter = new ParentHomeAdapter(getActivity(), childrenList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {

                Child lr = childrenList.get(pos);
                String Child_id = lr.getChild_id();
                Intent in = new Intent(getActivity(), ChildHome.class);
                in.putExtra("Child_id", Child_id);
                in.putExtra("userType", userType1);
//send usertype and selected child id to the child home
                startActivity(in);
            }
        });

        recyclerView = getActivity().findViewById(R.id.recyclerPH);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        //userID="rTTwi9dfa9W425lPa2A6MiU93yz1";// بدال هذا قيت اوث عشان الاي دي حقت البارنت
        //userID="rTTwi9dfa9MiU93yz1";

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    childrenList.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Child ch = messageSnapshot.getValue(Child.class);
                        //The sent report
                        if (ch.getParent_id().equals(userID)) { //if (rep.getUser_id().equals(userID))
                            childrenList.add(ch);
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


        childrenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childrf : snapshot.getChildren()) {
                    Child findCh = childrf.getValue(Child.class);
                    String user_id = findCh.getParent_id(); //String user_id = findRep.getUser_id();
                    if (user_id.equals(userID)) {
                        textView.setText("");
                        break;
                    } else {
                        textView.setText("No Existing Children");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //actionButton.findViewById(R.id.floating_action_button);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.floating_action_button);

        //floating_action_button for add child
        fab.setOnClickListener(v -> {

//            Fragment fragment = new AddNewChildFragment();

            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction().addToBackStack(null);

//            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).commit();

            Intent intent = new Intent(getActivity(), addSocialMediaCredintals.class);
            startActivity(intent);
        });
    }
}
