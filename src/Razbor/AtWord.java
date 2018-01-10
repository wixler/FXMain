package Razbor;


import java.util.ArrayList;
import java.util.HashSet;

import static HelpClasses.Constants.*;

public class AtWord {
    private ArrayList<Integer> attr;
    private int type;
    private int padej;
    private int rod;
    private int chislo;

    private int glagvid;
    private int glagperehod;
    private int glagface;
    private int glagtime;
    private int glagnaklonenie;

    public AtWord(HashSet<Integer> attr) {
        ArrayList<Integer> temp = new ArrayList<>(attr);
        for (int i = 0; i < temp.size(); i++) {
            sort(temp.get(i));
        }
        if(temp.size()>0) this.attr = temp;
    }

    private void sort(int value) {
        if(value<18){
            type = value;
//                temp.remove(i);
//                i--;
        }
        if(value>99&&value<113){
            padej = value;
//                temp.remove(i);
//                i--;
        }
        if(value>=160&&value<165){
            rod = value;
//                temp.remove(i);
//                i--;
        }
        if(value==171||value==172){
            chislo = value;
//                temp.remove(i);
//                i--;
        }
        if(value==36||value==37){
            glagvid = value;
//                temp.remove(i);
//                i--;
        }
        if(value==39||value==40){
            glagperehod = value;
//                temp.remove(i);
//                i--;
        }
        if(value==54||value==55){
            glagnaklonenie = value;
//                temp.remove(i);
//                i--;
        }
        if(value==46||value==47||value==48){
            glagface = value;
//                temp.remove(i);
//                i--;
        }
        if(value==50||value==51||value==52){
            glagtime = value;
//                temp.remove(i);
//                i--;
        }
    }

    public AtWord(int type) {
        this.type = type;
    }

    public ArrayList<Integer> getAttr() {
        return attr;
    }

    public void addAttr(int attribute) {
        if(attr!=null) {
            attr.add(attribute);
            sort(attribute);
        }
    }

    public boolean canBeSoglas(AtWord inputWord) {
        if(this.type== NOUN&&(inputWord.type==NOUN||inputWord.type==GLAG||inputWord.type==PRIL||inputWord.type==PRIL_KRATKOE))return true;
        if(this.type== PRIL&&(inputWord.type==NOUN||inputWord.type==PRIL))return true;
        return false;
    }
    public boolean soglas(AtWord inputWord) {
        if((this.type==PRIL&&inputWord.type==NOUN)||this.type==NOUN&&inputWord.type==PRIL){
            if(rod==inputWord.rod&&chislo==inputWord.chislo&&padej==inputWord.padej) return true;
        }
        return false;
    }
}
