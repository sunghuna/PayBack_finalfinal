package helloworld.example.com.payback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends Activity implements View.OnClickListener {

    ListView lv;
    DBAdapter db;
    TransAdapter adapter;
    ArrayList<TransItem> list = new ArrayList<TransItem>();
    private Button back, cal;
    private TextView tv_name;
    private int total = 0;
    private String name = "";
    EditText due;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        db = new DBAdapter(this);
        lv = (ListView) findViewById(R.id.detail_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        total = bundle.getInt("total");
        name = bundle.getString("name");

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        cal = (Button) findViewById(R.id.cal);
        cal.setTextColor(Color.BLACK);
        cal.setOnClickListener(this);
        tv_name = (TextView)findViewById(R.id.detail_name);
        tv_name.setText(name);

        setList();
        setAdapter();

    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void setAdapter() {
        adapter = new TransAdapter(this,
                R.layout.item_detail, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> translist, View v,
                                    int position, long resid) {

            }
        });
    }

    private class TransAdapter extends ArrayAdapter<TransItem> {
        private int resId;
        private ArrayList<TransItem> translist;
        private LayoutInflater Inflater;
        private Context context;

        public TransAdapter(Context context, int textViewResourceId,
                            List<TransItem> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            resId = textViewResourceId;
            translist = (ArrayList<TransItem>) objects;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            Inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder;
            if (v == null) {
                v = Inflater.inflate(resId, parent, false);
                holder = new ViewHolder();
                holder.tv_date = (TextView) v.findViewById(R.id.detail_date);
                holder.tv_cost = (TextView) v.findViewById(R.id.detail_cost);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            TransItem t = translist.get(position);

            if (t != null) {
                holder.tv_date.setText(t.getDate());
                holder.tv_cost.setText(t.getCost() + "");
                if(Integer.parseInt(holder.tv_cost.getText().toString()) >= 0) {
                    holder.tv_cost.setTextColor(Color.BLUE);
                    holder.tv_cost.setText("+" + holder.tv_cost.getText());
                }
                else
                    holder.tv_cost.setTextColor(Color.RED);
            }

            return v;
        }
        private class ViewHolder {
            TextView tv_date;
            TextView tv_cost;
        }
    }

    public void setList() {
        db.open();
        Cursor c = db.getTransaction(name);
        if (c.moveToFirst()) {
            do {
                TransItem t = new TransItem();
                t.setDate(c.getString(2));
                t.setCost(c.getInt(0));
                list.add(t);
            } while (c.moveToNext());
        }
        db.close();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == cal.getId()) {
            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_send, null);
            String str = "'" + name + "' 님에게 " + total + "원을 ";
            if (total >= 0)
                str += "주세요.";
            else
                str += "받으세요";
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
            builder.setView(view);
            builder.setMessage(str);
            builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.setNegativeButton("정산하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.open();
                    if (total < 0) {
                        total *= -1;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());
                        Date date = new Date();
                        String strDate = dateFormat.format(date);
                        int intDue = Integer.parseInt(strDate) + 7;
                        String strDue = intDue + "";
                        String d = strDue.substring(0, 4) + "-" + strDue.substring(4, 6) + "-" + strDue.substring(6);
                        long id = db.insertIngs(name, total, d + "");
                    }
                    db.deleteTransactions(name);
                    db.close();
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
