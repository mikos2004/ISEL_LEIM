package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class que representa uma consulta.
 */

public class Consulta {
    private String descricao;
    private String data;
    private String hora;
    private String paciente;
    private String medico;
    private String code;

    /**
     * Construtor da class Consulta.
     *
     * @param descricao a descrição da consulta
     * @param data a data da consulta
     * @param hora a hora da consulta
     * @param paciente o nome do paciente
     * @param medico o nome do médico
     * @param code o código da consulta
     */
    public Consulta(String descricao, String data, String hora, String paciente, String medico, String code) {
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
        this.paciente = paciente;
        this.medico = medico;
        this.code = code;
    }

    /**
     * Obtém o código da consulta.
     *
     * @return o código da consulta
     */
    public String getCode() {
        return code;
    }

    /**
     * Obtém a descrição da consulta.
     *
     * @return a descrição da consulta
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição da consulta.
     *
     * @param descricao a nova descrição da consulta
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém a data da consulta.
     *
     * @return a data da consulta
     */
    public String getData() {
        return data;
    }

    /**
     * Define a data da consulta.
     *
     * @param data a nova data da consulta
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Obtém a hora da consulta.
     *
     * @return a hora da consulta
     */
    public String getHora() {
        return hora;
    }

    /**
     * Define a hora da consulta.
     *
     * @param hora a nova hora da consulta
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * Obtém o paciente da consulta.
     *
     * @return o paciente da consulta
     */
    public String getPaciente() {
        return paciente;
    }

    /**
     * Define o paciente da consulta.
     *
     * @param paciente o novo paciente da consulta
     */
    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    /**
     * Obtém o médico da consulta.
     *
     * @return o médico da consulta
     */
    public String getMedico() {
        return medico;
    }

    /**
     * Define o médico da consulta.
     *
     * @param medico o novo médico da consulta
     */
    public void setMedico(String medico) {
        this.medico = medico;
    }

/////////////////////////////////////////////////////////////////
/////////////////////////XML////////////////////////////////////
////////////////////////////////////////////////////////////////

    /**
     * Constrói um Consulta a partir de um nó do XML.
     *
     * @param nNode O nó do XML que representa um consulta.
     * @return O Consulta criado a partir do nó XML.
     */
    public static Consulta build(Node nNode) {
        Element consultaElement = (Element) nNode;

        String descricao = consultaElement.getElementsByTagName("descricao").item(0).getTextContent();
        String data = consultaElement.getElementsByTagName("data").item(0).getTextContent();
        String hora = consultaElement.getElementsByTagName("hora").item(0).getTextContent();
        String paciente = consultaElement.getElementsByTagName("paciente").item(0).getTextContent();
        String medico = consultaElement.getElementsByTagName("medicoC").item(0).getTextContent();
        String code = consultaElement.getElementsByTagName("code").item(0).getTextContent();

        return new Consulta(descricao, data, hora, paciente, medico, code);
    }

    /**
     * Cria um elemento XML a partir de uma Consulta.
     *
     * @param doc O documento XML para onde o elemento será criado.
     * @return O elemento XML que representa uma Consulta.
     */
    public Element createElement(Document doc) {

        //Element consultasElement = doc.createElement("consultas");

        Element consultaElement = doc.createElement("consulta");

        Element descricaoElement = doc.createElement("descricao");
        descricaoElement.setTextContent(this.getDescricao());
        consultaElement.appendChild(descricaoElement);

        Element dataElement = doc.createElement("data");
        dataElement.setTextContent(this.getData());
        consultaElement.appendChild(dataElement);

        Element horaElement = doc.createElement("hora");
        horaElement.setTextContent(this.getCode());
        consultaElement.appendChild(horaElement);

        Element pacienteElement = doc.createElement("paciente");
        pacienteElement.setTextContent(this.getPaciente());
        consultaElement.appendChild(pacienteElement);

        Element medicoElement = doc.createElement("medicoC");
        medicoElement.setTextContent(this.getMedico());
        consultaElement.appendChild(medicoElement);

        Element codeElement = doc.createElement("code");
        codeElement.setTextContent(this.getCode());
        consultaElement.appendChild(codeElement);

        //return consultasElement;
        return consultaElement;
    }
}
