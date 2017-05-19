package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

	private Model m;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;
    
       @FXML
    void handleCoautori(ActionEvent event) {
    	Author r= boxPrimo.getValue();
    	List<Author> co_autori = new ArrayList<Author>();
    	List<Author> altri = new ArrayList<Author>();
    	altri=m.getAutori();
    	if(r==null){
    		txtResult.appendText("Seleziona almeno un autore \n");
    		return;
    	}
    	
    	//SOLUZIONE CON GRAFO (IMPLEMENTANDO jAVA - LENTA)
    	//m.CreaGrafo();
    	//txtResult.appendText(""+m.cercaAltriAutori(r)+"\n");
    	
    	//SOLUZIONE CON GRAFO (IMPLEMENTANDO SQL - VELOCE)
    	m.creaGrafo2();
    	co_autori=m.cercaAltriAutori(r);
    	txtResult.appendText(""+co_autori+"\n");
    	altri.remove(r);
    	for(Author a: m.getAutori()){
    		if(co_autori.contains(a))
    			altri.remove(a);
    		
    	}
    	boxSecondo.getItems().addAll(altri);
    	
    	
    	
    	//SOLUZIONE CON QUERY(SENZA GRAFO - NON UTILE PER ES 2)
    	//txtResult.appendText(""+m.cercaCoAutori(r).stampa()+"\n");
    	

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	Author r= boxPrimo.getValue();
    	Author b= boxSecondo.getValue();
    	if(b==null){
    		txtResult.appendText("Seleziona almeno un autore 2 \n");
    		return;
    	}
    	txtResult.appendText(""+m.calcolaCamminoMinimo(r,b));
    	

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }
    
    public void setModel(Model model){
    	this.m=model; 
    	boxPrimo.getItems().addAll(m.getAutori());
    	
    	   	
    }
}
