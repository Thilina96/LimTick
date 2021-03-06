package com.proj.limtick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proj.limtick.Model.Users;
import com.proj.limtick.prevalent.prevalent;

public class LoginActivity extends AppCompatActivity {
    private EditText InputNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private  String parentDbName="Users";
    private TextView AdminLink,NotAdminLink;
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputNumber = (EditText) findViewById(R.id.login_phone_num);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        AdminLink=(TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);
        loadingBar= new ProgressDialog(this);

        chkBoxRememberMe=(CheckBox) findViewById(R.id.remember_me);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";

            }
        });
    }

    private void loginUser() {
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

          if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Enter a phone number..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter the Password..", Toast.LENGTH_SHORT).show();
        }
        else
            {
                loadingBar.setTitle("Login Account");
                loadingBar.setMessage("Please wait while we are checking the Credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                AllowAccessToAccount(phone,password);


            }
    }

    private void AllowAccessToAccount(final String phone, final String password) {

        if (chkBoxRememberMe.isChecked())
        {
            Paper.book().write(prevalent.UserPhoneKey,phone);
            Paper.book().write(prevalent.UserPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData= dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "logged in Successfully..", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,AdminHomeActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "logged in Successfully..", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Invalid Password..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }
                else
                    {
                        Toast.makeText(LoginActivity.this, "Account with this "+ phone +" number do not exist", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
