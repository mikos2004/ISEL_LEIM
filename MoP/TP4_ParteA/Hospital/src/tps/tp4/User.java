package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class que representa um user do sistema.
 */
public class User {
    private String categoria;
    private String nome;
    private int idade;
    private String code;
    private String user;
    private String pass;

    /**
     * Construtor da class User.
     *
     * @param nome      O nome do usuário.
     * @param idade     A idade do usuário.
     * @param code      O código do usuário.
     * @param user      O nome de usuário do usuário.
     * @param pass      A senha do usuário.
     * @param categoria A categoria do usuário (admin, medico, enfermeiro, doente).
     */
    public User(String nome, int idade, String code, String user, String pass, String categoria) {
        this.nome = nome;
        this.idade = idade;
        this.code = code;
        this.user = user;
        this.pass = pass;
        this.categoria = categoria;
    }

    /**
     * Obtém o nome do user.
     *
     * @return O nome do user.
     */
    public String getNome(){
        return nome;
    }

    /**
     * Define o nome do user.
     *
     * @param nome O nome do user.
     */
    public void setNome(String nome){
        this.nome=nome;
    }

    /**
     * Obtém a idade do user.
     *
     * @return A idade do user.
     */
    public int getIdade(){
        return idade;
    }

    /**
     * Define a idade do user.
     *
     * @param idade A idade do user.
     */
    public void setIdade(int idade){
        this.idade=idade;
    }

    /**
     * Obtém o código do user.
     *
     * @return O código do user.
     */
    public String getCode(){
        return code;
    }

    /**
     * Define o código do user.
     *
     * @param code O código do user.
     */
    public void setCode(String code){
        this.code=code;
    }

    /**
     * Obtém o username do user.
     *
     * @return O username do user.
     */
    public String getUser(){
        return user;
    }

    /**
     * Define o username do user.
     *
     * @param user O username do user.
     */
    public void setUser(String user){
        this.user=user;
    }

    /**
     * Obtém a password do user.
     *
     * @return A password do user.
     */
    public String getPass(){
        return pass;
    }

    /**
     * Define a password do user.
     *
     * @param pass A password do user.
     */
    public void setPass(String pass){
        this.pass=pass;
    }

    /*/**
     * Verifica se o objeto é igual a outro user com base no código do.
     *
     * @param u O objeto a ser comparado.
     * @return true se o objeto for igual ao user, false caso contrário.

    public boolean equals(User u) {
        return u.getCode().equals(code);
    }*/

    /**
     * Obtém a categoria do user.
     *
     * @return A categoria do user.
     */
    public String getCategoria(){
        return categoria;
    }

    /**
     * Cria e retorna um User com base na categoria do elemento Node fornecido do XML.
     *
     * @param nNode O elemento Node que representa o user.
     * @return O User construído.
     */
    public static User build(Node nNode) {
        if (nNode.getNodeName().equals("medico")) {
            return Medico.build(nNode);
        } else if (nNode.getNodeName().equals("enfermeiro")) {
            return Enfermeiro.build(nNode);
        } else if (nNode.getNodeName().equals("doente")) {
            return Doente.build(nNode);
        } else if (nNode.getNodeName().equals("admin")) {
            return Admin.build(nNode);
        } else {
            return null;
        }
    }

    /**
     * Seleciona e cria um elemento específico com base no user fornecido para integrar o XML.
     *
     * @param user O user.
     * @param doc  O documento Document.
     * @return O elemento criado.
     */
    public Element selectCreateElem(User user, Document doc) {
        String categoria = user.getCategoria();

        if (categoria.equals("medico")) {
            return ((Medico) user).createElement(doc);
        } else if (categoria.equals("enfermeiro")) {
            return ((Enfermeiro) user).createElement(doc);
        } else if (categoria.equals("doente")) {
            return ((Doente) user).createElement(doc);
        } else if (categoria.equals("admin")) {
            return ((Admin) user).createElement(doc);
        } else {
            return null;
        }
    }

}