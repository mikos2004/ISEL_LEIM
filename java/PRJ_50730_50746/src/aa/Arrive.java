package aa;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import processing.core.PVector;

/**
 * Classe Arrive.
 * Representa o comportamento de um Boid para desacelerar e parar ao se aproximar de um alvo.
 */
public class Arrive extends Behavior {

    /**
     * Construtor da classe Arrive.
     * @param weight Peso associado ao comportamento de chegada.
     */
    public Arrive(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para que o Boid desacelere ao se aproximar de um alvo.
     * @param me O Boid que executa o comportamento.
     * @return Vetor de velocidade desejada.
     */
    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector targetPos = me.eye.target.getPos();
        PVector vd = PVector.sub(targetPos, me.getPos()); // Direção para o alvo
        float dist = vd.mag(); // Distância ao alvo
        float slowingRadius = 4.5f; // Raio para desaceleração
        if (dist < slowingRadius) {
            // Reduz gradualmente a velocidade conforme a distância ao alvo
            float desiredSpeed = me.getDna().getMaxSpeed() * (dist / slowingRadius);
            vd.setMag(desiredSpeed);
        } else {
            // Fora do raio, velocidade máxima
            vd.setMag(me.getDna().getMaxSpeed());
        }
        // Subtrai a velocidade atual para produzir uma aceleração suave
        vd.sub(me.getVel());
        return vd;
    }
}
