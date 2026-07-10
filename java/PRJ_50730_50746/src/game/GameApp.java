package game;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

import aa.Boid;
import aa.Flock;
import physics.Particle;
import physics.Body;
import physics.ParticleSystem;
import physics.ParticleSystemControl;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import setup.IProcessingApp;
import processing.sound.*;
import tools.SubPlot;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe GameApp.
 * Implementa a logica principal do jogo, incluindo gerenciamento de flocks, jogador e terreno.
 * Contem metodos para inicializacao, renderizacao e controlo de eventos.
 */
public class GameApp implements IProcessingApp {

    private boolean isInMenu = true;
    private boolean isInfo = false;

    private SoundFile soundGame;
    private int fireRate = 0;
    private Flock flockBat, flockSklt, flockZombie, flockSeaMonsters, flockDragon;
    private PImage batImage, zombieImage, skltImage, seamstrImage, dragonImage;

    private ParticleSystemControl particleSystemControl;
    private ParticleSystem particleSystem;

    private  float[] velControl = {0, PApplet.radians(360), 1, 3};

    private float[] sacWeights = {0f, 0f, 0f, 2f, 0f};
    private float[] viewport = {.05f, .05f, .7f, .9f};
    private float[] frame = { .04f, .04f, .72f, .92f};
    private SubPlot plt, moldura;
    private Terrain terrain;
    private Player player;

    private Entity target;

    private List<Body> allTrackingBodiesPlayer;
    private List<Body> allTrackingBodiesMonsters;

    private PImage menuImage, banner, info;

    private static int NUM_ZOMBIES = 3;
    private static int NUM_SKELETONS = 3;
    private static int NUM_SEAMSTRS = 3;
    private static int NUM_DRAGONS = 3;
    private static int NUM_BAT = 5;
    private static int DMG_BAT = 3;
    private static int DMG_ZOMBIE = 10;
    private static int DMG_SKELETONS = 15;
    private static int DMG_SEAMSTRS = 20;
    private static int DMG_DRAGONS = 30;

    private static final int XP_BAT = 5;
    private static final int XP_ZOMBIE = 10;
    private static final int XP_SKLT = 15;
    private static final int XP_SEA = 20;
    private static final int XP_DRAGON = 30;
    private float lastDamageTime = 0;
    private int waves = 1;

    private int killBAT=0;
    private int killZOMBIE=0;
    private int killSKLT=0;
    private int killSEA=0;
    private int killDRAGON=0;

    /**
     * Metodo de configuracao inicial do jogo.
     * Configura imagens, sons, e inicializa os principais componentes como terreno, jogador e flocks.
     * @param parent Referencia ao objeto PApplet usado para renderizacao e manipulacao de eventos.
     */
    @Override
    public void setup(PApplet parent) {
        initialize(parent);
        menuImage = parent.loadImage("assets/menu.png");
        banner = parent.loadImage("assets/background3.png");
        info = parent.loadImage("assets/info.png");

        menuImage.resize(parent.width, parent.height);
        banner.resize(parent.width, parent.height);
        info.resize(parent.width, parent.height);

        soundGame = new SoundFile(parent, "assets/soundGame.wav");
        soundGame.loop();
        soundGame.amp(.05f);
    }

    /**
     * Inicializa o jogo, configurando o terreno, jogador, flocks e outros componentes.
     * @param parent Referencia ao objeto PApplet usado para configuracao e inicializacao do jogo.
     */
    private void initialize(PApplet parent) {
        waves = 1;
        killSKLT = 0;
        killZOMBIE = 0;
        killBAT = 0;
        killSEA = 0;
        killDRAGON = 0;

        flockZombie = null;
        flockSklt = null;
        flockBat = null;
        flockSeaMonsters = null;
        flockDragon = null;

        // Inicializa o SubPlot com as dimensões da janela
        plt = new SubPlot(WorldConstants.WINDOW, viewport, parent.width, parent.height);
        moldura = new SubPlot(WorldConstants.WINDOW, frame, parent.width, parent.height);


        batImage = parent.loadImage(WorldConstants.BAT_IMG);
        skltImage = parent.loadImage(WorldConstants.SKLT_IMG);
        zombieImage = parent.loadImage(WorldConstants.ZOMBIE_IMG);
        seamstrImage = parent.loadImage(WorldConstants.SEAMSTR_IMG);
        dragonImage = parent.loadImage(WorldConstants.DRAGON_IMG);
        batImage.resize(30, 30);
        skltImage.resize(30, 30);
        zombieImage.resize(30, 30);
        seamstrImage.resize(30,30);
        dragonImage.resize(30,30);

        particleSystemControl = new ParticleSystemControl(velControl);

        // Cria o terreno
        terrain = new Terrain(parent, plt);
        terrain.setImageSrcs(WorldConstants.TERRAIN_IMAGES);
        terrain.initRandomCustom(WorldConstants.PATCH_TYPE_PROB);
        for (int i = 0; i < 2; i++) {
            terrain.majorityRule();
        }

        // Cria o jogador
        player = new Player(
                new PVector(0, 0),
                WorldConstants.PLAYER_MASS,
                WorldConstants.PLAYER_SIZE,
                WorldConstants.PLAYER_COLOR,
                parent,
                plt
        );
        player.setGameApp(this);
        player.setupPlayer(player, parent, plt, target, allTrackingBodiesPlayer);

        flockBat = new Flock(NUM_BAT, .1f, 0.8f, parent.color(200, 200, 0), sacWeights, parent, plt, WorldConstants.BAT_SRC, "bat");
        allTrackingBodiesMonsters = new ArrayList<>();
    }

    /**
     * Metodo de renderizacao principal.
     * Alterna entre a exibicao do menu, informacoes ou o jogo, dependendo do estado atual.
     * @param parent Referencia ao objeto PApplet usado para renderizacao.
     * @param dt     Intervalo de tempo desde o ultimo quadro, usado para calculos baseados no tempo.
     */
    @Override
    public void draw(PApplet parent, float dt) {
        if (isInMenu) {
            drawMenu(parent);
        } else if (isInfo) {
            drawInfo(parent);
        } else {
            drawGame(parent, dt);
        }
    }

    /**
     * Exibe o menu principal do jogo, com opcoes para iniciar ou acessar informacoes.
     * @param parent Referencia ao objeto PApplet usado para renderizacao.
     */
    private void drawMenu(PApplet parent) {
        parent.background(menuImage);
        parent.fill(255);
        parent.textSize(50);
        parent.textAlign(PApplet.CENTER, PApplet.CENTER);
        parent.text("Press Space to Start", parent.width / 2, parent.height / 2 + 200);
        parent.textSize(50);
        parent.text("Info", parent.width / 2, parent.height / 2 + 300);
    }

    /**
     * Exibe a tela de informacoes sobre o projeto, incluindo os autores e o professor.
     * @param parent Referencia ao objeto PApplet usado para renderizacao.
     */
    public void drawInfo(PApplet parent) {
        parent.background(info);

        PFont font = parent.createFont("Cooper Black", 40);
        parent.textFont(font);
        parent.fill(255);
        parent.textAlign(PApplet.CENTER, PApplet.CENTER);

        parent.text("ISEL 24/25 - 31D - MSSN", parent.width / 2, parent.height / 2 - 100);
        parent.text("Miguel Alcobia - 50746", parent.width / 2, parent.height / 2);
        parent.text("João Ramos - 50730", parent.width / 2, parent.height / 2 + 50);
        parent.text("Professor: Arnaldo Abrantes", parent.width / 2, parent.height / 2 + 150);
        parent.text("Back", parent.width - 150, parent.height / 2 + 300);
    }

    /**
     * Desenha a barra de vida no canto superior direito.
     * @param parent O objeto PApplet usado para desenhar.
     * @param player O jogador cuja vida será representada.
     */
    private void drawLifeBar(PApplet parent, Player player) {
        int barWidth = 200;
        int barHeight = 20;
        int x = parent.width - barWidth - 20;
        int y = 20;

        float lifeRatio = (float) player.getHp() / player.getMaxHp();

        // Desenha o fundo da barra
        parent.fill(200);
        parent.noStroke();
        parent.rect(x, y, barWidth, barHeight);

        // Desenha a barra de vida
        parent.fill(255, 0, 0);
        parent.rect(x, y, barWidth * lifeRatio, barHeight);
    }

    /**
     * Desenha a barra de experiencia no canto superior direito da tela.
     * A barra reflete o progresso atual de experiencia do jogador.
     * @param parent Referencia ao objeto PApplet usado para renderizacao.
     * @param player O jogador cuja experiencia sera representada na barra.
     */
    private void drawExpBar(PApplet parent, Player player) {
        int barWidth = 200;
        int barHeight = 20;
        int x = parent.width - barWidth - 20;
        int y = 60;

        float xpRatio = (float) player.getXp() / player.getMaxXp();

        // Desenha o fundo da barra
        parent.fill(200);
        parent.noStroke();
        parent.rect(x, y, barWidth, barHeight);

        // Desenha a barra de vida
        parent.fill(0, 0, 255);
        parent.rect(x, y, barWidth * xpRatio, barHeight);

        //Texto que indica o nível do jogador e a wave atual abaixo da barra
        parent.textSize(14);
        parent.textAlign(PApplet.LEFT, PApplet.TOP);
        parent.textFont(parent.createFont("Arial", 20, true));
        String levelText = "LEVEL: " + player.getLvl();
        parent.fill(255);
        parent.text(levelText, x, y + barHeight + 20);

        parent.textSize(14);
        parent.textAlign(PApplet.LEFT, PApplet.TOP);
        parent.textFont(parent.createFont("Arial", 20, true));
        String waveText = "WAVE: " + waves;
        parent.fill(255);
        parent.text(waveText, x, y + barHeight + 40);
    }

    /**
     * Exibe as skins disponiveis e bloqueadas para o jogador,
     * com base no nivel atual do jogador.
     * @param parent Referencia ao objeto PApplet usado para renderizacao.
     * @param player O jogador cujas skins serao exibidas.
     */
    public void drawSkins(PApplet parent, Player player){
        int x = parent.width - 200 - 20;
        int y = 60;

        // Carrega a imagem do cadeado
        PImage lockImage = parent.loadImage(WorldConstants.LOCK_IMG);
        lockImage.resize(60, 60);

        // Carrega as imagens das diferentes skins do player
        PImage skin5 = parent.loadImage(WorldConstants.PLAYER5_PNG_SRC);
        PImage skin10 = parent.loadImage(WorldConstants.PLAYER10_PNG_SRC);
        PImage skin15 = parent.loadImage(WorldConstants.PLAYER15_PNG_SRC);

        skin5.resize(60,60);
        skin10.resize(60,60);
        skin15.resize(60,60);

        int lockX = x + 200 / 2 - 15;
        int lockY = y + 20 + 90;
        int lockSpacing = 150;

        if (player.getLvl() <5){
            for (int i = 0; i < 3; i++) {
                parent.image(lockImage, lockX, lockY + i * lockSpacing);
            }
        } else if (player.getLvl() >= 5 && player.getLvl() <10) {
            for (int i = 0; i < 3; i++) {
                if (i == 0){
                    parent.image(skin5, lockX, lockY + i * lockSpacing);
                }else {
                    parent.image(lockImage, lockX, lockY + i * lockSpacing);
                }
            }
        } else if (player.getLvl() >= 10 && player.getLvl() <15) {
            for (int i = 0; i < 3; i++) {
                if (i == 0){
                    parent.image(skin5, lockX, lockY + i * lockSpacing);
                } else if (i == 1) {
                    parent.image(skin10, lockX, lockY + i * lockSpacing);
                } else {
                    parent.image(lockImage, lockX, lockY + i * lockSpacing);
                }
            }
        }else {
            for (int i = 0; i < 3; i++) {
                if (i == 0){
                    parent.image(skin5, lockX, lockY + i * lockSpacing);
                } else if (i == 1) {
                    parent.image(skin10, lockX, lockY + i * lockSpacing);
                } else {
                    parent.image(skin15, lockX, lockY + i * lockSpacing);
                }
            }
        }
    }

    /**
     * Exibe a tela de vitoria quando o jogador conclui todas as waves.
     * @param parent Referencia ao objeto PApplet usado para renderizacao.
     */
    private void drawVictoryScreen(PApplet parent) {
        float[] bb = moldura.getBoundingBox();

        parent.fill(0);
        parent.rect(bb[0], bb[1], bb[2], bb[3]);

        parent.fill(0, 255, 0);
        parent.textAlign(PApplet.CENTER, PApplet.CENTER);
        parent.textSize(50);
        parent.text("YOU WIN!", bb[0] + bb[2] / 2, bb[1] + bb[3] / 2 - 50);

        parent.textSize(30);
        parent.fill(0, 255, 0);
        parent.text("Press SPACE to return to the menu", bb[0] + bb[2] / 2, bb[1] + bb[3] / 2 + 50);
    }

    /**
     * Desenha as imagens dos monstros na zona inferior direita,
     * com o número de kills correspondente ao lado direito.
     * @param parent O objeto PApplet usado para desenhar.
     */
    private void drawKillCount(PApplet parent) {
        int xBase = parent.width - 120;
        int yBase = parent.height - 200;
        int spacing = 40;

        parent.fill(0, 150);
        parent.noStroke();
        parent.rect(xBase - 10, yBase - 40, 130, 140);

        parent.textAlign(PApplet.LEFT, PApplet.CENTER);
        parent.textSize(20);
        parent.fill(255);

        // Morcegos
        batImage.resize(30, 30);
        parent.image(batImage, xBase, yBase - 35);
        parent.text(killBAT, xBase + 40, yBase - 20);

        // Zumbis
        zombieImage.resize(30, 30);
        parent.image(zombieImage, xBase, yBase + spacing -35);
        parent.text(killZOMBIE, xBase + 40, yBase + spacing - 20);

        // Esqueletos
        skltImage.resize(30, 30);
        parent.image(skltImage, xBase, yBase + spacing * 2 -35);
        parent.text(killSKLT, xBase + 40, yBase + spacing * 2 - 20);

        // Monstros do mar
        seamstrImage.resize(30, 30);
        parent.image(seamstrImage, xBase, yBase + spacing * 3- 35);
        parent.text(killSEA, xBase + 40, yBase + spacing * 3 - 20);

        // Dragao
        dragonImage.resize(40, 40);
        parent.image(dragonImage, xBase, yBase + spacing * 4- 35);
        parent.text(killDRAGON, xBase + 40, yBase + spacing * 4 - 20);
    }

    /**
     * Renderiza a jogabilidade principal do jogo, incluindo a exibicao do jogador,
     * inimigos, terreno, barras de status e logica das waves.
     * @param parent Referencia ao objeto PApplet usado para renderizacao.
     * @param dt     Intervalo de tempo desde o ultimo quadro, usado para calculos baseados no tempo.
     */
    public void drawGame(PApplet parent, float dt) {
        float[] bb = moldura.getBoundingBox();

        if (waves > 25) {
            drawVictoryScreen(parent);
            return;
        }
        if(player.getHp() != 0){
            parent.background(banner);
            parent.fill(0);
            parent.rect(bb[0], bb[1], bb[2], bb[3]);

            terrain.display(parent);
            float currentTime = parent.millis() / 1000.0f;
            drawKillCount(parent);

            if (particleSystem != null) {
                particleSystem.setPos(player.getPos().copy());
                particleSystem.move(dt);

                //Colisões
                if(flockBat != null){
                    flockBat = checkCollisions(flockBat, particleSystem, player, XP_BAT);
                }
                if (flockSklt != null){
                    flockSklt = checkCollisions(flockSklt, particleSystem, player, XP_SKLT);
                }
                if (flockZombie != null){
                    flockZombie = checkCollisions(flockZombie, particleSystem, player, XP_ZOMBIE);
                }
                if (flockSeaMonsters != null){
                    flockSeaMonsters = checkCollisions(flockSeaMonsters, particleSystem, player, XP_SEA);
                }
                if (flockDragon != null){
                    flockDragon = checkCollisions(flockDragon, particleSystem, player, XP_DRAGON);
                }
                particleSystem.display(parent, plt);
            }

            if(player.getXp() == player.calcMaxXp(player.getLvl())){
                player.updateLevel(particleSystem);
            }

            // Atualiza a posição do jogador com o mouse
            player.playerMouse();

            // Exibe o jogador e aplica o Arrive
            player.applyBehavior(0, dt);
            player.display(parent, plt);

            // Desenha a barra de vida e exp do jogador
            drawLifeBar(parent, player);
            drawExpBar(parent, player);
            drawSkins(parent, player);

            // Flock morcegos
            if (flockBat != null){
                flockBat.applyBehavior(dt);
                flockBat.display(parent, plt, 60f, 60f);
                flockBat.Hunt(player, allTrackingBodiesMonsters, getNumBat());
                checkPlayerCollisions(flockBat, player, DMG_BAT, currentTime);
            }

            // Flock zombies
            if (flockZombie != null){
                flockZombie.applyBehavior(dt);
                flockZombie.display(parent, plt, 25f, 30f);
                flockZombie.Hunt(player, allTrackingBodiesMonsters, getNumZombies());
                checkPlayerCollisions(flockZombie, player, DMG_ZOMBIE, currentTime);
            }

            // Flock esqueletos
            if (flockSklt != null){
                flockSklt.applyBehavior(dt);
                flockSklt.display(parent, plt, 25f, 30f);
                flockSklt.Hunt(player, allTrackingBodiesMonsters, getNumSkeletons());
                checkPlayerCollisions(flockSklt, player, DMG_SKELETONS, currentTime);
            }

            // Flock monstro do mar
            if (flockSeaMonsters != null){
                flockSeaMonsters.applyBehavior(dt);
                flockSeaMonsters.display(parent, plt, 35f, 35f);
                flockSeaMonsters.Hunt(player, allTrackingBodiesMonsters, getNumSeaMonsters());
                checkPlayerCollisions(flockSeaMonsters, player, DMG_SEAMSTRS, currentTime);
            }

            // Flock dragao
            if (flockDragon != null){
                flockDragon.applyBehavior(dt);
                flockDragon.display(parent, plt, 60f, 60f);
                flockDragon.Hunt(player, allTrackingBodiesMonsters, getNumDragons());
                checkPlayerCollisions(flockDragon, player, DMG_DRAGONS, currentTime);
            }

            // Verifica se os flocks acabaram e inicia uma nova wave
            if (flockBat == null && flockSklt == null && flockZombie == null && flockSeaMonsters == null && flockDragon == null) {
                startNewWave(parent);
            }

        }else{
            // Desenha o ecrã de Game Over
            drawLifeBar(parent, player);
            parent.fill(0);
            parent.rect(bb[0], bb[1], bb[2], bb[3]);

            parent.fill(255, 0, 0); // Define a cor do texto como vermelho
            parent.textAlign(PApplet.CENTER, PApplet.CENTER); // Centraliza o texto
            parent.textSize(50); // Define o tamanho do texto

            float centerX = bb[0] + bb[2] / 2;
            float centerY = bb[1] + bb[3] / 2;
            parent.text("GAME OVER", centerX, centerY);

            parent.textSize(30);
            parent.text("Press SPACE to try again", centerX, centerY + 40);
        }
    }

    /**
     * Inicializa uma nova wave de inimigos ao criar grupos de diferentes tipos de inimigos
     * com base no numero da wave atual.
     * @param parent Referencia ao objeto PApplet usado para criacao e renderizacao dos grupos de inimigos.
     */
    private void startNewWave(PApplet parent) {
        waves++;

        if (waves <= 5) { //BAT
            flockBat = new Flock(getNumBat(), .1f, 0.8f, parent.color(200, 200, 0), sacWeights, parent, plt, WorldConstants.BAT_SRC, "bat");
        } else if (waves == 6) { //ZOMBIE
            flockZombie = new Flock(getNumZombies(), .1f, 0.8f, parent.color(0, 200, 0), sacWeights, parent, plt, WorldConstants.ZOMBIE_SRC, "zombie");
        } else if (6 < waves && waves <= 10) {
            setNumZombies(getNumZombies() + 1);
            flockZombie = new Flock(getNumZombies(), .1f, 0.8f, parent.color(0, 200, 0), sacWeights, parent, plt, WorldConstants.ZOMBIE_SRC, "zombie");
        } else if (waves == 11) { //SKELETON
            flockSklt = new Flock(getNumSkeletons(), .1f, 0.8f, parent.color(200, 0, 0), sacWeights, parent, plt, WorldConstants.SKLT_SRC, "sklt");
        } else if (11 < waves && waves <= 15) {
            setNumSkeletons(getNumSkeletons() + 1);
            flockSklt = new Flock(getNumSkeletons(), .1f, 0.8f, parent.color(200, 0, 0), sacWeights, parent, plt, WorldConstants.SKLT_SRC, "sklt");
        } else if (waves == 16) { //SEAMONSTER
            flockSeaMonsters = new Flock(getNumSeaMonsters(), .1f, 1.0f, parent.color(0, 0, 200), sacWeights, parent, plt, WorldConstants.SEAMSTR_SRC, "seamonster");
        } else if (16 < waves && waves <= 20) {
            setNumSeaMonsters(getNumSeaMonsters() + 1);
            flockSeaMonsters = new Flock(getNumSeaMonsters(), .1f, 1.0f, parent.color(0, 0, 200), sacWeights, parent, plt, WorldConstants.SEAMSTR_SRC, "seamonster");
        } else if (waves == 21) { //DRAGON
            flockDragon = new Flock(getNumDragons(), .2f, 1.5f, parent.color(255, 100, 0), sacWeights, parent, plt, WorldConstants.DRAGON_SRC, "dragon");
        } else if (21 < waves && waves <= 25) {
            setNumDragons(getNumDragons() + 1);
            flockDragon = new Flock(getNumDragons(), .2f, 1.5f, parent.color(255, 100, 0), sacWeights, parent, plt, WorldConstants.DRAGON_SRC, "dragon");
        }
    }

    /**
     * Lida com eventos de pressionamento de tecla, como iniciar o jogo, retornar ao menu,
     * ou ativar/desativar o sistema de particulas (tiros) do jogador.
     * @param parent Referencia ao objeto PApplet usado para manipulacao de eventos de teclado.
     */
    @Override
    public void keyPressed(PApplet parent) {
        if (waves > 25 && parent.key == ' ') {
            isInMenu = true;
            initialize(parent);
        } else if (isInMenu && parent.key == ' ') {
            isInMenu = false;
        } else if (!isInMenu) {
            if (parent.key == ' ' && player.getHp() != 0) {
                if (particleSystem == null) {
                    particleSystem = new ParticleSystem(
                            player.getPos().copy(),
                            new PVector(),
                            1F, .2F,
                            parent.color(255, 0, 0),
                            3F,
                            particleSystemControl
                    );
                    if (fireRate != 0) {
                        particleSystem.setParticlesPerSpawn(fireRate);
                    }
                }
            } else if (parent.key == 'q') {
                fireRate = particleSystem.getParticlesPerSpawn();
                particleSystem = null;
            } else {
                isInMenu = true;
                initialize(parent);
            }
        }
    }

    @Override
    public void keyReleased(PApplet parent) {}

    /**
     * Lida com eventos de clique do mouse, permitindo ao jogador interagir com elementos da interface,
     * como mudar skins ou selecionar alvos no terreno.
     * @param p Referencia ao objeto PApplet usado para manipulacao de eventos de mouse.
     */
    @Override
    public void mousePressed(PApplet p) {
        if (!isInMenu && isInfo == false){
            // Verifica cliques nas skins
            checkSkinClick(p, WorldConstants.PLAYER5_SRC, p.width - 200 - 20 + 200 / 2 - 15, 60 + + 20 + 90);
            checkSkinClick(p, WorldConstants.PLAYER10_SRC, p.width - 200 - 20 + 200 / 2 - 15, 60 + + 20 + 90 + 150);
            checkSkinClick(p, WorldConstants.PLAYER15_SRC, p.width - 200 - 20 + 200 / 2 - 15, 60 + + 20 + 90 + 300);

            // Atualiza target do player
            double[] ww = plt.getWorldCoord(p.mouseX, p.mouseY);
            player.getTarget().setPos(new PVector((float) ww[0], (float) ww[1]));

        } else if (isInMenu ==true && isInfo == false) {
            float infoTextX = p.width / 2;
            float infoTextY = p.height / 2 + 300;
            float textWidth = p.textWidth("Info");
            float textHeight = 50;

            // Calcula os limites do texto "Info"
            float left = infoTextX - textWidth / 2;
            float right = infoTextX + textWidth / 2;
            float top = infoTextY - textHeight / 2;
            float bottom = infoTextY + textHeight / 2;

            // Verifica se o clique está dentro dos limites do texto "Info"
            if (p.mouseX >= left && p.mouseX <= right && p.mouseY >= top && p.mouseY <= bottom) {
                isInfo = true;
                isInMenu = false;
            }
        } else if (isInMenu ==false && isInfo == true){
            float infoTextX = p.width - 150;
            float infoTextY = p.height / 2 + 300;
            float textWidth = p.textWidth("Back");
            float textHeight = 50;

            // Calcula os limites do texto "Info"
            float left = infoTextX - textWidth / 2;
            float right = infoTextX + textWidth / 2;
            float top = infoTextY - textHeight / 2;
            float bottom = infoTextY + textHeight / 2;

            // Verifica se o clique está dentro dos limites do texto "Info"
            if (p.mouseX >= left && p.mouseX <= right && p.mouseY >= top && p.mouseY <= bottom) {
                isInfo = false;
                isInMenu = true;
            }
        }
    }

    /**
     * Verifica se uma skin especifica esta disponivel para o jogador com base no nivel atual.
     * @param skin O caminho da skin a ser verificada.
     * @return true se a skin esta disponivel, false caso contrario.
     */
    private boolean isSkinAvailable(String skin) {
        int lvl = player.getLvl();

        if (skin == WorldConstants.PLAYER5_SRC && lvl >=5){
            return  true;
        } else if (skin == WorldConstants.PLAYER10_SRC && lvl >=10) {
            return  true;
        } else if (skin == WorldConstants.PLAYER15_SRC && lvl >= 15) {
            return  true;
        }
        return false;
    }

    /**
     * Verifica se um clique do mouse ocorreu numa area especifica onde uma skin esta exibida.
     * Caso a skin esteja disponivel, aplica-a ao jogador.
     * @param p Referencia ao objeto PApplet usado para manipulacao de eventos de mouse.
     * @param skin   Caminho da skin a ser verificada e aplicada.
     * @param skinX  Coordenada X da area clicavel da skin.
     * @param skinY  Coordenada Y da area clicavel da skin.
     */
    private void checkSkinClick(PApplet p, String skin, float skinX, float skinY) {
        float skinWidth = 65;
        float skinHeight = 65;
        if (p.mouseX >= skinX && p.mouseX <= skinX + skinWidth && p.mouseY >= skinY && p.mouseY <= skinY + skinHeight) {
            if (isSkinAvailable(skin)) {
                player.setImg(p, skin);
            }
        }
    }

    @Override
    public void mouseReleased(PApplet parent) {}

    @Override
    public void mouseDragged(PApplet parent) {}

    /**
     * Verifica colisoes entre as particulas(tiros) do jogador e os inimigos de um flock especifico.
     * Remove inimigos atingidos, atualiza o XP do jogador e, se necessario, remove o flock.
     * @param flock            O grupo de inimigos a ser verificado.
     * @param particleSystem   Sistema de particulas do jogador.
     * @param player           O jogador que esta causando as colisoes.
     * @param dropXP           Quantidade de XP atribuida por cada inimigo eliminado.
     * @return O flock atualizado apos a remocao dos inimigos eliminados, ou null se o flock estiver vazio.
     */
    private Flock checkCollisions(Flock flock, ParticleSystem particleSystem, Player player, int dropXP) {
        List<Boid> boidsToRemove = new ArrayList<>();

        for (Boid boid : flock.getBoidList()) {
            for (Particle particle : particleSystem.getParticles()) {
                float distance = PVector.dist(boid.getPos(), particle.getPos());
                if (distance < boid.getRadius()) {
                    boidsToRemove.add(boid);
                    if (flock.getType() == "bat"){
                        killBAT++;
                    } else if (flock.getType() == "zombie") {
                        killZOMBIE++;
                    } else if (flock.getType() == "sklt") {
                        killSKLT++;
                    } else if (flock.getType() == "seamonster") {
                        killSEA++;
                    } else if (flock.getType() == "dragon"){
                        killDRAGON++;
                    }
                    break;
                }
            }
        }
        if (!boidsToRemove.isEmpty()) {
            player.setXp(player.getXp() + boidsToRemove.size()*dropXP);
            System.out.println("XP: " + player.getXp() + "/" + player.getMaxXp());
        }
        flock.getBoidList().removeAll(boidsToRemove);
        return flock.getBoidList().isEmpty() ? null : flock;
    }

    /**
     * Verifica se algum boid colide com o jogador.
     * Se houver colisão, aplica dano ao jogador.
     * @param flock  O grupo de boids a ser verificado.
     * @param player O jogador que pode ser atingido.
     * @param dmg    O dano aplicado por cada boid que colidir.
     */
    private void checkPlayerCollisions(Flock flock, Player player, int dmg, float currentTime) {
        float damageInterval = 1.0f; // Intervalo de 1 segundo entre os danos

        for (Boid boid : flock.getBoidList()) {
            float distance = PVector.dist(boid.getPos(), player.getPos());
            if (distance < 1) { // Verifica colisão com base nos raios
                // Verifica se o intervalo de tempo foi respeitado
                if (currentTime - getLastDamageTime() >= damageInterval) {
                    player.setHp(player.getHp() - dmg); // Aplica dano ao jogador
                    setLastDamageTime(currentTime); // Atualiza o tempo do último dano
                }
                break;
            }
        }
    }

    //Get e Set do tempo do ultimo dano
    public float getLastDamageTime() {
        return lastDamageTime;
    }
    public void setLastDamageTime(float lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
    }

    //Gets e Sets dos números dos inimigos.
    public static int getNumBat() {
        return NUM_BAT;
    }
    public static void setNumBat(int numBat) {
        NUM_BAT = numBat;
    }
    public static int getNumZombies() {
        return NUM_ZOMBIES;
    }
    public static void setNumZombies(int numZombies) {
        NUM_ZOMBIES = numZombies;
    }
    public static int getNumSkeletons() {
        return NUM_SKELETONS;
    }
    public static void setNumSkeletons(int numSkeletons) {
        NUM_SKELETONS = numSkeletons;
    }
    public static int getNumSeaMonsters() {
        return NUM_SEAMSTRS;
    }
    public static void setNumSeaMonsters(int numSeaMonsters) {
        NUM_SEAMSTRS = numSeaMonsters;
    }
    public static int getNumDragons() {
        return NUM_DRAGONS;
    }
    public static void setNumDragons(int numDragons) {
        NUM_DRAGONS = numDragons;
    }

    public Terrain getTerrain() {
        return terrain;
    }
}