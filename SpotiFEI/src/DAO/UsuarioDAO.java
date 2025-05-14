/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Usuario;
import java.sql.*;

/**
 *
 * @author ester
 */
public class UsuarioDAO {

    private Connection conexao;

    public UsuarioDAO(Connection conexao) {
        this.conexao = conexao;
    }

    //Cadastra um novo usuário no banco de dados.
    public int cadastrarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
        try (PreparedStatement st = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, usuario.getNome());
            st.setString(2, usuario.getEmail());
            st.setString(3, usuario.getSenha());
            //executa o INSERT
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("ERRO AO CADASTRAR USUARIO: " + e.getMessage());

        }
        return -1;

    }

    //Busca um usuário com base no email e senha (login).
    public Usuario buscarPorEmailESenha(String email, String senha) throws SQLException {

        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setString(1, email);
            st.setString(2, senha);
            ResultSet rs = st.executeQuery();

            // se encontrar com essas credenciais cria o objetp
            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                return new Usuario(id, nome, email, senha);

            }

        }
        return null;
    }

    public boolean alterarSenha(String email, String senhaNova) throws SQLException {
        String sql = "UPDATE usuarios SET senha = ? WHERE email = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setString(1, senhaNova);
            st.setString(2, email);

            // Executa o UPDATE e verifica se alguma linha foi afetada
            int linhasAfetadas = st.executeUpdate();
            return linhasAfetadas > 0;
        }
    }
    
    
    // Busca um usuário pelo seu ID
    public Usuario buscarUsuarioPorId(int id) throws SQLException {

        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {

                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");

                return new Usuario(id, nome, email, senha);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Excluir um usuário do banco de dados com base no ID
    public boolean excluirUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuarios  WHERE id = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, id);

            int linhasAfetadas = st.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

}
