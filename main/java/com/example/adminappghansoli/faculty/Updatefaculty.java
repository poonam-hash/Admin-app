package com.example.adminappghansoli.faculty;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adminappghansoli.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Updatefaculty extends AppCompatActivity {
    FloatingActionButton fab;
    private RecyclerView ITDepartment,CsDepartment;
    private LinearLayout ITnodata,Csnodata;
    private List<TeacherData> list1,list2;
    private DatabaseReference  reference,dbref;
    private Teacheradapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatefaculty);
        fab=findViewById(R.id.fab);
        ITDepartment=findViewById(R.id.ITDepartment);
        CsDepartment=findViewById(R.id.CsDepartment);
        ITnodata=findViewById(R.id.ITnodata);
        Csnodata=findViewById(R.id.Csnodata);
        reference= FirebaseDatabase.getInstance().getReference().child("teachers");

        ITDepartment();
        CsDepartment();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Updatefaculty.this,Addteachers.class));

            }
        });
    }

    private void ITDepartment() {
        dbref=reference.child("Information Technology");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1=new ArrayList<>();
                if(!snapshot.exists()){
                    ITnodata.setVisibility(View.VISIBLE);
                    ITDepartment.setVisibility(View.GONE);

                }else{
                    ITnodata.setVisibility(View.GONE);
                    ITDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    ITDepartment.setHasFixedSize(true);
                    ITDepartment.setLayoutManager(new LinearLayoutManager(Updatefaculty.this));
                    adapter=new Teacheradapter(list1,Updatefaculty.this,"Information Technology");
                    ITDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Updatefaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void CsDepartment() {
        dbref=reference.child("Computer Science");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2=new ArrayList<>();
                if(!snapshot.exists()){
                    Csnodata.setVisibility(View.VISIBLE);
                    CsDepartment.setVisibility(View.GONE);

                }else{
                    Csnodata.setVisibility(View.GONE);
                    CsDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    CsDepartment.setHasFixedSize(true);
                    CsDepartment.setLayoutManager(new LinearLayoutManager(Updatefaculty.this));
                    adapter=new Teacheradapter(list2,Updatefaculty.this,"Computer Science");
                    CsDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Updatefaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}