package com.example.wordpassword.util;

import java.util.ArrayList;
import java.util.HashMap;

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

    private HashMap<String, ArrayList<String>> synonyms = null;
    private HashMap<String, ArrayList<String>> antonyms = null;
    private HashMap<String, ArrayList<String>> similar = null;

    public void addSynonyms(String key, String word){
        if(synonyms == null)
            synonyms = new HashMap<>();
        if(synonyms.get(key) != null)
            synonyms.get(key).add(word);
        else {
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(word);
            synonyms.put(key,arr);
        }

    }

    public String[] getWordList(String type, String word){
        String[] arr = {};
        if(synonyms != null) {
            arr = synonyms.get(word).toArray(new String[0]);
            return arr;
        }
        else
            return arr;
    }

    public void addAntonyms(String key, String word){
        if(antonyms == null)
            antonyms = new HashMap<>();
        if(antonyms.get(key) != null)
            antonyms.get(key).add(word);
        else {
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(word);
            antonyms.put(key,arr);
        }
    }

    public void addSimilar(String key, String word){
        if(similar == null)
            similar = new HashMap<>();
        if(similar.get(key) != null)
            similar.get(key).add(word);
        else {
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(word);
            similar.put(key,arr);
        }
    }

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
