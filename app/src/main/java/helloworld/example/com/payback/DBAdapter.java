package helloworld.example.com.payback;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "PayBack";
    static final int DATABASE_VERSION = 1;

    static final String TABLE_ING = "Ings";
    static final String KEY_ROWID = "_id";
    static final String INAME = "name";
    static final String ICOST = "cost";
    static final String DDAY = "dday";
    static final String CREATE_ING =
            "create table " + TABLE_ING
            + "(_id integer primary key autoincrement," +
                    "name text not null," +
                    "cost integer not null," +
                    "dday text not null);";

    static final String TABLE_CONTACTS = "Contacts";
    static final String PHOTO_ID = "photoid";
    static final String PHONE_NUM = "num";
    static final String NAME = "name";
    static final String CREATE_CONTACT =
            "create table " + TABLE_CONTACTS
                    + "(photoid integer not null,"
                    + "num text not null," +
                    "name text not null," +
                    "primary key(name));";

    static final String TABLE_TRANSACTIONS = "Transactions";
    static final String COST = "cost";
    static final String TNAME = "name";
    static final String DATE = "date";
    static final String PAYED = "payed";
    static final String CREATE_TRANSACTION =
            "create table " + TABLE_TRANSACTIONS
                    + "(cost integer,"
                    + "name text,"
                    + "date text,"
                    + "payed text,"
                    + "primary key(name, date),"
                    + "foreign key(name) references Contacts(name));";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_CONTACT);
                db.execSQL(CREATE_TRANSACTION);
                db.execSQL(CREATE_ING);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Contacts");
            db.execSQL("DROP TABLE IF EXISTS Transactions");
            db.execSQL("DROP TABLE IF EXISTS Ings");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }


    //Ings
    public long insertIngs(String name, int cost, String dday) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ICOST, cost);
        initialValues.put(INAME, name);
        initialValues.put(DDAY, dday);
        return db.insert(TABLE_ING, null, initialValues);
    }
    public Cursor getAllIngs() {
        return db.query(TABLE_ING, new String[]{
                INAME, ICOST, DDAY
        }, null, null, null, null, null);
    }

    public Cursor getIng(String name, int cost, String day) throws SQLException {
        Cursor mCursor = db.query(true, TABLE_ING, new String[]{
                        INAME, ICOST, DDAY},
                INAME + "=?",
                new String[]{String.valueOf(name) },
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void deleteIng(int rowId) {
        db.delete(TABLE_ING, KEY_ROWID + "=" + rowId, null);
    }
    //Transactions
    public long insertTransaction(int cost, String name, String date, String payed) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COST, cost);
        initialValues.put(TNAME, name);
        initialValues.put(DATE, date);
        initialValues.put(PAYED, payed);
        return db.insert(TABLE_TRANSACTIONS, null, initialValues);
    }
    public Cursor getAllTransactions() {
        return db.query(TABLE_TRANSACTIONS, new String[]{
                COST, TNAME, DATE, PAYED
        }, null, null, null, null, null);
    }

    public Cursor getTransaction(String name) throws SQLException {
        Cursor mCursor = db.query(true, TABLE_TRANSACTIONS, new String[]{
                        COST, TNAME, DATE, PAYED},
                TNAME + "=?",
                new String[]{String.valueOf(name) },
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void deleteTransactions(String name) {
        db.delete(TABLE_TRANSACTIONS, TNAME + "=?", new String[]{name});

    }

    //Contacts
    public long insertContact(long id, String phnum, String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PHOTO_ID, id);
        initialValues.put(PHONE_NUM, phnum);
        initialValues.put(NAME, name);
        return db.insert(TABLE_CONTACTS, null, initialValues);
    }

    public boolean deleteContact(long photoid) {
        return db.delete(TABLE_CONTACTS, PHOTO_ID + "=" + photoid, null) > 0;
    }

    public Cursor getAllContacts() {
        return db.query(TABLE_CONTACTS, new String[]{
                PHOTO_ID, PHONE_NUM, NAME
        }, null, null, null, null, null);
    }

    public Cursor getContact(String name) throws SQLException {
        Cursor mCursor = db.query(true, TABLE_CONTACTS, new String[]{
                        PHOTO_ID, PHONE_NUM, NAME}, NAME + "=?",
                new String[]{String.valueOf(name) },
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateContact(long rowId, long photoid, String phnum, String name) {
        ContentValues args = new ContentValues();
        args.put(PHONE_NUM, phnum);
        args.put(NAME, name);
        return db.update(TABLE_CONTACTS, args, PHOTO_ID + "=" + photoid, null) > 0;
    }
}