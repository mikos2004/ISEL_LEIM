package tps.tp4;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

import javax.swing.*;

/**
 * Class que simula uma base de dados e as suas funções.
 */
public class SistemaBD {
    public static User[] users = new User[] {
            new Admin("António Carvalho", 19, "A00001", "acarvalho", "pwd"),
            new Admin("Maria Lopes", 65, "A00002", "mlopes", "pwd2"),
            new Medico("Miguel Kleber", 45, "M50746", "mkleber","potterharry") {{
                setEspecialidade("Cardiologia");
            }},
            new Medico("Tiago Holmes", 64, "M00421", "tholmes", "watsonjonh") {{
                setEspecialidade("Neurologia");
            }},
            new Enfermeiro("João Machado", 32, "E76421", "jmachado", "kissbang") {{
                setArea("Urgências");
            }},
            new Doente("João Tosse", 15, "D70021", "jtosse", "rebucado")
            {{
                setDiagnostico("Tosse");
            }}
    };


    public static Consulta[] consultas = new Consulta[] {
            new Consulta("Consulta de Rotina","21/01/2023","17:30", "João Tosse", "Miguel Kleber", "C69520"),
            new Consulta("Exame aos olhos","13/05/2023","21:00", "João Tosse", "Tiago Holmes", "C49422"),

    };

    public static User userlogin;


    /**
     *Guarda um user no array e atualiza a informação.
     *@param user O user a ser guardado.
     */
    public static void saveUser(User user) {
        User[] lUsers = new User[users.length + 1];
        for(int i=0; i<users.length; i++) {
            lUsers[i] = users[i];
        }

        lUsers[lUsers.length - 1] = user;
        setUsers(lUsers);
    }

    /**
     *Guarda uma consulta no array e atualiza a informação.
     *@param consulta A consulta a ser guardado.
     */
    public static void saveConsulta(Consulta consulta) {
        Consulta[] lConsulta = new Consulta[consultas.length + 1];
        for(int i=0; i<consultas.length; i++) {
            lConsulta[i] = consultas[i];
        }

        lConsulta[lConsulta.length - 1] = consulta;
        setConsultas(lConsulta);
    }


    /**
     *Atualiza o array de users.
     *@param lUsers O novo array de users atualizado.
     */
    private static void setUsers(User[] lUsers) {
        users = lUsers;
    }

    /**
     *Atualiza o array de consultas.
     *@param lConsultas O novo array de users consultas.
     */
    private static void setConsultas(Consulta[] lConsultas) {
        consultas = lConsultas;
    }


    /**
     * Verifica se um user existe.
     *
     * @param user O user a ser testado.
     * @return true se o user existe, false caso contrário.
     */
    public static boolean existeU(User user) {
        for(User u : users) {
            if((u.getUser().equals(user.getUser())) && (u.getPass().equals(user.getPass()))) return true;
        }
        return false;
    }

    /**
     * Procura, guarda e retorna o user que fez login.
     *
     * @param user O user que se vai procurar na lista de users.
     * @return O User que corresponde aos dados de login, ou null se não for encontrado.
     */
    public static User getLoginUser(User user) {
        for(User u : users) {
            if((u.getUser().equals(user.getUser())) && (u.getPass().equals(user.getPass()))){
                guardaUser(u);
                return u;
            }
        }
        return null;
    }

    /**
     * Guarda o user para uso posterior.
     *
     * @param user O usuário a ser armazenado.
     */
    private static void guardaUser(User user){
        userlogin=user;
    }

    /**
     * Verifica se um user existe, pelo código do user.
     *
     * @param code O user a ser testado.
     * @return true se o user existe, false caso contrário.
     */
    public static User existeUcode(String code) {
        User ret = null;
        for(User u : users) {
            if((u.getCode().equals(code))) {
                ret = u;
            }
        }

        if  (ret!=null){
            return ret;
        }else {
            JOptionPane.showMessageDialog(null, "Código Inválido");
            return null;
        }
    }


    /**
     * Verifica se uma consulta existe.
     *
     * @param consulta A consulta a ser testado.
     * @return true se a consulta existe, false caso contrário.
     */
    public static boolean existeC(Consulta consulta) {
        for(Consulta c : consultas) {
            if((c.getCode().equals(consulta.getCode()))) return true;
        }
        return false;
    }


    /**
     * Verifica se um user existe, por um código.
     *
     * @param code O user a ser testado.
     * @return true se o user existe, false caso contrário.
     */
    public static boolean existeSU(String code) {
        for(User u : users) {
            if(code.equals(u.getCode())) return true;
        }
        return false;
    }

    /**
     * Verifica se uma consulta existe, por um código.
     *
     * @param code A consulta a ser testada.
     * @return true se a consulta existe, false caso contrário.
     */
    public static boolean existeSC(String code) {
        for(Consulta c : consultas) {
            if(code.equals(c.getCode())) return true;
        }
        return false;
    }

    /**
     *Procura um user com base no código fornecido.
     *@param code O código do user a ser procurado.
     *@return O User correspondente ao código fornecido, ou null se nenhum user for encontrado.
     */
    public static User procuraUser(String code) {
        for(User u : users) {
            if(code.equals(u.getCode())) return u;
        }
        return null;
    }

    /**
     *Procura uma consulta com base no código fornecido.
     *@param code O código da consulta a ser procurada.
     *@return A Consulta correspondente ao código fornecido, ou null se nenhuma consulta for encontrada.
     */
    public static Consulta procuraConsulta(String code) {
        for(Consulta c : consultas) {
            if(code.equals(c.getCode())) return c;
        }
        return null;
    }

    /**
     *Procura um user pelo nome.
     *@param nome O nome do user a ser procurado.
     *@return O User correspondente ao nome fornecido, ou null se nenhum user for encontrado.
     */
    public static User procuraUserNome(String nome) {
        for(User u : users) {
            if(nome.equals(u.getNome())) return u;
        }
        return null;
    }

    /**
     *Apaga um user com base no seu código.
     *@param code O código do user a ser apagdo.
     *@return true se o user foi apagado, false caso contrário.
     */
    public static boolean deleteU(String code) {
        if(existeSU(code)) {
            User[] lusers = new User[users.length - 1];
            for(int i=0, j=0; i<users.length; i++) {
                if(!code.equals(users[i].getCode())) {
                    lusers[j++] = users[i];
                }
            }
            setUsers(lusers);
            return true;
        }
        return false;
    }

    /**
     *Apaga uma consulta com base no seu código.
     *@param code O código da consulta a ser apagda.
     *@return true se a consulta foi apagada, false caso contrário.
     */
    public static boolean deleteC(String code) {
        if(existeSC(code)) {
            Consulta[] lconsulta = new Consulta[consultas.length - 1];
            for(int i=0, j=0; i<consultas.length; i++) {
                if(!code.equals(consultas[i].getCode())) {
                    lconsulta[j++] = consultas[i];
                }
            }
            setConsultas(lconsulta);
            return true;
        }
        return false;
    }

    /**
    *Faz merge dos users do XML com os users existentes.
    *@param usersFromXML um array de Users do XML a ser submetido ao merge
    */
    public static void mergeUsers(User[] usersFromXML) {
        User[] mergedUsers = new User[users.length + usersFromXML.length];

        // Copia os users existentes para o novo array
        System.arraycopy(users, 0, mergedUsers, 0, users.length);

        // Copia os users do XML para o novo array
        System.arraycopy(usersFromXML, 0, mergedUsers, users.length, usersFromXML.length);

        // Atualiza o array users com os users do merge
        setUsers(mergedUsers);
    }

    /**
     *Faz merge das consultas do XML com as consultas existentes.
     *@param consultasFromXML um array de Consultas do XML a ser submetido ao merge
     */
    public static void mergeConsultas(Consulta[] consultasFromXML) {
        Consulta[] mergedConsultas = new Consulta[consultas.length + consultasFromXML.length];

        // Copia os users existentes para o novo array
        System.arraycopy(consultas, 0, mergedConsultas, 0, consultas.length);

        // Copia os users do XML para o novo array
        System.arraycopy(consultasFromXML, 0, mergedConsultas, consultas.length, consultasFromXML.length);

        // Atualiza o array users com os users do merge
        setConsultas(mergedConsultas);
    }
}
