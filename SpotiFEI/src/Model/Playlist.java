/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ester
 */
public class Playlist {

    private int id;
    private String nome;
    private List<Musica> musicas;

    public Playlist(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.musicas = new ArrayList<>();
    }
    
    public void addMusica(Musica musica){
        musicas.add(musica);
    }
    
    public void removerMusica(Musica musica){
        musicas.remove(musica);
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

    public List<Musica> getMusicas() {
        return musicas;
    }

    

}
