/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import DAO.MusicaDAO;
import DAO.PlaylistDAO;
import DAO.UsuarioDAO;
import Model.Musica;
import Model.Playlist;
import Model.Usuario;
import UtilSQL.ConexaoSQL;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ester
 */
public class PlaylistView extends javax.swing.JFrame {

    /**
     * Creates new form PlaylistView
     */
    private JPanel painel;
    private JButton btnNovaPlaylist, btnVoltar, bttAdicionarMusica, bttRemoverMusica, bttRemoverPlaylist;
    private JScrollPane scrollPlaylists, scrollMusicas;
    private JList<String> listPlaylists, listMusicas;
    private DefaultListModel<String> playlistModel, musicasModel;
    private List<Playlist> playlists;
    private Usuario usuario;
    private PlaylistDAO playlistDAO;
    private MusicaDAO musicaDAO;

    public PlaylistView(Usuario usuario) {
        this.usuario = usuario;
        this.playlistModel = new DefaultListModel<>();
        this.musicasModel = new DefaultListModel<>();

        initDAOs();
        initComponentes();
        carregarPlaylistsUsuario();

    }

    private void initDAOs() {
        try {
            Connection conexao = ConexaoSQL.conectar();
            this.playlistDAO = new PlaylistDAO(conexao);
            this.musicaDAO = new MusicaDAO(conexao);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco: " + e.getMessage());
            dispose();
        }
    }

    private void initComponentes() {
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        painel = new JPanel();
        painel.setBackground(new Color(51, 255, 153));
        painel.setLayout(new BorderLayout());
        add(painel);

        // Listas
        listPlaylists = new JList<>(playlistModel);
        listPlaylists.setBackground(new Color(51, 255, 153));
        listPlaylists.setFont(new Font("Lucida Console", Font.PLAIN, 14));
        listPlaylists.addListSelectionListener(this::playlistSelecionada);
        scrollPlaylists = new JScrollPane(listPlaylists);
        scrollPlaylists.setBorder(BorderFactory.createTitledBorder("Minhas Playlists"));

        listMusicas = new JList<>(musicasModel);
        listMusicas.setBackground(new Color(51, 255, 153));
        listMusicas.setFont(new Font("Lucida Console", Font.PLAIN, 14));
        scrollMusicas = new JScrollPane(listMusicas);
        scrollMusicas.setBorder(BorderFactory.createTitledBorder("Músicas na Playlist"));

        JPanel centro = new JPanel(new GridLayout(1, 2));
        centro.add(scrollPlaylists);
        centro.add(scrollMusicas);
        painel.add(centro, BorderLayout.CENTER);

        // Botões
        btnNovaPlaylist = new JButton("Nova Playlist");
        bttRemoverPlaylist = new JButton("Remover Playlist");
        bttAdicionarMusica = new JButton("Adicionar Música");
        bttRemoverMusica = new JButton("Remover Música");
        btnVoltar = new JButton("Voltar");

        btnVoltar.addActionListener(e -> voltar());

        JPanel botoes = new JPanel(new FlowLayout());
        botoes.setBackground(new Color(51, 255, 153));
        botoes.add(btnNovaPlaylist);
        botoes.add(bttRemoverPlaylist);
        botoes.add(bttAdicionarMusica);
        botoes.add(bttRemoverMusica);
        botoes.add(btnVoltar);
        painel.add(botoes, BorderLayout.SOUTH);

        btnNovaPlaylist.addActionListener(e -> criarNovaPlaylist());
        bttRemoverPlaylist.addActionListener(e -> removerPlaylist());
        bttAdicionarMusica.addActionListener(e -> adicionarMusica());
        bttRemoverMusica.addActionListener(e -> removerMusica());
    }

    private void playlistSelecionada(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = listPlaylists.getSelectedIndex();
            if (index >= 0 && index < playlists.size()) {
                Playlist selecionada = playlists.get(index);
                carregarMusicasPlaylist(selecionada.getId());
            }
        }
    }

    private void carregarPlaylistsUsuario() {
        try {
            playlists = playlistDAO.listarPlaylistsUsuario(usuario.getId());
            playlistModel.clear();

            if (playlists.isEmpty()) {
                playlistModel.addElement("Nenhuma playlist encontrada");
            } else {
                for (Playlist p : playlists) {
                    playlistModel.addElement(p.getNome());
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar playlists: " + e.getMessage());
        }
    }

    private void carregarMusicasPlaylist(int playlistId) {
        musicasModel.clear();
        try {
            List<Musica> musicas = playlistDAO.buscarMusicasPlaylist(playlistId);
            if (musicas.isEmpty()) {
                musicasModel.addElement("Nenhuma música nesta playlist");
            } else {
                for (Musica m : musicas) {
                    musicasModel.addElement(m.getNome() + " - " + m.getArtista().getNome());
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar músicas: " + e.getMessage());
        }
    }

    private void voltar() {
        this.dispose();
        TelaInicialView tela = new TelaInicialView(usuario);
        tela.setVisible(true);
    }

    private void criarNovaPlaylist() {
        System.out.println("ID do usuário: " + usuario.getId());

        String nome = JOptionPane.showInputDialog(this, "Nome da nova playlist:");
        if (nome != null && !nome.trim().isEmpty()) {
            try {
                Playlist nova = new Playlist(nome, usuario);
                int id = playlistDAO.criarPlaylist(nova);
                if (id != -1) {
                    nova.setId(id);
                    playlists.add(nova);
                    playlistModel.addElement(nova.getNome());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao criar playlist: " + e.getMessage());
            }
        }
    }

    private void removerPlaylist() {
        int index = listPlaylists.getSelectedIndex();
        if (index != -1) {
            Playlist p = playlists.get(index);
            try {
                if (playlistDAO.removerPlaylist(p.getId())) {
                    playlists.remove(index);
                    playlistModel.remove(index);
                    musicasModel.clear();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao remover playlist: " + e.getMessage());
            }
        }
    }

    private void adicionarMusica() {
        int index = listPlaylists.getSelectedIndex();
        if (index == -1 || index >= playlists.size()) {
            JOptionPane.showMessageDialog(this, "Selecione uma playlist válida.");
            return;
        }

        try {
            List<Musica> todas = musicaDAO.buscarTodasMusicas();
            if (todas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Não há músicas disponíveis para adicionar.");
                return;
            }

            String[] opcoes = todas.stream()
                    .map(m -> m.getNome() + " - " + m.getArtista().getNome())
                    .toArray(String[]::new);

            String escolhida = (String) JOptionPane.showInputDialog(this, "Escolha uma música:", "Adicionar", JOptionPane.PLAIN_MESSAGE, null, opcoes, null);
            if (escolhida != null) {
                int idx = java.util.Arrays.asList(opcoes).indexOf(escolhida);
                if (idx == -1) {
                    JOptionPane.showMessageDialog(this, "Erro ao localizar a música selecionada.");
                    return;
                }

                Musica m = todas.get(idx);
                Playlist p = playlists.get(index);
                playlistDAO.adicionarMusica(p.getId(), m.getId());
                musicasModel.addElement(m.getNome() + " - " + m.getArtista().getNome());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar música: " + e.getMessage());
        }
    }

    private void removerMusica() {
        int playlistIndex = listPlaylists.getSelectedIndex();
        int musicaIndex = listMusicas.getSelectedIndex();

        if (playlistIndex == -1 || playlistIndex >= playlists.size()) {
            JOptionPane.showMessageDialog(this, "Selecione uma playlist.");
            return;
        }

        if (musicaIndex == -1 || musicaIndex >= musicasModel.size()) {
            JOptionPane.showMessageDialog(this, "Selecione uma música válida da lista.");
            return;
        }

        try {
            Playlist playlist = playlists.get(playlistIndex);
            List<Musica> musicas = playlistDAO.buscarMusicasPlaylist(playlist.getId());

            if (musicaIndex >= musicas.size()) {
                JOptionPane.showMessageDialog(this, "A música selecionada não existe mais na playlist.");
                return;
            }

            Musica musica = musicas.get(musicaIndex);
            playlistDAO.removerMusica(playlist.getId(), musica.getId());

            carregarMusicasPlaylist(playlist.getId()); // recarrega a lista após remoção

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover música: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PlaylistView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlaylistView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlaylistView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlaylistView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            Connection conexao = ConexaoSQL.conectar();
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            Usuario usuario = usuarioDAO.buscarUsuarioPorId(1); // Exemplo

            if (usuario != null) {
                java.awt.EventQueue.invokeLater(() -> {
                    new PlaylistView(usuario).setVisible(true);
                });
            } else {
                System.out.println("Usuário não encontrado no banco.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

