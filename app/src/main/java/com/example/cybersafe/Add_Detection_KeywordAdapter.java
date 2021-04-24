package com.example.cybersafe;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Keyword;
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.keyword_item, parent, false);
        Add_Detection_KeywordAdapter.KeywordHolder holder = new Add_Detection_KeywordAdapter.KeywordHolder(view);



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
        String id = keywordList.get(position).getKeyword_id();
        DatabaseReference keywordsReff = FirebaseDatabase.getInstance().getReference().child("Keywords");
        // if the user click "x" to delete the keyword
        holder.botBully2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordsReff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ch : snapshot.getChildren()) {

                            Keyword findKeyword =  ch.getValue(Keyword.class);
                            String KeyId = findKeyword.getKeyword_id();


                            if(KeyId.equals(id)){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                // Setting Alert Dialog Message
                                alertDialogBuilder.setMessage("Are you want to Delete this Keyword?");

                                // confirm delete
                                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //remove the keyword
                                        keywordsReff.child(KeyId).removeValue();
                                        //feedback for flagging
                                        Toast.makeText(context, "The keyword has been deleted Successfully", Toast.LENGTH_LONG).show();

                                    }
                                }).setNegativeButton("No", null).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
           }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    public class KeywordHolder extends RecyclerView.ViewHolder{
        TextView writeChildP;
        ImageView botBully2;

        public KeywordHolder(@NonNull View itemView) {
            super(itemView);
            writeChildP = (TextView) itemView.findViewById(R.id.writeKeyword);
            botBully2 = itemView.findViewById(R.id.botBully2);


        }
    }
}
