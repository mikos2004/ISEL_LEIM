package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para a interface gráfica da app, onde
 * estão os diversos Panels criados e a frame que
 * permite o funcionamento da aplicação
 */
public class InterApp {

    public static int opSelectLogin;
    public static int opMenus;
    public static String categoria;
    public static DefaultTableModel tableModelConslt;
    public static JFrame frame = new JFrame("Hospital MOP");
    public static JLabel nameFind;

    public static JPanel actualPanel; //painel atual
    public static JPanel selogMode;
    public static JPanel login;
    public static JPanel menuAdmin;
    public static JPanel menuMed;
    public static JPanel menuEnf;
    public static JPanel menuDnt;
    public static JPanel registoAdmin;
    public static JPanel registoMed;
    public static JPanel registoEnf;
    public static JPanel registoDnt;
    public static JPanel marcarConslt;
    public static JPanel delAdmin;
    public static JPanel delMed;
    public static JPanel delEnf;
    public static JPanel delDnt;
    public static JPanel delConslt;
    public static JPanel listaAdmin;
    public static JPanel listaMed;
    public static JPanel listaEnf;
    public static JPanel listaDnt;
    public static JPanel listaConslt;

    public static JPanel changePass;

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
            java.util.List<Element> userElements = new ArrayList<>();
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


        ////////////////////////////////////////////////////
        //////////////////JAVA SWING////////////////////////
        ////////////////////////////////////////////////////

        ImageIcon icon = new ImageIcon("src/tps/tp4/icon.png");
        frame.setIconImage(icon.getImage());

        //Criar Paineis

        //Painel para selecionar o Modo de Login
        selogMode = new JPanel();
        selogMode.setVisible(true); //único visivel de começo
        selogMode.setLayout(null);

        //Login Panel
        login = new LoginPanel();

        //Menus para os diferentes users
        menuAdmin = new MenuAdminPanel();
        menuMed = new MenuMedPanel();
        menuEnf = new MenuEnfPanel();
        menuDnt = new MenuDntPanel();

        //Menus para os registos dos diferentes users
        registoAdmin = new RegistAdminPanel();
        registoMed = new RegistMedPanel();
        registoEnf = new RegistEnfPanel();
        registoDnt = new RegistDntPanel();
        marcarConslt = new MarcarConsltPanel();

        //Menus para apagar os diferentes users
        delAdmin = new DeleteAdminPanel();
        delMed = new DeleteMedPanel();
        delEnf = new DeleteEnfPanel();
        delDnt = new DeleteDntPanel();
        delConslt = new DeleteConsltPanel();

        //Menus para listar os diferentes users
        listaAdmin = new ListaAdminsPanel();
        listaMed = new ListaMedsPanel();
        listaEnf = new ListaEnfsPanel();
        listaDnt = new ListaDntsPanel();
        listaConslt = new ListaConsltsPanel();

        /////////////////////////////////
        //////// SELECT LOGIN Elementos

        //Buttons
        JButton adminButton = new JButton("Login Admin");
        adminButton.setForeground(Color.WHITE);
        adminButton.setBackground(Color.DARK_GRAY);
        adminButton.setBounds(127, 140, 200, 46);
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(selogMode);
                frame.setVisible(false);
                frame.add(login);
                frame.setVisible(true);
                setActualPanel(login);
                opSelectLogin = 1;
            }
        });
        JButton medButton = new JButton("Login Médico");
        medButton.setForeground(Color.WHITE);
        medButton.setBackground(Color.DARK_GRAY);
        medButton.setBounds(127, 205, 200, 46);
        medButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(selogMode);
                frame.setVisible(false);
                frame.add(login);
                frame.setVisible(true);
                setActualPanel(login);
                opSelectLogin = 2;
            }
        });
        JButton enfButton = new JButton("Login Enfermeiro");
        enfButton.setForeground(Color.WHITE);
        enfButton.setBackground(Color.DARK_GRAY);
        enfButton.setBounds(127, 270, 200, 46);
        enfButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(selogMode);
                frame.setVisible(false);
                frame.add(login);
                frame.setVisible(true);
                setActualPanel(login);
                opSelectLogin = 3;
            }
        });
        JButton dntButton = new JButton("Login Doente");
        dntButton.setForeground(Color.WHITE);
        dntButton.setBackground(Color.DARK_GRAY);
        dntButton.setBounds(127, 335, 200, 46);
        dntButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(selogMode);
                frame.setVisible(false);
                frame.add(login);
                frame.setVisible(true);
                setActualPanel(login);
                opSelectLogin = 4;
            }
        });


        selogMode.add(adminButton);
        selogMode.add(medButton);
        selogMode.add(enfButton);
        selogMode.add(dntButton);

        JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Selec_LoginMenu.png"));
        image.setBounds(0,0,1024,640);

        //add's ao painel selogMode
        selogMode.add(image);

        //add's frame

        frame.add(selogMode);
        frame.setSize(1024, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Método para guardar o meodo de login
     * @return opção escolhida para login
     */
    public static int retOpSelectLogin() {
        return opSelectLogin;
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

            // Percorre os users e cria os elementos correspondentes
            for (User user : SistemaBD.users) {
                Element userElement = user.selectCreateElem(user, doc);
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

    //////////////////////////
    ///////// MENUS /////////
    /////////////////////////


    /**
     * Painel para efetuar o login
     */

    public static class LoginPanel extends JPanel {
        public static JTextField usernameText;
        public static JPasswordField passText;

        public LoginPanel() {
            setLayout(null); // Altere o layout para BoxLayout vertical

            JLabel userlabel = new JLabel("Username");
            userlabel.setFont(new Font("Roboto", Font.BOLD, 29));
            userlabel.setForeground(Color.WHITE);
            usernameText = new JTextField(15); //Campo para escrever user
            usernameText.setFont(new Font("Roboto", Font.PLAIN, 29));

            JLabel passlabel = new JLabel("Password");
            passlabel.setFont(new Font("Roboto", Font.BOLD, 29));
            passlabel.setForeground(Color.WHITE);
            passText = new JPasswordField(15); //Campo para escrever user
            passText.setFont(new Font("Roboto", Font.PLAIN, 29));

            JButton submitLogin = new JButton("Login"); //botão para submeter
            submitLogin.setForeground(Color.WHITE);
            submitLogin.setBackground(Color.DARK_GRAY);
            submitLogin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    App.start();
                }
            });

            JButton backButton = new JButton("Back"); //botão para submeter
            backButton.setForeground(Color.WHITE);
            backButton.setBackground(Color.DARK_GRAY);
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(login);
                    frame.setVisible(false);
                    frame.add(selogMode);
                    frame.setVisible(true);
                    setActualPanel(selogMode);
                }
            });

            JButton changeButton = new JButton("Alterar Password"); //botão para submeter
            changeButton.setForeground(Color.WHITE);
            changeButton.setBackground(Color.DARK_GRAY);
            changeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changePass = new ChangePassPanel();
                    frame.remove(login);
                    frame.setVisible(false);
                    frame.add(changePass);
                    frame.setVisible(true);
                    setActualPanel(changePass);
                }
            });

            //add's ao painel login
            userlabel.setBounds(260,189,185,46);
            usernameText.setBounds(412,189,200,46);
            passlabel.setBounds(260,249,185,46);
            passText.setBounds(412,249,200,46);
            submitLogin.setBounds(412,371,200,46);
            backButton.setBounds(64,529,99,48);
            changeButton.setBounds(188, 529,216,48);
            add(userlabel);
            add(usernameText);
            add(passlabel);
            add(passText);
            add(submitLogin);
            add(backButton);
            add(changeButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Selec_LoginMenu.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para o Menu Admin
     */
        public static class MenuAdminPanel extends JPanel {
        public MenuAdminPanel() {
            setLayout(null);

            //Buttons
            JButton regAdminButton = new JButton("Registar Admin");
            regAdminButton.setForeground(Color.WHITE);
            regAdminButton.setBackground(Color.DARK_GRAY);
            regAdminButton.setBounds(64, 64, 200, 46);
            regAdminButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(registoAdmin);
                    frame.setVisible(true);
                    setActualPanel(registoAdmin);
                }
            });
            JButton delAdminButton = new JButton("Apagar Adminstrador");
            delAdminButton.setForeground(Color.WHITE);
            delAdminButton.setBackground(Color.DARK_GRAY);
            delAdminButton.setBounds(64, 124, 200, 46);
            delAdminButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(delAdmin);
                    frame.setVisible(true);
                    setActualPanel(delAdmin);
                }
            });
            JButton listAdminButton = new JButton("Listar Adminstrador");
            listAdminButton.setForeground(Color.WHITE);
            listAdminButton.setBackground(Color.DARK_GRAY);
            listAdminButton.setBounds(64, 184, 200, 46);
            listAdminButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaAdmin);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(listaAdmin);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaAdmins();
                }
            });
            JButton regMedButton = new JButton("Registar Médico");
            regMedButton.setForeground(Color.WHITE);
            regMedButton.setBackground(Color.DARK_GRAY);
            regMedButton.setBounds(64, 244, 200, 46);
            regMedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(registoMed);
                    frame.setVisible(true);
                    setActualPanel(registoMed);
                }
            });
            JButton delMedButton = new JButton("Apagar Médico");
            delMedButton.setForeground(Color.WHITE);
            delMedButton.setBackground(Color.DARK_GRAY);
            delMedButton.setBounds(64, 304, 200, 46);
            delMedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(delMed);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(delMed);
                    frame.setVisible(true);
                }
            });
            JButton listMedButton = new JButton("Listar Médico");
            listMedButton.setForeground(Color.WHITE);
            listMedButton.setBackground(Color.DARK_GRAY);
            listMedButton.setBounds(291, 64, 200, 46);
            listMedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaMed);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(listaMed);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaMedicos();
                }
            });
            JButton regEnfButton = new JButton("Registar Enfermeiro");
            regEnfButton.setForeground(Color.WHITE);
            regEnfButton.setBackground(Color.DARK_GRAY);
            regEnfButton.setBounds(291, 124, 200, 46);
            regEnfButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(menuAdmin);
                    frame.add(registoEnf);
                    frame.setVisible(true);
                    setActualPanel(registoEnf);
                }
            });
            JButton delEnfButton = new JButton("Apagar Enfermeiro");
            delEnfButton.setForeground(Color.WHITE);
            delEnfButton.setBackground(Color.DARK_GRAY);
            delEnfButton.setBounds(290, 184, 200, 46);
            delEnfButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(delEnf);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(delEnf);
                    frame.setVisible(true);
                }
            });
            JButton listEnfButton = new JButton("Listar Enfermeiro");
            listEnfButton.setForeground(Color.WHITE);
            listEnfButton.setBackground(Color.DARK_GRAY);
            listEnfButton.setBounds(291, 244, 200, 46);
            listEnfButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaEnf);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(listaEnf);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaEnfermeiros();
                }
            });
            JButton regDntButton = new JButton("Registar Doente");
            regDntButton.setForeground(Color.WHITE);
            regDntButton.setBackground(Color.DARK_GRAY);
            regDntButton.setBounds(291, 304, 200, 46);
            regDntButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(registoDnt);
                    frame.setVisible(true);
                    setActualPanel(registoDnt);
                }
            });
            JButton delDntButton = new JButton("Apagar Doente");
            delDntButton.setForeground(Color.WHITE);
            delDntButton.setBackground(Color.DARK_GRAY);
            delDntButton.setBounds(518, 64, 200, 46);
            delDntButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(delDnt);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(delDnt);
                    frame.setVisible(true);
                }
            });
            JButton listDntButton = new JButton("Listar Doente");
            listDntButton.setForeground(Color.WHITE);
            listDntButton.setBackground(Color.DARK_GRAY);
            listDntButton.setBounds(518, 124, 200, 46);
            listDntButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaDnt);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(listaDnt);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaDoente();
                }
            });
            JButton listConsltutton = new JButton("Listar Consulta");
            listConsltutton.setForeground(Color.WHITE);
            listConsltutton.setBackground(Color.DARK_GRAY);
            listConsltutton.setBounds(518, 184, 200, 46);
            listConsltutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaConslt);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(listaConslt);
                    frame.setVisible(true);
                }
            });
            JButton marconsltButton = new JButton("Marcar Consulta");
            marconsltButton.setForeground(Color.WHITE);
            marconsltButton.setBackground(Color.DARK_GRAY);
            marconsltButton.setBounds(518, 244, 200, 46);
            marconsltButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(marcarConslt);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(marcarConslt);
                    frame.setVisible(true);
                }
            });
            JButton delConsltButton = new JButton("Apagar Consulta");
            delConsltButton.setForeground(Color.WHITE);
            delConsltButton.setBackground(Color.DARK_GRAY);
            delConsltButton.setBounds(518, 304, 200, 46);
            delConsltButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(delConslt);
                    frame.remove(menuAdmin);
                    frame.setVisible(false);
                    frame.add(delConslt);
                    frame.setVisible(true);
                }
            });
            JButton logoutButton = new JButton("Logout");
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setBackground(Color.DARK_GRAY);
            logoutButton.setBounds(64, 530, 117, 46);
            logoutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    InterApp.frame.setVisible(false);
                    InterApp.frame.remove(menuAdmin);
                    InterApp.frame.add(selogMode);
                    InterApp.frame.setVisible(true);
                    Login.logout();
                }
            });

            //add's ao painel menuAdmin
            add(regAdminButton);
            add(delAdminButton);
            add(listAdminButton);
            add(regMedButton);
            add(delMedButton);
            add(listMedButton);
            add(regEnfButton);
            add(delEnfButton);
            add(listEnfButton);
            add(regDntButton);
            add(delDntButton);
            add(listDntButton);
            add((listConsltutton));
            add(marconsltButton);
            add(delConsltButton);
            add(logoutButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Selec_LoginMenu.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para o Menu Médico
     */
    public static class MenuMedPanel extends JPanel {
        public MenuMedPanel() {
            setLayout(null);

            //Buttons

            JButton listMedButton = new JButton("Listar Médico");
            listMedButton.setForeground(Color.WHITE);
            listMedButton.setBackground(Color.DARK_GRAY);
            listMedButton.setBounds(64, 64, 200, 46);
            listMedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaMed);
                    frame.remove(menuMed);
                    frame.setVisible(false);
                    frame.add(listaMed);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaMedicos();
                }
            });
            JButton listEnfButton = new JButton("Listar Enfermeiro");
            listEnfButton.setForeground(Color.WHITE);
            listEnfButton.setBackground(Color.DARK_GRAY);
            listEnfButton.setBounds(64, 124, 200, 46);
            listEnfButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaEnf);
                    frame.remove(menuMed);
                    frame.setVisible(false);
                    frame.add(listaEnf);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaEnfermeiros();
                }
            });
            JButton listDntButton = new JButton("Listar Doente");
            listDntButton.setForeground(Color.WHITE);
            listDntButton.setBackground(Color.DARK_GRAY);
            listDntButton.setBounds(64, 184, 200, 46);
            listDntButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaDnt);
                    frame.remove(menuMed);
                    frame.setVisible(false);
                    frame.add(listaDnt);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaDoente();
                }
            });
            JButton listConsltutton = new JButton("Listar Consulta");
            listConsltutton.setForeground(Color.WHITE);
            listConsltutton.setBackground(Color.DARK_GRAY);
            listConsltutton.setBounds(64, 244, 200, 46);
            listConsltutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaConslt);
                    frame.remove(menuMed);
                    frame.setVisible(false);
                    frame.add(listaConslt);
                    frame.setVisible(true);
                }
            });
            JButton marconsltButton = new JButton("Marcar Consulta");
            marconsltButton.setForeground(Color.WHITE);
            marconsltButton.setBackground(Color.DARK_GRAY);
            marconsltButton.setBounds(64, 304, 200, 46);
            marconsltButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(marcarConslt);
                    frame.remove(menuMed);
                    frame.setVisible(false);
                    frame.add(marcarConslt);
                    frame.setVisible(true);
                }
            });
            JButton delConsltButton = new JButton("Apagar Consulta");
            delConsltButton.setForeground(Color.WHITE);
            delConsltButton.setBackground(Color.DARK_GRAY);
            delConsltButton.setBounds(291, 64, 200, 46);
            delConsltButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(delConslt);
                    frame.remove(menuMed);
                    frame.setVisible(false);
                    frame.add(delConslt);
                    frame.setVisible(true);
                }
            });
            JButton logoutButton = new JButton("Logout");
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setBackground(Color.DARK_GRAY);
            logoutButton.setBounds(64, 530, 117, 46);
            logoutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    InterApp.frame.setVisible(false);
                    InterApp.frame.remove(menuMed);
                    InterApp.frame.add(selogMode);
                    InterApp.frame.setVisible(true);
                    Login.logout();
                }
            });

            //add's ao painel menuAdmin
            add(listMedButton);
            add(listEnfButton);
            add(listDntButton);
            add((listConsltutton));
            add(marconsltButton);
            add(delConsltButton);
            add(logoutButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Selec_LoginMenu.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para o Menu Enfermeiro
     */
    public static class MenuEnfPanel extends JPanel {
        public MenuEnfPanel() {
            setLayout(null);

            //Buttons

            JButton listEnfButton = new JButton("Listar Enfermeiro");
            listEnfButton.setForeground(Color.WHITE);
            listEnfButton.setBackground(Color.DARK_GRAY);
            listEnfButton.setBounds(64, 64, 200, 46);
            listEnfButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaEnf);
                    frame.remove(menuEnf);
                    frame.setVisible(false);
                    frame.add(listaEnf);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaEnfermeiros();
                }
            });
            JButton listMedButton = new JButton("Listar Médico");
            listMedButton.setForeground(Color.WHITE);
            listMedButton.setBackground(Color.DARK_GRAY);
            listMedButton.setBounds(64, 124, 200, 46);
            listMedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaMed);
                    frame.remove(menuEnf);
                    frame.setVisible(false);
                    frame.add(listaMed);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaMedicos();
                }
            });
            JButton listDntButton = new JButton("Listar Doente");
            listDntButton.setForeground(Color.WHITE);
            listDntButton.setBackground(Color.DARK_GRAY);
            listDntButton.setBounds(64, 184, 200, 46);
            listDntButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaDnt);
                    frame.remove(menuEnf);
                    frame.setVisible(false);
                    frame.add(listaDnt);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaDoente();
                }
            });
            JButton logoutButton = new JButton("Logout");
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setBackground(Color.DARK_GRAY);
            logoutButton.setBounds(64, 530, 117, 46);
            logoutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    InterApp.frame.setVisible(false);
                    InterApp.frame.remove(menuEnf);
                    InterApp.frame.add(selogMode);
                    InterApp.frame.setVisible(true);
                    Login.logout();
                }
            });

            //add's ao painel menuAdmin

            add(listMedButton);
            add(listEnfButton);
            add(listDntButton);
            add(logoutButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Selec_LoginMenu.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para o Menu Doente
     */
    public static class MenuDntPanel extends JPanel {
        public MenuDntPanel() {
            setLayout(null);

            //Buttons
            JButton listMedButton = new JButton("Listar Médico");
            listMedButton.setForeground(Color.WHITE);
            listMedButton.setBackground(Color.DARK_GRAY);
            listMedButton.setBounds(64, 64, 200, 46);
            listMedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaMed);
                    frame.remove(menuDnt);
                    frame.setVisible(false);
                    frame.add(listaMed);
                    frame.setVisible(true);

                    // Preencher a tabela com os dados
                    Sistema.listaMedicos();
                }
            });
            JButton listConsltutton = new JButton("Listar Consulta");
            listConsltutton.setForeground(Color.WHITE);
            listConsltutton.setBackground(Color.DARK_GRAY);
            listConsltutton.setBounds(64, 124, 200, 46);
            listConsltutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActualPanel(listaConslt);
                    frame.remove(menuDnt);
                    frame.setVisible(false);
                    frame.add(listaConslt);
                    frame.setVisible(true);
                }
            });
            JButton logoutButton = new JButton("Logout");
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setBackground(Color.DARK_GRAY);
            logoutButton.setBounds(64, 530, 117, 46);
            logoutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    InterApp.frame.setVisible(false);
                    InterApp.frame.remove(menuDnt);
                    InterApp.frame.add(selogMode);
                    InterApp.frame.setVisible(true);
                    Login.logout();
                }
            });

            //add's ao painel menuAdmin
            add(listMedButton);
            add((listConsltutton));
            add(logoutButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Selec_LoginMenu.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para o Registo de Admins
     */
    public static class RegistAdminPanel extends JPanel {
        public static JTextField nomeField;
        public static JTextField apelidoField;
        public static JTextField idadeField;
        public static JTextField userField;
        public static JTextField codeField;

        public RegistAdminPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("REGISTAR ADMIN");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel nomelabel = new JLabel("Nome");
            nomelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            nomelabel.setForeground(Color.WHITE);
            nomeField = new JTextField(20);
            nomeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel apelidolabel = new JLabel("Apelido");
            apelidolabel.setFont(new Font("Roboto", Font.BOLD, 29));
            apelidolabel.setForeground(Color.WHITE);
            apelidoField = new JTextField(20);
            apelidoField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel idadelabel = new JLabel("Idade");
            idadelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            idadelabel.setForeground(Color.WHITE);
            idadeField = new JTextField(20);
            idadeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel userlabel = new JLabel("User");
            userlabel.setFont(new Font("Roboto", Font.BOLD, 29));
            userlabel.setForeground(Color.WHITE);
            userField = new JTextField(20);
            userField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código (AXXXXX)");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(20);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JButton submitButton = new JButton("Registar");
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.registAdmin();
                }
            });

            titleLabel.setBounds(48,41,360,46);

            nomelabel.setBounds(316,199,185,46);
            nomeField.setBounds(508,199,200,46);
            apelidolabel.setBounds(316,259,185,46);
            apelidoField.setBounds(508,259,200,46);
            idadelabel.setBounds(316,319,185,46);
            idadeField.setBounds(508,319,200,46);
            userlabel.setBounds(316,379,185,46);
            userField.setBounds(508,379,200,46);
            codelabel.setBounds(160,438,340,46);
            codeField.setBounds(508,438,200,46);
            submitButton.setBounds(408,530,200,46);
            add(titleLabel);
            add(nomelabel);
            add(nomeField);
            add(apelidolabel);
            add(apelidoField);
            add(idadelabel);
            add(idadeField);
            add(userlabel);
            add(userField);
            add(codelabel);
            add(codeField);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para o Registo de Médicos
     */
    public static class RegistMedPanel extends JPanel {
        public static JTextField nomeField;
        public static JTextField apelidoField;
        public static JTextField especialidadeField;
        public static JTextField idadeField;
        public static JTextField userField;
        public static JTextField codeField;

        public RegistMedPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("REGISTAR MÉDICO");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel nomelabel = new JLabel("Nome");
            nomelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            nomelabel.setForeground(Color.WHITE);
            nomeField = new JTextField(20);
            nomeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel apelidolabel = new JLabel("Apelido");
            apelidolabel.setFont(new Font("Roboto", Font.BOLD, 29));
            apelidolabel.setForeground(Color.WHITE);
            apelidoField = new JTextField(20);
            apelidoField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel especialidadelabel = new JLabel("Especialidade");
            especialidadelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            especialidadelabel.setForeground(Color.WHITE);
            especialidadeField = new JTextField(20);
            especialidadeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel idadelabel = new JLabel("Idade");
            idadelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            idadelabel.setForeground(Color.WHITE);
            idadeField = new JTextField(20);
            idadeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel userlabel = new JLabel("User");
            userlabel.setFont(new Font("Roboto", Font.BOLD, 29));
            userlabel.setForeground(Color.WHITE);
            userField = new JTextField(20);
            userField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código (MXXXXX)");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(20);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JButton submitButton = new JButton("Registar");
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.registMedico();
                }
            });

            titleLabel.setBounds(48,41,360,46);

            nomelabel.setBounds(316,143,185,46);
            nomeField.setBounds(508,143,200,46);
            apelidolabel.setBounds(316,203,185,46);
            apelidoField.setBounds(508,203,200,46);
            especialidadelabel.setBounds(248,263,256,46);
            especialidadeField.setBounds(508,261,200,46);
            idadelabel.setBounds(316,319,185,46);
            idadeField.setBounds(508,319,200,46);
            userlabel.setBounds(316,379,185,46);
            userField.setBounds(508,379,200,46);
            codelabel.setBounds(160,438,340,46);
            codeField.setBounds(508,438,200,46);
            submitButton.setBounds(408,530,200,46);
            add(titleLabel);
            add(nomelabel);
            add(nomeField);
            add(apelidolabel);
            add(apelidoField);
            add(especialidadelabel);
            add(especialidadeField);
            add(idadelabel);
            add(idadeField);
            add(userlabel);
            add(userField);
            add(codelabel);
            add(codeField);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para o Registo de Enfermeiros
     */
    public static class RegistEnfPanel extends JPanel {
        public static JTextField nomeField;
        public static JTextField apelidoField;
        public static JTextField areaField;
        public static JTextField idadeField;
        public static JTextField userField;
        public static JTextField codeField;

        public RegistEnfPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("REGISTAR ENFERMEIRO");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel nomelabel = new JLabel("Nome");
            nomelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            nomelabel.setForeground(Color.WHITE);
            nomeField = new JTextField(20);
            nomeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel apelidolabel = new JLabel("Apelido");
            apelidolabel.setFont(new Font("Roboto", Font.BOLD, 29));
            apelidolabel.setForeground(Color.WHITE);
            apelidoField = new JTextField(20);
            apelidoField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel arealabel = new JLabel("Área");
            arealabel.setFont(new Font("Roboto", Font.BOLD, 29));
            arealabel.setForeground(Color.WHITE);
            areaField = new JTextField(20);
            areaField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel idadelabel = new JLabel("Idade");
            idadelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            idadelabel.setForeground(Color.WHITE);
            idadeField = new JTextField(20);
            idadeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel userlabel = new JLabel("User");
            userlabel.setFont(new Font("Roboto", Font.BOLD, 29));
            userlabel.setForeground(Color.WHITE);
            userField = new JTextField(20);
            userField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código (AXXXXX)");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(20);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JButton submitButton = new JButton("Registar");
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.registEnfermeiro();
                }
            });

            titleLabel.setBounds(48,41,360,46);

            nomelabel.setBounds(316,143,185,46);
            nomeField.setBounds(508,143,200,46);
            apelidolabel.setBounds(316,203,185,46);
            apelidoField.setBounds(508,203,200,46);
            arealabel.setBounds(313,263,114,46);
            areaField.setBounds(508,261,200,46);
            idadelabel.setBounds(316,319,185,46);
            idadeField.setBounds(508,319,200,46);
            userlabel.setBounds(316,379,185,46);
            userField.setBounds(508,379,200,46);
            codelabel.setBounds(160,438,340,46);
            codeField.setBounds(508,438,200,46);
            submitButton.setBounds(408,530,200,46);
            add(titleLabel);
            add(nomelabel);
            add(nomeField);
            add(apelidolabel);
            add(apelidoField);
            add(arealabel);
            add(areaField);
            add(idadelabel);
            add(idadeField);
            add(userlabel);
            add(userField);
            add(codelabel);
            add(codeField);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para o Registo de Doente
     */
    public static class RegistDntPanel extends JPanel {
        public static JTextField nomeField;
        public static JTextField apelidoField;
        public static JTextField diagField;
        public static JTextField idadeField;
        public static JTextField userField;
        public static JTextField codeField;

        public RegistDntPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("REGISTAR DOENTE");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel nomelabel = new JLabel("Nome");
            nomelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            nomelabel.setForeground(Color.WHITE);
            nomeField = new JTextField(20);
            nomeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel apelidolabel = new JLabel("Apelido");
            apelidolabel.setFont(new Font("Roboto", Font.BOLD, 29));
            apelidolabel.setForeground(Color.WHITE);
            apelidoField = new JTextField(20);
            apelidoField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel diaglabel = new JLabel("Diagnóstico");
            diaglabel.setFont(new Font("Roboto", Font.BOLD, 29));
            diaglabel.setForeground(Color.WHITE);
            diagField = new JTextField(20);
            diagField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel idadelabel = new JLabel("Idade");
            idadelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            idadelabel.setForeground(Color.WHITE);
            idadeField = new JTextField(20);
            idadeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel userlabel = new JLabel("User");
            userlabel.setFont(new Font("Roboto", Font.BOLD, 29));
            userlabel.setForeground(Color.WHITE);
            userField = new JTextField(20);
            userField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código (DXXXXX)");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(20);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JButton submitButton = new JButton("Registar");
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.registDoente();
                }
            });

            titleLabel.setBounds(48,41,360,46);

            nomelabel.setBounds(316,143,185,46);
            nomeField.setBounds(508,143,200,46);
            apelidolabel.setBounds(316,203,185,46);
            apelidoField.setBounds(508,203,200,46);
            diaglabel.setBounds(248,263,256,46);
            diagField.setBounds(508,261,200,46);
            idadelabel.setBounds(316,319,185,46);
            idadeField.setBounds(508,319,200,46);
            userlabel.setBounds(316,379,185,46);
            userField.setBounds(508,379,200,46);
            codelabel.setBounds(160,438,340,46);
            codeField.setBounds(508,438,200,46);
            submitButton.setBounds(408,530,200,46);
            add(titleLabel);
            add(nomelabel);
            add(nomeField);
            add(apelidolabel);
            add(apelidoField);
            add(diaglabel);
            add(diagField);
            add(idadelabel);
            add(idadeField);
            add(userlabel);
            add(userField);
            add(codelabel);
            add(codeField);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para Marcar Consultas
     */
    public static class MarcarConsltPanel extends JPanel {
        public static JTextField pacienteField;
        public static JTextField medicoField;
        public static JTextField descField;
        public static JTextField dataField;
        public static JTextField horaField;
        public static JTextField codeField;

        public static JTextField codeFilterField;

        public MarcarConsltPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("MARCAR CONSULTA");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel pacientelabel = new JLabel("Paciente");
            pacientelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            pacientelabel.setForeground(Color.WHITE);
            pacienteField = new JTextField(20);
            pacienteField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel medicolabel = new JLabel("Médico");
            medicolabel.setFont(new Font("Roboto", Font.BOLD, 29));
            medicolabel.setForeground(Color.WHITE);
            medicoField = new JTextField(20);
            medicoField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel desclabel = new JLabel("Descrição");
            desclabel.setFont(new Font("Roboto", Font.BOLD, 29));
            desclabel.setForeground(Color.WHITE);
            descField = new JTextField(20);
            descField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel datalabel = new JLabel("Data");
            datalabel.setFont(new Font("Roboto", Font.BOLD, 29));
            datalabel.setForeground(Color.WHITE);
            dataField = new JTextField(20);
            dataField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel horalabel = new JLabel("Hora");
            horalabel.setFont(new Font("Roboto", Font.BOLD, 29));
            horalabel.setForeground(Color.WHITE);
            horaField = new JTextField(20);
            horaField.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código (CXXXXX)");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(20);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));

            JButton submitButton = new JButton("Marcar");
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.marcarConsulta();
                }
            });

            JLabel codefilterlabel = new JLabel("Insira o seu código");
            codefilterlabel.setFont(new Font("Roboto", Font.BOLD, 21));
            codefilterlabel.setForeground(Color.WHITE);
            codeFilterField = new JTextField(20);
            codeFilterField.setFont(new Font("Roboto", Font.BOLD, 29));

            JButton filterButton = new JButton("OK");
            filterButton.setFont(new Font("Roboto", Font.BOLD, 12));
            filterButton.setForeground(Color.WHITE);
            filterButton.setBackground(Color.DARK_GRAY);
            filterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.CodeOpts(codeFilterField.getText());
                    codeFilterField.setText("");
                    pacienteField.setText("");
                    medicoField.setText("");
                    descField.setText("");
                    dataField.setText("");
                    horaField.setText("");
                    codeField.setText("");
                }
            });

            titleLabel.setBounds(48,41,360,46);

            pacientelabel.setBounds(316,143,185,46);
            pacienteField.setBounds(508,143,200,46);
            medicolabel.setBounds(316,203,185,46);
            medicoField.setBounds(508,203,200,46);
            desclabel.setBounds(248,263,256,46);
            descField.setBounds(508,261,200,46);
            datalabel.setBounds(316,319,185,46);
            dataField.setBounds(508,319,200,46);
            horalabel.setBounds(316,379,185,46);
            horaField.setBounds(508,379,200,46);
            codelabel.setBounds(160,438,340,46);
            codeField.setBounds(508,438,200,46);
            submitButton.setBounds(408,530,200,46);
            codefilterlabel.setBounds(761,503,235,32);
            codeFilterField.setBounds(789,543,118,32);
            filterButton.setBounds(915,543,80,32);
            add(titleLabel);
            add(pacientelabel);
            add(pacienteField);
            add(medicolabel);
            add(medicoField);
            add(desclabel);
            add(descField);
            add(datalabel);
            add(dataField);
            add(horalabel);
            add(horaField);
            add(codelabel);
            add(codeField);
            add(submitButton);
            add(codefilterlabel);
            add(codeFilterField);
            add(filterButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para apagar Admins
     */
    public static class DeleteAdminPanel extends JPanel {
        public static JTextField codeField;

        public DeleteAdminPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("APAGAR ADMIN");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código do Administrador");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));


            JButton deleteButton = new JButton("Apagar");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.DARK_GRAY);
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.deleteAdmin();
                }
            });

            titleLabel.setBounds(48,41,360,46);
            codelabel.setBounds(130,319,437,46);
            codeField.setBounds(490,319,200,46);
            deleteButton.setBounds(408,530,200,46);
            add(titleLabel);
            add(codelabel);
            add(codeField);
            add(deleteButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para apagar Médicos
     */
    public static class DeleteMedPanel extends JPanel {
        public static JTextField codeField;

        public DeleteMedPanel() {
            setLayout(null);
            JLabel titleLabel = new JLabel("APAGAR MÉDICO");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código do Médico");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));


            JButton deleteButton = new JButton("Apagar");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.DARK_GRAY);
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.deleteMedico();
                }
            });

            titleLabel.setBounds(48,41,360,46);
            codelabel.setBounds(130,319,437,46);
            codeField.setBounds(490,319,200,46);
            deleteButton.setBounds(408,530,200,46);
            add(titleLabel);
            add(codelabel);
            add(codeField);
            add(deleteButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para apagar Enfermeiros
     */
    public static class DeleteEnfPanel extends JPanel {
        public static JTextField codeField;

        public DeleteEnfPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("APAGAR ENFERMEIRO");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código do Enfermeiro");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));


            JButton deleteButton = new JButton("Apagar");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.DARK_GRAY);
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.deleteEnfermeiro();
                }
            });

            titleLabel.setBounds(48,41,360,46);
            codelabel.setBounds(130,319,437,46);
            codeField.setBounds(490,319,200,46);
            deleteButton.setBounds(408,530,200,46);
            add(titleLabel);
            add(codelabel);
            add(codeField);
            add(deleteButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para apagar Doentes
     */
    public static class DeleteDntPanel extends JPanel {
        public static JTextField codeField;

        public DeleteDntPanel() {
            setLayout(null);
            JLabel titleLabel = new JLabel("APAGAR DOENTE");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código do Doente");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));


            JButton deleteButton = new JButton("Apagar");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.DARK_GRAY);
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.deleteDoente();
                }
            });

            titleLabel.setBounds(48,41,360,46);
            codelabel.setBounds(130,319,437,46);
            codeField.setBounds(490,319,200,46);
            deleteButton.setBounds(408,530,200,46);
            add(titleLabel);
            add(codelabel);
            add(codeField);
            add(deleteButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para apagar Consultas
     */
    public static class DeleteConsltPanel extends JPanel {
        public static JTextField codeField;
        public static JTextField codeFilterField;

        public DeleteConsltPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("APAGAR CONSULTA");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Código da Consulta");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 29));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 29));


            JButton deleteButton = new JButton("Apagar");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.DARK_GRAY);
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.deleteConsulta();
                    codeFilterField.setText("");
                }
            });

            JLabel codefilterlabel = new JLabel("Insira o seu código");
            codefilterlabel.setFont(new Font("Roboto", Font.BOLD, 21));
            codefilterlabel.setForeground(Color.WHITE);
            codeFilterField = new JTextField(20);
            codeFilterField.setFont(new Font("Roboto", Font.BOLD, 29));

            JButton filterButton = new JButton("OK");
            filterButton.setFont(new Font("Roboto", Font.BOLD, 12));
            filterButton.setForeground(Color.WHITE);
            filterButton.setBackground(Color.DARK_GRAY);
            filterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.CodeOpts(codeFilterField.getText());
                    codeFilterField.setText("");
                }
            });

            titleLabel.setBounds(48,41,360,46);
            codelabel.setBounds(130,319,437,46);
            codeField.setBounds(490,319,200,46);
            deleteButton.setBounds(408,530,200,46);
            codefilterlabel.setBounds(761,503,235,32);
            codeFilterField.setBounds(789,543,118,32);
            filterButton.setBounds(915,543,80,32);
            add(titleLabel);
            add(codelabel);
            add(codeField);
            add(deleteButton);
            add(codefilterlabel);
            add(codeFilterField);
            add(filterButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para listar Admins
     */
    public static class ListaAdminsPanel extends JPanel {
        public static JTable adminsTable;
        public static DefaultTableModel tableModel;
        public static JTextField codeField;
        public ListaAdminsPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("ADMINISTRADORES");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));
            JLabel titleLabel2 = new JLabel("REGISTADOS");
            titleLabel2.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Insira o seu código");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 21));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 21));

            String[] columnNames = {"CÓDIGO", "NOME"};
            tableModel = new DefaultTableModel(columnNames, 0);

            adminsTable = new JTable(tableModel);

            JTableHeader tableHeader = adminsTable.getTableHeader();
            tableHeader.setBackground(Color.DARK_GRAY);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(adminsTable);



            JButton submitButton = new JButton("OK");
            submitButton.setFont(new Font("Roboto", Font.BOLD, 12));
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.CodeOpts(codeField.getText());
                    codeField.setText("");
                }
            });

            titleLabel.setBounds(29,12,388,46);
            titleLabel2.setBounds(29,59,388,46);
            scrollPane.setBounds(129,152,767,387);
            codelabel.setBounds(129,560,235,32);
            codeField.setBounds(340,560,118,32);
            submitButton.setBounds(490,560,100,32);

            add(titleLabel);
            add(titleLabel2);
            add(codelabel);
            add(codeField);
            add(scrollPane);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para listar Médicos
     */
    public static class ListaMedsPanel extends JPanel {
        public static JTable adminsTable;
        public static DefaultTableModel tableModel;
        public static JTextField codeField;
        public ListaMedsPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("MÉDICOS");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));
            JLabel titleLabel2 = new JLabel("REGISTADOS");
            titleLabel2.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Insira o seu código");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 21));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 21));

            String[] columnNames = {"CÓDIGO", "NOME", "ESPECIALIADADE", "IDADE"};
            tableModel = new DefaultTableModel(columnNames, 0);

            adminsTable = new JTable(tableModel);

            JTableHeader tableHeader = adminsTable.getTableHeader();
            tableHeader.setBackground(Color.DARK_GRAY);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(adminsTable);



            JButton submitButton = new JButton("OK");
            submitButton.setFont(new Font("Roboto", Font.BOLD, 12));
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.CodeOpts(codeField.getText());
                    codeField.setText("");
                }
            });

            titleLabel.setBounds(29,12,388,46);
            titleLabel2.setBounds(29,59,388,46);
            scrollPane.setBounds(129,152,767,387);
            codelabel.setBounds(129,560,235,32);
            codeField.setBounds(340,560,118,32);
            submitButton.setBounds(490,560,100,32);

            add(titleLabel);
            add(titleLabel2);
            add(codelabel);
            add(codeField);
            add(scrollPane);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para listar Enfermeiros
     */
    public static class ListaEnfsPanel extends JPanel {
        public static JTable enfsTable;
        public static DefaultTableModel tableModel;
        public static JTextField codeField;
        public ListaEnfsPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("ENFERMEIROS");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));
            JLabel titleLabel2 = new JLabel("REGISTADOS");
            titleLabel2.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Insira o seu código");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 21));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 21));

            String[] columnNames = {"CÓDIGO", "NOME", "ÁREA", "IDADE"};
            tableModel = new DefaultTableModel(columnNames, 0);

            enfsTable = new JTable(tableModel);

            JTableHeader tableHeader = enfsTable.getTableHeader();
            tableHeader.setBackground(Color.DARK_GRAY);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(enfsTable);



            JButton submitButton = new JButton("OK");
            submitButton.setFont(new Font("Roboto", Font.BOLD, 12));
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.CodeOpts(codeField.getText());
                    codeField.setText("");
                }
            });

            titleLabel.setBounds(29,12,388,46);
            titleLabel2.setBounds(29,59,388,46);
            scrollPane.setBounds(129,152,767,387);
            codelabel.setBounds(129,560,235,32);
            codeField.setBounds(340,560,118,32);
            submitButton.setBounds(490,560,100,32);

            add(titleLabel);
            add(titleLabel2);
            add(codelabel);
            add(codeField);
            add(scrollPane);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para listar Doentes
     */
    public static class ListaDntsPanel extends JPanel {
        public static JTable dntsTable;
        public static DefaultTableModel tableModel;
        public static JTextField codeField;
        public ListaDntsPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("DOENTES");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));
            JLabel titleLabel2 = new JLabel("REGISTADOS");
            titleLabel2.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel codelabel = new JLabel("Insira o seu código");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 21));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 21));

            String[] columnNames = {"CÓDIGO", "NOME", "DIAGNÓSTICO", "IDADE"};
            tableModel = new DefaultTableModel(columnNames, 0);

            dntsTable = new JTable(tableModel);

            JTableHeader tableHeader = dntsTable.getTableHeader();
            tableHeader.setBackground(Color.DARK_GRAY);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(dntsTable);



            JButton submitButton = new JButton("OK");
            submitButton.setFont(new Font("Roboto", Font.BOLD, 12));
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.CodeOpts(codeField.getText());
                    codeField.setText("");
                }
            });

            titleLabel.setBounds(29,12,388,46);
            titleLabel2.setBounds(29,59,388,46);
            scrollPane.setBounds(129,152,767,387);
            codelabel.setBounds(129,560,235,32);
            codeField.setBounds(340,560,118,32);
            submitButton.setBounds(490,560,100,32);

            add(titleLabel);
            add(titleLabel2);
            add(codelabel);
            add(codeField);
            add(scrollPane);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Painel para listar Consultas
     */
    public static class ListaConsltsPanel extends JPanel {
        public static JTable consltsTable;
        public static JTextField codeField;
        public static JTextField codeSearchField;
        public static String categoria;
        public ListaConsltsPanel() {
            setLayout(null);

            JLabel titleLabel = new JLabel("CONSULTAS");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));

            JLabel tableTaglabel = new JLabel("Consultas de:");
            tableTaglabel.setFont(new Font("Roboto", Font.BOLD, 21));
            tableTaglabel.setForeground(Color.WHITE);

            nameFind = new JLabel("");

            JLabel codeSearchlabel = new JLabel("Código a filtrar:");
            codeSearchlabel.setFont(new Font("Roboto", Font.BOLD, 21));
            codeSearchlabel.setForeground(Color.WHITE);
            codeSearchField = new JTextField(10);
            codeSearchField.setFont(new Font("Roboto", Font.BOLD, 21));

            JLabel codelabel = new JLabel("Insira o seu código");
            codelabel.setFont(new Font("Roboto", Font.BOLD, 21));
            codelabel.setForeground(Color.WHITE);
            codeField = new JTextField(10);
            codeField.setFont(new Font("Roboto", Font.BOLD, 21));


            String[] columnNames = {"CÓDIGO","DATA", "HORA", "DESCRIÇÃO", ""};
            tableModelConslt = new DefaultTableModel(columnNames, 0);

            consltsTable = new JTable(tableModelConslt);

            JTableHeader tableHeader = consltsTable.getTableHeader();
            tableHeader.setBackground(Color.DARK_GRAY);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(consltsTable);

            JButton filterButton = new JButton("OK");
            filterButton.setFont(new Font("Roboto", Font.BOLD, 12));
            filterButton.setForeground(Color.WHITE);
            filterButton.setBackground(Color.DARK_GRAY);
            filterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Preencher a tabela com os dados
                    returnUserFilterName(codeSearchField.getText());
                    returnCategoriaConslt(codeSearchField.getText());
                    Sistema.listarConsultasPorCodigo();
                    codeSearchField.setText("");
                }
            });

            JButton submitButton = new JButton("OK");
            submitButton.setFont(new Font("Roboto", Font.BOLD, 12));
            submitButton.setForeground(Color.WHITE);
            submitButton.setBackground(Color.DARK_GRAY);
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sistema.CodeOpts(codeField.getText());
                    codeField.setText("");
                    codeSearchField.setText("");
                    tableModelConslt.setRowCount(0);
                    nameFind.setText("");
                }
            });

            titleLabel.setBounds(29,12,388,46);
            tableTaglabel.setBounds(294,156,235,32);
            nameFind.setBounds(450,156,405,32);
            codeSearchlabel.setBounds(672,35,235,32);
            codeSearchField.setBounds(706,75,118,32);
            filterButton.setBounds(832,75,100,32);
            scrollPane.setBounds(129,194,767,351);
            codelabel.setBounds(129,560,235,32);
            codeField.setBounds(340,560,118,32);
            submitButton.setBounds(490,560,100,32);

            add(titleLabel);
            add(codeSearchlabel);
            add(tableTaglabel);
            add(nameFind);
            add(codeSearchField);
            add(filterButton);
            add(codelabel);
            add(codeField);
            add(scrollPane);
            add(submitButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Menus.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }

    /**
     * Método para guardar o painel atual mostrado ao user
     * @param panel
     */
    public static void setActualPanel(JPanel panel){
        actualPanel = panel;
    }

    /**
     * Método para filtrar as colunas das tabelas das consultas
     * @param codigo
     */
    public static void returnCategoriaConslt(String codigo){
        categoria = "";

        if (codigo.isEmpty()){
            ListaConsltsPanel.codeSearchField.setText("");
        } else if (codigo.charAt(0)=='d' || codigo.charAt(0)=='D'){
            tableModelConslt.setColumnIdentifiers(new String[]{"CÓDIGO", "DATA", "HORA", "DESCRIÇÃO", "MÉDICO"});
        }else if (codigo.charAt(0)=='M' || codigo.charAt(0)=='m'){
            tableModelConslt.setColumnIdentifiers(new String[]{"CÓDIGO", "DATA", "HORA", "DESCRIÇÃO", "DOENTE"});
        }else{
            JOptionPane.showMessageDialog(null, "Insira um código válido [MXXXXX ou DXXXXX]");
            ListaConsltsPanel.codeSearchField.setText("");
            InterApp.tableModelConslt.setRowCount(0);
        }
    }

    /**
     * Método para apresentar uma tag consoante o código da consulta para o filtro
     * @param codigo
     */
    public static void returnUserFilterName(String codigo){
        for (User user : SistemaBD.users){
            if (codigo.equalsIgnoreCase(user.getCode())){
                nameFind.setText(user.getNome() + " ["+user.getCategoria()+"]");
                nameFind.setFont(new Font("Roboto", Font.BOLD, 21));
                nameFind.setForeground(Color.WHITE);
                return;
            }
        }
    }


    /**
     * Painel para alterar a password
     */
    public static class ChangePassPanel extends JPanel {
        public static JTextField codeText;
        public static JTextField passText;

        public ChangePassPanel() {
            setLayout(null); // Altere o layout para BoxLayout vertical

            JLabel titleLabel = new JLabel("ALTERAR");
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 29));
            titleLabel.setForeground(Color.WHITE);
            JLabel titleLabel2 = new JLabel("PASSSWORD");
            titleLabel2.setFont(new Font("Roboto", Font.BOLD, 29));
            titleLabel2.setForeground(Color.WHITE);

            JLabel userlabel = new JLabel("Código");
            userlabel.setFont(new Font("Roboto", Font.BOLD, 29));
            userlabel.setForeground(Color.WHITE);
            codeText = new JTextField(15);
            codeText.setFont(new Font("Roboto", Font.PLAIN, 29));

            JLabel passlabel = new JLabel("Nova Password");
            passlabel.setFont(new Font("Roboto", Font.BOLD, 29));
            passlabel.setForeground(Color.WHITE);
            passText = new JPasswordField(15);
            passText.setFont(new Font("Roboto", Font.PLAIN, 29));

            JButton submitLogin = new JButton("Alterar");
            submitLogin.setForeground(Color.WHITE);
            submitLogin.setBackground(Color.DARK_GRAY);
            submitLogin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    User userChange = SistemaBD.existeUcode(codeText.getText());
                    userChange.setPass(passText.getText());
                    JOptionPane.showMessageDialog(null, "Password alterada.");
                }
            });

            JButton backButton = new JButton("Back"); //botão para submeter
            backButton.setForeground(Color.WHITE);
            backButton.setBackground(Color.DARK_GRAY);
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(changePass);
                    frame.setVisible(false);
                    frame.add(login);
                    frame.setVisible(true);
                    setActualPanel(login);
                }
            });

            //add's ao painel login
            titleLabel.setBounds(29,12,388,46);
            titleLabel2.setBounds(29,59,388,46);
            userlabel.setBounds(260,189,185,46);
            codeText.setBounds(412,189,200,46);
            passlabel.setBounds(150,249,379,46);
            passText.setBounds(412,249,200,46);
            submitLogin.setBounds(412,371,200,46);
            backButton.setBounds(64,525,99,46);

            add(titleLabel);
            add(titleLabel2);
            add(userlabel);
            add(codeText);
            add(passlabel);
            add(passText);
            add(submitLogin);
            add(backButton);

            JLabel image = new JLabel(new ImageIcon("src/tps/tp4/Selec_LoginMenu.png"));
            image.setBounds(0,0,1024,640);
            add(image);
        }
    }
}
