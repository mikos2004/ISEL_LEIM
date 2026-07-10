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
import javax.swing.*;

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
        App app = new App();
        app.start();
    }

    /**
     * Inicia o programa, mostrando as opções de login e inicia o modo correspondente à categoria do user.
     */
    public static void start() {
        int op = InterApp.retOpSelectLogin();

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
    public static void startAdminMode() {
        Sistema sistema = new Sistema();
        sistema.onAdmin();
    }

    /**
     * Incia o modo Médico
     */
    private static void startMedMode() {
        Sistema sistema = new Sistema();
        sistema.onMed();
    }

    /**
     * Incia o modo Enfermeiro
     */
    private static void startEnfMode() {
        Sistema sistema = new Sistema();
        sistema.onEnf();
    }

    /**
     * Incia o modo Doente
     */
    private static void startDntMode() {
        Sistema sistema = new Sistema();
        sistema.onDnt();
    }
}