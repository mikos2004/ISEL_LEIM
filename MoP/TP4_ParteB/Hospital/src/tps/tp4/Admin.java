package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class que representa um administrador (admin).
 */
public class Admin  extends User{

    /**
     * Construtor da class Admin. Já com a categoria predefinida
     *
     * @param nome O nome do administrador.
     * @param idade A idade do administrador.
     * @param code O código do administrador.
     * @param user O username do administrador.
     * @param pass A password do administrador.
     */
    public Admin(String nome, int idade, String code, String user, String pass) {
        super(nome, idade, code, user, pass,"admin");
    }

    /**
     * Constrói um Admin a partir de um nó do XML.
     *
     * @param nNode O nó do XML que representa um admin.
     * @return O Admin criado a partir do nó XML.
     */
    public static Admin build(Node nNode) {
        Element medicoElement = (Element) nNode;

        String nome = medicoElement.getElementsByTagName("nome").item(0).getTextContent();
        int idade = Integer.parseInt(medicoElement.getElementsByTagName("idade").item(0).getTextContent());
        String code = medicoElement.getElementsByTagName("code").item(0).getTextContent();
        String user = medicoElement.getElementsByTagName("user").item(0).getTextContent();
        String pass = medicoElement.getElementsByTagName("pass").item(0).getTextContent();

        return new Admin(nome, idade, code, user, pass);
    }

    /**
     * Cria um elemento XML a partir de um Admin.
     *
     * @param doc O documento XML para onde o elemento será criado.
     * @return O elemento XML que representa um Admin.
     */
    public Element createElement(Document doc) {
        Element adminElement = doc.createElement("admin");

        Element nomeElement = doc.createElement("nome");
        nomeElement.setTextContent(this.getNome());
        adminElement.appendChild(nomeElement);

        Element idadeElement = doc.createElement("idade");
        idadeElement.setTextContent(Integer.toString(this.getIdade()));
        adminElement.appendChild(idadeElement);

        Element codeElement = doc.createElement("code");
        codeElement.setTextContent(this.getCode());
        adminElement.appendChild(codeElement);

        Element userElement = doc.createElement("user");
        userElement.setTextContent(this.getUser());
        adminElement.appendChild(userElement);

        Element passElement = doc.createElement("pass");
        passElement.setTextContent(this.getPass());
        adminElement.appendChild(passElement);

        Element categoriaElement = doc.createElement("categoria");
        categoriaElement.setTextContent(this.getCategoria());
        adminElement.appendChild(categoriaElement);

        return adminElement;
    }
}