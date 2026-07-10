package game;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import aa.Boid;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import tools.SubPlot;

/**
 * Classe Entity.
 * Representa uma entidade que herda as caracteristicas de um Boid e possui um alvo especifico.
 */
public class Entity extends Boid implements IEntity{

    private Entity target;
    protected PShape img;

    /**
     * Construtor da classe Animal.
     * @param pos    Posição inicial do animal.
     * @param mass   Massa do animal.
     * @param radius Raio do animal.
     * @param color  Cor do animal.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao do animal.
     */
    protected Entity(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, new PVector(), mass, radius, color, p, plt);
    }

    /**
     * Exibe a representacao grafica do animal.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do animal.
     */
    @Override
    public void display(PApplet p, SubPlot plt) {
        p.pushMatrix(); // guarda sistema de coordenadas
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.shape(this.img, pp[0] - 15f, pp[1] - 15f, 35f, 35f); // Draw the image
        p.popMatrix();
    }

    /**
     * Obtem o alvo da entidade.
     * @return O alvo da entidade.
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Define o alvo da entidade.
     * @param e Nova entidade alvo.
     */
    public void setTarget(Entity e) {
        this.target = e;
    }

    /**
     * Obtem a imagem associada à entidade.
     * @return A imagem associada à entidade.
     */
    public PShape getImg() {
        return img;
    }

    /**
     * Define a imagem da entidade a partir de um caminho especificado.
     * @param parent  Referencia ao objeto PApplet usado para carregar a imagem.
     * @param imgPath Caminho para o arquivo da imagem.
     */
    public void setImg(PApplet parent, String imgPath) {
        this.img =parent.loadShape(imgPath);
    }
}