/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Artista;
import Model.Genero;
import Model.Musica;
import Model.Historico;
import UtilSQL.ConexaoSQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ester
 */
public class HistoricoDAO {
    // conexao ao banco de dados
    private Connection conexao;

    public HistoricoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void registrarBusca(int usuarioId, int musicaId) throws SQLException {
        if (this.conexao == null || this.conexao.isClosed()) {
            this.conexao = ConexaoSQL.conectar();
        }
        // inserir o registro na tabela historico_buscas
        String sql = "INSERT INTO historico_buscas (usuario_id, musica_id) VALUES (?, ?)";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, usuarioId);
            st.setInt(2, musicaId);
            st.executeUpdate();
        }
    }

    public List<Musica> buscarUltimos10(int usuarioId) throws SQLException {
        List<Musica> musicas = new ArrayList<>();
        
        // SQL para buscar os últimos 10 registros do histórico, com informações completas
        String sql = """
        SELECT m.id, m.nome, m.duracao_segundos,
               a.id AS artista_id, a.nome AS artista_nome,
               g.id AS genero_id, g.nome AS genero_nome
        FROM historico_buscas h
        JOIN musicas m ON h.musica_id = m.id
        JOIN artistas a ON m.artista_id = a.id
        JOIN generos g ON m.genero_id = g.id
        WHERE h.usuario_id = ?
        ORDER BY h.id DESC
        LIMIT 10
    """;
        //execução da consulta
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, usuarioId);
            ResultSet rs = st.executeQuery(); 
            
            //resultado criado para musica completos 
            while (rs.next()) {
                Musica musica = new Musica();
                musica.setId(rs.getInt("id"));
                musica.setNome(rs.getString("nome"));
                musica.setDuracaoSegundos(rs.getInt("duracao_segundos"));

                Artista artista = new Artista();
                artista.setId(rs.getInt("artista_id"));
                artista.setNome(rs.getString("artista_nome"));
                musica.setArtista(artista);

                Genero genero = new Genero();
                genero.setId(rs.getInt("genero_id"));
                genero.setNome(rs.getString("genero_nome"));
                musica.setGenero(genero);
                
                
                //Add a musica na lista
                musicas.add(musica);
            }
        }
       //retorna a lista de musica 
        return musicas;
    }
}
