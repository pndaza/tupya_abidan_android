package mm.pndaza.thupyadictionary.adapters;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import mm.pndaza.thupyadictionary.R;
import mm.pndaza.thupyadictionary.models.Word;
import mm.pndaza.thupyadictionary.utils.MDetect;
import mm.pndaza.thupyadictionary.utils.Rabbit;
import mm.pndaza.thupyadictionary.utils.SharePref;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>
implements FastScrollRecyclerView.SectionedAdapter {

    protected OnItemClickListener listener;
    private ArrayList<Word> words;

    private static final String TAG = "WordAdapter";
    private String filterText = "";
    private int fontSize = 18;

    public WordAdapter(ArrayList<Word> words, OnItemClickListener listener){
        this.words = words;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        fontSize = SharePref.getInstance(parent.getContext()).getPrefFontSize();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wordlist_row_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView tv_word = holder.tv_word;
        Word word = words.get(position);
        if(filterText.isEmpty()) {
            tv_word.setText(MDetect.getDeviceEncodedText(word.getWord()));
        } else {
            tv_word.setText(getHighlightedText(word.getWord()));
        }

    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setFilteredWordList(ArrayList<Word> filterWords){
        words = filterWords;
        notifyDataSetChanged();
    }

    public void setFilterText(String filterText){
        this.filterText = filterText;
    }

    private SpannableString getHighlightedText(String word){

        String wordInDeviceEncoding = MDetect.getDeviceEncodedText(word);
        String filterInDeviceEncoding = MDetect.getDeviceEncodedText(filterText);

        SpannableString highlightedText = new SpannableString(wordInDeviceEncoding);
        int start_index = word.indexOf(filterInDeviceEncoding);
        int end_index = start_index + filterInDeviceEncoding.length();

        highlightedText.setSpan(
                new ForegroundColorSpan(Color.RED), start_index, end_index,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // set background color for query words

        highlightedText.setSpan(
                new BackgroundColorSpan(Color.YELLOW), start_index, end_index,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return highlightedText;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        char firstChar = words.get(position).getWord().charAt(0);
        return String.valueOf(firstChar);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_word;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_word = itemView.findViewById(R.id.tv_word);
            tv_word.setTextSize(fontSize);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(words.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Word word);
    }

}
