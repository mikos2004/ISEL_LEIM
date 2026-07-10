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
 * Classe Body.
 * Representa um corpo físico com propriedades como massa, raio e posição.
 */
public class Body extends Mover {

    private int color;
    private static double G = 6.67e-11; //constante Gravitacional
    protected float radius;

    /**
     * Construtor da classe Body
     * @param pos      Vetor representando a posição inicial do body.
     * @param vel      Vetor representando a velocidade inicial do body.
     * @param mass     A massa do body.
     * @param radius   O raio do body.
     * @param color    A cor do body.
     */
    public Body(PVector pos, PVector vel, float mass, float radius, int color) {
        super(pos, vel, mass, radius);
        this.color = color;
        this.radius = radius;
    }

    public Body(PVector pos) {
        super(pos, new PVector(), 0f, 0.8f);
    }

    /**
     * Calcula o vetor de atração gravitacional entre o corpo e outro objeto.
     * @param m O objeto do tipo Mover para o qual a força gravitacional é calculada.
     * @return O vetor de atração gravitacional.
     */
    public PVector attraction(Mover m){
        PVector r = PVector.sub(pos, m.pos);
        float dist = r.mag();
        float strength = (float) ((G * mass * m.mass) / Math.pow(dist, 2));
        return r.normalize().mult(strength);
    }

    /**
     * Representacao grafica do body.
     * @param p   Objeto PApplet usado para desenhar.
     * @param plt Objeto SubPlot usado para mapear as coordenadas.
     */
    public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getVectorCoord(radius, radius);

        p.noStroke();
        p.fill(this.color);
        p.circle(pp[0], pp[1], 2 * r[0]);
        p.popStyle();
    }

    /**
     * Obtém o raio do corpo.
     * @return O valor do raio.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Obtém a cor do corpo.
     * @return A cor do corpo.
     */
    public int getColor() {
        return color;
    }

    /**
     * Define a cor do corpo.
     * @param color A nova cor do corpo.
     */
    public void setColor(int color) {
        this.color = color;
    }
}
