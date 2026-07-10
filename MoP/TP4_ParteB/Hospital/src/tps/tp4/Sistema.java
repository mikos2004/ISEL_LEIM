package tps.tp4;

import javax.swing.*;

import static java.lang.Integer.parseInt;
import static tps.tp4.InterApp.*;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class que representa um sistema dum hosptial.
 * Ao ser estendida da class Login, o sistema funcionára consoante o login do user.
 */
public class Sistema {

/**
 * Executa as operações disponíveis para um admin.
 * Se o login for bem sucedido, é apresentado o menu do admin e
 * executa a operação selecionada.
 */
 public static void onAdmin() {

        if (Login.login()) {
            InterApp.frame.setVisible(false);
            InterApp.frame.remove(actualPanel);
            InterApp.frame.add(menuAdmin);
            InterApp.frame.setVisible(true);
        }
    }

    /**
     * Lista os admin registados no sistema.
     * Imprime na consola uma tabela com os códigos e nomes dos administradores.
     * Caso não encontre nenhum administrador registado, imprime uma mensagem apropriada.
     */
    public static void listaAdmins() {
        InterApp.ListaAdminsPanel.tableModel.setRowCount(0);
        for (User u : SistemaBD.users) {
            if ("admin".equalsIgnoreCase(u.getCategoria())){
                Admin admin = (Admin) u;
                String[] rowData = {admin.getCode(), admin.getNome()};
                InterApp.ListaAdminsPanel.tableModel.addRow(rowData);
            }
        }
    }

    /**
     * Apaga um admin do sistema:
     * Pede ao user (admin) o código do admin a ser excluído e verifica se o admin existe.
     * Caso o admin exista, pede uma confirmação antes de apagar o admin.
     * Se o user confirmar, o admin é excluído do sistema.
     * Se o user não confirmar ou o código do admin não for válido, aparece uma mensagem de erro.
     * Depois do procedimento, o programa volta ao menu do admin.
     */
    public static void deleteAdmin() {
        String CodeAdmin = InterApp.DeleteAdminPanel.codeField.getText();
        User u = SistemaBD.procuraUser(CodeAdmin);
        if (u != null && "admin".equalsIgnoreCase(u.getCategoria())) {
            int result = JOptionPane.showConfirmDialog(null, "Tem a certeza? " + u.getNome() + " ( " + u.getCode() + " ) ", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (SistemaBD.deleteU(u.getCode())) {
                    JOptionPane.showMessageDialog(null, "Administrador apagado com sucesso");
                    onAdmin();
                    InterApp.DeleteAdminPanel.codeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao apagar. Introduza um código válido...");
                }
            }else  {
                onAdmin();
                InterApp.DeleteMedPanel.codeField.setText("");
            }
        }else {
            JOptionPane.showMessageDialog(null, "Introduza um código válido.");
            onAdmin();
            InterApp.DeleteAdminPanel.codeField.setText("");
        }
    }

    /**
     * Registra um novo admin no sistema.
     */
    public static void registAdmin() {
        boolean CodeExiste = false;

        String nome = InterApp.RegistAdminPanel.nomeField.getText();
        String apelido = InterApp.RegistAdminPanel.apelidoField.getText();
        int idade = parseInt(InterApp.RegistAdminPanel.idadeField.getText());
        String user = InterApp.RegistAdminPanel.userField.getText();
        String code = InterApp.RegistAdminPanel.codeField.getText();

        Admin admin = new Admin(nome + " " + apelido, idade, code, user, code);
        admin.setPass("default");

        for (User u : SistemaBD.users){
            if (u.getCode().equalsIgnoreCase(admin.getCode())){
                CodeExiste = true;
            }
        }

        if (!CodeExiste){
            SistemaBD.saveUser(admin);
        }else {
            JOptionPane.showMessageDialog(null, "Código existente. Altere-o!");
        }


        if (SistemaBD.existeU(admin)) {
            JOptionPane.showMessageDialog(null, "Administrador registado com sucesso");
            onAdmin();
        }

    }

    ///////////////////////////
    //MÉDICOS

    /**
     * Executa as operações disponíveis para um médico.
     * Se o login for bem sucedido, é apresentado o menu do médico e
     * executa a operação selecionada.
     */
    public static void onMed() {

        if (Login.login()) {

            InterApp.frame.setVisible(false);
            InterApp.frame.remove(actualPanel);
            InterApp.frame.add(menuMed);
            InterApp.frame.setVisible(true);
        }

    }

    /**
     * Lista os médicos registados no sistema.
     * Imprime na consola uma tabela com os códigos e nomes dos médicos.
     * Caso não encontre nenhum médico registado, imprime uma mensagem apropriada.
     */
    public static void listaMedicos() {
        InterApp.ListaMedsPanel.tableModel.setRowCount(0);
        for (User u : SistemaBD.users) {
            if ("medico".equalsIgnoreCase(u.getCategoria())){
                Medico medico = (Medico) u;
                String[] rowData = {medico.getCode(), medico.getNome(), medico.getEspecialidade(), String.valueOf(medico.getIdade())};
                InterApp.ListaMedsPanel.tableModel.addRow(rowData);
            }
        }
    }

    /**
     * Apaga um médico do sistema:
     * Pede ao user o código do médico a ser excluído e verifica se o médico existe.
     * Caso o médico exista, pede uma confirmação antes de apagar o médico.
     * Se o user confirmar, o médico é excluído do sistema.
     * Se o user não confirmar ou o código do médico não for válido, aparece uma mensagem de erro.
     * Depois do procedimento, o programa volta ao menu do admin.
     */
    public static void deleteMedico() {
        String CodeMed = InterApp.DeleteMedPanel.codeField.getText();
        User u = SistemaBD.procuraUser(CodeMed);
        if (u != null && "medico".equalsIgnoreCase(u.getCategoria())) {
            int result = JOptionPane.showConfirmDialog(null, "Tem a certeza? " + u.getNome() + " ( " + u.getCode() + " ) ", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (SistemaBD.deleteU(u.getCode())) {
                    JOptionPane.showMessageDialog(null, "Médico apagado com sucesso");
                    onAdmin();
                    InterApp.DeleteMedPanel.codeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao apagar. Introduza um código válido...");
                    onAdmin();
                    InterApp.DeleteMedPanel.codeField.setText("");
                }
            }else  {
                onAdmin();
                InterApp.DeleteMedPanel.codeField.setText("");
            }
        }else {
            JOptionPane.showMessageDialog(null, "Introduza um código válido.");
            onAdmin();
            InterApp.DeleteMedPanel.codeField.setText("");
        }
    }

    /**
     * Registra um novo médico no sistema.
     */
    public static void registMedico() {
        boolean CodeExiste = false;

        String nome = InterApp.RegistMedPanel.nomeField.getText();
        String apelido = InterApp.RegistMedPanel.apelidoField.getText();
        String especialidade = RegistMedPanel.especialidadeField.getText();
        int idade = parseInt(InterApp.RegistMedPanel.idadeField.getText());
        String user = InterApp.RegistMedPanel.userField.getText();
        String code = InterApp.RegistMedPanel.codeField.getText();

        Medico medico = new Medico(nome + " " + apelido, idade, code, user, code);
        medico.setEspecialidade(especialidade);
        medico.setPass("default");


        for (User u : SistemaBD.users){
            if (u.getCode().equalsIgnoreCase(medico.getCode())){
                CodeExiste = true;
            }
        }

        if (!CodeExiste){
            SistemaBD.saveUser(medico);;
        }else {
            JOptionPane.showMessageDialog(null, "Código existente. Altere-o!");
        }


        if (SistemaBD.existeU(medico)) {
            JOptionPane.showMessageDialog(null, "Médico registado com sucesso");
            onAdmin();
        }
    }


    ///////////////////////////////
    //Enfermeiro

    /**
     * Executa as operações disponíveis para um enfermeiro.
     * Se o login for bem sucedido, é apresentado o menu do enfermeiro e
     * executa a operação selecionada.
     */
    public static void onEnf() {

        if (Login.login()) {
            InterApp.frame.setVisible(false);
            InterApp.frame.remove(actualPanel);
            InterApp.frame.add(menuEnf);
            InterApp.frame.setVisible(true);
        }
    }

    /**
     * Lista os enfermeiro registados no sistema.
     * Imprime na consola uma tabela com os códigos e nomes dos enfermeiro.
     * Caso não encontre nenhum enfermeiro registado, imprime uma mensagem apropriada.
     */
    public static void listaEnfermeiros() {
        InterApp.ListaEnfsPanel.tableModel.setRowCount(0);
        for (User u : SistemaBD.users) {
            if ("enfermeiro".equalsIgnoreCase(u.getCategoria())){
                Enfermeiro enfermeiro = (Enfermeiro) u;
                String[] rowData = {enfermeiro.getCode(), enfermeiro.getNome(), enfermeiro.getArea(), String.valueOf(enfermeiro.getIdade())};
                InterApp.ListaEnfsPanel.tableModel.addRow(rowData);
            }
        }
    }

    /**
     * Apaga um enfermeiro do sistema:
     * Pede ao user o código do enfermeiro a ser excluído e verifica se o enfermeiro existe.
     * Caso o enfermeiro exista, pede uma confirmação antes de apagar o enfermeiro.
     * Se o user confirmar, o enfermeiro é excluído do sistema.
     * Se o user não confirmar ou o código do enfermeiro não for válido, aparece uma mensagem de erro.
     * Depois do procedimento, o programa volta ao menu do admin.
     */
    public static void deleteEnfermeiro() {
        String CodeEnf = InterApp.DeleteEnfPanel.codeField.getText();
        User u = SistemaBD.procuraUser(CodeEnf);
        if (u != null && "enfermeiro".equalsIgnoreCase(u.getCategoria())) {
            int result = JOptionPane.showConfirmDialog(null, "Tem a certeza? " + u.getNome() + " ( " + u.getCode() + " ) ", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (SistemaBD.deleteU(u.getCode())) {
                    JOptionPane.showMessageDialog(null, "Enfermeiro apagado com sucesso");
                    onAdmin();
                    InterApp.DeleteEnfPanel.codeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao apagar. Introduza um código válido...");
                    onAdmin();
                    InterApp.DeleteEnfPanel.codeField.setText("");
                }
            }else  {
                onAdmin();
                InterApp.DeleteEnfPanel.codeField.setText("");
            }
        }else {
            JOptionPane.showMessageDialog(null, "Introduza um código válido.");
            onAdmin();
            InterApp.DeleteMedPanel.codeField.setText("");
        }
    }

    /**
     * Registra um novo enfermeiro no sistema.
     */
    public static void registEnfermeiro() {
        boolean CodeExiste = false;

        String nome = InterApp.RegistEnfPanel.nomeField.getText();
        String apelido = InterApp.RegistEnfPanel.apelidoField.getText();
        String area = RegistEnfPanel.areaField.getText();
        int idade = parseInt(InterApp.RegistEnfPanel.idadeField.getText());
        String user = InterApp.RegistEnfPanel.userField.getText();
        String code = InterApp.RegistEnfPanel.codeField.getText();

        Enfermeiro enfermeiro = new Enfermeiro(nome + " " + apelido, idade, code, user, code);
        enfermeiro.setArea(area);
        enfermeiro.setPass("default");


        for (User u : SistemaBD.users){
            if (u.getCode().equalsIgnoreCase(enfermeiro.getCode())){
                CodeExiste = true;
            }
        }

        if (!CodeExiste){
            SistemaBD.saveUser(enfermeiro);
        }else {
            JOptionPane.showMessageDialog(null, "Código existente. Altere-o!");
        }


        if (SistemaBD.existeU(enfermeiro)) {
            JOptionPane.showMessageDialog(null, "Enfermeiro registado com sucesso");
            onAdmin();
        }
    }

    ///////////////////////////////
    //Doente

    /**
     * Executa as operações disponíveis para um doente.
     * Se o login for bem sucedido, é apresentado o menu do doente e
     * executa a operação selecionada.
     */
    public static void onDnt() {

        if (Login.login()) {
            InterApp.frame.setVisible(false);
            InterApp.frame.remove(actualPanel);
            InterApp.frame.add(menuDnt);
            InterApp.frame.setVisible(true);
        }
    }

    /**
     * Lista os doentes registados no sistema.
     * Imprime na consola uma tabela com os códigos e nomes dos doentes.
     * Caso não encontre nenhum doente registado, imprime uma mensagem apropriada.
     */
    public static void listaDoente() {
        InterApp.ListaDntsPanel.tableModel.setRowCount(0);
        for (User u : SistemaBD.users) {
            if ("doente".equalsIgnoreCase(u.getCategoria())){
                Doente doente = (Doente) u;
                String[] rowData = {doente.getCode(), doente.getNome(), doente.getDiagnostico(), String.valueOf(doente.getIdade())};
                InterApp.ListaDntsPanel.tableModel.addRow(rowData);
            }
        }
    }

    /**
     * Apaga um doente do sistema:
     * Pede ao user o código do doente a ser excluído e verifica se o doente existe.
     * Caso o doente exista, pede uma confirmação antes de apagar o doente.
     * Se o user confirmar, o doente é excluído do sistema.
     * Se o user não confirmar ou o código do doente não for válido, aparece uma mensagem de erro.
     * Depois do procedimento, o programa volta ao menu do admin.
     */
    public static void deleteDoente() {
        String CodeEnf = InterApp.DeleteDntPanel.codeField.getText();
        User u = SistemaBD.procuraUser(CodeEnf);
        if (u != null && "doente".equalsIgnoreCase(u.getCategoria())) {
            int result = JOptionPane.showConfirmDialog(null, "Tem a certeza? " + u.getNome() + " ( " + u.getCode() + " ) ", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (SistemaBD.deleteU(u.getCode())) {
                    JOptionPane.showMessageDialog(null, "Doente apagado com sucesso");
                    onAdmin();
                    InterApp.DeleteDntPanel.codeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao apagar. Introduza um código válido...");
                    onAdmin();
                    InterApp.DeleteDntPanel.codeField.setText("");
                }
            }else  {
                onAdmin();
                InterApp.DeleteDntPanel.codeField.setText("");
            }
        }else {
            JOptionPane.showMessageDialog(null, "Introduza um código válido.");
            onAdmin();
            InterApp.DeleteDntPanel.codeField.setText("");
        }
    }

    /**
     * Registra um novo doente no sistema.
     */
    public static void registDoente() {
        boolean CodeExiste = false;

        String nome = InterApp.RegistDntPanel.nomeField.getText();
        String apelido = InterApp.RegistDntPanel.apelidoField.getText();
        String diagnostico = RegistDntPanel.diagField.getText();
        int idade = parseInt(InterApp.RegistDntPanel.idadeField.getText());
        String user = InterApp.RegistDntPanel.userField.getText();
        String code = InterApp.RegistDntPanel.codeField.getText();

        Doente doente = new Doente(nome + " " + apelido, idade, code, user, code);
        doente.setDiagnostico(diagnostico);

        for (User u : SistemaBD.users){
            if (u.getCode().equalsIgnoreCase(doente.getCode())){
                CodeExiste = true;
            }
        }

        if (!CodeExiste){
            SistemaBD.saveUser(doente);
        }else {
            JOptionPane.showMessageDialog(null, "Código existente. Altere-o!");
        }


        if (SistemaBD.existeU(doente)) {
            JOptionPane.showMessageDialog(null, "Doente registado com sucesso");
            onAdmin();
        }
    }

    ////CONSULTAS
    /**
     * Marca uma consulta:
     * Pede informações da consulta.
     * Cria um objeto Consulta com as informações fornecidas.
     * Guarda a consulta no sistema.
     * Exibe uma mensagem de sucesso se a consulta for registrada com sucesso.
     */
    public static void marcarConsulta() { //Registar
        boolean medicoExiste = false;
        boolean doenteExiste = false;
        boolean CodeExiste = false;
        boolean HoraCheck;
        boolean DataaCheck;

        String paciente = MarcarConsltPanel.pacienteField.getText();
        String medico = MarcarConsltPanel.medicoField.getText();
        String descricao = MarcarConsltPanel.descField.getText();
        String data = MarcarConsltPanel.dataField.getText();
        String hora = MarcarConsltPanel.horaField.getText();
        String code = InterApp.MarcarConsltPanel.codeField.getText();

        Consulta consulta = new Consulta(descricao,data,hora,paciente,medico,code);

        for (User u : SistemaBD.users){
            if (u.getNome().equalsIgnoreCase(medico)){
                medicoExiste = true;
            }
            if (u.getNome().equalsIgnoreCase(paciente)){
                doenteExiste = true;
            }
        }

        for (Consulta c : SistemaBD.consultas){
            if (c.getCode().equalsIgnoreCase(consulta.getCode())){
                CodeExiste = true;
            }
        }

        if (!medicoExiste){
            JOptionPane.showMessageDialog(null, "Médico não encontrado. Registe-o!");
        }
        if (!doenteExiste){
            JOptionPane.showMessageDialog(null, "Doente não encontrado. Registe-o!");
        }
        if (CodeExiste){
            JOptionPane.showMessageDialog(null, "Código existente. Altere-o!");
        }
        HoraCheck = checkHora(hora);
        DataaCheck = checkData(data);

        if (medicoExiste && doenteExiste && !CodeExiste && HoraCheck && DataaCheck){
            SistemaBD.saveConsulta(consulta);
        }

        if (SistemaBD.existeC(consulta) && !CodeExiste && HoraCheck && DataaCheck) {
            JOptionPane.showMessageDialog(null, "Consulta registado com sucesso");
        }
    }

    /**
     * Apaga uma consulta do sistema:
     * Pede ao user o código da consulta a ser excluído e verifica se a consulta existe.
     * Caso a consulta exista, pede uma confirmação antes de apagar a consulta.
     * Se o user confirmar, a consulta é excluído do sistema.
     * Se o user não confirmar ou o código da consulta não for válido, aparece uma mensagem de erro.
     */
    public static void deleteConsulta() {
        String CodeConslt = InterApp.DeleteConsltPanel.codeField.getText();
        Consulta c = SistemaBD.procuraConsulta(CodeConslt);
        if (c != null) {
            int result = JOptionPane.showConfirmDialog(null, "Tem a certeza? " + c.getDescricao() + " para " + c.getPaciente() + " com " + c.getMedico(), "Confirmação", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (SistemaBD.deleteC(c.getCode())) {
                    JOptionPane.showMessageDialog(null, "Consulta apagada com sucesso");
                    InterApp.DeleteConsltPanel.codeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao apagar. Introduza um código válido...");
                    InterApp.DeleteConsltPanel.codeField.setText("");
                }
            }else  {
                InterApp.DeleteConsltPanel.codeField.setText("");
            }
        }else {
            JOptionPane.showMessageDialog(null, "Introduza um código válido.");
            InterApp.DeleteConsltPanel.codeField.setText("");
        }
    }

    /**
     * Lista as consultas por código.
     * O user introduz o código (de Médico ou Doente) e o sitema filtra as consultas com aquele código
     * Caso seja introduzido um código doutra categoria, é exibida uma mensagemd e erro.
     */
    public static void listarConsultasPorCodigo() {
        boolean consultasEncontradas = false;
        InterApp.tableModelConslt.setRowCount(0);
        User user = SistemaBD.procuraUser(ListaConsltsPanel.codeSearchField.getText());

        if (user != null) {
            for (Consulta consulta : SistemaBD.consultas) {
                if (user instanceof Doente && consulta.getPaciente().equals(user.getNome())) {
                    consultasEncontradas = true;

                    String[] rowData = {consulta.getCode(),consulta.getData(), consulta.getHora(), consulta.getDescricao(), consulta.getMedico()};
                    InterApp.tableModelConslt.addRow(rowData);
                } else if (user instanceof Medico && consulta.getMedico().equals(user.getNome())) {
                    consultasEncontradas = true;

                    String[] rowData = {consulta.getCode(),consulta.getData(), consulta.getHora(), consulta.getDescricao(), consulta.getPaciente()};
                    InterApp.tableModelConslt.addRow(rowData);
                }
            }
        }
        if (!consultasEncontradas) {
            JOptionPane.showMessageDialog(null, "Nenhuma consulta encontrada para o código " + ListaConsltsPanel.codeSearchField.getText());
            InterApp.tableModelConslt.setRowCount(0);
        }
    }


    //////////OUTROS

    /**
     * Pede ao user que insira o seu código para pdoer ser redirecionado para o menu correspondente.
     */
    public static void CodeOpts(String code) {
        if (((code.charAt(0)=='A' || code.charAt(0)=='a') && code.length()==6) && code.equals(SistemaBD.userlogin.getCode())){
            onAdmin();
        }else if (((code.charAt(0)=='M' || code.charAt(0)=='m') && code.length()==6) && code.equals(SistemaBD.userlogin.getCode())){
            onMed();
        }else if (((code.charAt(0)=='E' || code.charAt(0)=='e') && code.length()==6) && code.equals(SistemaBD.userlogin.getCode())){
            onEnf();
        }else if (((code.charAt(0)=='D' || code.charAt(0)=='d') && code.length()==6) && code.equals(SistemaBD.userlogin.getCode())){
            onDnt();
        }else{
            JOptionPane.showMessageDialog(null, "Insira um código válido. Verifique o seu código.");

        }
    }

    /**
     * Método para veirficar o formato da Hora
     * @param hora Hora inserida pelo user
     * @return true ou false consoante a hora respeite o formato de hora definido
     */
    public static boolean checkHora(String hora){

        if (hora.length() != 5 || hora.charAt(2)!=':'){
            JOptionPane.showMessageDialog(null, "Respeite o formato da hora [hh:mm]");
            return false;
        }else{
            int hour = Integer.parseInt(hora.substring(0, 2));
            int min = Integer.parseInt(hora.substring(3));
            if (hour>=22 || hour<8 && min>=0){
                JOptionPane.showMessageDialog(null, "Só são marcadas consultas entre as 08:00 e as 22:00");
                return false;
            }else {
                return true;
            }
        }
    }

    /**
     * Método para veirficar o formato da data
     * @param data Data inserida pelo user
     * @return true ou false consoante a data respeite o formato de data definido
     */
    public static boolean checkData(String data){

        if (data.length() != 10 || (data.charAt(2)!='/'||data.charAt(5)!='/')){
            JOptionPane.showMessageDialog(null, "Respeite o formato da data [dd/mm/aaaa]");
            return false;
        }else{
            int day = Integer.parseInt(data.substring(0, 2));
            int month = Integer.parseInt(data.substring(3, 5));
            int year = Integer.parseInt(data.substring(6));

            if ((day < 1 || day > 31) || (month < 1 || month > 12) || (year < 1)) {
                JOptionPane.showMessageDialog(null, "Data inválida");
                return false;
            } else {
                return true;
            }
        }
    }
}