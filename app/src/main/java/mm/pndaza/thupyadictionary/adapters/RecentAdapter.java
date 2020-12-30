package mm.pndaza.thupyadictionary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mm.pndaza.thupyadictionary.R;
import mm.pndaza.thupyadictionary.database.DBOpenHelper;
import mm.pndaza.thupyadictionary.models.Favourite;
import mm.pndaza.thupyadictionary.models.Recent;
import mm.pndaza.thupyadictionary.utils.MDetect;
import mm.pndaza.thupyadictionary.utils.SharePref;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Recent> recents;
    private OnRecentItemClickListener onRecentItemClickListener;
    private int fontSize = 18;

    public RecentAdapter(ArrayList<Recent> recents, OnRecentItemClickListener onRecentItemClickListener) {
        this.recents = recents;
        this.onRecentItemClickListener = onRecentItemClickListener;
    }

    @NonNull
    @Override
    public RecentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        context = parent.getContext();
        fontSize = SharePref.getInstance(context).getPrefFontSize();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View bookmark_row_item = layoutInflater.inflate(R.layout.wordlist_row_item, parent, false);
        return new ViewHolder(bookmark_row_item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentAdapter.ViewHolder viewHolder, final int position) {
        Recent recent = recents.get(position);
        TextView tv_word = viewHolder.tv_word;
        tv_word.setText(MDetect.getDeviceEncodedText(recent.getWord()));
    }

    @Override
    public int getItemCount() {
        return recents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_word;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_word = itemView.findViewById(R.id.tv_word);
            tv_word.setTextSize(fontSize);
            tv_word.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecentItemClickListener.onRecentItemClick(recents.get(getAdapterPosition()));
        }
    }

    public void removeAll(){
        DBOpenHelper.getInstance(context).removeAllRecent();
        recents.clear();
        notifyDataSetChanged();
    }

    public interface OnRecentItemClickListener {
        void onRecentItemClick(Recent recent);
    }

}
