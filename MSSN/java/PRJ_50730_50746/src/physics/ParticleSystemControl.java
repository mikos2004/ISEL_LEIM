package physics;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import processing.core.PVector;

/**
 * Classe ParticleSystemControl.
 * Gerencia os parâmetros de controle para dispersão e velocidade das partículas.
 */
public class ParticleSystemControl {

    private float anguloMedio;
    private float anguloDispersao;
    private float velMin;
    private float velMax;
    private float[] control;

    /**
     * Construtor da classe ParticleSystemControl.
     * @param velcontrol Array contendo os parâmetros de controle: [ângulo médio, ângulo de dispersão, velocidade mínima, velocidade máxima].
     */
    public ParticleSystemControl(float[] velcontrol){
        this.control = velcontrol;
        setVelControl(velcontrol);
    }

    /**
     * Obtém os parâmetros de controle.
     * @return Array contendo os parâmetros de controle.
     */
    public float[] getControl() {
        return control;
    }

    /**
     * Obtém o ângulo médio.
     * @return O ângulo médio.
     */
    public float getAnguloMedio() {
        return anguloMedio;
    }

    /**
     * Define o ângulo médio.
     * @param anguloMedio Novo ângulo médio.
     */
    public void setAnguloMedio(float anguloMedio) {
        this.anguloMedio = anguloMedio;
    }

    /**
     * Obtém o ângulo de dispersão.
     * @return O ângulo de dispersão.
     */
    public float getAnguloDispersao() {
        return anguloDispersao;
    }

    /**
     * Define o ângulo de dispersão.
     * @param anguloDispersao Novo ângulo de dispersão.
     */
    public void setAnguloDispersao(float anguloDispersao) {
        this.anguloDispersao = anguloDispersao;
    }

    /**
     * Define os parâmetros de controle de velocidade e dispersão.
     * @param velcontrol Array contendo os parâmetros: [ângulo médio, ângulo de dispersão, velocidade mínima, velocidade máxima].
     */
    public void setVelControl(float[] velcontrol){
        anguloMedio = velcontrol[0];
        anguloDispersao = velcontrol[1];
        velMin = velcontrol[2];
        velMax = velcontrol[3];
    }

    /**
     * Gera uma velocidade aleatória com base nos parâmetros de controle.
     * @return Vetor representando a velocidade aleatória gerada.
     */
    public PVector getRandomVel(){
        // "anguloDispersao/2" porque considerando anguloDispersao como a dispersao total,
        // deste modo temos a dispersao para cada lado
        float angle =  getRandom(anguloMedio - anguloDispersao/2, anguloMedio + anguloDispersao/2);
        PVector v = PVector.fromAngle(angle);
        return  v.mult(getRandom(velMin, velMax));
    }

    /**
     * Gera um valor aleatório dentro de um intervalo.
     * @param min Valor mínimo do intervalo.
     * @param max Valor máximo do intervalo.
     * @return Valor aleatório gerado.
     */
    public static float getRandom(float min, float max){
        return min + (float) (Math.random() * (max - min));
    }
}