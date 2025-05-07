import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaBuscaMusica extends JFrame {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaBuscaMusica tela = new TelaBuscaMusica();
            tela.setVisible(true);
        });
    }

    public TelaBuscaMusica() {
        // Configuração básica da janela
        setTitle("Player de Músicas");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        
        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtBusca = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        
        painelBusca.add(new JLabel("Buscar:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);
        
        // Painel de resultados
        JPanel painelResultados = new JPanel();
        painelResultados.setLayout(new BoxLayout(painelResultados, BoxLayout.Y_AXIS));
        painelResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Exemplos
        adicionarMusica(painelResultados, "Bohemian Rhapsody", "Queen", "Rock", 354);
        adicionarMusica(painelResultados, "Halo", "Beyoncé", "Pop", 261);
        adicionarMusica(painelResultados, "Rolling in the Deep", "Adele", "Pop", 228);
        
        // Adicionando à janela
        painelPrincipal.add(painelBusca, BorderLayout.NORTH);
        painelPrincipal.add(new JScrollPane(painelResultados), BorderLayout.CENTER);
        
        add(painelPrincipal);
    }
    
    private void adicionarMusica(JPanel painel, String nome, String artista, String genero, int segundos) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Informações
        JLabel lblInfo = new JLabel(
            "<html><b style='font-size:14px'>" + nome + "</b><br>" +
            "<span style='color:#555'>" + artista + " • " + genero + "</span></html>"
        );
        
        // Duração
        String duracao = String.format("%d:%02d", segundos / 60, segundos % 60);
        JLabel lblTempo = new JLabel(duracao);
        lblTempo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTempo.setForeground(new Color(70, 70, 70));

        // Botão
        JButton btnPlay = new JButton("▶ Play");
        btnPlay.setPreferredSize(new Dimension(80, 25));
        
        // Layout
        card.add(lblInfo, BorderLayout.CENTER);
        card.add(lblTempo, BorderLayout.EAST);
        card.add(btnPlay, BorderLayout.SOUTH);
        
        // Efeitos
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        painel.add(card);
    }
}