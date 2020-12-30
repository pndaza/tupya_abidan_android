package mm.pndaza.thupyadictionary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import mm.pndaza.thupyadictionary.R;
import mm.pndaza.thupyadictionary.utils.MDetect;
import mm.pndaza.thupyadictionary.utils.Rabbit;
import mm.pndaza.thupyadictionary.utils.SharePref;


public class SettingFragment extends Fragment {

    private static final String FONT_SIZE_SMALL = "small";
    private static final String FONT_SIZE_NORMAL = "normal";
    private static final String FONT_SIZE_LARGE = "large";

    private OnSettingChangeListener onSettingChangeListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(MDetect.getDeviceEncodedText(getString(R.string.title_setting_mm)));
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RadioGroup radioGroupFontSize = view.findViewById(R.id.rg_fontsize);
        RadioGroup radioGroupTheme = view.findViewById(R.id.rg_theme);
        initView(radioGroupFontSize, radioGroupTheme);
        initViewTextEncoding(view);

        radioGroupFontSize.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            saveAndRestart(checkedId);
        });

        radioGroupTheme.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            saveAndRestart(checkedId);
        });

    }

    private void initView(RadioGroup radioGroupFontSize, RadioGroup radioGroupTheme){
        SharePref sharePref = SharePref.getInstance(this.getContext());
        String fontSize = sharePref.getPrefFontSizeInText();
        boolean nightModeState = sharePref.getPrefNightModeState();
        //
        if (fontSize.equals(FONT_SIZE_SMALL))
            radioGroupFontSize.check(R.id.radio_font_small);
        else if (fontSize.equals(FONT_SIZE_NORMAL))
            radioGroupFontSize.check(R.id.radio_font_normal);
        else if (fontSize.equals(FONT_SIZE_LARGE))
            radioGroupFontSize.check(R.id.radio_font_large);
        //
        if(nightModeState){
            radioGroupTheme.check(R.id.radio_theme_night);
        } else {
            radioGroupTheme.check((R.id.radio_theme_day));
        }

    }

    private void saveAndRestart(int checkedId){
        SharePref sharePref = SharePref.getInstance(this.getContext());
        switch (checkedId){
            case R.id.radio_font_small:
                sharePref.setPrefFontSize(FONT_SIZE_SMALL);
                break;
            case R.id.radio_font_normal:
                sharePref.setPrefFontSize(FONT_SIZE_NORMAL);
                break;
            case R.id.radio_font_large:
                sharePref.setPrefFontSize(FONT_SIZE_LARGE);
                break;
            case R.id.radio_theme_day:
                sharePref.setPrefNightModeState(false);
                break;
            case R.id.radio_theme_night:
                sharePref.setPrefNightModeState(true);
                break;
        }
        onSettingChangeListener.onChangeListener();
    }

/*    private void setTheme(boolean nightState){
        if (nightState) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }*/

    private void initViewTextEncoding(View view){
        TextView tvFontSizeTitle = view.findViewById(R.id.tv_fontSizeTitle);
        RadioButton rbFontSizeSmall = view.findViewById(R.id.radio_font_small);
        RadioButton rbFontSizeNormal = view.findViewById(R.id.radio_font_normal);
        RadioButton rbFontSizeLarge = view.findViewById(R.id.radio_font_large);
        TextView tvThemeTitle = view.findViewById(R.id.tv_themeTitle);
        RadioButton rbThemeDay = view.findViewById(R.id.radio_theme_day);
        RadioButton rbThemeNight = view.findViewById(R.id.radio_theme_night);

        String fontSizeTitle = getString(R.string.fontSize);
        String fontSizeSmall = getString(R.string.fontSizeSmall);
        String fontSizeNormal = getString(R.string.fontSizeNormal);
        String fontSizeLarge = getString(R.string.fontSizeLarge);
        String themeTitle = getString(R.string.themeTitle);
        String themeDay = getString(R.string.themeDay);
        String themeNight = getString(R.string.themeNight);

        if(!MDetect.isUnicode()){
            tvFontSizeTitle.setText(Rabbit.uni2zg(fontSizeTitle));
            rbFontSizeSmall.setText(Rabbit.uni2zg(fontSizeSmall));
            rbFontSizeNormal.setText(Rabbit.uni2zg(fontSizeNormal));
            rbFontSizeLarge.setText(Rabbit.uni2zg(fontSizeLarge));
            tvThemeTitle.setText(Rabbit.uni2zg(themeTitle));
            rbThemeDay.setText(Rabbit.uni2zg(themeDay));
            rbThemeNight.setText(Rabbit.uni2zg(themeNight));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            onSettingChangeListener = (OnSettingChangeListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implemented OnWordListSelectedListener");

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnSettingChangeListener{
        void onChangeListener();
    }

}
