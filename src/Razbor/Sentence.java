package Razbor;

import Postgres.Postgree;

import java.util.LinkedList;

import static HelpClasses.Constants.*;

public class Sentence {
    private LinkedList<SentenceToken> sentenceList = new LinkedList<>();
    private LinkedList<Integer> breakpointList = new LinkedList<>();
    Postgree postgree = new Postgree();

    public Sentence(LinkedList<Token> sentenceList) {

        for (int i = 0; i < sentenceList.size(); i++) {
            if(sentenceList.get(i).getType()==TokenType.WORD){
                this.sentenceList.add(new SentenceToken(i,postgree.getAttr(sentenceList.get(i).getToken()),sentenceList.get(i).getToken()));
            }/*else{
                if(sentenceList.get(i).getToken().equals(","))this.sentenceList.add(new SentenceToken(i,new AtWord(ZPT)));
                if(sentenceList.get(i).getToken().equals("."))this.sentenceList.add(new SentenceToken(i,new AtWord(DOT)));
            }*/
        }
        findObject();
        isSentenceGood();
        System.out.println("Done!");
    }

    private void findObject() {
        for (int token = 0; token < sentenceList.size(); token++) {
            if (sentenceList.get(token).contain(PREDLOG)){
                predlogCutter(token);
            }
        }
        for (int token = 0; token < sentenceList.size(); token++) {
            if (sentenceList.get(token).contain(NOUN, IMEN_PADEJ)||sentenceList.get(token).contain(MEST, IMEN_PADEJ)) {
                createSObject(token);
            }
        }
        System.out.println();
        System.out.println("Subjects!!");
        System.out.println();
        for (int token = 0; token < sentenceList.size(); token++) {
            if (sentenceList.get(token).contain(NOUN, VIN_PADEJ)||sentenceList.get(token).contain(MEST, VIN_PADEJ)) {
                createSSubject(token);
            }
        }
        preDefine();

    }

    private void predlogCutter(int position) {
        boolean nounFound=false;
        if(sentenceList.get(position).isFullyDefined()){
            for (int i = position+1; i < position+10; i++) {
                if(i>=sentenceList.size())break;
                if(sentenceList.get(i).contain(CHISLITELNOE)){
                    if(sentenceList.get(position-1).contain(NOUN)){
                        if(sentenceList.get(position-1).soglas(sentenceList.get(position))){
                            nounFound = true;
                            //addNoun
                        }
                    }
                    //addChisl
                    continue;
                }
                if(sentenceList.get(i).soglas(sentenceList.get(position))){
                    //addSoglas
                }else{
                    //PrichOborotSearch
                }
            }

        }else{

        }
    }

    private void createSObject(int position) {
        LinkedList<SentenceToken> resultList = new LinkedList<>();
        resultList.add(new SentenceToken(sentenceList.get(position)).getClean(IMEN_PADEJ));
        int tempposition = position;
        while (tempposition - 1 >= 0) {
            tempposition--;
            if(sentenceList.get(tempposition).contain(NARECHIE)){
                resultList.addFirst(sentenceList.get(tempposition));
            }else if(sentenceList.get(tempposition).contain(PREDLOG)){
                resultList = null;
                break;
            }
            else if(sentenceList.get(tempposition).contain(PRIL)){
                if(sentenceList.get(tempposition).contain(PRIL,IMEN_PADEJ))
                    resultList.addFirst(new SentenceToken(sentenceList.get(tempposition)).getClean(PRIL,IMEN_PADEJ));
                else {
                    resultList = null;
                    break;
                }
            }else break;
        }
        tempposition = position;
        while (tempposition + 1 < sentenceList.size()) {
            if(resultList==null)break;
            tempposition++;
            if(sentenceList.get(tempposition).contain(PRIL,ROD_PADEJ)){
                if(sentenceList.get(tempposition+1).contain(NOUN,ROD_PADEJ))
                    resultList.add(new SentenceToken(sentenceList.get(tempposition)).getClean(PRIL,ROD_PADEJ));
                else if(sentenceList.get(tempposition+2).contain(NOUN,ROD_PADEJ)){
                    resultList.add(new SentenceToken(sentenceList.get(tempposition)).getClean(PRIL,ROD_PADEJ));
                }
            }else if(sentenceList.get(tempposition).contain(NOUN,ROD_PADEJ)){
                resultList.add(new SentenceToken(sentenceList.get(tempposition)).getClean(NOUN,ROD_PADEJ));
            }else if(sentenceList.get(tempposition).contain(CHISLITELNOE)){
                resultList.add(new SentenceToken(sentenceList.get(tempposition)).getClean(CHISLITELNOE));
            }else if(sentenceList.get(tempposition).contain(PREDLOG)){
                resultList.add(new SentenceToken(sentenceList.get(tempposition)).getClean(PREDLOG));
            }
            else break;
        }

        if(resultList!=null) {
            for (int i = 0; i < resultList.size(); i++) {
                if(resultList.getLast().contain(PREDLOG))resultList.removeLast();
                else break;
            }
            for (int i = 0; i < resultList.size(); i++) {
                if(resultList.get(i).getPosition()==sentenceList.get(position).getPosition())System.out.print(resultList.get(i).getToken().toUpperCase() + " ");
                else System.out.print(resultList.get(i).getToken() + " ");
            }
            System.out.println();
        }
    }

    private void createSSubject(int position) {
        LinkedList<SentenceToken> resultList = new LinkedList<>();
        resultList.add(sentenceList.get(position));
        int tempposition = position;
        boolean predlogflag = true;
        while (tempposition - 1 >= 0) {
            tempposition--;
            if (predlogflag&&sentenceList.get(tempposition).contain(PREDLOG)){
                resultList = null;
                break;
            }else if(sentenceList.get(tempposition).contain(PREDLOG)){
                resultList.addFirst(sentenceList.get(tempposition));
                break;
            }
            if(sentenceList.get(tempposition).contain(NOUN)){
                if(sentenceList.get(tempposition).contain(NOUN,IMEN_PADEJ)){
                    break;
                }else {
                    predlogflag = false;
                    resultList.addFirst(sentenceList.get(tempposition));
                }
            }else if (sentenceList.get(tempposition).contain(NARECHIE)) {
                resultList.addFirst(sentenceList.get(tempposition));
            }else if (sentenceList.get(tempposition).contain(COMPARATIVE)) {
                resultList.addFirst(sentenceList.get(tempposition));
            }else if(sentenceList.get(tempposition).contain(PREDLOG)) {
                resultList.addFirst(sentenceList.get(tempposition));
            }else if(sentenceList.get(tempposition).contain(CHASTICA)) {
                resultList.addFirst(sentenceList.get(tempposition));
            }else if(sentenceList.get(tempposition).contain(CHISLITELNOE)) {
                resultList.addFirst(sentenceList.get(tempposition));
            }else if(sentenceList.get(tempposition).contain(PRIL)){
                if(sentenceList.get(tempposition).contain(PRIL,VIN_PADEJ))
                    resultList.addFirst(sentenceList.get(tempposition));
                else {
                    resultList = null;
                    break;
                }
            }else break;

        }
        tempposition = position;
        while (tempposition + 1 < sentenceList.size()) {
            if(resultList==null)break;
            tempposition++;
            if(sentenceList.get(tempposition).contain(PRIL,ROD_PADEJ)){
                if(sentenceList.get(tempposition+1).contain(NOUN,ROD_PADEJ))
                    resultList.add(sentenceList.get(tempposition));
                else if(sentenceList.get(tempposition+2).contain(NOUN,ROD_PADEJ)){
                    resultList.add(sentenceList.get(tempposition));
                }
            }else if(sentenceList.get(tempposition).contain(NOUN,ROD_PADEJ)){
                resultList.add(sentenceList.get(tempposition));
            }else if(sentenceList.get(tempposition).contain(CHISLITELNOE,ROD_PADEJ)){
                resultList.add(sentenceList.get(tempposition));
            }
            else break;

        }

        if(resultList!=null) {
            for (int i = 0; i < resultList.size(); i++) {
                if(resultList.get(i).equals(sentenceList.get(position)))System.out.print(resultList.get(i).getToken().toUpperCase() + " ");
                else System.out.print(resultList.get(i).getToken() + " ");
            }
            System.out.println();
        }
    }
    private void preDefine() {
        if(breakpointList.size()>0){

        }
    }

    public boolean isSentenceGood() {
        if(sentenceList.size()<2) return false;
        for (int i = 0; i < sentenceList.size(); i++) {
            if(sentenceList.get(i).getAttr()!=null&&sentenceList.get(i).getAttr().size()!=1) return false;

        }
        return false;
    }
}
