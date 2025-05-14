/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ester
 */
public class Musica {

    private int id;
    private String nome;
    private Artista artista;
    private Genero genero;
    private int duracaoSegundos;

    public Musica(int id, String nome, Artista artista, Genero genero, int duracaoSegundos) {
        this.id = id;
        this.nome = nome;
        this.artista = artista;
        this.genero = genero;
        this.duracaoSegundos = duracaoSegundos;
    }

    public Musica() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public int getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public void setDuracaoSegundos(int duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }
    
    
    // formatação do print na tela de busca
    public String getInfoFormatada() {
        return String.format("Música: %s | Artista: %s | Gênero: %s | Duração: %ds",
                nome, artista.getNome(), genero.getNome(), duracaoSegundos);
    }

}
