package Razbor;


import java.util.ArrayList;
import java.util.HashSet;

import static HelpClasses.Constants.PREDLOG;
import static HelpClasses.Constants.ROD_PADEJ;

public class SentenceToken {
    private int position;
    private String token;
    private ArrayList<AtWord> attr;
    private ArrayList<Integer> sentAttr;

    public SentenceToken(int position, ArrayList<HashSet<Integer>> attr, String token) {
        this.position = position;
        this.token = token;
        this.attr = new ArrayList<>();
        for (int i = 0; i < attr.size(); i++) {
            this.attr.add(new AtWord(attr.get(i)));
        }
    }

    public SentenceToken(SentenceToken sentenceToken) {
        this.position = sentenceToken.position;
        this.token = sentenceToken.token;
        this.attr = new ArrayList<>();
        if(sentenceToken.attr!=null)attr.addAll(sentenceToken.attr);
        sentAttr = new ArrayList<>();
        if(sentenceToken.sentAttr!=null)sentAttr.addAll(sentenceToken.sentAttr);
    }

    public SentenceToken(int position, AtWord word) {
        attr = new ArrayList<>();
        attr.add(word);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<AtWord> getAttr() {
        return attr;
    }

    public ArrayList<Integer> getSentAttr() {
        return sentAttr;
    }

    public String getToken() {
        return token;
    }

    public boolean contain(int param) {
        for (int i = 0; i < attr.size(); i++) {
            if(attr.get(i).getAttr()!=null&&attr.get(i).getAttr().contains(param))return true;
        }
        return false;
    }
    public boolean contain(int param1, int param2) {
        for (int i = 0; i < attr.size(); i++) {
            if(attr.get(i).getAttr()!=null&&attr.get(i).getAttr().contains(param1)&&attr.get(i).getAttr().contains(param2))return true;
        }
        return false;
    }

    public SentenceToken getClean(int param){
        for (int i = 0; i < attr.size(); i++) {
            if(attr.get(i).getAttr()!=null&&!attr.get(i).getAttr().contains(param)) attr.remove(i);
        }
        return this;
    }
    public SentenceToken getClean(int param1, int param2){
        for (int i = 0; i < attr.size(); i++) {
            if(attr.get(i).getAttr()!=null&&!(attr.get(i).getAttr().contains(param1)&&attr.get(i).getAttr().contains(param2)))attr.remove(i);
        }
        return this;
    }
    public boolean isFullyDefined() {
        return attr.size()==1;
    }

    public boolean soglas(SentenceToken inputToken) {
        if(this.contain(PREDLOG)||inputToken.contain(PREDLOG)){
            //TODO if(this and input contain PREDLOG)
            if(this.contain(PREDLOG)) return predlogSoglas(this, inputToken);
            else return predlogSoglas(inputToken, this);
        }
        for (int i = 0; i < attr.size(); i++) {
            for (int j = 0; j < inputToken.attr.size(); j++) {
                if(attr.get(i).soglas(inputToken.attr.get(j))){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean predlogSoglas(SentenceToken predlog, SentenceToken word) {
        for (int i = 0; i < predlog.attr.size(); i++) {
            for (int j = 0; j < predlog.attr.get(i).getAttr().size(); j++) {
                if(predlog.attr.get(i).getAttr().get(j)==PREDLOG){
                    if(predlog.token.equals("с")){
                        predlog.attr.get(i).addAttr(ROD_PADEJ);
                    }
                }
            }


        }
        return false;
    }
}
