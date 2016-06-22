package helloworld.example.com.payback;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class SettingTab extends Fragment {
    private Context mContext;

    ListView ls;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstatnceState) {
        View view = inflater.inflate(R.layout.tab4, container, false);
                ;
        ls = (ListView) view.findViewById(R.id.listView4);
        ArrayList<ListItem> items = new ArrayList<>();
        ListItem due = new ListItem(R.mipmap.due, "D-day 설정");
        ListItem acc = new ListItem(R.mipmap.account, "계좌설정");
        ListItem lock = new ListItem(R.mipmap.lock, "잠금설정");
        items.add(due);
        items.add(acc);
        items.add(lock);
        ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.item_setting, items);
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), Setdue.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), Account.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), ChangePW.class));
                        break;
                }
            }
        });
        return view;
    }
    public class ListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<ListItem> data;
        private int layout;

        /**
         * Constructor * * @param context * @param layout * @param data
         */
        public ListViewAdapter(Context context, int layout, ArrayList<ListItem> data) {
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        /**
         * @return
         */
        @Override
        public int getCount() {
            return data.size();
        }

        /**
         * @param position * @return
         */
        @Override
        public Object getItem(int position) {
            return data.get(position).getTxt();
        }

        /**
         * @param position * @return
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * @param position * @param convertView * @param parent * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }
            ListItem listviewitem = data.get(position);
            ImageView icon = (ImageView) convertView.findViewById(R.id.img);
            icon.setImageResource(listviewitem.getImg());
            TextView name = (TextView) convertView.findViewById(R.id.txt);
            name.setText(listviewitem.getTxt());
            return convertView;
        }
    }

}

class ListItem {
    private int img;
    private String txt;

    /**
     * Constructor * * @param img * @param txt
     */
    public ListItem(int img, String txt) {
        this.img = img;
        this.txt = txt;
    }

    /**
     * @return
     */
    public int getImg() {
        return img;
    }

    /**
     * @param img
     */
    public void setImg(int img) {
        this.img = img;
    }

    /**
     * @return
     */
    public String getTxt() {
        return txt;
    }

    /**
     * @param txt
     */
    public void setTxt(String txt) {
        this.txt = txt;
    }
}
