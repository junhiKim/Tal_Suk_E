package com.cookandroid.talsuke.Settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cookandroid.talsuke.Main.Constant;
import com.cookandroid.talsuke.Main.JsonConnection;
import com.cookandroid.talsuke.Main.MainActivity;
import com.cookandroid.talsuke.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SettingRegDelActivity extends AppCompatActivity {

    Button regDelOK;
    Button regDelCancel;
    EditText regDelPW;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_reg_del);
        this.setTitle("회원 탈퇴");

        regDelOK = (Button) findViewById(R.id.reg_del_ok);
        regDelCancel = (Button) findViewById(R.id.reg_del_cancel);
        regDelPW = (EditText) findViewById(R.id.reg_del_pw);

        regDelOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (regDelPW.toString().replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingRegDelActivity.this);
                    builder.setTitle("정말 탈퇴하시겠습니까?");
                    ((AlertDialog.Builder) builder).setMessage("회원정보는 삭제됩니다.");
                    ((AlertDialog.Builder) builder).setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                JSONObject userInfo = new JSONObject();
                                userInfo.put("username", getSharedPreferences("SESSION", MODE_PRIVATE).getString("username", ""));
                                userInfo.put("password", regDelPW.getText().toString());
                                @SuppressLint("StaticFieldLeak") JsonConnection connection = new JsonConnection(Constant.LEAVE_URL){
                                    @Override
                                    protected void onPostExecute(JSONObject jsonObject) {
                                        System.out.println(jsonObject);
                                        try {
                                            if (jsonObject.getString("message").equals("Success")) {
                                                SharedPreferences.Editor editor = getSharedPreferences("SESSION", MODE_PRIVATE).edit();
                                                editor.remove("username");
                                                editor.remove("fee");
                                                editor.apply();
                                                Toast.makeText(getApplicationContext(), "회원 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                connection.execute(userInfo);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            finish();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }

            }
        });

        regDelCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
