package com.proj.limtick;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.proj.limtick.Interface.OnSeatSelected;
import com.proj.limtick.prevalent.AbstractItem;
import com.proj.limtick.prevalent.AirplaneAdapter;
import com.proj.limtick.prevalent.CenterItem;
import com.proj.limtick.prevalent.EdgeItem;
import com.proj.limtick.prevalent.EmptyItem;

import java.util.ArrayList;
import java.util.List;

public class SeatActivity extends AppCompatActivity implements OnSeatSelected {
    private static final int COLUMNS = 4;
    private TextView txtSeatSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        txtSeatSelected = (TextView)findViewById(R.id.txt_seat_selected);

        List<AbstractItem> items = new ArrayList<>();
        for (int i=0; i<10; i++) {

            if (i%COLUMNS==0 || i%COLUMNS==4) {
                items.add(new EdgeItem(String.valueOf(i)));
            } else if (i%COLUMNS==1 || i%COLUMNS==3) {
                items.add(new CenterItem(String.valueOf(i)));

            }
        }
        GridLayoutManager manager = new GridLayoutManager(this,COLUMNS);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);

        AirplaneAdapter adapter = new AirplaneAdapter(this, items);
        recyclerView.setAdapter(adapter);



    }
    @Override
    public void onSeatSelected(int count) {

        txtSeatSelected.setText("Book "+count+" seats");
    }
}
