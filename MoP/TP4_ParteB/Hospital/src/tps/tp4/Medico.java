package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class que representa um médico.
 */

public class Medico extends User {
    private String especialidade;

    /**
     * Construtor da classe Medico. Já com a categoria predefinida.
     *
     * @param nome O nome do médico.
     * @param idade A idade do médico.
     * @param code O código do médico.
     * @param user O username do médico.
     * @param pass A password do médico.
     */

    public Medico(String nome, int idade, String code, String user, String pass) {
        super(nome, idade, code, user, pass,"medico");
    }

    /**
     * Obtém a especialidade do médico.
     *
     * @return A especialidade do médico.
     */
    public String getEspecialidade() {
        return especialidade;
    }

    /**
     * Difine a especialidade do médico.
     *
     * @param especialidade A especialidade do médico.
     */
    public void setEspecialidade(String especialidade){
        this.especialidade=especialidade;
    }


/////////////////////////////////////////////////////////////////
/////////////////////////XML////////////////////////////////////
////////////////////////////////////////////////////////////////

    /**
     * Constrói um Médico a partir de um nó do XML.
     *
     * @param nNode O nó do XML que representa um médico.
     * @return O Médico criado a partir do nó XML.
     */
    public static Medico build(Node nNode) {
        Element medicoElement = (Element) nNode;

        String nome = medicoElement.getElementsByTagName("nome").item(0).getTextContent();
        int idade = Integer.parseInt(medicoElement.getElementsByTagName("idade").item(0).getTextContent());
        String code = medicoElement.getElementsByTagName("code").item(0).getTextContent();
        String user = medicoElement.getElementsByTagName("user").item(0).getTextContent();
        String pass = "";
        Node passNode = medicoElement.getElementsByTagName("pass").item(0);
        if (passNode != null) {
            pass = passNode.getTextContent();
        }
        //String especialidade = medicoElement.getElementsByTagName("especialidade").item(0).getTextContent();
        final String especialidade;
        Node especialidadeNode = medicoElement.getElementsByTagName("especialidade").item(0);
        if (especialidadeNode != null) {
            especialidade = especialidadeNode.getTextContent();
        } else {
            especialidade = "";
        }


        return new Medico(nome, idade, code, user, pass){{
            setEspecialidade(especialidade);
        }};
    }

    /**
     * Cria um elemento XML a partir de um Médico.
     *
     * @param doc O documento XML para onde o elemento será criado.
     * @return O elemento XML que representa um Médico.
     */
    public Element createElement(Document doc) {
        Element medicoElement = doc.createElement("medico");

        Element nomeElement = doc.createElement("nome");
        nomeElement.setTextContent(this.getNome());
        medicoElement.appendChild(nomeElement);

        Element idadeElement = doc.createElement("idade");
        idadeElement.setTextContent(Integer.toString(this.getIdade()));
        medicoElement.appendChild(idadeElement);

        Element codeElement = doc.createElement("code");
        codeElement.setTextContent(this.getCode());
        medicoElement.appendChild(codeElement);

        Element userElement = doc.createElement("user");
        userElement.setTextContent(this.getUser());
        medicoElement.appendChild(userElement);

        Element passElement = doc.createElement("pass");
        passElement.setTextContent(this.getPass());
        medicoElement.appendChild(passElement);

        Element especialdiadeElement = doc.createElement("especialdiade");
        especialdiadeElement.setTextContent(this.getEspecialidade());
        medicoElement.appendChild(especialdiadeElement);

        Element categoriaElement = doc.createElement("categoria");
        categoriaElement.setTextContent(this.getCategoria());
        medicoElement.appendChild(categoriaElement);

        return medicoElement;
    }
}


