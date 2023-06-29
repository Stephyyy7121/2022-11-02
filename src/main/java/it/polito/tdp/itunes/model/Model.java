package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	List<Genre> generi;
	ItunesDAO dao;
	Graph<Track, DefaultEdge> grafo;
	List<Track> vertici;
	
	
	public Model() {
		this.dao = new ItunesDAO();
		generi = new ArrayList<>();
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
	}
	
	public List<Genre> getGenre() {
		this.generi = dao.getAllGenres();
		Collections.sort(generi);
		return generi;
	}
	
	public void loadNodes(Genre genere, int min, int max) {
		
		if (this.vertici.isEmpty()) {
			this.vertici = this.dao.getVertici(genere, min, max);
		}
	}
	
	public void clearGraph() {
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		this.vertici = new ArrayList<>();
	}
	
	
	public void creaGrafo(Genre genere, int min, int max) {
		
		clearGraph();
		loadNodes(genere, min, max);
		
		Graphs.addAllVertices(this.grafo, this.vertici );
		
		//archi
		for (Track t1 : this.vertici) {
			for (Track t2: this.vertici) {
				if (!t1.equals(t2) && t1.getNumPlaylist() == t2.getNumPlaylist()) {
					this.grafo.addEdge(t2, t1);
				}
			}
		}
	}
	
	public List<Set<Track>> getComponenteConnessa() {
		
		ConnectivityInspector<Track,DefaultEdge> ci = new ConnectivityInspector<>(this.grafo) ;
		List<Set<Track>> connessa = ci.connectedSets();
		return connessa;

	}
	
	public int getPlylistDistinta(Set<Track> connessa) {
		
		List<Integer> numPlaylist = new ArrayList<>();
		
		for (Track t : connessa) {
			int num = t.getNumPlaylist();
			if (!numPlaylist.contains(num)) {
				numPlaylist.add(num);
			}
		}
		return numPlaylist.size();
	}
	
	public List<Track> getVertici() {
		return this.vertici;
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
}
