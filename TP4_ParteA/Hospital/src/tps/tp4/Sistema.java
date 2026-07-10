package tps.tp4;

import java.util.Scanner;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

/**
 * Class que representa um sistema dum hosptial.
 * Ao ser estendida da class Login, o sistema funcionára consoante o login do user.
 */
public class Sistema extends Login {

/**
 * Executa as operações disponíveis para um admin.
 * Se o login for bem sucedido, é apresentado o menu do admin e
 * executa a operação selecionada.
 */
 public void onAdmin() {

        if (login()) {
            CmdSistema.AdminOpts();
            int opcao = CmdSistema.lerOpts();
            System.out.println("----------------------------------------\n");
            switch (opcao) {
                case 1:
                    registAdmin();
                    break;
                case 2:
                    deleteAdmin();
                    break;
                case 3:
                    listaAdmins();
                    break;
                case 4:
                    registMedico();
                    break;
                case 5:
                    deleteMedico();
                    break;
                case 6:
                    listaMedicos();
                    break;
                case 7:
                    registEnfermeiro();
                    break;
                case 8:
                    deleteEnfermeiro();
                    break;
                case 9:
                    listaEnfermeiros();
                    break;
                case 10:
                    registDoente();
                    break;
                case 11:
                    deleteDoente();
                    break;
                case 12:
                    listaDoente();
                    break;
                case 13:
                    listarConsultasPorCodigo();
                    break;
                case 14:
                    marcarConsulta();
                    break;
                case 15:
                    deleteConsulta();
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.print("\nEscolha uma opção válida ");
                    onAdmin();
            }

        }

    }

    /**
     * Lista os admin registados no sistema.
     * Imprime na consola uma tabela com os códigos e nomes dos administradores.
     * Caso não encontre nenhum administrador registado, imprime uma mensagem apropriada.
     */
    private void listaAdmins() {
        boolean topo = true; //bool para fazer o topo da tabela
        boolean AdminExiste = false; // Indica se encontra algum admin
        for (User u : SistemaBD.users) {
            if ("admin".equalsIgnoreCase(u.getCategoria())) {
                AdminExiste = true;
                Admin admin = (Admin) u;
                if (topo) {
                    System.out.println("ADMINISTRADORES REGISTADOS ");
                    System.out.println("\t\t--------------------------------------------------------------------------");
                    System.out.println("\t	|	CÓDIGO		|	NOME		|");
                    System.out.println("\t\t--------------------------------------------------------------------------");
                    topo = false;
                }

                String code;

                if (admin.getCode().length() < 10) {
                    code = admin.getCode() + "\t";
                } else {
                    code = admin.getCode();
                }

                String nome;

                if (admin.getNome().length()<16){
                    nome = admin.getNome() + "\t";
                }else {
                    nome = admin.getNome();
                }

                System.out.println("\t	|	" + code + "		|	" + nome + "	|");
                System.out.println("\t\t--------------------------------------------------------------------------");
            }
        }

        if (!AdminExiste) {
            System.out.println("Administrador não encontrado");
        }
        CodeOpts();
    }

    /**
     * Apaga um admin do sistema:
     * Pede ao user (admin) o código do admin a ser excluído e verifica se o admin existe.
     * Caso o admin exista, pede uma confirmação antes de apagar o admin.
     * Se o user confirmar, o admin é excluído do sistema.
     * Se o user não confirmar ou o código do admin não for válido, aparece uma mensagem de erro.
     * Depois do procedimento, o programa volta ao menu do admin.
     */
    private void deleteAdmin() {
        Scanner scan = CmdSistema.scanner();
        System.out.print("Digite o código do administrador a apagar: ");
        String CodeAdmin = scan.next();
        User u = SistemaBD.procuraUser(CodeAdmin);
        if (u != null && "admin".equalsIgnoreCase(u.getCategoria())) {
            System.out.println("Tem a certeza? " + u.getNome() + "( " + u.getCode() + " ) : [S/N] ? ");
            String e = scan.next();
            if ("s".equalsIgnoreCase(e) || "sim".equalsIgnoreCase(e)) {
                if (SistemaBD.deleteU(u.getCode())) {
                    System.out.println("Administrador apagado com sucesso");
                } else {
                    System.out.println("Erro ao apagar. Introduza um código válido...");
                }
            }
        }
        onAdmin();
    }

    /**
     * Registra um novo admin no sistema.
     */
    private void registAdmin() {
        Scanner scan = CmdSistema.scanner();
        System.out.println("-----------------------------------------");
        System.out.println("************* REGISTAR ADMIN ************");
        System.out.println("-----------------------------------------");
        System.out.println("-------------------------------------------------------------------");
        System.out.print("| Nome  : ");
        String nome = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Apelido  : ");
        String apelido = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Idade : ");
        int idade = scan.nextInt();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  User : ");
        String user = scan.next();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Código (AXXXXX): ");
        String code = scan.next();
        System.out.println("-------------------------------------------------------------------");

        Admin admin = new Admin(nome + " " + apelido, idade, code, user, code);
        //admin.setIdade(idade);

        SistemaBD.saveUser(admin);

        if (SistemaBD.existeU(admin)) {
            System.out.println("Administrador registado com sucesso");
        }

        onAdmin();

    }

    ///////////////////////////
    //MÉDICOS

    /**
     * Executa as operações disponíveis para um médico.
     * Se o login for bem sucedido, é apresentado o menu do médico e
     * executa a operação selecionada.
     */
    public void onMed() {

        if (login()) {
            CmdSistema.MedOpts();
            int opcao = CmdSistema.lerOpts();
            System.out.println("----------------------------------------\n");
            switch (opcao) {
                case 1:
                    listaMedicos();
                    break;
                case 2:
                    listaDoente();
                    break;
                case 3:
                    listaEnfermeiros();
                    break;
                case 4:
                    listarConsultasPorCodigo();
                    break;
                case 5:
                    marcarConsulta();
                    break;
                case 6:
                    deleteConsulta();
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.print("\nEscolha uma opção válida ");
                    onMed();
            }

        }

    }

    /**
     * Lista os médicos registados no sistema.
     * Imprime na consola uma tabela com os códigos e nomes dos médicos.
     * Caso não encontre nenhum médico registado, imprime uma mensagem apropriada.
     */
    private void listaMedicos() {
        boolean topo = true, medExiste = false;
        for (User u : SistemaBD.users) {
            if ("medico".equalsIgnoreCase(u.getCategoria())) {
                medExiste = true;
                Medico medico = (Medico) u;
                if (topo) {
                    System.out.println("MÉDICOS REGISTADOS ");
                    System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
                    System.out.println("\t	|	CÓDIGO		|	NOME		|	ESPECIALIDADE	|	IDADE	|");
                    System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
                    topo = false;
                }

                String code;

                if (medico.getCode().length() < 10) {
                    code = medico.getCode() + "\t";
                } else {
                    code = medico.getCode();
                }

                String nome;

                if (medico.getNome().length()<16){
                    nome = medico.getNome() + "\t";
                }else {
                    nome = medico.getNome();
                }

                System.out.println("\t	|	" + code + "		|	" + nome + "	|	" + medico.getEspecialidade() + "	|	" + medico.getIdade() + "		|");
                System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
            }
        }

        if (!medExiste) {
            System.out.println("Médicos não encontrados");
        }
        CodeOpts();
    }

    /**
     * Apaga um médico do sistema:
     * Pede ao user o código do médico a ser excluído e verifica se o médico existe.
     * Caso o médico exista, pede uma confirmação antes de apagar o médico.
     * Se o user confirmar, o médico é excluído do sistema.
     * Se o user não confirmar ou o código do médico não for válido, aparece uma mensagem de erro.
     * Depois do procedimento, o programa volta ao menu do admin.
     */
    private void deleteMedico() {
        Scanner scan = CmdSistema.scanner();
        System.out.print("Digite o código do médico a apagar: ");
        String CodeMed = scan.next();
        User u = SistemaBD.procuraUser(CodeMed);
        if (u != null && "medico".equalsIgnoreCase(u.getCategoria())) {
            System.out.println("Tem a certeza? " + u.getNome() + "( " + u.getCode() + " ) : [S/N] ? ");
            String e = scan.next();
            if ("s".equalsIgnoreCase(e) || "sim".equalsIgnoreCase(e)) {
                if (SistemaBD.deleteU(u.getCode())) {
                    System.out.println("Médico apagado com sucesso");
                } else {
                    System.out.println("Erro ao apagar. Introduza um código válido...");
                }
            }
        }
        onAdmin();
    }

    /**
     * Registra um novo médico no sistema.
     */
    private void registMedico() {
        Scanner scan = CmdSistema.scanner();
        System.out.println("-----------------------------------------");
        System.out.println("********** REGISTAR MÉDICO *********");
        System.out.println("-----------------------------------------");
        System.out.println("-------------------------------------------------------------------");
        System.out.print("| Nome  : ");
        String nome = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Apelido  : ");
        String apelido = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Especialidade : ");
        String especialidade = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Idade (int) : ");
        int idade = scan.nextInt();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  User : ");
        String user = scan.next();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Código (MXXXXX): ");
        String code = scan.next();
        System.out.println("-------------------------------------------------------------------");

        Medico medico = new Medico(nome + " " + apelido, idade, code, user, code);
        medico.setEspecialidade(especialidade);

        SistemaBD.saveUser(medico);

        if (SistemaBD.existeU(medico)) {
            System.out.println("Médico registado com sucesso");
        }

        onAdmin();

    }


    ///////////////////////////////
    //Enfermeiro

    /**
     * Executa as operações disponíveis para um enfermeiro.
     * Se o login for bem sucedido, é apresentado o menu do enfermeiro e
     * executa a operação selecionada.
     */
    public void onEnf() {

        if (login()) {
            CmdSistema.EnfOpts();
            int opcao = CmdSistema.lerOpts();
            System.out.println("----------------------------------------\n");
            switch (opcao) {
                case 1:
                    listaEnfermeiros();
                    break;
                case 2:
                    listaDoente();
                    break;
                case 3:
                    listaMedicos();
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.print("\nEscolha uma opção válida ");
                    onEnf();
            }

        }

    }

    /**
     * Lista os enfermeiro registados no sistema.
     * Imprime na consola uma tabela com os códigos e nomes dos enfermeiro.
     * Caso não encontre nenhum enfermeiro registado, imprime uma mensagem apropriada.
     */
    private void listaEnfermeiros() {
        boolean topo = true, EnfExiste = false;
        for (User u : SistemaBD.users) {
            if ("enfermeiro".equalsIgnoreCase(u.getCategoria())) {
                EnfExiste = true;
                Enfermeiro enfermeiro = (Enfermeiro) u;
                if (topo) {
                    System.out.println("ENFERMEIROS REGISTADOS ");
                    System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
                    System.out.println("\t	|	CÓDIGO		|	NOME		|	ÁREA	|	IDADE	|");
                    System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
                    topo = false;
                }

                String code;

                if (enfermeiro.getCode().length() < 10) {
                    code = enfermeiro.getCode() + "\t";
                } else {
                    code = enfermeiro.getCode();
                }

                String nome;

                if (enfermeiro.getNome().length()<16){
                    nome = enfermeiro.getNome() + "\t";
                }else {
                    nome = enfermeiro.getNome();
                }

                System.out.println("\t	|	" + code + "		|	" + nome + "	|	" + enfermeiro.getArea() + "	|	" + enfermeiro.getIdade() + "		|");
                System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
            }
        }

        if (!EnfExiste) {
            System.out.println("Enfermeiros não encontrados");
        }
        CodeOpts();
    }

    /**
     * Apaga um enfermeiro do sistema:
     * Pede ao user o código do enfermeiro a ser excluído e verifica se o enfermeiro existe.
     * Caso o enfermeiro exista, pede uma confirmação antes de apagar o enfermeiro.
     * Se o user confirmar, o enfermeiro é excluído do sistema.
     * Se o user não confirmar ou o código do enfermeiro não for válido, aparece uma mensagem de erro.
     * Depois do procedimento, o programa volta ao menu do admin.
     */
    private void deleteEnfermeiro() {
        Scanner scan = CmdSistema.scanner();
        System.out.print("Digite o código do enfermeiro a apagar: ");
        String CodeEnf = scan.next();
        User u = SistemaBD.procuraUser(CodeEnf);
        if (u != null && "enfermeiro".equalsIgnoreCase(u.getCategoria())) {
            System.out.println("Tem a certeza? " + u.getNome() + "( " + u.getCode() + " ) : [S/N] ? ");
            String e = scan.next();
            if ("s".equalsIgnoreCase(e) || "sim".equalsIgnoreCase(e)) {
                if (SistemaBD.deleteU(CodeEnf)) {
                    System.out.println("Enfermeiro apagado com sucesso");
                } else {
                    System.out.println("Erro ao apagar. Introduza um código válido...");
                }
            }
        }
        onAdmin();
    }

    /**
     * Registra um novo enfermeiro no sistema.
     */
    private void registEnfermeiro() {
        Scanner scan = CmdSistema.scanner();
        System.out.println("-----------------------------------------");
        System.out.println("********** REGISTAR ENFERMEIRO *********");
        System.out.println("-----------------------------------------");
        System.out.println("-------------------------------------------------------------------");
        System.out.print("| Nome  : ");
        String nome = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Apelido  : ");
        String apelido = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Área : ");
        String area = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Idade : ");
        int idade = scan.nextInt();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  User : ");
        String user = scan.next();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Código (EXXXXX): ");
        String code = scan.next();
        System.out.println("-------------------------------------------------------------------");

        Enfermeiro enfermeiro = new Enfermeiro(nome + " " + apelido, idade, code, user, code);
        enfermeiro.setArea(area);


        SistemaBD.saveUser(enfermeiro);

        if (SistemaBD.existeU(enfermeiro)) {
            System.out.println("Enferimro registado com sucesso");
        }

        onAdmin();

    }

    ///////////////////////////////
    //Doente

    /**
     * Executa as operações disponíveis para um doente.
     * Se o login for bem sucedido, é apresentado o menu do doente e
     * executa a operação selecionada.
     */
    public void onDnt() {

        if (login()) {
            CmdSistema.DntOpts();
            int opcao = CmdSistema.lerOpts();
            System.out.println("----------------------------------------\n");
            switch (opcao) {
                case 1:
                    listaMedicos();
                    break;
                case 2:
                    listarConsultasPorCodigo();
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.print("\nEscolha uma opção válida ");
                    onDnt();
            }

        }

    }

    /**
     * Lista os doentes registados no sistema.
     * Imprime na consola uma tabela com os códigos e nomes dos doentes.
     * Caso não encontre nenhum doente registado, imprime uma mensagem apropriada.
     */
    private void listaDoente() {
        boolean topo = true, DoentExiste = false;
        for (User u : SistemaBD.users) {
            if ("doente".equalsIgnoreCase(u.getCategoria())) {
                DoentExiste = true;
                Doente doente = (Doente) u;
                if (topo) {
                    System.out.println("DOENTES REGISTADOS ");
                    System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
                    System.out.println("\t	|	CÓDIGO		|	   NOME		|		DIAGNÓSTICO		|	IDADE	|");
                    System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
                    topo = false;
                }

                String code;

                if (doente.getCode().length() < 10) {
                    code = doente.getCode() + "\t";
                } else {
                    code = doente.getCode();
                }

                String nome;

                if (doente.getNome().length()<16){
                    nome = doente.getNome() + "\t";
                }else {
                    nome = doente.getNome();
                }

                System.out.println("\t	|	" + code + "		|	" + nome + "	|	" + doente.getDiagnostico() + "	|	" + doente.getIdade() + "		|");
                System.out.println("\t\t-----------------------------------------------------------------------------------------------------------------");
            }
        }

        if (!DoentExiste) {
            System.out.println("Doentes não encontrados");
        }
        CodeOpts();
    }

    /**
     * Apaga um doente do sistema:
     * Pede ao user o código do doente a ser excluído e verifica se o doente existe.
     * Caso o doente exista, pede uma confirmação antes de apagar o doente.
     * Se o user confirmar, o doente é excluído do sistema.
     * Se o user não confirmar ou o código do doente não for válido, aparece uma mensagem de erro.
     * Depois do procedimento, o programa volta ao menu do admin.
     */
    private void deleteDoente() {
        Scanner scan = CmdSistema.scanner();
        System.out.print("Digite o código do doente a apagar: ");
        String CodeEnf = scan.next();
        User u = SistemaBD.procuraUser(CodeEnf);
        if (u != null && "doente".equalsIgnoreCase(u.getCategoria())) {
            System.out.println("Tem a certeza? " + u.getNome() + "( " + u.getCode() + " ) : [S/N] ? ");
            String e = scan.next();
            if ("s".equalsIgnoreCase(e) || "sim".equalsIgnoreCase(e)) {
                if (SistemaBD.deleteU(CodeEnf)) {
                    System.out.println("Doente apagado com sucesso");
                } else {
                    System.out.println("Erro ao apagar. Introduza um código válido...");
                }
            }
        }
        onAdmin();
    }

    /**
     * Registra um novo doente no sistema.
     */
    private void registDoente() {
        Scanner scan = CmdSistema.scanner();
        System.out.println("-----------------------------------------");
        System.out.println("********** REGISTAR DOENTE *********");
        System.out.println("-----------------------------------------");
        System.out.println("-------------------------------------------------------------------");
        System.out.print("| Nome  : ");
        String nome = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Apelido  : ");
        String apelido = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Diagnóstico : ");
        String diagnóstico = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Idade : ");
        int idade = scan.nextInt();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  User : ");
        String user = scan.next();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Código (DXXXXX): ");
        String code = scan.next();
        System.out.println("-------------------------------------------------------------------");

        Doente doente = new Doente(nome + " " + apelido, idade, code, user, code);
        doente.setDiagnostico(diagnóstico);

        SistemaBD.saveUser(doente);

        if (SistemaBD.existeU(doente)) {
            System.out.println("Doente registado com sucesso");
        }

        onAdmin();
    }

    ////CONSULTAS
    /**
     * Marca uma consulta:
     * Pede informações da consulta.
     * Cria um objeto Consulta com as informações fornecidas.
     * Guarda a consulta no sistema.
     * Exibe uma mensagem de sucesso se a consulta for registrada com sucesso.
     */
    private void marcarConsulta() { //Registar
        Scanner scan = CmdSistema.scanner();
        System.out.println("-----------------------------------------");
        System.out.println("************ MARCAR CONSULTA ************");
        System.out.println("-----------------------------------------");
        System.out.println("-------------------------------------------------------------------");
        System.out.print("| Paciente  : ");
        String nPaciente = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Médico  : ");
        String nMedico = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Descrição : ");
        String descricao = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Data : ");
        String data = scan.nextLine();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Hora : ");
        String hora = scan.next();
        System.out.println("-------------------------------------------------------------------");
        System.out.print("|  Código (CXXXXX): ");
        String code = scan.next();
        System.out.println("-------------------------------------------------------------------");

        Consulta consulta = new Consulta(descricao, data, hora, nPaciente, nMedico, code);

        SistemaBD.saveConsulta(consulta);

        if (SistemaBD.existeC(consulta)) {
            System.out.println("Consulta registado com sucesso");
        }

        CodeOpts();
    }

    /**
     * Apaga uma consulta do sistema:
     * Pede ao user o código da consulta a ser excluído e verifica se a consulta existe.
     * Caso a consulta exista, pede uma confirmação antes de apagar a consulta.
     * Se o user confirmar, a consulta é excluído do sistema.
     * Se o user não confirmar ou o código da consulta não for válido, aparece uma mensagem de erro.
     */
    private void deleteConsulta() {
        Scanner scan = CmdSistema.scanner();
        System.out.print("Digite o código da consulta a desmarcar: ");
        String CodeConslt = scan.next();
        Consulta c = SistemaBD.procuraConsulta(CodeConslt);
        if (c != null) {
            System.out.println("Tem a certeza? " + c.getDescricao() + "para " + c.getPaciente() + " com " + c.getMedico() + " : [S/N] ? ");
            String e = scan.next();
            if ("s".equalsIgnoreCase(e) || "sim".equalsIgnoreCase(e)) {
                if (SistemaBD.deleteC(CodeConslt)) {
                    System.out.println("Consulta apagada com sucesso");
                } else {
                    System.out.println("Erro ao apagar. Introduza um código válido...");
                }
            }
        }
        CodeOpts();
    }

    /**
     * Lista as consultas por código.
     * O user introduz o código (de Médico ou Doente) e o sitema filtra as consultas com aquele código
     * Caso seja introduzido um código doutra categoria, é exibida uma mensagemd e erro.
     */
    private void listarConsultasPorCodigo() {
        Scanner scan = CmdSistema.scanner();
        System.out.print("Insira o código para o filtro desejado: ");
        String codigo = scan.next();

        boolean consultasEncontradas = false;

        String categoria = "";

        if (codigo.charAt(0)=='d' || codigo.charAt(0)=='D'){
            categoria = "MÉDICO";
        }else if (codigo.charAt(0)=='M' || codigo.charAt(0)=='m'){
            categoria = "DOENTE";
        }else{
            System.out.print("Insira um código válido: ");
        }

        System.out.println("---- CONSULTAS DO PACIENTE/MÉDICO COM O CÓDIGO " + codigo + " --------------\n");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("|  DATA    |     HORA    |   DESCRIÇÃO    |   "+categoria);
        System.out.println("---------------------------------------------------------------------");

        User user = SistemaBD.procuraUser(codigo);

        if (user != null) {
            for (Consulta consulta : SistemaBD.consultas) {
                if (user instanceof Doente && consulta.getPaciente().equals(user.getNome())) {
                    consultasEncontradas = true;

                    String data = consulta.getData();
                    String hora = consulta.getHora();
                    String descricao = consulta.getDescricao();
                    String nomeMedico = consulta.getMedico();

                    System.out.println("| " + data + " | " + hora + " | " + descricao + " | " + nomeMedico);
                    System.out.println("---------------------------------------------------------------------");
                } else if (user instanceof Medico && consulta.getMedico().equals(user.getNome())) {
                    consultasEncontradas = true;

                    String data = consulta.getData();
                    String hora = consulta.getHora();
                    String descricao = consulta.getDescricao();
                    String nomePaciente = consulta.getPaciente();

                    System.out.println("| " + data + " | " + hora + " | " + descricao + " | " + nomePaciente);
                    System.out.println("---------------------------------------------------------------------");
                }
            }
        }

        System.out.println("---------------------------------------------------------------------");

        if (!consultasEncontradas) {
            System.out.println("Nenhuma consulta encontrada para o código " + codigo);
        }

        CodeOpts();
    }


    //////////OUTROS

    /**
     * Pede ao user que insira o seu código para pdoer ser redirecionado para o menu correspondente.
     */
    public void CodeOpts() {
        Scanner scan = CmdSistema.scanner();
        System.out.print("Selecione o seu código: ");
        String code = scan.next();

        if (((code.charAt(0)=='A' || code.charAt(0)=='a') && code.length()==6) && code.equals(SistemaBD.userlogin.getCode())){
            onAdmin();
        }else if (((code.charAt(0)=='M' || code.charAt(0)=='m') && code.length()==6) && code.equals(SistemaBD.userlogin.getCode())){
            onMed();
        }else if (((code.charAt(0)=='E' || code.charAt(0)=='e') && code.length()==6) && code.equals(SistemaBD.userlogin.getCode())){
            onEnf();
        }else if (((code.charAt(0)=='D' || code.charAt(0)=='d') && code.length()==6) && code.equals(SistemaBD.userlogin.getCode())){
            onDnt();
        }else{
            System.out.print("Insira um código válido.\n");
            CodeOpts();
        }
    }
}