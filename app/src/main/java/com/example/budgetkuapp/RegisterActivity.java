package com.example.budgetkuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password;
    Button buttonRegister, buttonBackRegister;
    DataHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.UsernameRegister);
        password = (EditText) findViewById(R.id.PasswordRegister);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonBackRegister= (Button) findViewById(R.id.buttonBackRegister);
        db = new DataHelper(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Username or Password Cannot be Blank", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuser = db.checkusername(user);
                    if(checkuser==false){
                        Boolean insert = db.insertData(user, pass);
                        if(insert==true){
                            Toast.makeText(RegisterActivity.this, "Successfully Sign Up!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegisterActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "User Already Exists! Please Sign In", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });

        buttonBackRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}