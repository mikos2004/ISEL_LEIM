package aa;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

/**
 * Classe DNA.
 * Contém os parâmetros genéticos que definem o comportamento de um Boid.
 */
public class DNA {
    public float maxSpeed;
    public float maxForce;
    public float visionDistance;
    public float visionSafeDistance;
    public float visionAngle;
    public float deltaTPursuit;
    public float radiusArrive;
    public float deltaTWander;
    public float radiusWander;
    public float deltaPhiWander;

    /**
     * Construtor da classe DNA.
     * Inicializa os parâmetros genéticos com valores aleatórios.
     */
    public DNA() {
        //Physics
        maxSpeed = random(1f, 2.5f);
        maxForce = random(4f, 7f);
        //Vision
        visionDistance = random(1.5f, 3f);
        visionSafeDistance = 0.25f * visionDistance;
        visionAngle = (float)  Math.PI * 0.35f; //visão de 360
        //Pursuit
        deltaTPursuit = random(0.5f, 1f);
        //Arrive
        radiusArrive = random(3, 5);
        //Wander
        deltaTWander = random(.3f, .6f);//0.5f, 0.7f
        radiusWander = random(1f, 3f); //2, 4
        deltaPhiWander = (float) (Math.PI/8); //(Math.PI/8)
    }

    /**
     * Gera um numero aleatorio dentro do intervalo especificado.
     * @param min Valor minimo do intervalo.
     * @param max Valor maximo do intervalo.
     * @return Numero aleatorio dentro do intervalo especificado.
     */
    public static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }

    /**
     * Define a velocidade máxima.
     * @param maxSpeed Nova velocidade máxima.
     */
    public void setMaxSpeed(float maxSpeed){
        this.maxSpeed = maxSpeed;
    }

    /**
     * Obtém a velocidade máxima.
     * @return Velocidade máxima atual.
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }
}
