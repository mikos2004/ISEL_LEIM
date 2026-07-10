package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class que representa um doente.
 */

public class Doente extends User {
    private String diagnostico;

    /**
     * Construtor da class Doente. Já com a categoria predefinida.
     *
     * @param nome O nome do doente.
     * @param idade A idade do doente.
     * @param code O código do doente.
     * @param user O username do doente.
     * @param pass A password do doente.
     */
    public Doente(String nome, int idade, String code, String user, String pass) {
        super(nome, idade, code, user, pass, "doente");
    }

    /**
     * Obtém o diagnóstico do doente.
     *
     * @return O diagnóstico do doente.
     */
    public String getDiagnostico() {
        return diagnostico;
    }

    /**
     * Difine o diagnóstico do doente.
     *
     * @param diagnostico O diagnóstico do doente.
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

/////////////////////////////////////////////////////////////////
/////////////////////////XML////////////////////////////////////
////////////////////////////////////////////////////////////////

    /**
     * Constrói um Doente a partir de um nó do XML.
     *
     * @param nNode O nó do XML que representa um doente.
     * @return O Doente criado a partir do nó XML.
     */
public static Doente build(Node nNode) {
    Element medicoElement = (Element) nNode;

    String nome = medicoElement.getElementsByTagName("nome").item(0).getTextContent();
    int idade = Integer.parseInt(medicoElement.getElementsByTagName("idade").item(0).getTextContent());
    String code = medicoElement.getElementsByTagName("code").item(0).getTextContent();
    String user = medicoElement.getElementsByTagName("user").item(0).getTextContent();
    String pass = medicoElement.getElementsByTagName("pass").item(0).getTextContent();
    String diagnostico = medicoElement.getElementsByTagName("diagnostico").item(0).getTextContent();

    return new Doente(nome, idade, code, user, pass){{
        setDiagnostico(diagnostico);
    }};
}

    /**
     * Cria um elemento XML a partir de um Doente.
     *
     * @param doc O documento XML para onde o elemento será criado.
     * @return O elemento XML que representa um Doente.
     */
    public Element createElement(Document doc) {
        Element doenteElement = doc.createElement("doente");

        Element nomeElement = doc.createElement("nome");
        nomeElement.setTextContent(this.getNome());
        doenteElement.appendChild(nomeElement);

        Element idadeElement = doc.createElement("idade");
        idadeElement.setTextContent(Integer.toString(this.getIdade()));
        doenteElement.appendChild(idadeElement);

        Element codeElement = doc.createElement("code");
        codeElement.setTextContent(this.getCode());
        doenteElement.appendChild(codeElement);

        Element userElement = doc.createElement("user");
        userElement.setTextContent(this.getUser());
        doenteElement.appendChild(userElement);

        Element passElement = doc.createElement("pass");
        passElement.setTextContent(this.getPass());
        doenteElement.appendChild(passElement);

        Element diagnosticoElement = doc.createElement("diagnostico");
        diagnosticoElement.setTextContent(this.getDiagnostico());
        doenteElement.appendChild(diagnosticoElement);

        Element categoriaElement = doc.createElement("categoria");
        categoriaElement.setTextContent(this.getCategoria());
        doenteElement.appendChild(categoriaElement);

        return doenteElement;
    }
}

