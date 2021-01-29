package com.example.adminappghansoli.notice;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminappghansoli.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class noticeadapter  extends RecyclerView.Adapter<noticeadapter.NoticeviewAdapter> {
    private Context context;
    private ArrayList<NoticeData> list;

    public noticeadapter(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeviewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.newsfeeddelete, parent,false);
        return new NoticeviewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeviewAdapter holder, int position) {
        NoticeData currentItem=list.get(position);
        holder.deletenoticeTitle.setText(currentItem.getTitle());
        try {
            if(currentItem.getImage() !=null)
                Picasso.get().load(currentItem.getImage()).into(holder.deletenoticeimg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.deletenotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Notice");
                reference.child(currentItem.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();


                    }
                });
                notifyItemRemoved(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoticeviewAdapter extends RecyclerView.ViewHolder {
        private Button deletenotice;
        private TextView deletenoticeTitle;
        private ImageView deletenoticeimg;

        public NoticeviewAdapter(@NonNull View itemView) {
            super(itemView);
            deletenotice=itemView.findViewById(R.id.deletenotice);
            deletenoticeTitle=itemView.findViewById(R.id.deletenoticeTitle);
            deletenoticeimg=itemView.findViewById(R.id.deletenoticeimg);

        }
    }
}
