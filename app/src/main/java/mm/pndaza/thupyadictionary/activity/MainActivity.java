package mm.pndaza.thupyadictionary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import mm.pndaza.thupyadictionary.R;
import mm.pndaza.thupyadictionary.database.DBOpenHelper;
import mm.pndaza.thupyadictionary.fragment.FavouriteFragment;
import mm.pndaza.thupyadictionary.fragment.HomeFragment;
import mm.pndaza.thupyadictionary.fragment.InfoFragment;
import mm.pndaza.thupyadictionary.fragment.RecentFragment;
import mm.pndaza.thupyadictionary.fragment.SettingFragment;
import mm.pndaza.thupyadictionary.models.Favourite;
import mm.pndaza.thupyadictionary.models.Recent;
import mm.pndaza.thupyadictionary.models.Word;
import mm.pndaza.thupyadictionary.utils.MDetect;
import mm.pndaza.thupyadictionary.utils.SharePref;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnWordClickListener,
        FavouriteFragment.OnFavouriteCallbackListener,
        RecentFragment.OnRecentCallbackListener,
        SettingFragment.OnSettingChangeListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        // load theme
//        if (SharePref.getInstance(this).getPrefNightModeState()) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MDetect.init(this);

        setSupportActionBar(findViewById(R.id.toolbar));
        setTitle(MDetect.getDeviceEncodedText(getString(R.string.app_name_mm)));



//        openFragment(new HomeFragment());
        if (savedInstanceState == null) {
            openFragment(new HomeFragment());
        }

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);

        navView.setOnNavigationItemSelectedListener(item -> {

            Fragment selectedFragment = null;
            switch ((item.getItemId())) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_favourite:
                    selectedFragment = new FavouriteFragment();
                    break;
                case R.id.navigation_recent:
                    selectedFragment = new RecentFragment();
                    break;
                case R.id.navigation_setting:
                    selectedFragment = new SettingFragment();
                    break;
                case R.id.navigation_info:
                    selectedFragment = new InfoFragment();
                    break;
            }
            openFragment(selectedFragment);
            return true;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void openFragment(Fragment selectedFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, selectedFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onWordClick(Word word) {
        word.setDetail(DBOpenHelper.getInstance(this).getDetail(word.getId()));
        Log.d(TAG, "onWordClick: " + word.getWord());
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("word", word);
        startActivity(intent);
    }

    @Override
    public void onChangeListener() {
//        openFragment(new HomeFragment());
        recreate();
//        openFragment(new SettingFragment());
    }

    @Override
    public void onFavouriteClick(Favourite favourite) {
        Word word = new Word(favourite.getId(), favourite.getWord());
        word.setDetail(DBOpenHelper.getInstance(this).getDetail(word.getId()));
        Log.d(TAG, "onWordClick: " + word.getWord());
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("word", word);
        startActivity(intent);

    }

    @Override
    public void onRecentClick(Recent recent) {
        Word word = new Word(recent.getId(), recent.getWord());
        word.setDetail(DBOpenHelper.getInstance(this).getDetail(word.getId()));
        Log.d(TAG, "onWordClick: " + word.getWord());
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("word", word);
        startActivity(intent);
    }
}
