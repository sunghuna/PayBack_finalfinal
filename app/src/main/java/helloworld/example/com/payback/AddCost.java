package helloworld.example.com.payback;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddCost extends Activity {

    private Button back;
    private ImageButton btn[] = new ImageButton[12];
    private int[] btnId = {R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6,
            R.id.n7, R.id.n8, R.id.n9, R.id.x, R.id.won};
    private String prev = "";
    private EditText input;
    private int type = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);
        ActivityStacks.getInstance().addActivity(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = bundle.getInt("type");

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        input = (EditText)findViewById(R.id.input);
        for(int i = 0; i < btnId.length; i++) {
            btn[i] = (ImageButton)findViewById(btnId[i]);
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < btnId.length; j++) {
                        if (v.getId() == btn[j].getId()) {
                            if (j == 11) {
                                if(input.getText().toString().equals("0"))
                                    Toast.makeText(getApplicationContext(), "금액을 입력하세요.", Toast.LENGTH_SHORT).show();
                                else {
                                    Intent intent = new Intent(AddCost.this, AddActivity.class);
                                    Bundle bundle = new Bundle();
                                    int cost = Integer.parseInt(input.getText().toString());
                                    bundle.putInt("type", type);
                                    bundle.putInt("total", cost);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, 1);
                                }
                            } else if (j == 10) {
                                int update = Integer.parseInt(input.getText().toString()) / 10;
                                input.setText(update + "");
                            } else {
                                if (input.getText().toString().equals("0"))
                                    input.setText(j + "");
                                else
                                    input.setText(input.getText().toString() + j);
                            }

                        }
                    }
                }
            });
        }
    }
}