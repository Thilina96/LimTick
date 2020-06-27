package com.proj.limtick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proj.limtick.Model.Routes;
import com.squareup.picasso.Picasso;
import android.widget.TimePicker;
import java.util.Calendar;

public class SelectActivity extends AppCompatActivity {

    private ImageView rouImage;
    private ElegantNumberButton numbutton;
    TextView routename,routedesc,routeprice;
    private String routeID = "";
    EditText time_edt;
    TimePickerDialog timePickerDialog;
    String amPm;
    private final static int TIME_PICKER_INTERVAL = 15;
    private boolean mIgnoreEvent=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        routeID = getIntent().getStringExtra("rid");

        numbutton = (ElegantNumberButton) findViewById(R.id.number_btn);
        rouImage = (ImageView) findViewById(R.id.select_image);
        routename = (TextView) findViewById(R.id.select_name);
        routedesc = (TextView) findViewById(R.id.select_desc);
        routeprice = (TextView) findViewById(R.id.select_price);
        time_edt=(EditText)findViewById(R.id.selecttime);


        getRouteDetails(routeID);

        time_edt.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                timePickerDialog= new TimePickerDialog(SelectActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        int nextMinute = 0;
                        if (minutes> 0 && minutes <= 15)
                            nextMinute = 15;
                        else if(minutes > 15 && minutes <= 30)
                            nextMinute = 30;
                        else if(minutes > 30 && minutes <= 45)
                            nextMinute = 45;

                        else {
                            nextMinute = 0;
                        }

                        if (hourOfDay>=12){
                            amPm="PM";
                        }else {
                            amPm="AM";
                        }


                        time_edt.setText(String.format("%02d:%02d", hourOfDay,nextMinute) + amPm );


                    }
                },0,0, false);
                        timePickerDialog.show();




            }
        });
        
    }



    private void getRouteDetails(String routeID)
    {
        DatabaseReference routesRef = FirebaseDatabase.getInstance().getReference().child("Routes");

        routesRef.child(routeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Routes routes = dataSnapshot.getValue(Routes.class);

                    routename.setText(routes.getRoutename());
                    routeprice.setText(routes.getPrice());
                    routedesc.setText(routes.getDescription());
                    Picasso.get().load(routes.getImage()).into(rouImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
