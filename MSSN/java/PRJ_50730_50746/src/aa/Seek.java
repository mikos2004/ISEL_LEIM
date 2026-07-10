package aa;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import physics.Body;
import processing.core.PVector;

/**
 * Classe Seek.
 * Representa o comportamento de busca, em que o Boid tenta se mover em direção a um alvo.
 */
public class Seek extends Behavior{

    /**
     * Construtor da classe Seek.
     * @param weight Peso associado a este comportamento.
     */
    public Seek(float weight) {
        super(weight);
    }

    /**
     * Calcula e retorna a velocidade desejada para o comportamento de busca.
     * @param me Boid que esta executando o comportamento.
     * @return Vetor de velocidade desejada para se mover em direcao ao alvo.
     */
    @Override
    public PVector getDesiredVelocity(Boid me){
        Body bodyTarget = me.eye.target;
        return PVector.sub(bodyTarget.getPos(), me.getPos());
    }
}
