package com.example.adminappghansoli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//import com.example.adminappghansoli.faculty.Updatefaculty;
//import com.example.adminappghansoli.notice.DeletenoticeActivity;
import com.example.adminappghansoli.faculty.Updatefaculty;
import com.example.adminappghansoli.notice.DeletenoticeActivity;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    CardView uploadnotice, addGallery, addEbook,addfaculty,deletenotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadnotice=findViewById((R.id.addNotice));
        addGallery=findViewById((R.id.addGallery));
        addEbook=findViewById((R.id.addEbook));
        addfaculty=findViewById((R.id.addfaculty));
        deletenotice=findViewById((R.id.deletenotice));


        uploadnotice.setOnClickListener(this);
        addGallery.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        addfaculty.setOnClickListener(this);
        deletenotice.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.addNotice:
                intent=new Intent(MainActivity.this, com.example.adminappghansoli.notice.uploadnotice.class);
                startActivity(intent);
                break;
            case R.id.addGallery:
                intent=new Intent(MainActivity.this,Uploadimage.class);
                startActivity(intent);
                break;
            case R.id.addEbook:
                intent=new Intent(MainActivity.this,Uploadpdf.class);
                startActivity(intent);
                break;
            case R.id.addfaculty:
                intent=new Intent(MainActivity.this, Updatefaculty.class);
                startActivity(intent);
                break;
            case R.id.deletenotice:
                intent=new Intent(MainActivity.this, DeletenoticeActivity.class);
                startActivity(intent);
                break;

        }


    }
}