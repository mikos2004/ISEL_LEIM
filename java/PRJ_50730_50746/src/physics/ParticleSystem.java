package physics;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe ParticleSystem.
 * Gerencia um sistema de partículas, permitindo criar, mover e renderizar partículas.
 */
public class ParticleSystem extends Mover{

    private List<Particle> particles;
    private float lifetime;
    private int particleColor;
    private int particlesPerSpawn = 1;
    private float spawnInterval = 1.0f;
    private float timeSinceLastSpawn = 0;
    private ParticleSystemControl particleSystemControl;

    /**
     * Construtor da classe ParticleSystem.
     * @param pos                   Posição inicial do sistema de partículas.
     * @param vel                   Velocidade inicial do sistema de partículas.
     * @param mass                  Massa do sistema.
     * @param radius                Raio das partículas geradas.
     * @param particleColor         Cor das partículas.
     * @param lifetime              Tempo de vida de cada partícula.
     * @param particleSystemControl Controle da dispersão e velocidade das partículas.
     */
    public ParticleSystem(PVector pos, PVector vel, float mass, float radius, int particleColor, float lifetime, ParticleSystemControl particleSystemControl) {
        super(pos, vel, mass, radius);
        this.particleColor = particleColor;
        this.lifetime = lifetime;
        this.particleSystemControl = particleSystemControl;
        this.particles = new ArrayList<Particle>();
    }

    /**
     * Define a posição e velocidade do sistema de partículas.
     * @param pos Posição do sistema.
     * @param vel Velocidade do sistema.
     */
    public void setParticleSystem(PVector pos, PVector vel) {
        setPos(pos);
        setVel(vel);
    }

    /**
     * Move o sistema e atualiza as partículas.
     * @param dt Intervalo de tempo para o movimento.
     */
    @Override
    public void move(float dt) {
        super.move(dt);
        timeSinceLastSpawn += dt;
        if (timeSinceLastSpawn >= spawnInterval) {
            for (int i = 0; i < particlesPerSpawn; i++) {
                addParticle();
            }
            timeSinceLastSpawn = 0;
        }
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.move(dt);
            if (particle.isDead()) {
                iterator.remove();
            }
        }
    }

    /**
     * Adiciona uma nova partícula ao sistema.
     */
    private void addParticle(){
        Particle particle = new  Particle(pos, particleSystemControl.getRandomVel(), radius, particleColor, lifetime);
        particles.add(particle);
    }

    /**
     * Exibe o sistema de partículas e todas as suas partículas na tela.
     * @param p   Objeto PApplet usado para renderização.
     * @param plt Objeto SubPlot usado para mapear coordenadas.
     */
    public void display(PApplet p, SubPlot plt){
        p.pushStyle();
        float [] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getDimInPixel(radius, radius);

        p.noStroke();
        p.fill(p.color(255));
        p.circle(pp[0], pp[1], 2*r[0]);
        p.popStyle();
        for (Particle particle : particles){
            particle.display(p, plt);
        }
    }

    /**
     * Obtém a lista de partículas.
     * @return Lista de partículas.
     */
    public List<Particle> getParticles() {
        return particles;
    }

    /**
     * Define a lista de partículas.
     * @param particles Lista de partículas.
     */
    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    /**
     * Obtém o número de partículas geradas por spawn.
     * @return Número de partículas por spawn.
     */
    public int getParticlesPerSpawn() {
        return particlesPerSpawn;
    }

    /**
     * Define o número de partículas geradas por spawn.
     * @param particlesPerSpawn Novo número de partículas por spawn.
     */
    public void setParticlesPerSpawn(int particlesPerSpawn) {
        this.particlesPerSpawn = particlesPerSpawn;
    }

    /**
     * Obtém o intervalo de spawn.
     * @return Intervalo de spawn em segundos.
     */
    public float getSpawnInterval() {
        return spawnInterval;
    }

    /**
     * Define o intervalo de spawn.
     * @param spawnInterval Novo intervalo de spawn em segundos.
     */
    public void setSpawnInterval(float spawnInterval) {
        this.spawnInterval = spawnInterval;
    }
}