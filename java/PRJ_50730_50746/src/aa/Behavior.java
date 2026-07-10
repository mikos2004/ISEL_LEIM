package aa;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

/**
 * Classe Behavior.
 * Classe abstrata para representar um comportamento associado a um Boid.
 */
public abstract class Behavior implements IBehavior {

    protected float weight;

    /**
     * Construtor da classe Behavior.
     * @param weight Peso associado ao comportamento.
     */
    public Behavior(float weight){
        this.weight = weight;
    }


    /**
     * Define o peso do comportamento.
     * @param weight Novo peso do comportamento.
     */
    @Override
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Obtém o peso do comportamento.
     * @return Peso atual do comportamento.
     */
    @Override
    public float getWeight() {
        return weight;
    }
}
