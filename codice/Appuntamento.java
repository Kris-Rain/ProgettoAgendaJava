/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package codice;

import java.util.ArrayList;

public class Appuntamento {
	private String data;
	private String orario;
	private String durata;
	private String luogo;
	private String nome;
	private ArrayList<String> nomePersone;
	
	public Appuntamento(String data, String orario, String durata, String luogo, String nome) {
		this.data=data;
		this.orario=orario;
		this.durata=durata;
		this.luogo=luogo;
		this.nome=nome;
		nomePersone=new ArrayList<String>();
		nomePersone.add(nome);
	}
	public String getData() {
		return data;
	}
	public String getNome() {
		return nome;
	}
	public String getOrario() {
		return orario;
	}
	public String getDurata() {
		return durata;
	}
	public String getLuogo() {
		return luogo;
	}
	public boolean matchNome(String nome) {
		if(this.nome.contains(nome)) return true;
		return false;
	}
	public boolean matchData(String data) {
		if(this.data.contains(data)) return true;
		return false;
	}
}
