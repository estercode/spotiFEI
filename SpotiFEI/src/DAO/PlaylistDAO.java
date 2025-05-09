/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Artista;
import Model.Genero;
import Model.Playlist;
import Model.Musica;
import Model.Usuario;
import UtilSQL.ConexaoSQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ester
 */
public class PlaylistDAO {

    private Connection conexao;

    public PlaylistDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Criar nova playlist
    public int criarPlaylist(Playlist playlist) throws SQLException {
        String sql = "INSERT INTO playlists (nome, usuario_id) VALUES (?, ?)";

        try (PreparedStatement st = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, playlist.getNome());
            st.setInt(2, playlist.getDono().getId());
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean adicionarMusica(int playlistId, int musicaId) throws SQLException {
        String sql = "INSERT INTO playlist_musicas (playlist_id, musica_id) VALUES (?, ?)";

        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, playlistId);
            st.setInt(2, musicaId);
            return st.executeUpdate() > 0;
        }
    }

    public boolean removerMusica(int playlistId, int musicaId) throws SQLException {
        String sql = "DELETE FROM playlist_musicas WHERE playlist_id = ? AND musica_id = ?";

        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, playlistId);
            st.setInt(2, musicaId);
            return st.executeUpdate() > 0;
        }
    }

    public List<Playlist> listarPlaylistsUsuario(int usuarioId) throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT id, nome FROM playlists WHERE usuario_id = ?";

        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, usuarioId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Usuario dono = new Usuario();
                dono.setId(usuarioId);

                Playlist playlist = new Playlist(rs.getString("nome"), dono);
                playlist.setId(rs.getInt("id"));
                playlists.add(playlist);
            }
        }
        return playlists;
    }

    public List<Musica> buscarMusicasPlaylist(int playlistId) throws SQLException {
        List<Musica> musicas = new ArrayList<>();
        String sql = """
            SELECT m.id, m.nome, m.duracao_segundos, 
                   a.id AS artista_id, a.nome AS artista_nome,
                   g.id AS genero_id, g.nome AS genero_nome
            FROM playlist_musicas pm
            JOIN musicas m ON pm.musica_id = m.id
            JOIN artistas a ON m.artista_id = a.id
            JOIN generos g ON m.genero_id = g.id
            WHERE pm.playlist_id = ?
            """;

        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, playlistId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Artista artista = new Artista(rs.getInt("artista_id"), rs.getString("artista_nome"), null);
                Genero genero = new Genero(rs.getInt("genero_id"), rs.getString("genero_nome"));
                Musica musica = new Musica(rs.getInt("id"), rs.getString("nome"), artista, genero, rs.getInt("duracao_segundos"));
                musicas.add(musica);
            }
        }
        return musicas;
    }

    public boolean removerPlaylist(int playlistId) throws SQLException {
        String apagarMusicas = "DELETE FROM playlist_musicas WHERE playlist_id = ?";
        String apagarPlaylist = "DELETE FROM playlists WHERE id = ?";

        try {
            conexao.setAutoCommit(false);  // inicia transação

            try (PreparedStatement st1 = conexao.prepareStatement(apagarMusicas); PreparedStatement st2 = conexao.prepareStatement(apagarPlaylist)) {

                st1.setInt(1, playlistId);
                st1.executeUpdate();

                st2.setInt(1, playlistId);
                int afetados = st2.executeUpdate();

                conexao.commit(); // sucesso
                return afetados > 0;
            }
        } catch (SQLException e) {
            conexao.rollback(); // erro, desfaz alterações
            throw e;
        } finally {
            conexao.setAutoCommit(true);  // restaura estado
        }

    }

    public boolean editarPlaylist(int playlistId, String novoNome) throws SQLException {
        String sql = "UPDATE playlists SET nome = ? WHERE id = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setString(1, novoNome);
            st.setInt(2, playlistId);
            return st.executeUpdate() > 0;
        }
    }

    public List<Playlist> buscarPlaylistsPorUsuario(int usuarioId) throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, u.id AS usuario_id, u.nome AS usuario_nome, u.email AS usuario_email "
                + "FROM playlists p "
                + "JOIN usuarios u ON p.usuario_id = u.id "
                + "WHERE p.usuario_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Criar o objeto Usuario para o dono da playlist
                Usuario dono = new Usuario(rs.getInt("usuario_id"), rs.getString("usuario_nome"), rs.getString("usuario_email"), rs.getString("senha"));
                // Criar a playlist
                Playlist playlist = new Playlist(rs.getString("nome"), dono);
                playlist.setId(rs.getInt("id"));
                playlists.add(playlist);
            }
        }
        return playlists;
    }

}
