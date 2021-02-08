package com.example.ets;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText Username;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText)findViewById(R.id.usernameid);
        Password = (EditText)findViewById(R.id.passwordid);
        Info = (TextView)findViewById(R.id.tvinfo);
        Login = (Button)findViewById(R.id.loginbutton);
        Info.setText("No. of attempts remaining: 5");
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Username.getText().toString(), Password.getText().toString());
            }
        });
    }
    private void validate(String userName, String userPassword) {
        if ((userName.equals("")) && (userPassword.equals("")))
        {
            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
            startActivity(intent);
        }
        else
        {
            counter--;
            Info.setText("No. of attempts remaining: " +String.valueOf(counter));

            if(counter == 0)
            {
                Login.setEnabled(false);
            }
        }
    }
}
