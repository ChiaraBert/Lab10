package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Co_autori;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {
	
	Model m;

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<Author> listAutori(AuthorIdMap map) {
		
		final String sql = "SELECT id, lastname, firstname FROM author ORDER BY lastname ASC";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			List<Author> autori = new ArrayList<Author>();

			while (rs.next()) {
				Author a = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				a=map.put(a);
				autori.add(a);
			}
			
			rs.close();
			conn.close();
			
			return autori;

		} catch (SQLException e) {
			 e.printStackTrace();
			 System.out.println("Database Error -- loadListAutori");
			throw new RuntimeException("Errore Db");
		}
	}
	
	
		public void CollegamentoAutoreOpera() {
			
		final String sql = "SELECT creator.eprintid, paper.title, paper.issn, paper.publication,paper.type,paper.types,author.id, author.lastname, author.firstname "+
							"FROM creator,author,paper "+
							"WHERE author.id=creator.authorid AND paper.eprintid=creator.eprintid";	
		try {
			
			Map<Integer, Author> map = new TreeMap<Integer,Author>();
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) { 
				Paper p = new Paper(rs.getInt("creator.eprintid"), rs.getString("paper.title"),rs.getString("paper.issn"),rs.getString("paper.publication"),rs.getString("paper.type"),rs.getString("paper.types"));
				Author a = new Author(rs.getInt("author.id"), rs.getString("author.lastname"), rs.getString("author.firstname"));
				
				Author old=null;
				if(map.isEmpty()==false){
				old=map.get(rs.getInt("author.id"));}
				if(old==null){
					map.put(rs.getInt("author.id"), a);
					old=a;}
				old.aggiungiPaper(p);
				
								
			}	rs.close();
				conn.close();
				

			} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

		public List<Paper> getTuttiArticoli() {
		
			final String sql = "SELECT eprintid, title, issn, publication, type, types FROM paper";

			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				List<Paper> paper = new ArrayList<Paper>();

				ResultSet rs = st.executeQuery();

				while (rs.next()) {
					Paper p = new Paper(rs.getInt("eprintid"), rs.getString("title"),rs.getString("issn"),rs.getString("publication"),rs.getString("type"),rs.getString("types"));
					paper.add(p);
				}

				rs.close();
				conn.close();
				return paper;

			} catch (SQLException e) {
				 e.printStackTrace();
				throw new RuntimeException("Errore Db");
			}
			
		}

		public List<Integer> getArticoliAutore(int id) {
			
			final String sql="SELECT eprintid FROM creator WHERE authorid=? ";
			
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, id);
				List<Integer> p = new ArrayList<Integer>();

				ResultSet rs = st.executeQuery();
				

				while(rs.next()) {
					p.add(rs.getInt("eprintid"));
				}
				
				rs.close();
				conn.close();
				return p;

			} catch (SQLException e) {
				 e.printStackTrace();
				throw new RuntimeException("Errore Db");
			}
			
		}

		public void getAutori(Integer id_opera, AuthorIdMap co_autori) {
			
			final String sql="SELECT id,lastname,firstname FROM author,creator WHERE author.id=creator.authorid AND creator.eprintid=? ";
			
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, id_opera);
				
				ResultSet rs = st.executeQuery();
				

				while(rs.next()) {
					Author a= new Author(rs.getInt("id"), rs.getString("lastname"),rs.getString("firstname"));
					co_autori.put(a);
				}
				
				rs.close();
				conn.close();


			} catch (SQLException e) {
				 e.printStackTrace();
				throw new RuntimeException("Errore Db");
			}
			
		}

		public List<Paper> getArticoliDIAutore(int id) {
			
			final String sql="SELECT paper.eprintid, title,issn,publication,type,types FROM paper,creator "+
								"WHERE paper.eprintid=creator.eprintid AND creator.authorid=? ";
			
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, id);
				List<Paper> p = new ArrayList<Paper>();

				ResultSet rs = st.executeQuery();
				

				while(rs.next()) {
					Paper a = new Paper(rs.getInt("paper.eprintid"), rs.getString("title"),rs.getString("issn"),rs.getString("publication"),rs.getString("type"),rs.getString("types"));
					p.add(a);
				}
				
				rs.close();
				conn.close();
				return p;

			} catch (SQLException e) {
				 e.printStackTrace();
				throw new RuntimeException("Errore Db");
			}
			
		}
		
			public List<Co_autori> getArchi(AuthorIdMap map) {
			
			final String sql="SELECT DISTINCT A1.id, A1.lastname,A1.firstname,A2.id,A2.lastname,A2.firstname "+
							"FROM author A1, author A2,creator C1,creator C2 "+
							"WHERE A1.id=C1.authorid AND C1.eprintid=C2.eprintid AND A2.id=C2.authorid ";
			
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				
				List<Co_autori> arco = new ArrayList<>();

				ResultSet rs = st.executeQuery();
				

				while(rs.next()) {
					Author a = new Author(rs.getInt("A1.id"), rs.getString("A1.lastname"), rs.getString("A1.firstname"));
					Author a2 = new Author(rs.getInt("A2.id"), rs.getString("A2.lastname"), rs.getString("A2.firstname"));
					a=map.put(a);
					a2=map.put(a2);
					Co_autori c = new Co_autori(a,a2);
					arco.add(c);
				}
				
				rs.close();
				conn.close();
				return arco;

			} catch (SQLException e) {
				 e.printStackTrace();
				throw new RuntimeException("Errore Db");
			}
			}
			
	
	
}