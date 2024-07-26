package mm.pndaza.thupyadictionary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import mm.pndaza.thupyadictionary.Constants;

public class SharePref {

    private static final String PREF_FILENAME = "setting";
    private static final String PREF_FIRST_TIME = "FirstTime";
    private static final String PREF_DB_COPY = "DBCopy";
    private static final String PREF_DB_VERSION = "DBVersion";
    private static final String PREF_FONT_SIZE = "FontSize";
    private static final String PREF_NIGHT_MODE = "NightMode";

    private Context context;
    private static SharePref prefInstance;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    public SharePref(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharePref getInstance(Context Context) {
        if (prefInstance == null) {
            prefInstance = new SharePref(Context);
        }
        return prefInstance;
    }

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(PREF_FIRST_TIME, true);
    }

    public void setPrefFontSize(String fontSizeInText){
        editor.putString(PREF_FONT_SIZE, fontSizeInText);
        editor.apply();
    }

    public int getPrefFontSize(){
        String fontSizeInText = sharedPreferences.getString(PREF_FONT_SIZE, Constants.FONT_SIZE_NORMAL);
        int fontSize = 18; // normal - default
        switch (fontSizeInText) {
            case Constants.FONT_SIZE_SMALL:
                fontSize = 16;
                break;
            case Constants.FONT_SIZE_LARGE:
                fontSize = 22;
                break;
            case Constants.FONT_SIZE_EXTRA_LARGE:
                fontSize = 26;
                break;
        }
        return fontSize;
    }

    public String getPrefFontSizeInText(){
        return sharedPreferences.getString(PREF_FONT_SIZE, Constants.FONT_SIZE_NORMAL);
    }

    public void setPrefNightModeState(boolean state){
        editor.putBoolean(PREF_NIGHT_MODE, state);
        editor.apply();
    }

    public boolean getPrefNightModeState(){
        return sharedPreferences.getBoolean(PREF_NIGHT_MODE, false);
    }

    public void setDbCopyState(boolean state){
        editor.putBoolean(PREF_DB_COPY, state);
        editor.apply();
    }

    public boolean isDatabaseCopied(){
        return sharedPreferences.getBoolean(PREF_DB_COPY, true);
    }

    public int getDatabaseVersion(){
        return sharedPreferences.getInt(PREF_DB_VERSION,1);
    }

    public void setDatabaseVersion(int version){
        editor.putInt(PREF_DB_VERSION, version);
    }

    public void saveDefault(){
        editor.putBoolean(PREF_FIRST_TIME, false);
        editor.putBoolean(PREF_DB_COPY, false);
        editor.putString(PREF_FONT_SIZE, Constants.FONT_SIZE_NORMAL);
        editor.putBoolean(PREF_NIGHT_MODE, false);
        editor.apply();
    }

}
