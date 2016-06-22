package helloworld.example.com.payback;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddActivity extends Activity implements View.OnClickListener{

    DBAdapter db;
    ListView lv;
    private Button back, comp;
    private ImageButton goSearch;
    private AutoCompleteTextView search;
    ArrayList<Contact> current = new ArrayList<Contact>();
    ArrayList<Contact> update = new ArrayList<Contact>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Contact> selected = new ArrayList<Contact>();
    ContactsAdapter adapter;
    boolean checkResult = false;
    private int total = 0, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActivityStacks.getInstance().addActivity(this);

        db = new DBAdapter(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        total = bundle.getInt("total");
        type = bundle.getInt("type");

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        comp = (Button) findViewById(R.id.comp);
        comp.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.contactList);
        current = getContactList();
        setAdapter();
        search = (AutoCompleteTextView) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        search.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, names));
        goSearch = (ImageButton) findViewById(R.id.goSearch);
        goSearch.setOnClickListener(this);
    }

    public void setAdapter() {
        adapter = new ContactsAdapter(AddActivity.this,
                R.layout.item_contact, current);
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for (int k = 0; k < current.size(); k++) {
            for (int l = 0; l < selected.size(); l++) {
                if (current.get(k).getName().equals(selected.get(l).getName()))
                    adapter.setChecked(k);
            }
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> contactlist, View v,
                                    int position, long resid) {
                checkResult = adapter.setChecked(position);
                adapter.notifyDataSetChanged();
                if (checkResult == true)
                    updateFace(current.get(position), true);
                else
                    updateFace(current.get(position), false);
                if (selected.size() > 0)
                    comp.setTextColor(Color.BLACK);
                else
                    comp.setTextColor(Color.WHITE);
            }
        });
    }

    /**
     * 거래 참여자 update
     *
     * @param newface
     * @param addOrRemove
     */
    public void updateFace(Contact newface, boolean addOrRemove) {
        int i;
        if(addOrRemove == true) {
            for (i = 0; i < selected.size(); i++) {
                if (selected.get(i).getName().equals(newface.getName()))
                    break;
            }
            if (i == selected.size())
                selected.add(newface);
        }
        else
            for (i = 0; i < selected.size(); i++) {
                if (selected.get(i).getName().equals(newface.getName()))
                    selected.remove(i);
            }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == goSearch.getId()) {
            update.clear();
            if(search.getText().toString().equals(""))
                update = getContactList();
            else {
                current = getContactList();
                for (int i = 0; i < current.size(); i++) {
                    if (current.get(i).getName().contains(search.getText().toString()))
                        update.add(current.get(i));
                }
            }
            current = update;
            setAdapter();
        }
        else if(v.getId() == back.getId()) {
            finish();
        }
        else if(v.getId() == comp.getId()) {
            int forEach = total / selected.size();
            forEach *= type;
            db.open();
            Contact sel;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
            Date date = new Date();
            String strDate = dateFormat.format(date);
            for(int i = 0; i < selected.size(); i++) {
                sel = selected.get(i);
                long id = db.insertContact(sel.getPhotoid(), sel.getPhonenum(), sel.getName());
                id = db.insertTransaction(forEach, sel.getName(), strDate, "no");
            }
            db.close();
            startActivity(new Intent(AddActivity.this, MainActivity.class));
        }
    }


    /**
     * 연락처를 가져오는 메소드.
     *
     * @return
     */
    private ArrayList<Contact> getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        ArrayList<Contact> contactlist = new ArrayList<Contact>();

        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-",
                        "");
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 6) + "-"
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 7) + "-"
                            + phonenumber.substring(7);
                }

                Contact acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));
                names.add(contactCursor.getString(2));

                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }

        return contactlist;

    }

    private class ContactsAdapter extends ArrayAdapter<Contact> {

        private int resId;
        private ArrayList<Contact> contactlist;
        private LayoutInflater Inflater;
        private Context context;
        private boolean[] isCheckedConfrim;

        public ContactsAdapter(Context context, int textViewResourceId,
                               List<Contact> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            resId = textViewResourceId;
            contactlist = (ArrayList<Contact>) objects;
            Inflater = (LayoutInflater) ((Activity) context)
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.isCheckedConfrim = new boolean[contactlist.size()];
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder holder;
            if (v == null) {
                v = Inflater.inflate(resId, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
                holder.iv_photoid = (ImageView) v.findViewById(R.id.iv_photo);
                holder.check = (CheckBox) v.findViewById(R.id.check);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            Contact acontact = contactlist.get(position);

            if (acontact != null) {
                holder.tv_name.setText(acontact.getName());
                Bitmap bm = openPhoto(acontact.getPhotoid());
                // 사진없으면 기본 사진 보여주기
                if (bm != null) {
                    holder.iv_photoid.setImageBitmap(bm);
                } else {
                    holder.iv_photoid.setImageDrawable(getResources()
                            .getDrawable(R.mipmap.profile));
                }

            }
            holder.check.setChecked(isCheckedConfrim[position]);

            return v;
        }

        /**
         * 연락처 사진 id를 가지고 사진에 들어갈 bitmap을 생성.
         *
         * @param contactId 연락처 사진 ID
         * @return bitmap  연락처 사진
         */
        private Bitmap openPhoto(long contactId) {
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    contactId);
            InputStream input = ContactsContract.Contacts
                    .openContactPhotoInputStream(context.getContentResolver(),
                            contactUri);

            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }

            return null;
        }

        private class ViewHolder {
            ImageView iv_photoid;
            TextView tv_name;
            CheckBox check;
        }

        public boolean setChecked(int position) {
            isCheckedConfrim[position] = !isCheckedConfrim[position];
            if(isCheckedConfrim[position] == true)
                return true;
            else
                return false;
        }

        public ArrayList<Integer> getChecked() {
            int tempSize = isCheckedConfrim.length;
            ArrayList<Integer> mArrayList = new ArrayList<Integer>();
            for (int b = 0; b < tempSize; b++) {
                if (isCheckedConfrim[b]) {
                    mArrayList.add(b);
                }
            }
            return mArrayList;
        }
    }
}