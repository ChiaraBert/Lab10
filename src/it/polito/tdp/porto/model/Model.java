package it.polito.tdp.porto.model;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	UndirectedGraph<Author,DefaultEdge> grafo;
	List<Author> autori;
	AuthorIdMap map;
	List<Paper> articoli ;
	
	
	public Model(){
		grafo= new SimpleGraph<Author,DefaultEdge>(DefaultEdge.class);
		autori= new ArrayList<Author>();
		articoli= new ArrayList<Paper>();
		this.map=new AuthorIdMap();
	}

	public List<Author> getAutori() {
		PortoDAO pd = new PortoDAO();
		autori=pd.listAutori(map);
		
		return autori;
	}
	
	//SOLUZIONE CON GRAFO
			//Prendo tutti articoli
			//Prendo tutti gli autori
			//Creo vertici partendo dagli autori
			//Per ogni autore della lista autori cerco gli articoli che ha scritto
			//Per ogni articolo della lista di articoli che l'autore ha scritto cerco co_autori
			//Per ogni co_autore, diverso dall'autore partenza, creo arco e lo aggiungo al grafo
	
	public void CreaGrafo(){
		PortoDAO pd = new PortoDAO();
		articoli = pd.getTuttiArticoli();
		autori=pd.listAutori(map);
		AuthorIdMap co_autori = new AuthorIdMap();
		Graphs.addAllVertices(grafo,autori);
		
		for(Author autorePartenza: autori){
			articoli=pd.getArticoliDIAutore(autorePartenza.getId());
			
			for(Paper art : articoli){
				pd.getAutori(art.getEprintid(),co_autori);	
			
					for(Author autoreDestinazione:co_autori.stampa()){
						if(!autorePartenza.equals(autoreDestinazione)){
							grafo.addEdge(autorePartenza, autoreDestinazione);
							
						}
					}
			}
		}
		
	}
	
	//SOLUZIONE CON QUERY
	//Dato l'autore cerco le sue opere
	//Per ogni opera cerco autori e li salvo in una lista controllando di non inserirli due volte
	//Stampo lista

	public AuthorIdMap cercaCoAutori(Author r) {
		
		PortoDAO pd = new PortoDAO();
		List<Integer> id_articoli = new ArrayList<Integer>();
		AuthorIdMap co_autori = new AuthorIdMap();
		id_articoli.clear();
		co_autori.pulisci();
		
		id_articoli = pd.getArticoliAutore(r.getId());
		co_autori.put(r);
		
		for(Integer i: id_articoli ){
				pd.getAutori(i,co_autori);		
		}
		
		co_autori.remove(r);
		
		return co_autori;
		
	}

	public List<Author> cercaAltriAutori(Author r) {
		List<Author> visitati = new ArrayList<Author>();
		visitati=Graphs.neighborListOf(grafo, r);
		return visitati;
	}
		
	public void creaGrafo2(){
		PortoDAO pd = new PortoDAO();
 		List<Co_autori> m = new ArrayList<>();
		autori=pd.listAutori(map);
		Graphs.addAllVertices(grafo,autori);
		m=pd.getArchi(map);
		
		for(Co_autori a: m){
			if(!a.getA1().equals(a.getA2()))
			grafo.addEdge(a.getA1(), a.getA2());
			
		}
			
		
		
		
	}

}
