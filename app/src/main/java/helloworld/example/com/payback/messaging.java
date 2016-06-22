package helloworld.example.com.payback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class messaging extends AppCompatActivity {
    int cyear, cmonth, cday;
    Context mContext;
    EditText smsTextContext;
    DBAdapter db;
    String person, cost, due;
    TextView text;
    String phnum;
    int position;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        db = new DBAdapter(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position");
        person = bundle.getString("name");
        cost = bundle.getString("cost");
        due = bundle.getString("date");
        mContext = this;
        Calendar c = Calendar.getInstance();
        text = (TextView) findViewById(R.id.send_dday);
        text.setText(due);
        cyear = c.get(Calendar.YEAR);
        cmonth = c.get(Calendar.MONTH);
        cday = c.get(Calendar.DAY_OF_MONTH);

        smsTextContext = (EditText) findViewById(R.id.smsText);
        setMessage(text.getText().toString());

        findViewById(R.id.day).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(messaging.this, dateSetListener, cyear, cmonth, cday).show();
            }
        });
    }

    public void setMessage(String msg) {
        SharedPreferences sh_Pref = getSharedPreferences("myPreference", Context.MODE_MULTI_PROCESS);
        String account = sh_Pref.getString("account", " ");
        smsTextContext.setText(person + "님, " + msg + "까지 " + cost + "을(를) " + "국민 172602-04-099448 로 보내세요.");
    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            //Change the D-day change.

            String m = (monthOfYear + 1) + "";
            if(monthOfYear + 1 < 10)
                m = "0" + m;
            String msg = String.format("%d-%s-%d", year, m, dayOfMonth);
            text.setText(msg);
            db.open();
            int w = Integer.parseInt(cost.substring(0, cost.indexOf("원")));
            //db.deleteIng(position + 1);
            //long id = db.insertIngs(person, w, msg);
            db.close();
            setMessage(text.getText().toString());

        }
    };
    public void sendSMS(View v){
        String smsText = smsTextContext.getText().toString();

        if ( smsText.length()>0){
            sendSMS(smsText);
        }else{
            Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendSMS(String smsText) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        /**
         * SMS가 발송될때 실행
         * When the SMS massage has been sent
         */
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));
        SmsManager mSmsManager = SmsManager.getDefault();
        db.open();
        Cursor c = db.getContact(person);
        if (c.moveToFirst()) {
            do {
                phnum = c.getString(1);
            } while(c.moveToNext());
        }
        db.close();
        mSmsManager.sendTextMessage(phnum, null, smsText, sentIntent, deliveredIntent);
        startActivity(new Intent(messaging.this, MainActivity.class));
    }
}