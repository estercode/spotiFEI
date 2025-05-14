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
    // Busca músicas por nome switch case

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
    // Busca músicas por nome do artista switch case

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

    // Busca músicas por nome do genero switch case
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

    // Método reutilizável para executar as consultas acima com filtro
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
    // Retorna todas as músicas da base ordenadas pelo nome

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
    // Registra uma música curtida por um usuário (sem duplicação - ON CONFLICT DO NOTHING)

    public void curtirMusica(int usuarioId, int musicaId) throws SQLException {
        String sql = "INSERT INTO musicas_curtidas (usuario_id, musica_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, musicaId);
            stmt.executeUpdate();
        }
    }
    // Registra uma música descurtidas por um usuário (sem duplicação - ON CONFLICT DO NOTHING)

    public void descurtirMusica(int usuarioId, int musicaId) throws SQLException {
        String sql = "INSERT INTO musicas_descurtidas (usuario_id, musica_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, musicaId);
            stmt.executeUpdate();
        }
    }

    //Listas de musicas curtidas
    public List<Musica> buscarCurtidas(int usuarioId) throws SQLException {
        String sql = """
            SELECT m.*, a.nome AS nome_artista, g.nome AS nome_genero
            FROM musicas_curtidas c
            JOIN musicas m ON c.musica_id = m.id
            JOIN artistas a ON m.artista_id = a.id
            JOIN generos g ON m.genero_id = g.id
            WHERE c.usuario_id = ?
        """;
        List<Musica> musicas = new ArrayList<>();

        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, usuarioId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Musica m = new Musica();
                    m.setId(rs.getInt("id"));
                    m.setNome(rs.getString("nome"));
                    m.setDuracaoSegundos(rs.getInt("duracao_segundos"));

                    Artista a = new Artista();
                    a.setNome(rs.getString("nome_artista"));
                    m.setArtista(a);

                    Genero g = new Genero();
                    g.setNome(rs.getString("nome_genero"));
                    m.setGenero(g);

                    musicas.add(m);
                }
            }
        }

        return musicas;
    }
    //Listas de musicas descurtidas

    public List<Musica> buscarDescurtidas(int usuarioId) throws SQLException {
        String sql = """
            SELECT m.*, a.nome AS nome_artista, g.nome AS nome_genero
            FROM musicas_descurtidas d
            JOIN musicas m ON d.musica_id = m.id
            JOIN artistas a ON m.artista_id = a.id
            JOIN generos g ON m.genero_id = g.id
            WHERE d.usuario_id = ?
        """;
        List<Musica> musicas = new ArrayList<>();

        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, usuarioId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Musica m = new Musica();
                    m.setId(rs.getInt("id"));
                    m.setNome(rs.getString("nome"));
                    m.setDuracaoSegundos(rs.getInt("duracao_segundos"));

                    Artista a = new Artista();
                    a.setNome(rs.getString("nome_artista"));
                    m.setArtista(a);

                    Genero g = new Genero();
                    g.setNome(rs.getString("nome_genero"));
                    m.setGenero(g);

                    musicas.add(m);
                }
            }
        }

        return musicas;
    }
    
    // Busca uma música específica por ID, usada para fins de histórico

    public Musica buscarPorIdHist(int idMusica) throws SQLException {
        String sql = "SELECT m.*, a.nome as artista_nome, g.nome as genero_nome "
                + "FROM musicas m "
                + "JOIN artistas a ON m.id_artista = a.id "
                + "JOIN generos g ON m.id_genero = g.id "
                + "WHERE m.id = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, idMusica);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Musica musica = new Musica();
                musica.setId(rs.getInt("id"));
                musica.setNome(rs.getString("nome"));

                Artista artista = new Artista();
                artista.setId(rs.getInt("id_artista"));
                artista.setNome(rs.getString("artista_nome"));
                musica.setArtista(artista);

                Genero genero = new Genero();
                genero.setId(rs.getInt("id_genero"));
                genero.setNome(rs.getString("genero_nome"));
                musica.setGenero(genero);

                return musica;
            }
        }
        return null; // Retorna null caso não encontre a música
    }
}
