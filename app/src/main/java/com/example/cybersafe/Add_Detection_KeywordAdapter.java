package com.example.cybersafe;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Keyword;
import com.example.cybersafe.Objects.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Add_Detection_KeywordAdapter extends RecyclerView.Adapter<Add_Detection_KeywordAdapter.KeywordHolder> {
    private Context context;
    private List<Keyword> keywordList;
    private OnItemClickListener listener;
    private DatabaseReference keywordsRef;
    View view;

    public Add_Detection_KeywordAdapter(Context context, List<Keyword> keywordList) {
        this.context = context;
        this.keywordList = keywordList;
    }

    public Add_Detection_KeywordAdapter(Context context, List<Keyword> keywordList, OnItemClickListener listener) {
        this.context = context;
        this.keywordList = keywordList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KeywordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_home_item, parent, false);
        Add_Detection_KeywordAdapter.KeywordHolder holder = new Add_Detection_KeywordAdapter.KeywordHolder(view);

/*        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(view, holder.getAdapterPosition());
            }
        });*/

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordHolder holder, int position) {

        keywordsRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        String keyword = keywordList.get(position).getKeyword();

        keywordsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                holder.writeChildP.setText(keyword);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    public class KeywordHolder extends RecyclerView.ViewHolder{
        TextView writeChildP;
        LinearLayout reportRe ;


        public KeywordHolder(@NonNull View itemView) {
            super(itemView);
            writeChildP = (TextView) itemView.findViewById(R.id.writeChildP);
//            reportRe = (LinearLayout) itemView.findViewById(R.id.reportRe);


        }
    }
}
