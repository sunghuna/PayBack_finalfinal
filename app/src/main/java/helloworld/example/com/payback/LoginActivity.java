package helloworld.example.com.payback;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;

public class LoginActivity extends Activity {

    private final CryptWithMD5 encryption = new CryptWithMD5();
    private TextView notice;
    private EditText pw[] = new EditText[4];
    private int[] pwId = {R.id.pw1, R.id.pw2, R.id.pw3, R.id.pw4};
    private Button btn[] = new Button[12];
    private int[] btnId = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn, R.id.btn0, R.id.btnX};
    private static MessageDigest md;
    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;
    private int count = 0;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityStacks.getInstance().addActivity(this);
        notice = (TextView) findViewById(R.id.notice);
        //프리퍼런스 생성
        sharedPreferences();
        for (int i = 0; i < pwId.length; i++) {
            pw[i] = (EditText) findViewById(pwId[i]);
            pw[i].setFocusableInTouchMode(false);
        }
        for (int i = 0; i < btnId.length; i++) {
            btn[i] = (Button) findViewById(btnId[i]);
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < btnId.length; j++) {
                        if (v.getId() == btn[j].getId() && j != 9) {
                            if (j == 11) {
                                if (count > 0) pw[--count].setText("");
                            } else {
                                if (count < 4) pw[count++].setText(btn[j].getText().toString());
                            } //4자리 들어오는 순간 기존 패스워드와 비교
                            if (count == 4) {
                                String origin = "";
                                sh_Pref = getSharedPreferences("myPreference", Context.MODE_MULTI_PROCESS);
                                if (sh_Pref != null && sh_Pref.contains("password")) {
                                    origin = sh_Pref.getString("password", CryptWithMD5.cryptWithMD5("0000"));
                                    if (origin.startsWith("00")) {
                                        origin = CryptWithMD5.cryptWithMD5(origin);
                                    }
                                }
                                String password = "";
                                for (int i = 0; i < 4; i++) {
                                    password += pw[i].getText().toString();
                                }

                                Log.d(" LoginActivity ", " input " + password);
                                //패스워드 일치시 액티비티 이동
                                if (CryptWithMD5.cryptWithMD5(password).equals(origin)) {
                                    for (int i = 0; i < 4; i++) {
                                        pw[i].setText("");
                                    }
                                    count = 0;
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                                //불일치시 토스트 띄워주고 리셋
                                else {
                                    notice.setText("Incorrect password.\nCheck your password.");
                                    for (int i = 0; i < 4; i++) {
                                        pw[i].setText("");
                                    }
                                    count = 0;
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public void sharedPreferences() {
        sh_Pref = getSharedPreferences("myPreference", Context.MODE_MULTI_PROCESS);
        //패스워드 없을 경우 초기값 설정
        if (!(sh_Pref.contains("password"))) {
            toEdit = sh_Pref.edit();
            toEdit.putString("password", CryptWithMD5.cryptWithMD5("0000"));
            toEdit.commit();
        }
    }
}
