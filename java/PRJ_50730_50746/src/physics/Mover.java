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
 * Classe Mover.
 * Representa um objeto que pode se mover sob influência de forças físicas.
 */
public class Mover {
    protected PVector pos;
    protected PVector vel;
    protected PVector acc;
    protected float mass;
    protected float radius;

    /**
     * Construtor da classe Mover.
     * @param pos    Vetor que representa a posição inicial.
     * @param vel    Vetor que representa a velocidade inicial.
     * @param mass   A massa do objeto.
     * @param radius O raio do objeto.
     */
    public Mover(PVector pos, PVector vel, float mass, float radius) {
        this.pos = pos.copy();
        this.vel = vel;
        this.mass = mass;
        this.radius = radius;
        this.acc = new PVector();
    }

    /***
     * Função para aplicar um vetor de Força no corpo/objeto
     * @param f Vetor que representa a Força
     */
    public void applyForce(PVector f) {
        acc.add(PVector.div(f, mass));
    }

    /**
     * Move o objeto com base na sua aceleração e velocidade.
     * @param dt O intervalo de tempo.
     */
    public void move(float dt){
        acc.mult(dt);
        vel.add(acc);
        pos.add(PVector.mult(vel,dt));
        acc.mult(0);
    }

    /**
     * Representação gráfica do objeto. Deve ser implementada por subclasses.
     * @param p   Objeto PApplet usado para renderização.
     * @param plt Objeto SubPlot usado para mapear coordenadas.
     */
    public void display(PApplet p, SubPlot plt){};

    /***
     * Retorna o vetor da posição
     * @return pos - Vetor da posição
     */
    public PVector getPos() {
        return pos;
    }

    /***
     * Altera o vetor da posição
     * @param pos Vetor com o valor da posição pretendida
     */
    public void setPos(PVector pos) {
        this.pos = pos;
    }

    /***
     * Retorna o vetor da velocidade
     * @return vel - Vetor da velocidade
     */
    public PVector getVel() {
        return vel;
    }

    /***
     * Altera o vetor da velociddde
     * @param vel Vetor com o valor da velocidade pretendida
     */
    public void setVel(PVector vel) {
        this.vel = vel;
    }

    /***
     * Retorna o valor da massa
     * @return mass - Valor da massa
     */
    public float getMass() {
        return mass;
    }

    /***
     * Altera o valor da massa
     * @param mass Valor da massa pretendida
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /***
     * Retorna o valor do raio do corpo
     * @return radius - Valor do raio do corpo
     */
    public float getRadius() {
        return radius;
    }

    /***
     * Altera o valor do raio do corpo
     * @param radius Valor do raio do corpo
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }
}
