package pl.poznan.put.cs.si.puttalky;

import java.util.HashSet;

public class ListaDodatkow {
    private HashSet<String> dodatki;
    private HashSet<String> zakazane;
    public ListaDodatkow(){
        dodatki = new HashSet<String>();
        zakazane = new HashSet<String>();
    }
    public void add(String dodatek){
        if(dodatek != null)
            this.dodatki.add(dodatek);
    }

    public HashSet<String> getDodatki() {
        return dodatki;
    }
    public void forbid(String zakazany){
        if(zakazany != null)
            this.zakazane.add(zakazany);
    }

    public HashSet<String> getZakazane() {
        return this.zakazane;
    }
    public String pizzaSpecjalna(){
        String output = "";
        String symbolBufor = "";
        String symbol = "";
        String next = "";
        for(String d:dodatki){
            output += symbol + next;
            next = d.split("#")[1];
            symbol = symbolBufor;
            symbolBufor = ", ";
        }
        if(symbol.equals(", ")){
            symbol = " i ";
        }
        output += symbol + next;

        return "Nie istnieje taka pizza w systemie \n" +
                "Polecam pizzę specjalną z dodatkami:  " + output;
    }
}
