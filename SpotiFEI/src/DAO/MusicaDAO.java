package DAO;

import Model.Artista;
import Model.Genero;
import Model.Musica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicaDAO {

    private Connection conexao;

    public MusicaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public List<Musica> buscarPorNome(String nomeBusca) throws SQLException {
        String sql = """
            SELECT m.id, m.nome, m.duracao_segundos, 
                   a.id AS artista_id, a.nome AS artista_nome,
                   g.id AS genero_id, g.nome AS genero_nome
            FROM musicas m
            JOIN artistas a ON m.artista_id = a.id
            JOIN generos g ON m.genero_id = g.id
            WHERE m.nome ILIKE ?
            """;

        return executarConsulta(sql, "%" + nomeBusca + "%");
    }

    public List<Musica> buscarPorArtista(String nomeArtista) throws SQLException {
        String sql = """
            SELECT m.id, m.nome, m.duracao_segundos, 
                   a.id AS artista_id, a.nome AS artista_nome,
                   g.id AS genero_id, g.nome AS genero_nome
            FROM musicas m
            JOIN artistas a ON m.artista_id = a.id
            JOIN generos g ON m.genero_id = g.id
            WHERE a.nome ILIKE ?
            """;

        return executarConsulta(sql, "%" + nomeArtista + "%");
    }

    public List<Musica> buscarPorGenero(String nomeGenero) throws SQLException {
        String sql = """
            SELECT m.id, m.nome, m.duracao_segundos, 
                   a.id AS artista_id, a.nome AS artista_nome,
                   g.id AS genero_id, g.nome AS genero_nome
            FROM musicas m
            JOIN artistas a ON m.artista_id = a.id
            JOIN generos g ON m.genero_id = g.id
            WHERE g.nome ILIKE ?
            """;

        return executarConsulta(sql, "%" + nomeGenero + "%");
    }

    private List<Musica> executarConsulta(String sql, String parametro) throws SQLException {
        List<Musica> musicas = new ArrayList<>();

        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setString(1, parametro);
            ResultSet rs = st.executeQuery();

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

                musicas.add(musica);
            }
        } catch (SQLException e) {
            System.err.println("Erro na consulta: " + sql);
            System.err.println("Parâmetro: " + parametro);
            throw e;
        }

        return musicas;
    }

    public List<Musica> buscarTodasMusicas() throws SQLException {
        List<Musica> musicas = new ArrayList<>();
        String sql = """
        SELECT m.id, m.nome, m.duracao_segundos, 
               a.id AS artista_id, a.nome AS artista_nome,
               g.id AS genero_id, g.nome AS genero_nome
        FROM musicas m
        JOIN artistas a ON m.artista_id = a.id
        JOIN generos g ON m.genero_id = g.id
        ORDER BY m.nome
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Musica musica = new Musica();
                musica.setId(rs.getInt("id"));
                musica.setNome(rs.getString("nome"));
                musica.setDuracaoSegundos(rs.getInt("duracao_segundos"));

                // Configurar artista
                Artista artista = new Artista();
                artista.setId(rs.getInt("artista_id"));
                artista.setNome(rs.getString("artista_nome"));
                musica.setArtista(artista);

                // Configurar gênero
                Genero genero = new Genero();
                genero.setId(rs.getInt("genero_id"));
                genero.setNome(rs.getString("genero_nome"));
                musica.setGenero(genero);

                musicas.add(musica);
            }
        }
        return musicas;
    }

    public void curtirMusica(int usuarioId, int musicaId) throws SQLException {
        String sql = "INSERT INTO musicas_curtidas (usuario_id, musica_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, musicaId);
            stmt.executeUpdate();
        }
    }

    public void descurtirMusica(int usuarioId, int musicaId) throws SQLException {
        String sql = "INSERT INTO musicas_descurtidas (usuario_id, musica_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, musicaId);
            stmt.executeUpdate();
        }
    }

}
