package pl.poznan.put.cs.si.puttalky;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.kie.api.runtime.KieSession;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/** Author: agalawrynowicz<br>
 * Date: 19-Dec-2016 */

public class BazaWiedzy {

    private OWLOntologyManager manager = null;
    private OWLOntology ontologia;
    private Set<OWLClass> listaKlas;
    private Set<OWLClass> listaDodatkow;
    private Set<OWLClass> listaPizz;
    
    OWLReasoner silnik;
    
    public void inicjalizuj() {
		InputStream plik = this.getClass().getResourceAsStream("/pizza.owl");
		manager = OWLManager.createOWLOntologyManager();
		
		try {
			ontologia = manager.loadOntologyFromOntologyDocument(plik);
			silnik = new Reasoner.ReasonerFactory().createReasoner(ontologia);
			listaKlas = ontologia.getClassesInSignature();
            listaDodatkow = new HashSet<OWLClass>();
            listaPizz = new HashSet<OWLClass>();

            OWLClass dodatek  = manager.getOWLDataFactory().getOWLClass(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#Dodatek"));
            for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasa: silnik.getSubClasses(dodatek, false)) {
                listaDodatkow.add(klasa.getRepresentativeElement());
            }
            OWLClass pizza  = manager.getOWLDataFactory().getOWLClass(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#Pizza"));
            for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasa: silnik.getSubClasses(pizza, false)) {
                listaPizz.add(klasa.getRepresentativeElement());
            }
			
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }

    public Set<String> dopasujDodatek(String s){
        Set<String> result = new HashSet<String>();
        String[] tmp;

        if(!s.contains("pizz"))for (OWLClass klasa : listaDodatkow){
            tmp = klasa.toString().toLowerCase().split("#");
            if(tmp.length>1)if (tmp[1].contains(s.toLowerCase()) && s.length()>2){
                result.add(klasa.getIRI().toString());
            }
        }
        return result;
    }

    public String dopasujNajlepszyDodatek(String s){
        Set<String> candidates = dopasujDodatek(s);
        if(candidates.isEmpty()){
            return null;
        }
        String ret = candidates.iterator().next().toString();
        for(String candidate : candidates){
            if(candidate.length() < ret.length()){
                ret = candidate.toString();
            }
        }
        return ret;
    }

    public Set<String> dopasujPizze(String s){
        Set<String> nazwy = new HashSet<String>();
        String[] tmp;

        if(!s.contains("pizz"))for (OWLClass klasa : listaPizz){
            tmp = klasa.toString().toLowerCase().split("#");
            if(tmp.length>1)if (tmp[1].contains(s.toLowerCase()) && s.length()>2 ){
                nazwy.add(klasa.getIRI().toString().split("#")[1]);
            }
        }
        return nazwy;
    }

    public Set<String> wyszukajPizzePoDodatkach(String iri){
        Set<String> pizze = new HashSet<String>();
        OWLObjectProperty maDodatek = manager.getOWLDataFactory().getOWLObjectProperty(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#maDodatek"));
        Set<OWLClassExpression> ograniczeniaEgzystencjalne = new HashSet<OWLClassExpression>();

        OWLClass dodatek = manager.getOWLDataFactory().getOWLClass(IRI.create(iri));
        OWLClassExpression wyrazenie = manager.getOWLDataFactory().getOWLObjectSomeValuesFrom(maDodatek, dodatek);
        ograniczeniaEgzystencjalne.add(wyrazenie);

        OWLClassExpression pozadanaPizza = manager.getOWLDataFactory().getOWLObjectIntersectionOf(ograniczeniaEgzystencjalne);

        for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasa: silnik.getSubClasses(pozadanaPizza, false)) {
            pizze.add(klasa.getEntities().iterator().next().asOWLClass().getIRI().getFragment());
        }

        return pizze;
    }
    public Set<String> wyszukajPizzePoDodatkach(Set<String> required_iri,Set<String> forbidden_iri){
        Set<String> pizzaSet = new HashSet();
        Set<String> blackList = new HashSet();
        for(OWLClass owlPizza: listaPizz){
            pizzaSet.add(owlPizza.getIRI().getFragment());
        }
        for(String iri: required_iri){
            pizzaSet.retainAll(wyszukajPizzePoDodatkach(iri));
        }
        for(String iri: forbidden_iri){
            blackList.addAll(wyszukajPizzePoDodatkach(iri));
        }
        blackList.retainAll(pizzaSet);
        pizzaSet.removeAll(blackList);

        return pizzaSet;
    }
	
	public static void main(String[] args) {
		BazaWiedzy baza = new BazaWiedzy();
		baza.inicjalizuj();
		/*
		OWLClass mieso = baza.manager.getOWLDataFactory().getOWLClass(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#DodatekMięsny"));
		for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasa: baza.silnik.getSubClasses(mieso, true)) {
			System.out.println("klasa:"+klasa.toString());
		}
        */
		for (OWLClass d:  baza.listaPizz){
			System.out.println("dodatek: "+d.toString());
		}
        //baza.silnik.is
	}

}
