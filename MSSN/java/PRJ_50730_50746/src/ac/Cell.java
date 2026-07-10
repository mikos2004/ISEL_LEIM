package ac;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Classe Cell.
 * Representa uma celula no automato celular.
 */
public class Cell {

    private int linha;
    private int coluna;
    protected int state;
    private Cell[] vizinhos;
    private int raioVizinhanca = 1;
    protected CellularAutomata ca;
    private PImage cellImg;

    /**
     * Construtor da classe Cell.
     * @param ca     Automato celular ao qual a celula pertence.
     * @param linha  Linha da celula.
     * @param coluna Coluna da celula.
     */
    public Cell(CellularAutomata ca, int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
        this.state = 0;
        this.vizinhos = new Cell[(int) Math.pow(2* raioVizinhanca +1,2)];
        this.ca = ca;
        this.cellImg = null;
    }

    /**
     * Exibe a representacao grafica da celula.
     * @param p Objeto PApplet usado para desenhar.
     */
    public void display(PApplet p) {
        p.pushStyle();
        p.noStroke();
        if (cellImg == null){
            setCellImg();
        }
        p.image(cellImg, ca.xmin + coluna * ca.cellLargura, ca.ymin +  linha * ca.cellAltura, ca.cellLargura, ca.cellAltura);
        p.popStyle();
    }

    /**
     * Obtém o estado atual da celula.
     * @return O estado atual.
     */
    public int getState() {
        return state;
    }

    /**
     * Define o estado da celula.
     * @param state Novo estado da celula.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Define a imagem da celula com base no estado.
     */
    public void setCellImg() {
        this.cellImg = ca.p.loadImage(ca.getImageSrcs()[state]);
    }

    /**
     * Obtém os vizinhos da celula.
     * @return Um array de celulas representando os vizinhos.
     */
    public Cell[] getVizinhos() {
        return vizinhos;
    }

    /**
     * Define os vizinhos da celula.
     * @param vizinhos Array de celulas vizinhas.
     */
    public void setVizinhos(Cell[] vizinhos) {
        this.vizinhos = vizinhos;
    }
}