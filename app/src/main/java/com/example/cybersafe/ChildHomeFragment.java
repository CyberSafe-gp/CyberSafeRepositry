package com.example.cybersafe;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChildHomeFragment extends Fragment {

    public Button btn1,btn4,btn5,btn6;
    LinearLayout bullyComments, flagComments;
//btn1 for buuly coment page transfer button
// btn4 for flag page button transfer button
// btn5 for edit page transfer button
// btn6 for delete child button



    String ChildID,userType1,userID;


    public ChildHomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_child_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = this.getArguments();
       // int myInt = bundle.getInt(key, defaultValue);
        //get the current loged-in user
        if (user != null) {

            userID = user.getUid();
            //bring user id
            //ChildID = getActivity().getIntent().getStringExtra("Child_id");
            ChildID = bundle.getString("Child_id");
            //take from the parent home page the selected child id
            //userType1=getActivity().getIntent().getStringExtra("userType");
            userType1= bundle.getString("userType");
            //store what type of loged-in user is
        } else {
            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(getActivity(), ParentLogin.class);
            startActivity(in);
            //to go back for the parent home
        }


        bullyComments = getActivity().findViewById(R.id.bullyComments);
        //bully comment



        flagComments=  getActivity().findViewById(R.id.flagComment);
        //flag



        bullyComments.setOnClickListener(v -> {
/*            Intent intent =new Intent(getActivity(), BullyCommentMain.class);
            intent.putExtra("Child_id",ChildID);
            startActivity(intent);*/

            //Go to Bully Comments page
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = new BullyCommentMainFragment();
            Bundle bundleBully= new Bundle();
            bundleBully.putString("Child_id", ChildID);
            fragment.setArguments(bundleBully);
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).addToBackStack(null).commit();


        });





        flagComments.setOnClickListener(v -> {
  //          Intent intent = new Intent(getActivity(),FlagMain.class);
//            intent.putExtra("Child_id",ChildID);
//            startActivity(intent);

            //Go to Flag page
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = new FlagMainFragment();
            Bundle bundleFlag = new Bundle();
            bundleFlag.putString("Child_id", ChildID);
            fragment.setArguments(bundleFlag);
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).addToBackStack(null).commit();

        });


  /*      btn5.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),Edit_Child_Profile.class);
            intent.putExtra("Child_id",ChildID);
            startActivity(intent);

        });*/




    }
}
