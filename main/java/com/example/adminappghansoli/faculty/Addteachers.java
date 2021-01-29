package com.example.adminappghansoli.faculty;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adminappghansoli.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Addteachers extends AppCompatActivity {
    private ImageView addteacherimg;
    private EditText Addteachername,Addteacherpost,AddteacherSubject;
    private Spinner addteachercategory;
    private Button AddteacherButton;

    private final int REQ=1;
    private DatabaseReference reference,dbref;
    private StorageReference storageReference;
    private ProgressDialog pd;
    private String category;
    private Bitmap bitmap;
    private String name,Subject,post,downloadUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteachers);
        reference= FirebaseDatabase.getInstance().getReference().child("teachers");
        storageReference= FirebaseStorage.getInstance().getReference();

        pd=new ProgressDialog(this);

        addteacherimg=findViewById(R.id.addteacherimg);
        Addteachername=findViewById(R.id.Addteachername);
        Addteacherpost=findViewById(R.id.Addteacherpost);
        AddteacherSubject=findViewById(R.id.AddteacherSubject);
        addteachercategory=findViewById(R.id.addteachercategory);
        AddteacherButton=findViewById(R.id.AddteacherButton);


        String[] items=new String []{"Select Category","IT Department", "Commerce Department","Others"};
        addteachercategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));
        addteachercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=addteachercategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addteacherimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        AddteacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();

            }
        });

    }

    private void checkValidation() {
        name=Addteachername.getText().toString();
        post=Addteacherpost.getText().toString();
        Subject=AddteacherSubject.getText().toString();
        if(name.isEmpty()){
            Addteachername.setError("Empty");
            Addteachername.requestFocus();
        }else if(Subject.isEmpty()){
            AddteacherSubject.setError("Empty");
            AddteacherSubject.requestFocus();

        }else if(post.isEmpty()){
            Addteacherpost.setError("Empty");
            Addteacherpost.requestFocus();

        }else if(category.equals("Select Category")){
            Toast.makeText(this, "Please provide Teacher category", Toast.LENGTH_SHORT).show();



        }else if(bitmap==null){
            insertData();

        }else{
            uploadImage();

        }



    }

    private void uploadImage(){
        pd.setMessage("Uploading....");
        pd.show();
        ByteArrayOutputStream boas=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,boas);
        byte[] finalimg =boas.toByteArray();
        final StorageReference filePath;
        filePath=storageReference.child("teachers").child(finalimg+"jpg");
        final UploadTask uploadTask=filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(Addteachers.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    insertData();

                                }
                            });
                        }
                    });
                }else{
                    pd.dismiss();
                    Toast.makeText(Addteachers.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void insertData() {
        dbref=reference.child("category");
        final String uniquekey=dbref.push().getKey();


        TeacherData teacherData=new TeacherData(name,Subject,post,downloadUrl,uniquekey);
        dbref.child(uniquekey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(Addteachers.this, "Teacher Added", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Addteachers.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void openGallery() {
        Intent pickimage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage,REQ);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            addteacherimg.setImageBitmap(bitmap);

        }
    }
}