package mm.pndaza.thupyadictionary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mm.pndaza.thupyadictionary.models.Favourite;
import mm.pndaza.thupyadictionary.models.Recent;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static DBOpenHelper sInstance;
    private static final String DATABASE_PATH = "/databases/";
    private static final String DATABASE_NAME = "words.db";
    private static final int DATABASE_VERSION = 1;


    public static synchronized DBOpenHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DBOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBOpenHelper(Context context) {
        super(context, context.getFilesDir() + DATABASE_PATH + DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor getWords() {
        return getReadableDatabase().rawQuery("SELECT rowid, word FROM words", null);
    }

    public String getWord(int id) {
        String word = "";
        Cursor cursor = getReadableDatabase().rawQuery("SELECT word FROM words WHERE rowid = " + id, null);
        if (cursor != null && cursor.moveToFirst()) {
            word = cursor.getString(cursor.getColumnIndex("word"));
        }
        cursor.close();
        return word;
    }

    public String getDetail(int rowid) {
        String detail = "";
        Cursor cursor = getReadableDatabase()
                .rawQuery("SELECT detail FROM words WHERE rowid = " + rowid, null);
        if (cursor != null && cursor.moveToFirst()) {
            detail = cursor.getString(cursor.getColumnIndex("detail"));
        }
        cursor.close();
        return detail;
    }


    public ArrayList<Favourite> getAllFavourites() {
        ArrayList<Favourite> favourites = new ArrayList<>();
        int id;
        String word;
        Cursor cursor = getReadableDatabase().
                rawQuery("SELECT id FROM favourites ORDER BY rowid DESC", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    word = getWord(id);
                    favourites.add(new Favourite(id, word));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return favourites;
    }

    public boolean isFavouriteExist(int id) {
        Cursor cursor = this.getReadableDatabase().rawQuery
                ("SELECT id FROM favourites Where id = " + id, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void addToFavourite(int id) {
        getWritableDatabase()
                .execSQL("INSERT INTO favourites (id) VALUES (?)", new Object[]{id});
    }

    public void removeFromFavourite(int id) {
        getWritableDatabase()
                .execSQL("DELETE FROM favourites WHERE id = ?", new Object[]{id});
    }

    public void removeAllFavourite() {
        getWritableDatabase()
                .execSQL("DELETE FROM favourites");
    }

    public ArrayList<Recent> getAllRecents() {
        ArrayList<Recent> recents = new ArrayList<>();
        int id;
        String word;
        Cursor cursor = getReadableDatabase().
                rawQuery("SELECT id FROM recent ORDER BY rowid DESC", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    word = getWord(id);
                    recents.add(new Recent(id, word));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return recents;
    }

    public boolean isRecentExist(int id) {
        Cursor cursor = this.getReadableDatabase().rawQuery
                ("SELECT id FROM recent Where id = " + id, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void addToRecent(int id) {
        getWritableDatabase()
                .execSQL("INSERT INTO recent (id) VALUES (?)", new Object[]{id});
    }

/*
    public void removeFromRecent(int id) {
        getWritableDatabase()
                .execSQL("DELETE FROM recent WHERE id = ?", new Object[]{id});
    }
*/

    public void removeAllRecent(){
        getWritableDatabase().execSQL("DELETE FROM recent");
    }

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }
}
