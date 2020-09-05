package com.example.gopal.yod;

import android.app.AlertDialog;

import com.example.gopal.yod.ui.BookmarkActivity;
import com.example.gopal.yod.ui.NotificationActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private RealmResults<Word> realmResults;
    private ArrayList<Word> wordArrayList;
    private RelativeLayout rootLayout;
    private DataAdapter dataAdapter;
    boolean hasDoublePressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.root_layout);
        final TextView emptyTextView = findViewById(R.id.empty_text_view);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        wordArrayList = new ArrayList<>();
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        realmResults = realm.where(Word.class).sort(Word.CREATION_TIME, Sort.DESCENDING).findAll();
        if(realmResults.size()==0) {
            emptyTextView.setVisibility(View.VISIBLE);
        }

            wordArrayList.addAll(realmResults);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            dataAdapter = new DataAdapter(this, wordArrayList, realm, MainActivity.TAG);
            recyclerView.setAdapter(dataAdapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialog();
            }
        });
        try {
//            if (realmResults.size() != 0)
                realmResults.addChangeListener(new RealmChangeListener<RealmResults<Word>>() {
                    @Override
                    public void onChange(RealmResults<Word> words) {
                        try {
                            if(words.size()>0)
                                emptyTextView.setVisibility(View.GONE);

                            if (wordArrayList != null && wordArrayList.size() > 0){
                                wordArrayList.clear();

                            }
                            wordArrayList.addAll(words);
                            dataAdapter.notifyDataSetChanged();
                        }catch (Exception ex){
                            Log.e(TAG, "onChange: " );
                        }
                    }
                });
        }catch (Exception ex){
            Log.e(TAG, "onCreate: ");
        }

    }

    private void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.activity_add_item, null);
        builder.setView(customLayout);
        builder.setCancelable(true);
        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText wordEditText = customLayout.findViewById(R.id.word_edit_text);
        final EditText meaningEditText = customLayout.findViewById(R.id.meaning_edit_text);
        Button button = customLayout.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterWord = wordEditText.getText().toString();
                String wordMeaning = meaningEditText.getText().toString();
                if(TextUtils.isEmpty(enterWord)){
                    Toast.makeText(MainActivity.this, "Enter word", Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(wordMeaning)){
                    Toast.makeText(MainActivity.this, "Enter Meaning", Toast.LENGTH_SHORT).show();
                    return;
                }

                Word word = new Word(enterWord, wordMeaning, new Date());
                try(Realm realm = Realm.getDefaultInstance()) {
                    realm.beginTransaction();
                    realm.copyToRealm(word);
                    realm.commitTransaction();
                    wordEditText.setText("");
                    meaningEditText.setText("");
                    dialog.dismiss();


                }
            }
        });




    }

    private String getCurrentDate(){
//       long creationTimeAsLong = System.currentTimeMillis();
//       Date date = new Date();
//        Log.e(TAG, "creationTimeAsLong: " + creationTimeAsLong);
//        Log.e(TAG, "date: " + date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        return currentDate;
    }

    @Override
    public void onBackPressed() {
        if (hasDoublePressed){
            super.onBackPressed();
            return;
        }
        hasDoublePressed = true;
        Snackbar.make(rootLayout, "Please click back again to exit", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                hasDoublePressed=false;
            }
        }, 2000);
    }

   SearchView.OnQueryTextListener onQueryTextListener =  new SearchView.OnQueryTextListener(){
       @Override
       public boolean onQueryTextChange(String newText) {
           return false;
       }

       @Override
       public boolean onQueryTextSubmit(String query) {
           return false;
       }
   };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String t = query;
//                if( ! searchView.isIconified()) {
//                    searchView.setIconified(true);
//                }
//                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                String  t = s;
                if(!TextUtils.isEmpty(s)) {
                    dataAdapter.setFilter(s);
                }else {
                    if(realmResults.size()>0){
                        ArrayList<Word> words = new ArrayList<>();
                        words.addAll(realmResults);
                        dataAdapter.notifyForChange(words);

                    }
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.book_marked:
                startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
                break;
            case R.id.notification:
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
            case R.id.search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search_action:
                Toast.makeText(this, "Search action", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
