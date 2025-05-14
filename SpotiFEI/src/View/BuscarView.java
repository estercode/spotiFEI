/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import DAO.MusicaDAO;
import DAO.HistoricoDAO;
import Model.Artista;
import Model.Genero;
import Model.Musica;
import Model.Usuario;
import UtilSQL.ConexaoSQL;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 *
 * @author ester
 */
public class BuscarView extends javax.swing.JFrame {

    private JPanel painelPrincipal;
    private JLabel lblBuscar;
    private JTextField txtBuscar;
    private JButton bttBuscar;
    private JButton bttVoltar;
    private JPanel painelResultados;
    private JScrollPane scrollPane;
    private JComboBox<String> comboFiltro;
    private JLabel lblFiltrarPor;
    private Usuario usuario;
    private Connection conexao;
    private HistoricoDAO historicoDAO;

    public BuscarView(Usuario usuario) {
        this.usuario = usuario;

        this.historicoDAO = new HistoricoDAO(conexao);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(null);
        painelPrincipal.setBackground(new Color(0, 255, 153));

        lblBuscar = new JLabel("Buscar música:");
        lblBuscar.setBounds(50, 30, 200, 30);
        painelPrincipal.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(180, 30, 400, 30);
        painelPrincipal.add(txtBuscar);

        bttBuscar = new JButton("Buscar");
        bttBuscar.setBounds(600, 30, 100, 30);
        painelPrincipal.add(bttBuscar);
//💖💔
        bttVoltar = new JButton("Voltar");
        bttVoltar.setBounds(50, 520, 100, 30);
        painelPrincipal.add(bttVoltar);

        lblFiltrarPor = new JLabel("Filtrar por:");
        lblFiltrarPor.setBounds(50, 70, 100, 30);
        painelPrincipal.add(lblFiltrarPor);

        comboFiltro = new JComboBox<>(new String[]{"Música", "Artista", "Gênero"});
        comboFiltro.setBounds(180, 70, 150, 30);
        painelPrincipal.add(comboFiltro);

        painelResultados = new JPanel();
        painelResultados.setLayout(new BoxLayout(painelResultados, BoxLayout.Y_AXIS));
        painelResultados.setBackground(new Color(0, 255, 153));

        scrollPane = new JScrollPane(painelResultados);
        scrollPane.setBounds(50, 100, 700, 400);
        painelPrincipal.add(scrollPane);
        add(painelPrincipal);

        bttBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMusicas();
            }
        });

        bttVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a tela atual
                TelaInicialView inicio = new TelaInicialView(usuario);
                inicio.setVisible(true);
            }
        });

        setVisible(true);
    }

    private void buscarMusicas() {
        String termoBusca = txtBuscar.getText();
        String tipoBusca = (String) comboFiltro.getSelectedItem();

        painelResultados.removeAll();

        try (Connection conexao = ConexaoSQL.conectar()) {
            MusicaDAO musicaDAO = new MusicaDAO(conexao);
            List<Musica> resultados;

            switch (tipoBusca) {
                case "Artista":
                    resultados = musicaDAO.buscarPorArtista(termoBusca);
                    break;
                case "Gênero":
                    resultados = musicaDAO.buscarPorGenero(termoBusca);
                    break;
                case "Música":
                default:
                    resultados = musicaDAO.buscarPorNome(termoBusca);
            }
            conexao.close();
            if (resultados.isEmpty()) {
                painelResultados.add(new JLabel("Nenhum resultado encontrado."));
            } else {
                for (Musica m : resultados) {
                    adicionarMusicaPanel(m);
                    historicoDAO.registrarBusca(usuario.getId(), m.getId());

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            painelResultados.add(new JLabel("Erro ao buscar."));
        }

        painelResultados.revalidate();
        painelResultados.repaint();
    }

    private void adicionarMusicaPanel(Musica musica) {

        JPanel musicaPanel = new JPanel();
        musicaPanel.setLayout(new BorderLayout());
        musicaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        musicaPanel.setMaximumSize(new Dimension(680, 60));
        musicaPanel.setBackground(Color.WHITE);

        String nomeArtista = musica.getArtista().getNome();
        String nomeGenero = musica.getGenero().getNome();

        JLabel lblInfo = new JLabel("<html><b>Música:</b> " + musica.getNome()
                + " | <b>Artista:</b> " + nomeArtista
                + " | <b>Gênero:</b> " + nomeGenero
                + " | <b>Duração:</b> " + formatarDuracao(musica.getDuracaoSegundos()) + "</html>");
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botões 💖 💔
        JButton bttCurtir = new JButton("💖");
        JButton bttDescurtir = new JButton("💔");
        JPanel botoesPanel = new JPanel();
        botoesPanel.add(bttCurtir);
        botoesPanel.add(bttDescurtir);
        musicaPanel.add(botoesPanel, BorderLayout.EAST);

        try {
            Connection conexao = ConexaoSQL.conectar();
            MusicaDAO musicaDAO = new MusicaDAO(conexao);

            bttCurtir.addActionListener(e -> {
                try {
                    musicaDAO.curtirMusica(usuario.getId(), musica.getId());
                    JOptionPane.showMessageDialog(this, "Música curtida!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    if (conexao != null) try {
                        conexao.close();
                    } catch (SQLException ignore) {
                    }
                }

            });

            bttDescurtir.addActionListener(e -> {
                try {
                    musicaDAO.descurtirMusica(usuario.getId(), musica.getId());
                    JOptionPane.showMessageDialog(this, "Música descurtida!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        musicaPanel.add(lblInfo, BorderLayout.CENTER);
        painelResultados.add(musicaPanel);
    }

    private String formatarDuracao(int segundos) {
        int minutos = segundos / 60;
        int segundosRestantes = segundos % 60;
        return String.format("%d:%02d", minutos, segundosRestantes);
    }

    @Override
    public void dispose() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose(); // Chama o método original para fechar a janela
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
            .addGap(0, 784, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(BuscarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BuscarView().setVisible(true);
            }

        });*/
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

