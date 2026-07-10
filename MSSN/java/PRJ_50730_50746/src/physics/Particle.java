package physics;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

/**
 * Classe Particle.
 * Representa uma partícula que se move e possui um tempo de vida (lifespan).
 */
public class Particle  extends Mover{

    private float lifespan;
    private int color;
    private float timer;
    private float alpha;

    /**
     * Construtor da classe Particle.
     * @param pos      Vetor que representa a posição inicial da partícula.
     * @param vel      Vetor que representa a velocidade inicial da partícula.
     * @param radius   O raio da partícula.
     * @param color    A cor da partícula.
     * @param lifespan A duração de vida da partícula.
     */
    public Particle(PVector pos, PVector vel, float radius, int color, float lifespan) {
        super(pos, vel, 0f, radius);
        this.color = color;
        this.lifespan = lifespan;
        timer = 0;
    }

    /**
     * Move a partícula e atualiza o seu temporizador de vida.
     * @param dt Intervalo de tempo para o movimento.
     */
    public void move(float dt){
        super.move(dt);
        timer += dt;
    }

    /**
     * Verifica se a partícula está morta com base no seu tempo de vida.
     * @return true se a partícula morreu, false caso contrário.
     */
    public boolean isDead(){
        return timer > lifespan;
    }

    /**
     * Exibe a partícula na tela.
     * @param p   Objeto PApplet usado para renderização.
     * @param plt Objeto SubPlot usado para mapear coordenadas.
     */
    public void display(PApplet p, SubPlot plt){
        p.pushStyle();
        float[] bb = plt.getBoundingBox();
        float [] pp = plt.getPixelCoord(pos.x, pos.y);
        float [] r = plt.getDimInPixel(radius, radius);

        if (pp[0] < bb[0] || pp[0] > bb[0] + bb[2] ||
                pp[1] < bb[1] || pp[1] > bb[1] + bb[3]) {
            alpha = 0;
        }else{
            alpha = PApplet.map(timer, 0, lifespan, 255, 0);
        }

        p.fill(color, alpha);
        p.noStroke();
        p.circle(pp[0], pp[1],2*r[0]);
        p.popStyle();
    }
}
