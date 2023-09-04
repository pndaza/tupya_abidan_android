package mm.pndaza.thupyadictionary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mm.pndaza.thupyadictionary.R;
import mm.pndaza.thupyadictionary.utils.MDetect;

public class InfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(MDetect.getDeviceEncodedText(getString(R.string.title_info_mm)));
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.tv_info);
        String htmlContent = MDetect.getDeviceEncodedText(getInfo());
//        markwon.setMarkdown(textView, htmlContent);
        textView.setText(HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_LEGACY));
//        textView.setText(htmlContent);
    }

    private String getInfo() {
        StringBuilder info = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().getAssets().open("info.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                info.append(line);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return info.toString();
    }
}
