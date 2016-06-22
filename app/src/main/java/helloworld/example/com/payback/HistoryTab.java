package helloworld.example.com.payback;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import helloworld.example.com.payback.R;

public class HistoryTab extends Fragment {
    private Context mContext;
    HistoryListAdapter adapter;
    ImageButton imgBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstatnceState) {

        View view = inflater.inflate(R.layout.tab2, null);
        ListView listview = (ListView) view.findViewById(R.id.listView2);
        imgBtn = (ImageButton)view.findViewById(R.id.iconItem);
        adapter = new HistoryListAdapter(getActivity());
        Resources res = getResources();

        adapter.addItem(new HistoryItem("SW11주환석오빠","Good","1","Bad","2", "30,000 원", "Num"+(1+2)));
        adapter.addItem(new HistoryItem("SW13김정화","Good","3","Bad","2", "30,000 원", "Num"+(3+2)));

        listview.setAdapter(adapter);
        return view;
    }
}
