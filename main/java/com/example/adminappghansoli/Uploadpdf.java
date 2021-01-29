package com.example.adminappghansoli;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Uploadpdf extends AppCompatActivity {
    private CardView addPdf;
    private EditText PdfTitle;
    private Button UploadPdfButton;
    private TextView pdftextview;

    private final int REQ = 1;
    private Uri pdfdata;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl="";
    private String pdfname,title;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpdf);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        pd=new ProgressDialog(this);
        addPdf=findViewById(R.id.addPdf);
        PdfTitle=findViewById(R.id.PdfTitle);
        UploadPdfButton=findViewById(R.id.UploadPdfButton);
        pdftextview=findViewById(R.id.pdftextview);
        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        UploadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                title=PdfTitle.getText().toString();
                if(title.isEmpty()){
                    PdfTitle.setError("Empty");
                    PdfTitle.requestFocus();

                }else if(pdfdata== null){
                    Toast.makeText(Uploadpdf.this, "Please Upload PDf", Toast.LENGTH_SHORT).show();
                }else{
                    Uploadpdf();
                }
            }
        });
    }

    private void Uploadpdf() {
        pd.setTitle("Please wait ..");
        pd.setMessage("Uploading PDF...");
        pd.show();
        StorageReference reference=storageReference.child("pdf/"+ pdfname +"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfdata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(uriTask.isComplete());
                Uri uri=uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Uploadpdf.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void uploadData(String valueOf) {
        String uniquekey=databaseReference.child("pdf").push().getKey();
        HashMap data=new HashMap();
        data.put("pdftitle",title);
        data.put("pdfUrl",downloadUrl);
        databaseReference.child("pdf").child(uniquekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(Uploadpdf.this, "PDF Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                PdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Uploadpdf.this, "Failed to Upload", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void openGallery() {
        Intent intent=new Intent();
        intent.setType("*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQ);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK) {
            pdfdata=data.getData();
            if (pdfdata.toString().startsWith("content://")){
                Cursor cursor=null;
                try {
                    cursor=Uploadpdf.this.getContentResolver().query(pdfdata,null,null,null,null);
                    if(cursor != null && cursor.moveToFirst()){
                        pdfname=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if(pdfdata.toString().startsWith(("file://")))
            {
                pdfname=new File(pdfdata.toString()).getName();
            }
            pdftextview.setText(pdfname);

        }
    }
}