package com.example.cybersafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Report;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ParentHomeAdapter extends RecyclerView.Adapter<ParentHomeAdapter.ChildHolder> {
    View view;
    private Context context;
    private List<Child> childList;
    private OnItemClickListener listener;


    public ParentHomeAdapter(List<Child> childList, OnItemClickListener listener) {
        this.childList = childList;
        this.listener = listener;
    }

    public ParentHomeAdapter(Context context, List<Child> childList, OnItemClickListener listener) {
        this.context = context;
        this.childList = childList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_home_item, parent, false);
        ParentHomeAdapter.ChildHolder holder = new ParentHomeAdapter.ChildHolder(view);
        System.out.println("onCreateViewHolder");
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.OnItemClick(view, holder.getAdapterPosition());

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
        String name = childList.get(position).getFirstName();
        String gender=childList.get(position).getGender();
        holder.writeChildP.setText(name);

        if (gender.equals("Male")) {
            holder.icon.setBackground(ContextCompat.getDrawable(context, R.drawable.people_avatar_boy_child_person_icon_131264));//Boy Avatar
        }
        else{
            holder.icon.setBackground(ContextCompat.getDrawable(context, R.drawable.avatar_woman_female_girl_people_icon_131282));//Girl Avatar

        }



    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public class ChildHolder extends RecyclerView.ViewHolder{
        TextView writeChildP;
        ImageView icon;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            writeChildP = (TextView) itemView.findViewById(R.id.writeChildP);
            icon=itemView.findViewById(R.id.Avatar);


        }
    }

}
