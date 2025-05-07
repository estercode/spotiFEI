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
public class Artista extends Pessoa {

    private List<Musica> musicas = new ArrayList<>();

    public Artista() {
        super();
    }

    public Artista(int id, String nome, String email) {
        super(id, nome, email);
    }

    public void adicionarMusica(Musica m) {
        musicas.add(m);
    }

    public List<Musica> getMusicas() {
        return musicas;
    }

}
