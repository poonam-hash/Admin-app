package com.example.adminappghansoli.faculty;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateTeacherActivity extends AppCompatActivity {
    private ImageView updateTeacherimg;
    private EditText updateTeachername,updateTeacherSubject,updateTeacherpost;
    private Button updateTeacherbtn,deleteTeacherbtn;
    private String name,Subject,Image,post,downloadUrl="",category,uniquekey;
    private final int REQ=1;
    private Bitmap bitmap=null;
    private StorageReference storageReference;
    private DatabaseReference reference,dbref;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        name=getIntent().getStringExtra("name");
        Subject=getIntent().getStringExtra("Subject");
        post=getIntent().getStringExtra("post");
        Image=getIntent().getStringExtra("Image");

        storageReference= FirebaseStorage.getInstance().getReference();
        reference= FirebaseDatabase.getInstance().getReference().child("teachers");

        pd=new ProgressDialog(this);
        uniquekey=getIntent().getStringExtra("key");
        category=getIntent().getStringExtra("category");


        updateTeacherimg=findViewById(R.id.updateTeacherimg);
        updateTeachername=findViewById(R.id.updateTeachername);
        updateTeacherSubject=findViewById(R.id.updateTeacherSubject);
        updateTeacherpost=findViewById(R.id.updateTeacherpost);
        updateTeacherbtn=findViewById(R.id.updateTeacherbtn);
        deleteTeacherbtn=findViewById(R.id.deleteTeacherbtn);


        try {
            Picasso.get().load(Image).into(updateTeacherimg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTeachername.setText(name);
        updateTeacherSubject.setText(Subject);
        updateTeacherpost.setText(post);

        updateTeacherimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
        updateTeacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=updateTeachername.getText().toString();
                Subject=updateTeacherSubject.getText().toString();
                post=updateTeacherpost.getText().toString();
                checkValidation();

            }
        });
        deleteTeacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedata();
            }
        });
    }

    private void deletedata() {
        reference.child(category).child(uniquekey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateTeacherActivity.this, "Teacher Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UpdateTeacherActivity.this,Updatefaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void checkValidation() {
        if(name.isEmpty()){
            updateTeachername.setError("Empty");
            updateTeachername.requestFocus();
        }else if(Subject.isEmpty()){
            updateTeacherSubject.setError("Empty");
            updateTeacherSubject.requestFocus();
        }else if(post.isEmpty()){
            updateTeacherpost.setError("Empty");
            updateTeacherpost.requestFocus();
        }else if(bitmap==null){
            updatedata("Image");


        }else{
            uploadImage();
        }

    }

    private void uploadImage(){
        //pd.setMessage("Uploading....");
        //pd.show();
        ByteArrayOutputStream boas=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,boas);
        byte[] finalimg =boas.toByteArray();
        final StorageReference filePath;
        filePath=storageReference.child("teachers").child(finalimg+"jpg");
        final UploadTask uploadTask=filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UpdateTeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    updatedata(downloadUrl);

                                }
                            });
                        }
                    });
                }else{
                    //pd.dismiss();
                    Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updatedata(String s) {
        HashMap hp=new HashMap();
        hp.put("name",name);
        hp.put("Subject",Subject);
        hp.put("post",post);
        hp.put("Image",s);


        reference.child(category).child(uniquekey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateTeacherActivity.this, "Teacher Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UpdateTeacherActivity.this,Updatefaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

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
            updateTeacherimg.setImageBitmap(bitmap);

        }
    }
}