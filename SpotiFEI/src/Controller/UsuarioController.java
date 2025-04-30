/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Usuario;

/**
 *
 * @author ester
 */
public class UsuarioController {

    public void cadastrarUsuario(int id, String nome, String email, String senha) {
        Usuario novoUsuario = new Usuario(id, nome, email, senha);

    }
}
