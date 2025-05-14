/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ester
 */
public class Historico {

    private int id;
    private Usuario usuario;
    private Musica musica;
    private int id_musica; 

    public Historico() {
    }

    public Historico(int id, Usuario usuario, Musica musica, int id_musica) {
        this.id = id;
        this.usuario = usuario;
        this.musica = musica;
        this.id_musica = id_musica;
    }

    
    // getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public int getId_musica() {
        return id_musica;
    }

    public void setId_musica(int id_musica) {
        this.id_musica = id_musica;
    }
    
    
}
