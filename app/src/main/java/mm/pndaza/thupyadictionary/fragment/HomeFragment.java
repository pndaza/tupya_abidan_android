package mm.pndaza.thupyadictionary.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mm.pndaza.thupyadictionary.R;
import mm.pndaza.thupyadictionary.adapters.WordAdapter;
import mm.pndaza.thupyadictionary.database.DBOpenHelper;
import mm.pndaza.thupyadictionary.models.Word;
import mm.pndaza.thupyadictionary.utils.MDetect;
import mm.pndaza.thupyadictionary.utils.Rabbit;

public class HomeFragment extends Fragment implements WordAdapter.OnItemClickListener {

    private final ArrayList<Word> words = new ArrayList<>();
    private final ArrayList<Word> filterWords = new ArrayList<>();
    private final WordAdapter adapter = new WordAdapter(words, this);
    private OnWordClickListener onWordClickListener;
    private TextView tv_empty_info;

    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(MDetect.getDeviceEncodedText(getString(R.string.app_name_mm)));
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.wordListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        new LoadWords().execute();

        tv_empty_info = view.findViewById(R.id.empty_info);
        final EditText searchInput = view.findViewById(R.id.search);
        final ImageButton btnClear = view.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(v -> searchInput.setText(""));
//        setupClearButton(btnClear, searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setViewStateTo(btnClear, editable.toString().isEmpty());
                doFilter(editable.toString());
            }
        });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onWordClickListener = (OnWordClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implemented OnWordListSelectedListener");

        }
    }

    public class LoadWords extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            int count = 0;
            Cursor cursor = DBOpenHelper.getInstance(getContext()).getWords();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("rowid"));
                    String word = cursor.getString(cursor.getColumnIndex("word"));
                    words.add(new Word(id, word));
                    if( words.size() + 10 > count){
                        count = words.size();
                        publishProgress();
                    }
                } while (cursor.moveToNext());

            }
            cursor.close();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(Word word) {
        Log.d(TAG, "onItemClick: " + word.getWord());
        onWordClickListener.onWordClick(word);
    }

    public interface OnWordClickListener {
        void onWordClick(Word word);
    }

    private void doFilter(String filter) {
        filterWords.clear();
        if (filter.isEmpty()) {
            adapter.setFilteredWordList(words);
            adapter.setFilterText("");
        } else {
            if(!MDetect.isUnicode()){
                filter = Rabbit.zg2uni(filter);
            }
            for (Word word : words) {
                if (word.getWord().contains(filter)) {
                    filterWords.add(word);
                }
            }
            adapter.setFilteredWordList(filterWords);
            adapter.setFilterText(filter);
            if (filterWords.size() == 0) {
                tv_empty_info.setText(MDetect.getDeviceEncodedText("ရှာမတွေ့ပါ!"));
            } else {
                tv_empty_info.setText("");
            }
        }

    }

    private void setupClearButton(ImageButton btnClear, final EditText search) {

        btnClear.setOnClickListener(view -> search.setText(""));
    }

    private void setViewStateTo(ImageButton imageButton, boolean isEmpty) {

        if (isEmpty) {
            imageButton.setVisibility(View.INVISIBLE);
        } else {
            imageButton.setVisibility(View.VISIBLE);
        }
    }
}
