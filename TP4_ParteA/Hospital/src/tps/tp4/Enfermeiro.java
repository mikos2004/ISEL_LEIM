package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class que representa um enfermeiro.
 */

public class Enfermeiro extends User {
    private String area;

    /**
     * Construtor da class Medico. Já com a categoria predefinida.
     *
     * @param nome O nome do enfermeiro.
     * @param idade A idade do enfermeiro.
     * @param code O código do enfermeiro.
     * @param user O username do enfermeiro.
     * @param pass A password do enfermeiro.
     */
    public Enfermeiro(String nome, int idade, String code, String user, String pass) {
        super(nome, idade, code, user, pass, "enfermeiro");
    }

    /**
     * Obtém a área do enfermeiro.
     *
     * @return A área do enfermeiro.
     */
    public String getArea() {
        return area;
    }

    /**
     * Difine a área do enfermeiro.
     *
     * @param area A área do enfermeiro.
     */
    public void setArea(String area){
        this.area=area;
    }


/////////////////////////////////////////////////////////////////
/////////////////////////XML////////////////////////////////////
////////////////////////////////////////////////////////////////

    /**
     * Constrói um Enfermeiro a partir de um nó do XML.
     *
     * @param nNode O nó do XML que representa um enfermeiro.
     * @return O Enfermeiro criado a partir do nó XML.
     */
    public static Enfermeiro build(Node nNode) {
        Element enfermeiroElement = (Element) nNode;

        String nome = enfermeiroElement.getElementsByTagName("nome").item(0).getTextContent();
        int idade = Integer.parseInt(enfermeiroElement.getElementsByTagName("idade").item(0).getTextContent());
        String code = enfermeiroElement.getElementsByTagName("code").item(0).getTextContent();
        String user = enfermeiroElement.getElementsByTagName("user").item(0).getTextContent();
        String pass = enfermeiroElement.getElementsByTagName("pass").item(0).getTextContent();
        String area = enfermeiroElement.getElementsByTagName("area").item(0).getTextContent();

        return new Enfermeiro(nome, idade, code, user, pass){{
            setArea(area);
        }};
    }

    /**
     * Cria um elemento XML a partir de um Enfermeiro.
     *
     * @param doc O documento XML para onde o elemento será criado.
     * @return O elemento XML que representa um Enfermeiro.
     */
    public Element createElement(Document doc) {
        Element enfermeiroElement = doc.createElement("enfermeiro");

        Element nomeElement = doc.createElement("nome");
        nomeElement.setTextContent(this.getNome());
        enfermeiroElement.appendChild(nomeElement);

        Element idadeElement = doc.createElement("idade");
        idadeElement.setTextContent(Integer.toString(this.getIdade()));
        enfermeiroElement.appendChild(idadeElement);

        Element codeElement = doc.createElement("code");
        codeElement.setTextContent(this.getCode());
        enfermeiroElement.appendChild(codeElement);

        Element userElement = doc.createElement("user");
        userElement.setTextContent(this.getUser());
        enfermeiroElement.appendChild(userElement);

        Element passElement = doc.createElement("pass");
        passElement.setTextContent(this.getPass());
        enfermeiroElement.appendChild(passElement);

        Element areaElement = doc.createElement("area");
        areaElement.setTextContent(this.getArea());
        enfermeiroElement.appendChild(areaElement);

        Element categoriaElement = doc.createElement("categoria");
        categoriaElement.setTextContent(this.getCategoria());
        enfermeiroElement.appendChild(categoriaElement);

        return enfermeiroElement;
    }
}

