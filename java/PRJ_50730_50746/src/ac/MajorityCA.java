package ac;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import processing.core.PApplet;
import tools.SubPlot;

/**
 * Classe MajorityCA.
 * Implementa um automato celular baseado na regra da maioria, onde as celulas atualizam os seus estados
 * com base na frequencia dos estados dos vizinhos.
 */
public class MajorityCA extends CellularAutomata{

    /**
     * Construtor da classe MajorityCA.
     * @param p       Objeto PApplet usado para desenhar.
     * @param plt     Subplot associado ao automato celular.
     * @param row     Numero de linhas.
     * @param col     Numero de colunas.
     * @param nStates Numero de estados possiveis para cada celula.
     * @param raio    Raio da vizinhança de cada celula.
     */
    public MajorityCA(PApplet p, SubPlot plt, int row, int col, int nStates, int raio) {
        super(p, plt, row, col, nStates, raio);
    }

    /**
     * Cria as celulas no automato.
     */
    @Override
    protected void createCells(){
        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                cells[i][j] = new MajorityCell(this, i, j);
            }
        }
        setVizinhanca();
    }

    /**
     * Aplica a regra da maioria para atualizar os estados das celulas.
     * Primeiro, calcula-se o histograma dos estados dos vizinhos para cada celula.
     * Depois, aplica-se a regra da maioria para decidir o novo estado.
     * @return true se houver mudancas nos estados das celulas, false caso contrario.
     */
    public boolean majorityRule(){
        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                ((MajorityCell) cells[i][j]).computeHistogram();
            }
        }
        boolean mudanca = false;
        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                boolean changed = ((MajorityCell) cells[i][j]).applyMajorityRule();
                if (changed) mudanca = true;
            }
        }
        return mudanca;
    }
}