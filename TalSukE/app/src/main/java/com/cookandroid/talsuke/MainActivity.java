package com.cookandroid.talsuke;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText userpw;
    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("탈숙이");

        username = findViewById(R.id.login_id);
        userpw = findViewById(R.id.login_pw);
        String username = getSharedPreferences("SESSION", MODE_PRIVATE).getString("username", "");
        System.out.println("AAAA" + username);
        if (username != null && !username.equals("")) {
            Toast.makeText(getApplicationContext(), getSharedPreferences("SESSION", MODE_PRIVATE).getString("username", "")+"님 환영합니다.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

    void login(View v) throws JSONException, IOException {
        JSONObject userInfo = new JSONObject();
        userInfo.put("username", username.getText().toString());
        userInfo.put("password", userpw.getText().toString());

        @SuppressLint("StaticFieldLeak")
        JsonConnection connection = new JsonConnection(Constant.LOGIN_URL) {
            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    if (jsonObject == null) {
                        Toast.makeText(getApplicationContext(), "에러 발생", Toast.LENGTH_LONG).show();
                    } else if (jsonObject.getString("message").equals("Exist")) {
                        SharedPreferences.Editor editor = getSharedPreferences("SESSION", MODE_PRIVATE).edit();
                        editor.putString("username", username.getText().toString());
                        editor.apply();
                        Intent intent = new Intent();
                        if (jsonObject.getString("permission").equals("user")) {
                            intent = new Intent(getApplicationContext(), HomeActivity.class);
                            Toast.makeText(getApplicationContext(), getSharedPreferences("SESSION", MODE_PRIVATE).getString("username", "")+"님 환영합니다.", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "해당 계정이 없습니다.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "에러 발생", Toast.LENGTH_LONG).show();
                }
            }
        };
        connection.execute(userInfo);
    }

    void temp(View v){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    void register(View v){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }


}
