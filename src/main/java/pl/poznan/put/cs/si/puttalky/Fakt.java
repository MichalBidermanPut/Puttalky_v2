package pl.poznan.put.cs.si.puttalky;

/** Author: agalawrynowicz<br>
 * Date: 19-Dec-2016 */

public class Fakt {
	
	private String nazwa;
	private String wartosc;
	
	public Fakt(){}
	
	public Fakt(String nazwa, String wartosc)
	{
		this.nazwa=nazwa;
		this.wartosc = wartosc;
	}

	
	public String getNazwa() {
        return this.nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getWartosc() {
        return this.wartosc;
    }

    public void setWartosc(String wartosc) {
        this.wartosc = wartosc;
    }

    public String pizzaSpecjalna(){
	    String dodatek = this.getWartosc().split("#")[1];
        return "Nie istnieje taka pizza w systemie \n" +
                "Polecam pizzę specjalną z dodatkiem: " + dodatek;
    }

}
