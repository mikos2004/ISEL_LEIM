package ac;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import tools.Histogram;

/**
 * Classe MajorityCell.
 * Representa uma celula que aplica a regra da maioria para atualizar seu estado.
 */
public class MajorityCell extends Cell{
    private Histogram hist;

    /**
     * Construtor da classe MajorityCell
     * @param ca    Automato celular ao qual a cell pertence.
     * @param linha Linha da cell.
     * @param coluna Coluna da cell.
     */
    public MajorityCell(CellularAutomata ca, int linha, int coluna) {
        super(ca, linha, coluna);
    }

    /**
     * Calcula o histograma dos estados dos vizinhos da celula.
     * Utiliza o estado de cada vizinho para construir um histograma de frequencias.
     */
    public void computeHistogram(){
        Cell[] vizinhos = getVizinhos();
        int[] data = new int[vizinhos.length];
        for (int i=0; i<vizinhos.length; i++){
            data[i] = vizinhos[i].getState();
        }
        hist = new Histogram(data, ca.nStates);
    }

    /**
     * Aplica a regra da maioria, atualizando o estado da cell se necessario.
     * O novo estado da celula passa a ser o estado mais frequente entre os vizinhos.
     * @return true se o estado da celula foi alterado, false caso contrario.
     */
    public boolean applyMajorityRule(){
        int mode = hist.getMode(0);
        boolean changed = false;
        if (getState() != mode){
            setState(mode);
            setCellImg();
            changed = true;
        }
        return changed;
    }
}
