package com.example.gopal.yod;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Gopal on 2/12/2020.
 */

public class Word extends RealmObject {
    public static final String CREATION_TIME = "wordCreationTime";
    public static final String WORD_NAME = "wordName";
    public static final String IS_BOOKMARKED = "isBookmarked";
    private String wordName;
    private String wordMeaning;
    private Date wordCreationTime;
    private int notifyCount;
    private boolean isBookmarked;
    public Word(){}

    public Word(String wordName) {
        this.wordName = wordName;
    }

    public Word(String wordName, String wordMeaning, Date wordCreationTime) {
        this.wordName = wordName;
        this.wordMeaning = wordMeaning;
        this.wordCreationTime = wordCreationTime;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordMeaning() {
        return wordMeaning;
    }

    public void setWordMeaning(String wordMeaning) {
        this.wordMeaning = wordMeaning;
    }

    public Date getWordCreationTime() {
        return wordCreationTime;
    }

    public void setWordCreationTime(Date wordCreationTime) {
        this.wordCreationTime = wordCreationTime;
    }

    public int getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(int notifyCount) {
        this.notifyCount = notifyCount;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
