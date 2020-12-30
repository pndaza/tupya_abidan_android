package mm.pndaza.thupyadictionary.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import mm.pndaza.thupyadictionary.R;
import mm.pndaza.thupyadictionary.database.DBOpenHelper;
import mm.pndaza.thupyadictionary.models.Word;
import mm.pndaza.thupyadictionary.utils.MDetect;
import mm.pndaza.thupyadictionary.utils.SharePref;

public class DetailActivity extends AppCompatActivity {

    private TextView tv_detail;
    private Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        MDetect.init(this);
        setTitle(MDetect.getDeviceEncodedText(getString(R.string.title_detail)));
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        word = getIntent().getParcelableExtra("word");
        tv_detail = findViewById(R.id.tv_detail);
        tv_detail.setText(MDetect.getDeviceEncodedText(word.getDetail()));
        tv_detail.setTextSize(SharePref.getInstance(this).getPrefFontSize());
        tv_detail.setTextIsSelectable(true);

        manageRecent(word.getId());

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tools, menu);
        MenuItem fav = menu.findItem(R.id.menu_favourite);
        setIcon(fav); // set icon based on bookmark exist ot not
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_copy:
                copyToClipboard();
                return true;
            case R.id.menu_favourite:
                manageFavourites(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void copyToClipboard(){
        String textToCopy = tv_detail.getText().toString();
        ClipboardManager clipboard= (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copy", textToCopy);
        clipboard.setPrimaryClip(clip);
        Snackbar.make(tv_detail,MDetect.getDeviceEncodedText("ကော်ပီကူးယူပြီးပါပြီ"), Snackbar.LENGTH_SHORT).show();

    }

    private void manageFavourites(MenuItem item){
        if(isFavouriteExist(word.getId())){
            removeFromFavourite(word.getId());
            item.setIcon(R.drawable.ic_add_to_favourite);
        } else {
            addToFavourite(word.getId());
            item.setIcon(R.drawable.ic_added_favorite);
        }
    }

    private boolean isFavouriteExist(int id){
        return DBOpenHelper.getInstance(this).isFavouriteExist(id);
    }

    private void addToFavourite(int id) {
        DBOpenHelper.getInstance(this).addToFavourite(id);
        Snackbar.make(tv_detail,MDetect.getDeviceEncodedText("စိတ်ကြိုက်စာရင်းသို့ ထည့်လိုက်ပါပြီ။"), Snackbar.LENGTH_SHORT).show();
    }

    private void removeFromFavourite(int id){
        DBOpenHelper.getInstance(this).removeFromFavourite(id);
        Snackbar.make(tv_detail,MDetect.getDeviceEncodedText("စိတ်ကြိုက်စာရင်းမှ ပယ်ဖျက်လိုက်ပါပြီ"), Snackbar.LENGTH_SHORT).show();
    }

    private void setIcon(MenuItem item){
        item.setIcon(isFavouriteExist(word.getId())? R.drawable.ic_added_favorite : R.drawable.ic_add_to_favourite);
    }

    private void manageRecent(int id){
        if(!isRecentExist(id)){
            addToRecent(id);
        }
    }

    private boolean isRecentExist(int id){
        return DBOpenHelper.getInstance(this).isRecentExist(id);
    }

    private void addToRecent(int id) {
        DBOpenHelper.getInstance(this).addToRecent(id);
    }

}