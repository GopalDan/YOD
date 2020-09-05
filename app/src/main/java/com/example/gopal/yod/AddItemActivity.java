package com.example.gopal.yod;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        final EditText wordEditText = findViewById(R.id.word_edit_text);
        final EditText meaningEditText = findViewById(R.id.meaning_edit_text);
        Button button = findViewById(R.id.save_button);
    /*    button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterWord = wordEditText.getText().toString();
                String wordMeaning = meaningEditText.getText().toString();
                if(TextUtils.isEmpty(enterWord)){
                    Toast.makeText(AddItemActivity.this, "Enter word", Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(wordMeaning)){
                    Toast.makeText(AddItemActivity.this, "Enter Meaning", Toast.LENGTH_SHORT).show();
                    return;
                }

                Word word = new Word(enterWord, wordMeaning, getCurrentDate());
                try(Realm realm = Realm.getDefaultInstance()) {
                    realm.beginTransaction();
                    realm.copyToRealm(word);
                    realm.commitTransaction();
                    wordEditText.setText("");

                }
            }
        });*/

    }

    private String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        return currentDate;
    }
}
