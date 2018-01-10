package Razbor;

import java.util.ArrayList;
import java.util.HashSet;

public class Token {
    private TokenType type;
    private SubtokenType subtype;
    private boolean spased;
    private String token;
    private ArrayList<HashSet<Integer>> attr;

    public Token() {
    }

    public Token(TokenType type, SubtokenType subtype, String token) {
        this.type = type;
        this.subtype = subtype;
        this.token = token;
    }

    public TokenType getType() {
        return type;
    }

    public SubtokenType getSubtype() {
        return subtype;
    }

    public boolean isSpased() {
        return spased;
    }

    public String getToken() {
        return token;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public void setSubtype(SubtokenType subtype) {
        this.subtype = subtype;
    }

    public void setSpased(boolean spased) {
        this.spased = spased;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAttr(ArrayList<HashSet<Integer>> attr) {
        this.attr = attr;
    }
    public void addAttr(HashSet<Integer> attr) {
        if(this.attr==null) attr = new HashSet<>();
        this.attr.add(attr);
    }

    public ArrayList<HashSet<Integer>> getAttr() {
        return attr;
    }
}
