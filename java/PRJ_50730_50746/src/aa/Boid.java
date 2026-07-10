package aa;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import physics.Body;
import processing.core.*;
import tools.SubPlot;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Boid.
 * Representa um agente autonomo que pode executar comportamentos num ambiente simulado.
 */
public class Boid extends Body {

    private SubPlot plt;
    public PShape shape;
    protected List<Behavior> behaviors;
    protected DNA adn;
    protected Eye eye;
    protected float phiWander;
    private float sumWeights;

    /**
     * Construtor da classe Boid.
     * @param pos     Posição inicial do Boid.
     * @param pVector Vetor inicial associado ao Boid (não usado diretamente).
     * @param mass    Massa do Boid.
     * @param radius  Raio do Boid.
     * @param color   Cor do Boid.
     * @param p       Instância de PApplet para renderização gráfica.
     * @param plt     SubPlot associado ao Boid.
     */
    public Boid(PVector pos, PVector pVector, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, new PVector(), mass, radius, color);
        adn = new DNA();
        behaviors = new ArrayList<Behavior>();
        this.plt = plt;
        setShape(p, plt, color);
    }

    /**
     * Obtém a lista de comportamentos associados ao Boid.
     * @return Lista de comportamentos.
     */
    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    /**
     * Define o olho do Boid.
     * @param eye Objeto Eye associado ao Boid.
     */
    public void setEye(Eye eye){
        this.eye = eye;
    }

    /**
     * Obtém o olho do Boid.
     * @return Objeto Eye associado ao Boid.
     */
    public Eye getEye(){return this.eye;}

    /**
     * Define o DNA do Boid.
     * @param adn Objeto DNA representando as características geneticas do Boid.
     */
    public void setDna(DNA adn){
        this.adn = adn;
    }

    /**
     * Obtem o DNA do Boid.
     * @return Objeto DNA que representa as caracteristicas geneticas do Boid.
     */
    public DNA getDna(){return this.adn;}

    /**
     * Define a forma grafica do Boid.
     * @param p     Instancia de PApplet para renderização grafica.
     * @param plt   SubPlot associado ao Boid.
     * @param color Cor do Boid.
     */
    public void setShape(PApplet p, SubPlot plt, int color) {
        float[] rr = plt.getVectorCoord(radius, radius);
        shape = p.createShape();
        shape.beginShape();
        shape.vertex(-rr[0], rr[0] / 2);
        shape.vertex(rr[0], 0);
        shape.vertex(-rr[0], -rr[0] / 2);
        shape.vertex(-rr[0] / 2, 0);
        shape.fill(color);
        shape.endShape(PConstants.CLOSE);
    }

    /**
     * Atualiza a soma dos pesos dos comportamentos associados ao Boid.
     */
    private void updateSumWeights(){
        sumWeights = 0;
        for(Behavior behavior : behaviors){
            sumWeights += behavior.getWeight();
        }
    }

    /**
     * Adiciona um comportamento a lista de comportamentos do Boid.
     * @param behavior Comportamento a ser adicionado.
     */
    public void addBehavior(Behavior behavior){
        behaviors.add(behavior);
        updateSumWeights();
    }

    /**
     * Remove um comportamento da lista de comportamentos do Boid.
     * @param behavior Comportamento a ser removido.
     */
    public void removeBehavior(Behavior behavior){
        if (behaviors.contains(behavior))
            behaviors.remove(behavior);
        updateSumWeights();
    }

    /**
     * Aplica um comportamento especifico ao Boid com base no indice do comportamento.
     * @param i  Indice do comportamento a ser aplicado.
     * @param dt Intervalo de tempo.
     */
    public void applyBehavior(int i, float dt){
        if (eye!=null){
            eye.look();
        }
        Behavior behavior = behaviors.get(i);
        PVector vd = behavior.getDesiredVelocity(this);
        move(dt, vd, plt.getWindow());
    }

    /**
     * Aplica todos os comportamentos associados ao Boid.
     * @param dt Intervalo de tempo.
     */
    public void applyBehaviors(float dt){
        if (eye!=null){
            eye.look();
        }
        PVector vd = new PVector();
        for(Behavior behavior : behaviors){
            PVector vdd = behavior.getDesiredVelocity(this);
            vdd.mult(behavior.getWeight()/sumWeights);
            vd.add(vdd);
        }
        move(dt, vd, plt.getWindow());
    }

    /**
     * Move o Boid com base na velocidade desejada e verifica se esta dentro dos limites da janela.
     * @param dt     Intervalo de tempo.
     * @param vd     Velocidade desejada.
     * @param window  Array com os limites da janela.
     */
    public void move(float dt, PVector vd, double[] window){

        vd.normalize().mult(adn.maxSpeed);
        PVector fs = PVector.sub(vd, vel);
        applyForce(fs.limit(adn.maxForce));
        super.move(dt);

        // Verifica se o boid está fora dos limites da janela
        if (pos.x >= window[1]) {
            pos.x = (float) window[0];
        } else if (pos.x <= window[0]) {
            pos.x = (float) window[1];
        }
        if (pos.y >= window[3]) {
            pos.y = (float) window[2];
        } else if (pos.y <= window[2]) {
            pos.y = (float) window[3];
        }
    }

    /**
     * Exibe o Boid na tela.
     * @param p   Instancia de PApplet para renderizacao grafica.
     * @param plt SubPlot associado ao Boid.
     */
    public void display(PApplet p, SubPlot plt, float c, float d) {
        p.pushMatrix(); // guarda sistema de coordenadas
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.shape(this.getShape(), pp[0] - 15f, pp[1] - 15f, c, c); // Draw the image
        p.popMatrix();
    }

    /**
     * Realiza a mutacao dos comportamentos do Boid, especificamente para o comportamento de evitar obstaculos.
     */
    public void mutateBehaviors(){
        for (Behavior b: behaviors){
            if (b instanceof AvoidObstacle){
                b.weight += DNA.random(-0.5f, 0.5f);
                b.weight = Math.max(0, b.weight);
            }
        }
        updateSumWeights();
    }

    /**
     * Define a forma  gráfica do Boid.
     * @param shape Objeto PShape que representa a nova forma do Boid.
     */
    public void setShape(PShape shape) {
        this.shape = shape;
    }

    /**
     * Obtém a forma gráfica do Boid.
     * @return Objeto PShape que representa a forma atual do Boid.
     */
    public PShape getShape() {
        return shape;
    }
}