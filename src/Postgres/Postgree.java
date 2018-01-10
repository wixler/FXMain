package Postgres;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.CharBuffer;
import java.sql.*;
import java.util.*;

import static HelpClasses.Constants.*;

public class Postgree {
    private Connection conn;
    private Statement stat;
    private ResultSet rs;
    private String sqlQuery;
    private LinkedHashMap<String, ArrayList<HashSet<Integer>>> buffer = new LinkedHashMap<>(5000);
    private HashMap<String, ArrayList<HashSet<Integer>>> innerbuffer = new HashMap<>();
    public Postgree() {
        fillInnerBuffer();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "ghbdtn");
            stat = conn.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean isLemmaInDB(String lemma) {
        lemma = lemma.toLowerCase();
        sqlQuery = "SELECT * FROM inftable where lemma like '"+lemma+"';";
        try {
            rs = stat.executeQuery(sqlQuery);
            if(rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<HashSet<Integer>> getAttr(String lemma) {
        lemma = lemma.toLowerCase();
        ArrayList<HashSet<Integer>> resultArray = new ArrayList<>();
        if(innerbuffer.containsValue(lemma)) resultArray.addAll(innerbuffer.get(lemma));
        if(buffer.containsValue(lemma)) {
            resultArray.addAll(buffer.get(lemma));
            return resultArray;
        }
        else {
            sqlQuery = "SELECT mainattr FROM inftable WHERE lemma like '" + lemma + "';";
            try {
                rs = stat.executeQuery(sqlQuery);
                while (rs.next()) {
                    resultArray.add(convertStrAttrToSet(rs.getString(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            buffer.put(lemma, resultArray);
            return resultArray;
        }
    }

    private void fillInnerBuffer() {
        File file = new File(".\\src\\Data\\predlog.txt");
        StringBuilder sb = new StringBuilder();
        String temps;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((temps = reader.readLine()) != null) {
                sb.append(temps);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(sb.toString());
        String predlog;
        ArrayList<HashSet<Integer>> temp;
        HashSet<Integer> set;
        JSONArray arr = jsonObject.getJSONArray("start");
        for (int i = 0; i < arr.length(); i++) {
            predlog = arr.getJSONObject(i).getString("predlog");
            temp = new ArrayList<>();
            JSONArray attrarr = arr.getJSONObject(i).getJSONArray("attr");
            for (int j = 0; j < attrarr.length(); j++) {
                JSONArray hasharr = attrarr.getJSONObject(j).getJSONArray("hash");
                set = new HashSet<>();
                for (int k = 0; k < hasharr.length(); k++) {
                    set.add((Integer) hasharr.get(k));
                }
                temp.add(set);
            }
            innerbuffer.put(predlog, temp);
        }
    }


    public String convertIntList(List<Integer> al){
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = 0; i < al.size(); i++) {
            sb.append(al.get(i));
            if(i<al.size()-1)sb.append(',');
        }
        sb.append('}');
        return sb.toString();
    }
    private String convertStringArray(Object[] inputArray){
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = 0; i < inputArray.length; i++) {
            sb.append('\"').append(inputArray[i]).append('\"');
            if(i<inputArray.length-1)sb.append(',');
        }
        sb.append('}');
        return sb.toString();
    }
    private ArrayList<Integer> convertStrAttrToList(String attr){
        int tempInt;
        ArrayList<Integer> resultList = new ArrayList<>();
        StringBuilder sb = new StringBuilder(attr);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        String[] parts = sb.toString().split(",");
        for (int i = 0; i < parts.length; i++) {
            if(parts[i].length()>0) {
                tempInt = Integer.parseInt(parts[i]);
                resultList.add(tempInt);
            }
        }
        return resultList;
    }
    private HashSet<Integer> convertStrAttrToSet(String attr){
        int tempInt;
        HashSet<Integer> resultList = new HashSet<>();
        StringBuilder sb = new StringBuilder(attr);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        String[] parts = sb.toString().split(",");
        for (int i = 0; i < parts.length; i++) {
            if(parts[i].length()>0) {
                tempInt = Integer.parseInt(parts[i]);
                resultList.add(tempInt);
            }
        }
        return resultList;
    }
    private ArrayList<String> convertStrToList(String part){
        ArrayList<String> resultList = new ArrayList<>();
        StringBuilder sb = new StringBuilder(part);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        String[] parts = sb.toString().split(",");
        for (int i = 0; i < parts.length; i++) {
            if(parts[i].length()>0) {
                resultList.add(parts[i]);
            }
        }
        return resultList;
    }
}
