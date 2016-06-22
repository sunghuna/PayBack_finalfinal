package helloworld.example.com.payback;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;

import java.util.Date;
import java.text.SimpleDateFormat;
public class IngTab extends Fragment {
    private Context mContext;
    IngListAdapter adapter;
    ImageButton imgBtn;
    DBAdapter db;
    ArrayList<inglist> list = new ArrayList<inglist>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstatnceState) {

        View view = inflater.inflate(R.layout.tab2, null);
        db = new DBAdapter(getContext());
        ListView listview = (ListView) view.findViewById(R.id.listView2);
        imgBtn = (ImageButton)view.findViewById(R.id.iconItem);
        adapter = new IngListAdapter(getActivity());
        Resources res = getResources();
        list.clear();
        setList();

        for(int i = 0; i < list.size(); i++) {
            //list.get(i).getDate()
            adapter.addItem(new IngItem(res.getDrawable(R.drawable.message), list.get(i).getName(), list.get(i).getCost() + "원",    list.get(i).getDate()+"\n"+getDminus(29,6,2016) ));
        }
        listview.setAdapter(adapter);
        return view;

    }
    public String getDminus(int d, int m, int y){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        String inputString3 = String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y);

        Calendar now = Calendar.getInstance();

        int month = now.get(Calendar.MONTH) + 1; // 월을 리턴
        int day = now.get(Calendar.DAY_OF_MONTH); // 일을 리턴
        int year = now.get((Calendar.YEAR));

        String inputString4 = String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);

        Date date1;
        Date date2;
        long diff=0;
        try {
            date1 = myFormat.parse(inputString3);
            date2 = myFormat.parse(inputString4);
            diff = date1.getTime() - date2.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)>0)
            return "D-"+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        else if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)<0)
            return "D+"+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        else
            return "D-Day" ;
    }
    public void setList() {
        db.open();
        Cursor c = db.getAllIngs();
        if (c.moveToFirst()) {
            do {
                inglist t = new inglist();
                t.setDate(c.getString(2));
                t.setName(c.getString(0));
                t.setCost(c.getInt(1));
                list.add(t);
            } while(c.moveToNext());
        }
        db.close();
    }

}


class inglist {
    int cost;
    String name;
    String date;

    inglist() {
        super();
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getCost() {
        return cost;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
}