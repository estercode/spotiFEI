/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import UtilSQL.ConexaoSQL;
import View.CadastroUsuarioView;
import javax.swing.SwingUtilities;
import java.sql.Connection;
/**
 *
 * @author ester
 */
public class Main {
      public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                
                Connection conexao = ConexaoSQL.conectar();
                
              
                new CadastroUsuarioView();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

