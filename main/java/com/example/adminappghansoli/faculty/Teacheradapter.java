package com.example.adminappghansoli.faculty;


import android.content.Context;
import android.content.Intent;
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
import com.example.adminappghansoli.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.security.auth.Subject;

public class Teacheradapter extends RecyclerView.Adapter<Teacheradapter.Teacherviewadpter> {
    private List<TeacherData> list;
    private Context context;
    private String category;

    public Teacheradapter(List<TeacherData> list, Context context,String category) {
        this.list = list;
        this.context = context;
        this.category=category;
    }

    @NonNull
    @Override
    public Teacherviewadpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.faculty_item_layout, parent,false);
        return new Teacherviewadpter(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Teacherviewadpter holder, int position) {
        TeacherData item=list.get(position);
        holder.name.setText(item.getName());
        holder.name.setText(item.getSubject());
        holder.name.setText(item.getPost());
        try {
            Picasso.get().load(item.getImage()).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UpdateTeacherActivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("Subject",item.getSubject());
                intent.putExtra("post",item.getPost());
                intent.putExtra("Image",item.getImage());
                intent.putExtra("key",item.getKey());
                intent.putExtra("category",category);
                context.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Teacherviewadpter extends RecyclerView.ViewHolder {
        private TextView name, Subject, post;
        private Button update;
        private ImageView imageView;


        public Teacherviewadpter(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.teachername);
            Subject=itemView.findViewById(R.id.teacherSubject);
            post=itemView.findViewById(R.id.teacherpost);
            update=itemView.findViewById(R.id.teachbutton);
            imageView=itemView.findViewById(R.id.teacherimg);
        }
    }
}

