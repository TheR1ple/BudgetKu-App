package com.example.budgetkuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button buttonLogin, buttonBackLogin;
    DataHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.UsernameLogin);
        password = (EditText) findViewById(R.id.PasswordLogin);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonBackLogin = (Button) findViewById(R.id.buttonBackLogin);
        db = new DataHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals("")) {
                    Toast.makeText(MainActivity.this, "Username or Password Cannot be Blank", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkuserpass = db.checkuserpass(user, pass);
                    if(checkuserpass==true){
                        Toast.makeText(MainActivity.this, "Sign In Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}