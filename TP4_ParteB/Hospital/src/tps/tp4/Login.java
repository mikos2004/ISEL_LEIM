package tps.tp4;

import javax.swing.*;
import java.util.Scanner;

import static tps.tp4.InterApp.*;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class para o processo de login
 */
public class Login {
    protected static boolean login;

    /**
     * Realiza o processo de login do user.
     *
     * @return true se o login for bem sucedido, false caso contrário.
     */
    public static boolean login() {
        if (!login) {
            String user = InterApp.LoginPanel.usernameText.getText();
            String pass = InterApp.LoginPanel.passText.getText();
            User usert = new User("", 0, "", user, pass, "");

            if (SistemaBD.existeU(usert)) {
                User usert2 = SistemaBD.getLoginUser(usert);
                if (checkLoginCategoria(usert2, InterApp.retOpSelectLogin()) && !pass.equals("default")) {
                    //System.out.println("======== Login com Sucesso ========");
                    JOptionPane.showMessageDialog(null, "Login com Sucesso");
                    login = true;
                } else {
                    //System.out.println("======== Categoria de Login Inválida! Não coincide com a sua. ========");
                    JOptionPane.showMessageDialog(null, " - Categoria de Login Inválida! Não coincide com a sua.\n - Poderá ainda ter a password padrão do registo. Para sua segurança, só poderá entrar depois de alterá-la.");
                    login = false;
                }
            } else {
                //System.out.println("======== Credenciais Erradas! Tente outra vez ========");
                JOptionPane.showMessageDialog(null, "Credenciais Erradas! Tente outra vez. \n - Poderá ainda ter a password padrão do registo. Para sua segurança, só poderá entrar depois de alterá-la.");
                InterApp.LoginPanel.usernameText.setText("");
                InterApp.LoginPanel.passText.setText("");
                login = false;
            }
        }
        return login;
    }

    /**
     * Verifica se a categoria do user corresponde à opção de login escolhida.
     *
     * @param user O user do qual se tem de verificar a categoria.
     * @param opt A opção de login selecionada.
     * @return true se a categoria do user corresponder à opção de login selecionada, false caso contrário.
     */
    private static boolean checkLoginCategoria(User user, int opt) {
        String code = user.getCode();

        switch (opt) {
            case 1: // Login Administrador
                return code.startsWith("A");
            case 2: // Login Médico
                return code.startsWith("M");
            case 3: // Login Enfermeiro
                return code.startsWith("E");
            case 4: // Login Doente
                return code.startsWith("D");
            default:
                return false;
        }
    }


    /**
     * Processo de logout
     * Salva as alterações no XML.
     */
    public static void logout() {
        login = false;
        InterApp.saveDataToXML();
        InterApp.LoginPanel.usernameText.setText("");
        InterApp.LoginPanel.passText.setText("");
    }
}
