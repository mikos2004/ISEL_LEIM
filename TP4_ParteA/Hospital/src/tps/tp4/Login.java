package tps.tp4;

import java.util.Scanner;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class para o processo de login
 */
public class Login {
    protected boolean login;

    /**
     * Realiza o processo de login do user.
     *
     * @return true se o login for bem sucedido, false caso contrário.
     */
    public boolean login() {
        if (!login) {
            Scanner scan = CmdSistema.scanner();
            System.out.print("Username : ");
            String user = scan.next();
            System.out.print("Password : ");
            String pass = scan.next();
            User usert = new User("", 0, "", user, pass, "");

            if (SistemaBD.existeU(usert)) {
                User usert2 = SistemaBD.getLoginUser(usert);
                if (checkLoginCategoria(usert2, App.getOpt())) {
                    System.out.println("======== Login com Sucesso ========");
                    login = true;
                } else {
                    System.out.println("======== Categoria de Login Inválida! Não coincide com a sua. ========");
                    login = false;
                }
            } else {
                System.out.println("======== Credenciais Erradas! Tente outra vez ========");
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
    private boolean checkLoginCategoria(User user, int opt) {
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
    public void logout() {
        login = false;
        System.out.println("======== Logout com Sucesso ========");
        App.saveDataToXML();
    }
}
