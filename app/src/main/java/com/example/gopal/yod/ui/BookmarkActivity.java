package com.example.gopal.yod.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.gopal.yod.DataAdapter;
import com.example.gopal.yod.R;
import com.example.gopal.yod.Word;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    public static final String TAG = "BookmarkActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        RecyclerView recyclerView = findViewById(R.id.bookmark_recycler_view);
        TextView emptyTextView = findViewById(R.id.empty_text_view);
        setTitle("Bookmarked Words");
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Word> wordRealmResults = realm.where(Word.class).equalTo(Word.IS_BOOKMARKED, true).findAll();
        if(wordRealmResults.size()==0){
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            emptyTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            ArrayList<Word> bookmarkedWords = new ArrayList<>();
            bookmarkedWords.addAll(wordRealmResults);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            DataAdapter dataAdapter = new DataAdapter(this, bookmarkedWords, realm, BookmarkActivity.TAG);
            recyclerView.setAdapter(dataAdapter);
        }
    }

}