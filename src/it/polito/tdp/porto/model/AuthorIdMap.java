package it.polito.tdp.porto.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



public class AuthorIdMap {
	
	private Map<Integer, Author > map= new HashMap<Integer,Author>();
	
	public Author put(Author a){
		Author old=map.get(a.getId());
		if(old==null){
			map.put(a.getId(), a);
			return a;
		}else{
			return old;
		}
	}

	public Collection<Author> stampa() {
		return map.values();
	}

	public void remove(Author r) {
		
		map.remove(r.getId());
		
	}

	public void pulisci() {
		map.clear();
		
	}
	

	
}
