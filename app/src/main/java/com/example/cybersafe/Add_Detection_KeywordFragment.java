
package com.example.cybersafe;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.cybersafe.Objects.Keyword;
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
public class Add_Detection_KeywordFragment extends Fragment {

    public Button dK;
    private String userID, userType;
    DatabaseReference keywordsRef, keywordRef;
    RecyclerView recyclerView;
    ArrayList<Keyword> keywordArrayList= new ArrayList();
    Add_Detection_KeywordAdapter adapter;


    public Add_Detection_KeywordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_add__detection__keyword, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            //userType = getIntent().getStringExtra("userType");
            userType = getActivity().getIntent().getExtras().getString("userType");

        } else {
            System.out.println("userID out");
            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(getActivity(), ParentLogin.class);
            startActivity(in);
        }

        TextView textView = (TextView) getActivity().findViewById(R.id.noKeyword);

        keywordsRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        keywordRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        keywordRef.keepSynced(true);

        recyclerView = getActivity().findViewById(R.id.recyclerViewAddDe);
        adapter = new Add_Detection_KeywordAdapter(getActivity(), keywordArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //Now we get the keyword List
        keywordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keywordArrayList.clear();
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Keyword key = messageSnapshot.getValue(Keyword.class);
                        //The comment not bully and same as the child's SMAccountCredential ID add it to the comment list
                        if (key.getParent_id().equals(userID)){
                            keywordArrayList.add(key);
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
        //If no comment found print no existing comments
        keywordsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotrf) {
                for (DataSnapshot childrf : snapshotrf.getChildren()) {
                    Keyword findKey = childrf.getValue(Keyword.class);
                    //String acu = findCom.getSMAccountCredentials_Account();
                    if (findKey.getParent_id().equals(userID)) {
                        textView.setText("");
                        break;
                    } else {
                        textView.setText("no existing Keyword");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Add-Detection-Keyword-Button
/*
        dK= getActivity().findViewById(R.id.addDetectionK);

        dK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent inn = new Intent(getActivity(),Add_Detection_Keyword2.class);
                inn.putExtra("userType",userType);
                startActivity(inn);
            }
        });*/

    }
}
