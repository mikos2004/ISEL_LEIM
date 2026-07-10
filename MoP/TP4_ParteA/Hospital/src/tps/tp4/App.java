package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    /**
     * @author Miguel Alcobia - A50746
     * ISEL - LEIM 22/23
     */
    public static int op;

    /**
     * Método principal da aplicação.
     * Carrega dados de XML, incia o sistema.
     * Une os dados da Base de Dados, com o XML
     */
    public static void main(String[] args) {

        try {
            File xmlFile = new File("src/HospitalBD.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);

            Element sistemaBD = (Element) doc.getElementsByTagName("sistemaBD").item(0);
            Element usersElement = (Element) sistemaBD.getElementsByTagName("users").item(0);
            Element consultasElement = (Element) sistemaBD.getElementsByTagName("consultas").item(0);

            // Cria uma lista com os elementos admins, medicos, enfermeiros e doentes
            List<Element> userElements = new ArrayList<>();
            NodeList adminsList = usersElement.getElementsByTagName("admin");
            NodeList medicosList = usersElement.getElementsByTagName("medico");
            NodeList enfermeirosList = usersElement.getElementsByTagName("enfermeiro");
            NodeList doentesList = usersElement.getElementsByTagName("doente");

            for (int i = 0; i < adminsList.getLength(); i++) {
                userElements.add((Element) adminsList.item(i));
            }
            for (int i = 0; i < medicosList.getLength(); i++) {
                userElements.add((Element) medicosList.item(i));
            }
            for (int i = 0; i < enfermeirosList.getLength(); i++) {
                userElements.add((Element) enfermeirosList.item(i));
            }
            for (int i = 0; i < doentesList.getLength(); i++) {
                userElements.add((Element) doentesList.item(i));
            }

            User[] usersXML = new User[userElements.size()];

            // Loop através dos elementos de usuário
            for (int i = 0; i < userElements.size(); i++) {
                Element userElement = userElements.get(i);
                String categoria = userElement.getElementsByTagName("categoria").item(0).getTextContent();

                // Constrói o usuário apropriado conforme a categoria
                User user;
                switch (categoria) {
                    case "admin":
                        user = Admin.build(userElement);
                        break;
                    case "medico":
                        user = Medico.build(userElement);
                        break;
                    case "enfermeiro":
                        user = Enfermeiro.build(userElement);
                        break;
                    case "doente":
                        user = Doente.build(userElement);
                        break;
                    default:
                        user = null;
                        break;
                }
                usersXML[i] = user;
            }

            // Cria uma lista com os elementos admins, medicos, enfermeiros e doentes
            List<Element> consultaElements = new ArrayList<>();
            NodeList consultaList = consultasElement.getElementsByTagName("consulta");
            for (int i = 0; i < consultaList.getLength(); i++) {
                consultaElements.add((Element) consultaList.item(i));
            }

            Consulta[] consultasXML = new Consulta[consultaElements.size()];

            for (int i = 0; i < consultaElements.size(); i++) {
                Element consultaElement = consultaElements.get(i);
                Consulta consulta;
                consulta = Consulta.build(consultaElement);
                consultasXML[i] = consulta;
            }

            ///Updates e Saves
            SistemaBD.mergeUsers(usersXML);//atualiza com o array existente
            SistemaBD.mergeConsultas(consultasXML);

        } catch (Exception e) {
            e.printStackTrace();
        }

        CmdSistema.Banner();
        App app = new App();
        app.start();

    }

    /**
     * Inicia o programa, mostrando as opções de login e inicia o modo correspondente à categoria do user.
     */
    private void start() {
        CmdSistema.AppOpts();
        op = CmdSistema.lerOpts();

        switch(op) {
            case 1  : startAdminMode(); break;
            case 2  : startMedMode(); break;
            case 3  : startEnfMode(); break;
            case 4  : startDntMode(); break;
            default : System.out.println("default"); break;
        }
    }

    /**
     * Incia o modo Admin
     */
    private void startAdminMode() {
        Sistema sistema = new Sistema();
        sistema.onAdmin();
    }

    /**
     * Incia o modo Médico
     */
    private void startMedMode() {
        Sistema sistema = new Sistema();
        sistema.onMed();
    }

    /**
     * Incia o modo Enfermeiro
     */
    private void startEnfMode() {
        Sistema sistema = new Sistema();
        sistema.onEnf();
    }

    /**
     * Incia o modo Doente
     */
    private void startDntMode() {
        Sistema sistema = new Sistema();
        sistema.onDnt();
    }

    /**
     * Guarda a opção escolhida no menu de login para a validação do mesmo.
     * @return opção guardada
     */
    public static int getOpt() {
        return op;
    }

    ///////////////////////////////////////
    //////////       XML       ////////////
    //////////////////////////////////////
    //////////////////////////////////////


    /**
     * Guarda os dados atualizados num XML
     */
    public static void saveDataToXML() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Criação do documento XML
            Document doc = docBuilder.newDocument();

            // Criação do elemento raiz
            Element rootElement = doc.createElement("sistemaBD");
            doc.appendChild(rootElement);

            Element userstElementC = doc.createElement("users");
            rootElement.appendChild(userstElementC);

            Element adminsElement = doc.createElement("admins");
            userstElementC.appendChild(adminsElement);
            Element medicosElement = doc.createElement("medicos");
            userstElementC.appendChild(medicosElement);
            Element enfermeirosElement = doc.createElement("enfermeiros");
            userstElementC.appendChild(enfermeirosElement);
            Element doentesElement = doc.createElement("doentes");
            userstElementC.appendChild(doentesElement);

            // Percorre os usuários e cria os elementos correspondentes
            for (User user : SistemaBD.users) {
                Element userElement = user.selectCreateElem(user,doc);
                String categoria = user.getCategoria();

                if (categoria.equals("medico")) {
                    medicosElement.appendChild(userElement);
                } else if (categoria.equals("enfermeiro")) {
                    enfermeirosElement.appendChild(userElement);
                } else if (categoria.equals("doente")) {
                    doentesElement.appendChild(userElement);
                } else if (categoria.equals("admin")) {
                    adminsElement.appendChild(userElement);
                }
            }

            // Criação do elemento raiz
            Element conultasElement = doc.createElement("consultas");
            rootElement.appendChild(conultasElement);

            // Percorre as consultas e cria os elementos correspondentes
            for (Consulta consulta : SistemaBD.consultas) {
                Element consultaElement = consulta.createElement(doc);
                conultasElement.appendChild(consultaElement);
            }

            // Grava o documento XML em um arquivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            // Especifica o caminho para o arquivo de saída
            StreamResult result = new StreamResult(new File("src/Hospital.xml"));

            // Salva o documento XML no arquivo
            transformer.transform(source, result);

            System.out.println("Users salvos no XML com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}