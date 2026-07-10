package aa;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import game.Player;
import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Flock.
 * Gere um grupo de Boids que executam apenas o comportamento Seek.
 */
public class Flock {

    private List<Boid> boidList;
    private PVector followTarget;
    private Seek seek;
    private String type;

    /**
     * Construtor da classe Flock.
     * @param nboids      Número de Boids no grupo.
     * @param mass        Massa de cada Boid.
     * @param radius      Raio de cada Boid.
     * @param color       Cor dos Boids.
     * @param sacWeights  Pesos para os comportamentos (apenas o índice do Seek é usado).
     * @param p           Instância do PApplet.
     * @param plt         SubPlot para posicionamento.
     * @param img_src     Caminho para a imagem dos Boids.
     * @param type        Tipo do grupo (ex.: "bat", "zombie").
     */
    public Flock(int nboids, float mass, float radius, int color, float[] sacWeights, PApplet p, SubPlot plt, String img_src, String type) {
        double[] w = plt.getWindow();
        followTarget = new PVector();
        boidList = new ArrayList<>();
        this.type = type;

        // Calcular o centro da janela
        float centerX = (float) ((w[0] + w[1]) / 2);
        float centerY = (float) ((w[2] + w[3]) / 2);

        // Calcular a largura e altura das áreas para distribuição
        float flockWidth = (float) ((w[1] - w[0]) / 2);
        float flockHeight = (float) ((w[3] - w[2]) / 2);

        // Gerar posições para o primeiro grupo
        float startX = p.random((float) w[0], (float) (w[0] + flockWidth));
        float startY = p.random((float) w[2], (float) w[3]);

        for (int i = 0; i < nboids; i++) {
            float x, y;

            // Distribuir as posições dos boids
            x = p.random(startX - 20, startX + 20);
            y = p.random(startY - 20, startY + 20);

            // Verificar se a posição está dentro de um raio de 20px do centro
            while (PVector.dist(new PVector(x, y), new PVector(centerX, centerY)) < 20) {
                x = p.random(startX - 20, startX + 20);
                y = p.random(startY - 20, startY + 20);
            }
            Boid boid = new Boid(new PVector(x, y), new PVector(), mass, radius, color, p, plt);
            boid.setShape(p.loadShape(img_src)); // Atribui a imagem para cada boid
            seek = new Seek(sacWeights[3]);
            boid.addBehavior(seek);
            boidList.add(boid);
        }
        List<Body> bodyList = boidList2BodyList(boidList);
        for (Boid boid : boidList) {
            boid.setEye(new Eye(boid, bodyList));
        }
    }

    /**
     * Converte uma lista de Boids numa lista de corpos.
     * @param boidList Lista de Boids.
     * @return Lista de objetos Body correspondente.
     */
    private List<Body> boidList2BodyList(List<Boid> boidList) {
        List<Body> bodyList = new ArrayList<>();
        for (Boid boid : boidList) {
            bodyList.add(boid);
        }
        return bodyList;
    }

    /**
     * Obtem um Boid da lista pelo indice.
     * @param i Indice do Boid na lista.
     * @return Boid correspondente.
     */
    public Boid getBoid(int i) {
        return boidList.get(i);
    }

    /**
     * Aplica o comportamento Seek a todos os Boids do grupo.
     * @param dt Intervalo de tempo.
     */
    public void applyBehavior(float dt) {
        for (Boid boid : boidList) {
            boid.applyBehaviors(dt);
        }
    }

    /**
     * Exibe todos os Boids do grupo na tela.
     * @param p   Instancia de PApplet para renderização.
     * @param plt SubPlot associado.
     * @param c   Escala de largura do Boid.
     * @param d   Escala de altura do Boid.
     */
    public void display(PApplet p, SubPlot plt, float c, float d) {
        for (Boid b : boidList) {
            b.display(p, plt, c, d);
        }
    }

    /**
     * Obtem a lista de Boids.
     * @return Lista de Boids.
     */
    public List<Boid> getBoidList() {
        return boidList;
    }

    /**
     * Define a lista de Boids.
     * @param boidList Nova lista de Boids.
     */
    public void setBoidList(List<Boid> boidList) {
        this.boidList = boidList;
    }

    /**
     * Obtem o tipo do grupo de Boids.
     * @return Tipo do grupo.
     */
    public String getType() {
        return type;
    }

    /**
     * Define o tipo do grupo de Boids.
     * @param type Novo tipo do grupo.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Adiciona o comportamento de caça ao grupo de Boids.
     * @param player             Jogador a ser caçado.
     * @param allTrackingBodies  Lista de corpos que os Boids podem rastrear.
     * @param numBoids           Número de Boids.
     */
    public void Hunt(Player player, List allTrackingBodies, int numBoids) {
        float[] weights = {0f, 0f, 0f, 2f, 0f};
        for (int i = 0; i < boidList.size(); i++) {
            Boid predador = this.getBoid(i);
            predador.addBehavior(new Seek(weights[3]));
            allTrackingBodies.add(player);
            Eye eyeBat = new Eye(predador, allTrackingBodies);
            predador.setEye(eyeBat);
        }
    }
}
