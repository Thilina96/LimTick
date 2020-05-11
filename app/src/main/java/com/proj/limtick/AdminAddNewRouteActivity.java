package com.proj.limtick;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewRouteActivity extends AppCompatActivity {

    private Button AddNewRouteButton;
    private ImageView InputRouteImage;
    private EditText InputRouteName,InputRouteDescription,InputRoutePrice;
    private static final int GalleryPic=1;
    private Uri ImageUri;
    private String Description,Price,rname,saveCurrentDate,saveCurrentTime;
    private String routeRandomKey,downloadImageUrl;
    private StorageReference RouteImagesRef;
    private DatabaseReference RoutesRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_route);

        RouteImagesRef = FirebaseStorage.getInstance().getReference().child("Route Images");
        RoutesRef=FirebaseDatabase.getInstance().getReference().child("Routes");

        AddNewRouteButton = (Button) findViewById(R.id.add_new_route);
        InputRouteImage = (ImageView) findViewById(R.id.select_route_image);
        InputRouteName = (EditText) findViewById(R.id.route_name);
        InputRouteDescription = (EditText) findViewById(R.id.route_description);
        InputRoutePrice = (EditText) findViewById(R.id.route_price);
        loadingBar= new ProgressDialog(this);

        InputRouteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        AddNewRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateRouteData();
            }
        });
    }

    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPic && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            InputRouteImage.setImageURI(ImageUri);
        }
    }

    private void ValidateRouteData()
    {
        Description= InputRouteDescription.getText().toString();
        Price= InputRoutePrice.getText().toString();
        rname= InputRouteName.getText().toString();

        if (ImageUri==null)
        {
            Toast.makeText(this, "Product Image is required...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please enter Route Description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please enter Route Price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(rname))
        {
            Toast.makeText(this, "Please enter Route ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            storeRouteInformation();

        }
    }

    private void storeRouteInformation() {
        loadingBar.setTitle("Add New Route");
        loadingBar.setMessage("Please wait while we are adding the new route");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        routeRandomKey= saveCurrentDate + saveCurrentTime;

        final StorageReference filepath = RouteImagesRef.child(ImageUri.getLastPathSegment()+ routeRandomKey +".jpg");

        final UploadTask uploadTask=filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message =e.toString();
                Toast.makeText(AdminAddNewRouteActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewRouteActivity.this, "Image Uploaded Succesfully..", Toast.LENGTH_SHORT).show();

                Task<Uri>urlTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();

                            Toast.makeText(AdminAddNewRouteActivity.this, "Image data url saved to Database", Toast.LENGTH_SHORT).show();

                            SaveRouteInfoToDatabase();
                        }

                    }
                });

            }
        });
    }

    private void SaveRouteInfoToDatabase() {

        HashMap<String,Object>routeMap=new HashMap<>();
        routeMap.put("rid",routeRandomKey);
        routeMap.put("date",saveCurrentDate);
        routeMap.put("time",saveCurrentTime);
        routeMap.put("description",Description);
        routeMap.put("image",downloadImageUrl);
        routeMap.put("price",Price);
        routeMap.put("routename",rname);

        RoutesRef.child(routeRandomKey).updateChildren(routeMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewRouteActivity.this,HomeActivity.class);
                            startActivity(intent);


                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewRouteActivity.this, "Route added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else 
                        {
                            loadingBar.dismiss();
                            String message=task.getException().toString();
                            Toast.makeText(AdminAddNewRouteActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
