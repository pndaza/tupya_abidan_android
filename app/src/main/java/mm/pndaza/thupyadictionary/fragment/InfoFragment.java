package mm.pndaza.thupyadictionary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.sufficientlysecure.htmltextview.HtmlTextView;

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

        HtmlTextView htmlTextView = view.findViewById(R.id.tv_info);
        htmlTextView.setHtml(MDetect.getDeviceEncodedText(getInfo()));
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
