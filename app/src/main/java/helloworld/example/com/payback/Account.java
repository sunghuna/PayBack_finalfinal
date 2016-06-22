package helloworld.example.com.payback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Account extends AppCompatActivity {

    EditText e1, e2;
    Button bt;

    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        sh_Pref = getSharedPreferences("myPreference", Context.MODE_MULTI_PROCESS);
        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);
        bt = (Button) findViewById(R.id.button);
        e1.setText("국민");
        e2.setText("172602-04-099448");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sh_Pref != null && sh_Pref.contains("account")) {
                    toEdit = sh_Pref.edit();
                    Toast.makeText(getApplicationContext(), e1.getText().toString(), Toast.LENGTH_SHORT).show();
                    toEdit.putString("account", "국민");
                    toEdit.putString("bank", "172602-04-099448");
                    toEdit.commit();
                }
                startActivity(new Intent(Account.this, MainActivity.class));

            }
        });

    }
    public void sharedPreferences() {
        sh_Pref = getSharedPreferences("myPreference", Context.MODE_MULTI_PROCESS);
        //패스워드 없을 경우 초기값 설정
        if (!(sh_Pref.contains("account"))) {
            toEdit = sh_Pref.edit();
            toEdit.putString("account", "");
            toEdit.commit();
        }
    }
}
