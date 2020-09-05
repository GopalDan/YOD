package com.example.gopal.yod;

import android.app.AlertDialog;
import android.content.Context;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gopal.yod.ui.BookmarkActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gopal on 2/12/2020.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private Context context;
    private ArrayList<Word> words;
    private Realm mRealm;
    private String INITIATOR;


    public DataAdapter(Context context, ArrayList<Word> words, Realm realm, String initiator) {
        this.context = context;
        this.words = words;
        mRealm = realm;
        INITIATOR = initiator;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, final int position) {
        final Word word = words.get(position);
        holder.wordNameTextView.setText(word.getWordName());
        if (word.isBookmarked())
            holder.bookMarkImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_book_mark, context.getTheme()));
        else
            holder.bookMarkImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_un_bookmark, context.getTheme()));

        //        holder.wordMeaningTextView.setText(word.getWordMeaning());
//        holder.wordCreationTimeTextView.setText(formatDate(word.getWordCreationTime()));
        onBookmarkButtonClick(holder, word);

        onWordClick(holder, word);
    }

    private void onWordClick(DataViewHolder holder, final Word word) {
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWordInfoDialog(word);
//                Toast.makeText(context, "Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onBookmarkButtonClick(DataViewHolder holder, final Word word) {
        holder.bookMarkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final Word result = mRealm.where(Word.class).equalTo(Word.WORD_NAME, word.getWordName()).findFirst();
                    if (!result.isBookmarked()) {
                        //  holder.bookMarkImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_book_mark, context.getTheme()));
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                result.setBookmarked(true);
                                notifyDataSetChanged();
                            }
                        });
                    } else {
                        //  holder.bookMarkImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_un_bookmark, context.getTheme()));
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                result.setBookmarked(false);
                                if (INITIATOR.equals(BookmarkActivity.TAG)){
                                    words.remove(word);
                                }
                                notifyDataSetChanged();
                            }
                        });
                    }



            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    private void showWordInfoDialog(Word word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customLayout = LayoutInflater.from(context).inflate(R.layout.word_info_layout, null);
        builder.setView(customLayout);
        builder.setCancelable(true);
        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.show();
        TextView wordTextView = customLayout.findViewById(R.id.word_text_view_dialog);
        TextView wordMeaningTextView = customLayout.findViewById(R.id.word_meaning_text_view_dialog);
        TextView wordCreationTimeTextView = customLayout.findViewById(R.id.word_creation_time_text_view_dialog);
        wordTextView.setText(word.getWordName());
        wordMeaningTextView.setText(word.getWordMeaning());
        wordCreationTimeTextView.setText(formatDate(word.getWordCreationTime()));

    }

    private String formatDate(Date date) {
//       long creationTimeAsLong = System.currentTimeMillis();
//       Date date = new Date();
//        Log.e(TAG, "creationTimeAsLong: " + creationTimeAsLong);
//        Log.e(TAG, "date: " + date);

//        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
        String formatDate = sdf.format(date);
        return formatDate;
    }

    public void onUnBookmarkRemoveWordFromActivity(){
        RealmResults<Word> wordRealmResults = mRealm.where(Word.class).equalTo(Word.IS_BOOKMARKED, true).findAll();
        words.clear();
        words.addAll(wordRealmResults);
        notifyDataSetChanged();

    }

    public void setFilter(String subQuery) {
        ArrayList<Word> filteredWords = new ArrayList<>();
        for(Word word : words){
            if (word.getWordName().toLowerCase().contains(subQuery.toLowerCase())){
                filteredWords.add(word);
            }
        }
        words.clear();
        words = filteredWords;
        notifyDataSetChanged();
    }

    public void notifyForChange(ArrayList<Word> wordArrayList) {
        words.clear();
        words = wordArrayList;
        notifyDataSetChanged();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        TextView wordNameTextView, wordMeaningTextView, wordCreationTimeTextView;
        ImageView bookMarkImageView;
        RelativeLayout rootLayout;

        public DataViewHolder(View itemView) {
            super(itemView);
            wordNameTextView = itemView.findViewById(R.id.word_text_view);
            wordMeaningTextView = itemView.findViewById(R.id.word_meaning_text_view);
            wordCreationTimeTextView = itemView.findViewById(R.id.word_creation_time_text_view);
            bookMarkImageView = itemView.findViewById(R.id.book_mark_image_view);
            rootLayout = itemView.findViewById(R.id.root_layout);

        }
    }


}
