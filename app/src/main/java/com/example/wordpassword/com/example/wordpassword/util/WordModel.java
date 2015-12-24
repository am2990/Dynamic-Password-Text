package com.example.wordpassword.com.example.wordpassword.util;

import java.util.ArrayList;

/**
 * Created by apurv on 12/23/2015.
 */
public class WordModel {

    private int _id;

    private String original_sentence;
    private String trimmed_sentence;

    private int passwordSelection;

    private String serealized_synonyms;
    private String serealized_antonyms;;
    private String serealized_similiars;;

    public String getSerealizedSynonyms() {
        return serealized_synonyms;
    }

    public void setSerealizedSynonyms(String serealized_synonyms) {
        this.serealized_synonyms = serealized_synonyms;
    }

    public String getSerealizedAntonyms() {
        return serealized_antonyms;
    }

    public void setSerealizedAntonyms(String serealized_antonyms) {
        this.serealized_antonyms = serealized_antonyms;
    }

    public String getSerealizedSimiliars() {
        return serealized_similiars;
    }

    public void setSerealizedSimiliars(String serealized_similiars) {
        this.serealized_similiars = serealized_similiars;
    }

    public int getPasswordSelection() {
        return passwordSelection;
    }

    public void setPasswordSelection(int passwordSelection) {
        this.passwordSelection = passwordSelection;
    }

    public String getTrimmedSentence() {
        return trimmed_sentence;
    }

    public void setTrimmedSentence(String trimmed_sentence) {
        this.trimmed_sentence = trimmed_sentence;
    }

    public String getOriginalSentence() {
        return original_sentence;
    }

    public void setOriginalSentence(String original_sentence) {
        this.original_sentence = original_sentence;
    }
}
