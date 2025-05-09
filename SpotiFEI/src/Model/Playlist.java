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
    private Usuario dono;
    private List<Musica> musicas;

    public Playlist(String nome, Usuario dono) {
        if (dono.getId() <= 0) {
            throw new IllegalArgumentException("Usuário inválido (ID não definido)");
        }

        this.nome = nome;
        this.dono = dono;
        this.musicas = new ArrayList<>();
    }

    public void addMusica(Musica musica) {
        if (!musicas.contains(musica)) {
            musicas.add(musica);
        }
    }

    public boolean removerMusica(Musica musica) {
        return musicas.remove(musica);
    }

    public boolean contemMusica(Musica musica) {
        return musicas.contains(musica);
    }

    public int quantidadeMusicas() {
        return musicas.size();
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

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public List<Musica> getMusicas() {
        return new ArrayList<>(musicas);
    }

}
