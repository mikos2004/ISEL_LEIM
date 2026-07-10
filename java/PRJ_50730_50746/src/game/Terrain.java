package game;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import ac.MajorityCA;
import processing.core.PApplet;
import tools.SubPlot;

/**
 * Classe Terrain.
 * Representa o terreno do jogo, que herda as caracteristicas de MajorityCA e adiciona funcionalidades especificas.
 */
public class Terrain extends MajorityCA {

    /**
     * Construtor da classe Terrain.
     * @param p   Referencia ao objeto PApplet, usado para renderizaçao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do terreno.
     */
    public Terrain(PApplet p, SubPlot plt) {
        super(p, plt, WorldConstants.LINHAS, WorldConstants.COLUNAS, WorldConstants.NSTATES, 1);
    }

    /**
     * Cria as celulas do terreno usando o metodo padrao de MajorityCA.
     */
    @Override
    protected void createCells() {
        // Usa o método padrão de MajorityCA para criar as células
        super.createCells();
    }
}