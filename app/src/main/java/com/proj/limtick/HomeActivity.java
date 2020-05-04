package com.proj.limtick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.proj.limtick.Model.Routes;
import com.proj.limtick.ViewHolder.RouteViewHolder;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    private DatabaseReference RoutesRef;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        RoutesRef = FirebaseDatabase.getInstance().getReference().child("Routes");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.lay);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerOptions<Routes>options=
                new FirebaseRecyclerOptions.Builder<Routes>()
                .setQuery(RoutesRef,Routes.class)
                .build();
        FirebaseRecyclerAdapter<Routes, RouteViewHolder> adapter =
                new FirebaseRecyclerAdapter<Routes, RouteViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RouteViewHolder holder, int position, @NonNull Routes model) {
                        holder.txtRouteName.setText(model.getRoutename());
                        holder.txtRouteDescription.setText(model.getDescription());
                        holder.txtRoutePrice.setText(model.getPrice() + " lkr");
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                    }

                    @NonNull
                    @Override
                    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_layout, parent, false);
                        RouteViewHolder holder = new RouteViewHolder(view);
                        return holder;
                    }

                };
      recyclerView.setAdapter(adapter);
      adapter.startListening();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){
            case R.id.action_logout:
                displayMessage("Successfully Logged Out..");

                Paper.book().destroy();
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


                case  R.id.action_add:
                Intent intent1 = new Intent(HomeActivity.this,AdminAddNewRouteActivity.class);
                startActivity(intent1);


        }
        return super.onOptionsItemSelected(item);
    }
    private void displayMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
