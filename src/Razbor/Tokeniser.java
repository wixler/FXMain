package Razbor;

import HelpClasses.Constants;
import Postgres.Postgree;

import java.util.HashSet;
import java.util.LinkedList;

public class Tokeniser {
    LinkedList<Token> tokensList;
    HashSet<Character> letters = new HashSet<>();
    HashSet<Character> cap_letters = new HashSet<>();
    HashSet<Character> nums = new HashSet<>();
    HashSet<Character> symbols = new HashSet<>();
    Postgree postgree;

    public Tokeniser() {
        postgree = new Postgree();
        char[] letters = Constants.ALPHABET.toCharArray();
        char[] cap_letters = Constants.CAP_ALPH.toCharArray();
        char[] nums = Constants.NUMERAL.toCharArray();
        char[] symbols = Constants.SYMBOLS.toCharArray();
        for (char letter : letters) {
            this.letters.add(letter);
        }
        for (char cap_letter : cap_letters) {
            this.cap_letters.add(cap_letter);
        }
        for (char cap_letter : nums) {
            this.nums.add(cap_letter);
        }
        for (char cap_letter : symbols) {
            this.symbols.add(cap_letter);
        }
    }

    public LinkedList<Token> getData(String inputString) {
        tokensList = new LinkedList<>();
        String[] spasedTokens;
        String[] paragraphs = inputString.split("\n");

        for (String paragraph : paragraphs) {
            spasedTokens = paragraph.split(" ");
            for (String spasedToken : spasedTokens) {
                tokensList.addAll(createToken(spasedToken));
            }
            tokensList.add(new Token(TokenType.SUBTOKEN,SubtokenType.NEW_STRING, "/n"));
        }
        foldingAsLiteraturalText();
        return tokensList;
    }

    private LinkedList<Token> createToken(String inputString) {
        StringBuilder sb = new StringBuilder(inputString);
        StringBuilder tempSB = new StringBuilder();
        LinkedList<Token> resultToken = new LinkedList<>();
        char tempChar;
        //Проверка, если есть незарегистрированные символы то отправка в сложные
        for (int i = 0; i < sb.length(); i++) {
            tempChar = sb.charAt(i);
            if(!(letters.contains(tempChar)||cap_letters.contains(tempChar)||nums.contains(tempChar)||symbols.contains(tempChar))) {
                Token token = new Token();
                token.setSpased(true);
                token.setToken(inputString);
                token.setType(TokenType.COMPLEX);
                resultToken.add(token);
                return resultToken;
            }
        }
        Token token = new Token();
        for (int i = 0; i < sb.length(); i++) {
            tempChar = sb.charAt(i);
            if(nums.contains(tempChar)){
                if(token.getType()==null||token.getType()==TokenType.NUMERAL)token.setType(TokenType.NUMERAL);
                else{
                    if(token.getType()!=null)resultToken.add(token);
                    token = new Token();
                    tempSB = new StringBuilder();
                    token.setType(TokenType.NUMERAL);
                }
                tempSB.append(tempChar);
                token.setToken(tempSB.toString());
                if(i==sb.length()-1)token.setSpased(true);
                else token.setSpased(false);
            }
            if(letters.contains(tempChar)||cap_letters.contains(tempChar)){
                if(token.getType()==null||token.getType()==TokenType.WORD)token.setType(TokenType.WORD);
                else{
                    if(token.getType()!=null)resultToken.add(token);
                    token = new Token();
                    tempSB = new StringBuilder();
                    token.setType(TokenType.WORD);
                }
                tempSB.append(tempChar);
                token.setToken(tempSB.toString());
                if(i==sb.length()-1)token.setSpased(true);
                else token.setSpased(false);
            }
            if(symbols.contains(tempChar)){
                if(token.getType()!=null)resultToken.add(token);
                token = new Token();
                tempSB = new StringBuilder();
                if(i==sb.length()-1)token.setSpased(true);
                else token.setSpased(false);
                token.setToken(String.valueOf(tempChar));
                token.setType(TokenType.SYMBOL);
                resultToken.add(token);
                token = new Token();
            }
        }
        token.setSpased(true);
        if(token.getType()!=null)resultToken.add(token);

        return resultToken;
    }

    private void foldingAsLiteraturalText(){
        for (int i = 0; i < tokensList.size(); i++) {
            if(tokensList.get(i).getToken()!=null&&tokensList.get(i).getToken().equals(".")&& tokensList.get(i).isSpased()){
                if(i-2>=0){
                    if(tokensList.get(i-1).getToken().equals(".")&&!tokensList.get(i-1).isSpased()&&tokensList.get(i-2).getToken().equals(".")&&!tokensList.get(i-2).isSpased()){
                        tokensList.remove(i-2);
                        tokensList.remove(i-2);
                        tokensList.remove(i-2);
                        Token token = new Token();
                        token.setSpased(true);
                        token.setType(TokenType.SYMBOL);
                        token.setSubtype(SubtokenType.ThreeDotted);
                        token.setToken("...");
                        tokensList.add(i-2,token);
                        i--;
                        i--;
                    }
                }
            }
            if(tokensList.get(i).getToken()!=null&&tokensList.get(i).getToken().equals("-")) {
                if(i-1>=0&&tokensList.get(i-1).getType()==TokenType.WORD&&!tokensList.get(i-1).isSpased()){
                    if(i+1<tokensList.size()&&tokensList.get(i+1).getType()==TokenType.WORD&&!tokensList.get(i).isSpased()){
                        String temp = tokensList.get(i-1).getToken()+"-"+tokensList.get(i+1).getToken();
                        if(postgree.isLemmaInDB(temp)){
                            Token token = new Token();
                            token.setSpased(tokensList.get(i+1).isSpased());
                            tokensList.remove(i-1);
                            tokensList.remove(i-1);
                            tokensList.remove(i-1);
                            token.setType(TokenType.WORD);
                            token.setToken(temp);
                            tokensList.add(i-1,token);
                            i--;
                        }
                    }
                    if(i+2<tokensList.size()&&tokensList.get(i+1).getSubtype()!=null&&tokensList.get(i+1).getSubtype()==SubtokenType.NEW_STRING){
                        if(tokensList.get(i+2).getType()==TokenType.WORD){
                            String temp = tokensList.get(i-1).getToken()+"-"+tokensList.get(i+2).getToken();
                            if(postgree.isLemmaInDB(temp)){
                                Token token = new Token();
                                token.setSpased(tokensList.get(i+2).isSpased());
                                tokensList.remove(i-1);
                                tokensList.remove(i-1);
                                tokensList.remove(i-1);
                                tokensList.remove(i-1);
                                token.setType(TokenType.WORD);
                                token.setToken(temp);
                                tokensList.add(i-1,token);
                                i--;
                                i--;
                            }
                            temp = tokensList.get(i-1).getToken()+tokensList.get(i+2).getToken();
                            if(postgree.isLemmaInDB(temp)){
                                Token token = new Token();
                                token.setSpased(tokensList.get(i+2).isSpased());
                                tokensList.remove(i-1);
                                tokensList.remove(i-1);
                                tokensList.remove(i-1);
                                tokensList.remove(i-1);
                                token.setType(TokenType.WORD);
                                token.setToken(temp);
                                tokensList.add(i-1,token);
                                i--;
                                i--;
                            }
                        }
                    }
                }
            }
        }
    }
}
