package game;

/**
 * @author João Ramos- A50730
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 24/25
 */

/**
 * Classe WorldConstants.
 * Define constantes globais para configuracoes do mundo, terreno, jogador e entidades do jogo.
 */
public class WorldConstants {

    //Mundo
    public final static double[] WINDOW = {-10, 10, -10, 10};

    //Terreno
    public final static int LINHAS = 20;
    public final static int COLUNAS = 20;

    //Tipo de patch no terreno.
    public enum PatchType{
        EMPTY, OBSTACLE, MUD, WATER
    }

    public final static double[] PATCH_TYPE_PROB = {.50f, .15f, .15f, .2f};
    public final static int NSTATES = PatchType.values().length;
    public static String[] TERRAIN_IMAGES = {
            "assets/empty.png", "assets/stone.png", "assets/mud.png", "assets/water.png"
    };

    public static String PLAYER_SRC = "assets/ninja0.svg";
    public static String PLAYER5_SRC = "assets/ninja1.svg";
    public static String PLAYER10_SRC = "assets/ninja2.svg";
    public static String PLAYER15_SRC = "assets/ninja3.svg";

    public static String PLAYER5_PNG_SRC = "assets/ninja1.png";
    public static String PLAYER10_PNG_SRC = "assets/ninja2.png";
    public static String PLAYER15_PNG_SRC = "assets/ninja3.png";

    public static String BAT_SRC = "assets/bat.svg";
    public static String SKLT_SRC = "assets/skeleton.svg";
    public static String ZOMBIE_SRC = "assets/zombie.svg";
    public static String SEAMSTR_SRC = "assets/SeaMonster.svg";
    public static String DRAGON_SRC = "assets/dragao.svg";

    public static String BAT_IMG = "assets/bat.png";
    public static String SKLT_IMG = "assets/skeleton.png";
    public static String ZOMBIE_IMG = "assets/zombie.png";
    public static String SEAMSTR_IMG = "assets/SeaMonster.png";
    public static String DRAGON_IMG = "assets/dragao.png";

    public static String LOCK_IMG = "assets/cadeado.png";

    public final static float PLAYER_SIZE = .5f;
    public final static float PLAYER_MASS = 1.7f;
    public final static float PLAYER_SPEED = 6.0f;
    public static int PLAYER_COLOR = 255;
}