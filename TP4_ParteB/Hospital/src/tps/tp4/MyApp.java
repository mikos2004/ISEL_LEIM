package tps.tp4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class MyApp extends JFrame {
    private JLabel background;

    public MyApp() {
        setTitle("My App");
        setSize(1024, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Configurar a imagem de fundo
        background = new JLabel();
        background.setIcon(new ImageIcon("src/tps/tp4/Selec_LoginMenu.png")); // Substitua pelo caminho da imagem desejada
        add(background);

        // Configurar os botões
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));

        JButton adminButton = new JButton("Login Adminstrador");
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAdminMode();
            }
        });
        buttonPanel.add(adminButton);

        JButton medButton = new JButton("Login Médico");
        medButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startMedMode();
            }
        });
        buttonPanel.add(medButton);

        JButton enfButton = new JButton("Login Enfermeiro");
        enfButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startEnfMode();
            }
        });
        buttonPanel.add(enfButton);

        JButton dntButton = new JButton("Login Doente");
        dntButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startDntMode();
            }
        });
        buttonPanel.add(dntButton);

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyApp();
            }
        });
    }

    /*public void start() {
        // Ler a opção selecionada
        op = lerOpts();

        switch (op) {
            case 1:
                startAdminMode();
                break;
            case 2:
                startMedMode();
                break;
            case 3:
                startEnfMode();
                break;
            case 4:
                startDntMode();
                break;
            default:
                System.out.println("default");
                break;
        }
    }*/

    public void AppOpts() {
        // Exibir opções na linha de comando (apenas para referência)
        System.out.println("[1] -> Login Administrador");
        System.out.println("[2] -> Login Médico");
        System.out.println("[3] -> Login Enfermeiro");
        System.out.println("[4] -> Login Doente");
        System.out.println("----------------------------------------");
    }

    public static int lerOpts() {
        System.out.print("Opção desejada: ");
        return scanner().nextInt();
    }

    public static Scanner scanner() {
        return new Scanner(System.in);
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
     *
     * @return opção guardada
     */
    /*public int getOpt() {
        return op;
    }*/
}


