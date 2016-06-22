package helloworld.example.com.payback;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ListTab extends Fragment {
    ListView lv;
    ArrayList<TransItem> list = new ArrayList<TransItem>();
    DBAdapter db;
    TransAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstatnceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        db = new DBAdapter(getContext());
        lv = (ListView) view.findViewById(R.id.listView1);
        list.clear();
        setList();
        setAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void setAdapter() {
        adapter = new TransAdapter(getActivity(),
                R.layout.item_transaction, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> translist, View v,
                                    int position, long resid) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", list.get(position).getName());
                bundle.putInt("total", list.get(position).getCost());
                intent.putExtras(bundle);
                startActivityForResult(intent, 202);
            }
        });
    }

    public void setList() {
        db.open();
        Cursor c = db.getAllTransactions();
        if (c.moveToFirst()) {
            do {
                int i = 0;
                for (; i < list.size(); i++) {
                    if (list.get(i).getName().equals(c.getString(1))) {
                        list.get(i).setCost(list.get(i).getCost() + c.getInt(0));
                        break;
                    }
                }
                if (i == list.size()) {
                    TransItem t = new TransItem();
                    t.setName(c.getString(1));
                    t.setCost(c.getInt(0));
                    list.add(t);
                }
            } while(c.moveToNext());
        }
        db.close();
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
                holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
                holder.tv_cost = (TextView) v.findViewById(R.id.tv_cost);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            TransItem t = translist.get(position);

            if (t != null) {
                holder.tv_name.setText(t.getName());
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
            TextView tv_name;
            TextView tv_cost;
        }
    }
}



