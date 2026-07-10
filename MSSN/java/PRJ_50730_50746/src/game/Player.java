package game;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import aa.Arrive;
import aa.Eye;
import ac.Cell;
import physics.Body;
import physics.ParticleSystem;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Player.
 * Representa o jogador no jogo, que herda as caracteristicas de uma entidade e pode interagir com o terreno e outros elementos.
 */
public class Player extends Entity {

    private PApplet parent;
    private GameApp gameApp;
    private SubPlot plt;
    private Body b;

    private int lvl = 1;
    private int hp = 20;
    private int xp = 0;
    private int maxHp = 20;
    private int maxXp = 10;
    private boolean update = false;

    /**
     * Construtor da classe Player.
     * @param pos    Posicao inicial do jogador.
     * @param mass   Massa do jogador.
     * @param radius Raio do jogador.
     * @param color  Cor do jogador.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao do jogador.
     */
    protected Player(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, mass, radius, color, p, plt);
        this.parent = p;
        this.plt = plt;
        this.img = p.loadShape(WorldConstants.PLAYER_SRC);

        getDna().setMaxSpeed(WorldConstants.PLAYER_SPEED);
        this.addBehavior(new Arrive(1f));
        b = new Body(pos.copy(), new PVector(), 1f, 0f, p.color(0));
        ArrayList<Body> allTrackingBodies = new ArrayList<Body>();
        allTrackingBodies.add(b);
        this.setEye(new Eye(this, allTrackingBodies));
    }

    /**
     * Ajusta a velocidade do jogador com base no tipo de terreno.
     */
    public void adjustSpeedBasedOnTerrain() {
        if (gameApp == null) return; // Garante que o GameApp foi configurado

        Terrain terrain = gameApp.getTerrain();
        Cell currentCell = terrain.world2Cell(pos.x, pos.y);

        if (currentCell.getState() == WorldConstants.PatchType.OBSTACLE.ordinal() ||
                currentCell.getState() == WorldConstants.PatchType.MUD.ordinal() ||
                currentCell.getState() == WorldConstants.PatchType.WATER.ordinal()) {
            getDna().setMaxSpeed(WorldConstants.PLAYER_SPEED / 2);
        } else {
            getDna().setMaxSpeed(WorldConstants.PLAYER_SPEED);
        }
    }

    /**
     * Aplica o comportamento do jogador com ajuste de velocidade baseado no terreno.
     * @param idx Indice do comportamento a ser aplicado.
     * @param dt  Intervalo de tempo.
     */
    @Override
    public void applyBehavior(int idx, float dt) {
        adjustSpeedBasedOnTerrain();
        super.applyBehavior(idx, dt);
    }

    /**
     * Configura a referencia ao objeto GameApp.
     * @param gameApp Instancia do GameApp.
     */
    public void setGameApp(GameApp gameApp) {
        this.gameApp = gameApp;
    }


    /**
     * Atualiza a posicao do corpo associado ao jogador com base nas coordenadas do mouse.
     */
    public void playerMouse(){
        double[] w = plt.getWorldCoord(parent.mouseX, parent.mouseY);
        b.setPos(new PVector((float) w[0], (float) w[1]));
    }

    /**
     * Configura os comportamentos e o alvo do jogador.
     * @param player                  Jogador.
     * @param parent                  Referencia ao objeto PApplet.
     * @param plt                     SubPlot associado.
     * @param target                  Alvo inicial do jogador.
     * @param allTrackingBodiesPlayer Lista de corpos rastreados pelo jogador.
     */
    public void setupPlayer(Player player, PApplet parent, SubPlot plt, Entity target, List<Body> allTrackingBodiesPlayer){
        player.addBehavior(new Arrive(1f));
        target = new Entity(new PVector(), 1f, 0.2f, parent.color(250, 0, 0), parent, plt);
        player.setTarget(target);

        allTrackingBodiesPlayer = new ArrayList<Body>();
        allTrackingBodiesPlayer.add(target);
        Eye eye = new Eye(player, allTrackingBodiesPlayer);
        player.setEye(eye);
    }

    /**
     * Obtém os pontos de vida do jogador.
     * @return Pontos de vida atuais.
     */
    public int getHp() {
        return hp;
    }

    /**
     * Define os pontos de vida do jogador.
     * @param hp Novos pontos de vida.
     */
    public void setHp(int hp) {
        if (hp <= 0){
            hp = 0;
        }
        this.hp = hp;
    }

    /**
     * Obtém o valor máximo de pontos de vida.
     * @return Valor máximo de pontos de vida.
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Define o valor máximo de pontos de vida.
     * @param maxHp Novo valor máximo de pontos de vida.
     */
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * Obtém a experiência atual do jogador.
     * @return Experiência atual.
     */
    public int getXp() {
        return xp;
    }

    /**
     * Define a experiência atual do jogador, respeitando o limite por nível.
     * @param xp Nova experiência.
     */
    public void setXp(int xp) {
        if (lvl < 15) {
            if (xp >= this.getMaxXp()) {
                this.xp = this.getMaxXp();
            } else {
                this.xp = xp;
            }
        } else {
            this.xp = this.getMaxXp();
        }
    }

    /**
     * Obtém o valor máximo de experiência para o nível atual.
     * @return Valor máximo de experiência.
     */
    public int getMaxXp() {
        return maxXp;
    }

    /**
     * Define o valor máximo de experiência.
     * @param maxXp Novo valor máximo de experiência.
     */
    public void setMaxXp(int maxXp) {
        this.maxXp = maxXp;
    }

    /**
     * Obtém o nível atual do jogador.
     * @return Nível atual.
     */
    public int getLvl() {
        return lvl;
    }

    /**
     * Define o nível atual do jogador.
     * @param lvl Novo nível.
     */
    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    /**
     * Calcula o valor máximo de experiência para o próximo nível.
     * @param lvl Nível atual.
     * @return Valor máximo de experiência para o próximo nível.
     */
    public int calcMaxXp(int lvl){
        this.setMaxXp(10+(lvl*10));
        return  10+(lvl*10);
    }

    /**
     * Atualiza o nível do jogador, aumentando atributos e redefinindo comportamentos.
     * @param particleSystem Sistema de partículas associado ao jogador.
     */
    public void updateLevel(ParticleSystem particleSystem){
        if (lvl < 15) {
            this.hp += 10;
            this.maxHp += 10;
            System.out.println("Level Up!");
            System.out.println("HP: " + this.hp + "/" + this.maxHp);

            setXp(0);
            this.lvl += 1;

            if (lvl == 5) {
                particleSystem.setParticlesPerSpawn(2);
                this.setImg(this.parent, WorldConstants.PLAYER5_SRC);
            } else if (lvl == 10) {
                particleSystem.setParticlesPerSpawn(3);
                this.setImg(this.parent, WorldConstants.PLAYER10_SRC);
            } else if (lvl == 15) {
                particleSystem.setParticlesPerSpawn(4);
                this.setImg(this.parent, WorldConstants.PLAYER15_SRC);
            }
        }
    }
}