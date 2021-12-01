import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class Model extends JPanel implements ActionListener{
    
    private Dimension d; //height and weidth jpanel
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);//written font 
    private boolean inGame = false; //if game is going or not
    private boolean dying = false;//if pacman is alive or not


    private final int BLOCK_SIZE = 24; //dimension of a block
    private final int N_BLOCKS = 25; //25 blocks in height, 25 in width..625 total
    private final int SCREEN_SIZE = BLOCK_SIZE * N_BLOCKS; //screen size
    private final int MAX_GHOSTS = 6;//max number of ghost
    private final int PACMAN_SPEED = 6;//pacman speed

    private int N_GHOSTS = 4;//starting number of ghost
    private int lives, score, highscore;
    private int[] dx, dy;//indicate where each ghost want to move
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;//ghost_x and ghost_Y are arrays with ghosts coordinates, ghost_dx and ghost_dy indicate the direction where ghosts are going to move 

    private Image heart, ghost, ghost1, ghost2, ghost3, ghost4;
    private Image up, down, left, right, life, seg1, seg2, seg3, seg4, seg5, seg6, seg7, seg8, seg9, seg10, seg11, seg12, seg13, seg14, seg15, seg16, seg17, seg18, seg19, seg20, seg21, seg22, seg23, seg24, titleScreenImage;
    private Image gameOver;
    private Image food, bigDott;

    private boolean scared = false;//if ghost are blue or not
    private int scaredcount = 500;//how blue ghost last, nearly 10 seconds


    private int pacman_x, pacman_y, pacmand_x, pacmand_y;//pacman_x and pacman_y indicate the coordinates of pacman, pacmand_x and pacmand_y indicates the direction where pacman is going to move
    private int req_dx, req_dy;//movement request from who move pacman



    //0=blue 1=left border 2=top border 4=rigth border 8=bottom border 16=white dots 32=big dots

    private final short levelData3[] = { //level 3
         0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
         0, 19, 26, 26, 26, 18, 26, 26, 26, 26, 26, 22,  0, 19, 26, 26, 26, 26, 26, 18, 26, 26, 26, 22,  0,
         0, 21,  0,  0,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0,  0,  0, 21,  0,
         0, 21,  0,  0,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0,  0,  0, 21,  0,
         0, 17, 26, 26, 26, 16, 26, 26, 26, 26, 26, 16, 26, 16, 26, 26, 26, 26, 26, 16, 26, 26, 26, 20,  0,
         0, 21,  0,  0,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0,  0,  0, 21,  0,
         0, 25, 26, 18, 26, 16, 26, 18, 26, 18, 26, 28,  0, 25, 26, 18, 26, 18, 26, 16, 26, 18, 26, 28,  0,
         0,  0,  0, 21,  0, 21,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,  0, 21,  0, 21,  0,  0,  0,
         0, 19, 26, 28,  0, 21,  0, 21,  0, 25, 26, 22,  0, 19, 26, 28,  0, 21,  0, 21,  0, 25, 26, 22,  0,
         0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0,
         0, 17, 26, 26, 26, 16, 26, 24, 26, 18, 26, 24, 18, 24, 26, 18, 26, 24, 26, 16, 26, 26, 26, 20,  0,
         0, 21,  0,  0,  0, 21,  0,  0,  0, 21,  0,  0, 21,  0,  0, 21,  0,  0,  0, 21,  0,  0,  0, 21,  0,
         0, 25, 26, 22,  0, 21,  0,  0,  0, 21,  0,  3,  0,  6,  0, 21,  0,  0,  0, 21,  0, 19, 26, 28,  0,
         0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0,  9,  8, 12,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0,
         0, 19, 26, 24, 26, 16, 26, 26, 26, 20,  0,  0,  0,  0,  0, 17, 26, 26, 26, 16, 26, 24, 26, 22,  0,
         0, 21,  0,  0,  0, 21,  0,  0,  0, 17, 26, 26, 26, 26, 26, 20,  0,  0,  0, 21,  0,  0,  0, 21,  0,
         0, 21,  0,  0,  0, 17, 26, 22,  0, 21,  0,  0,  0,  0,  0, 21,  0, 19, 26, 20,  0,  0,  0, 21,  0,
         0, 17, 26, 26, 26, 20,  0, 17, 26, 24, 26, 18, 26, 18, 26, 24, 26, 20,  0, 17, 26, 26, 26, 20,  0,
         0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0,
         0, 25, 26, 22,  0, 17, 26, 16, 26, 18, 26, 28,  0, 25, 26, 18, 26, 16, 26, 20,  0, 19, 26, 28,  0,
         0,  0,  0, 21,  0, 21,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,  0, 21,  0, 21,  0,  0,  0,
         0, 19, 26, 24, 26, 20,  0, 21,  0, 25, 26, 22,  0, 19, 26, 28,  0, 21,  0, 17, 26, 24, 26, 22,  0,
         0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0,
         0, 25, 26, 26, 26, 24, 26, 24, 26, 26, 26, 24, 26, 24, 26, 26, 26, 24, 26, 24, 26, 26, 26, 28,  0,
         0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,

    };
    
    
    
    private final short levelData2[] = { //level 2
        0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
        0, 19, 26, 26, 18, 26, 26, 26, 26, 26, 26, 22,  0, 19, 26, 26, 26, 26, 26, 26, 18, 26, 26, 22,  0,
        0, 21,  0,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0,  0, 21,  0,
        0, 21,  0,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0,  0, 21,  0,
        0, 17, 26, 26, 16, 26, 18, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 18, 26, 16, 26, 26, 20,  0,
        0, 21,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0, 21,  0,
        0, 25, 26, 26, 20,  0, 25, 26, 26, 26, 26, 22,  0, 19, 26, 26, 26, 26, 28,  0, 17, 26, 26, 28,  0,
        0,  0,  0,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0,  0,  0,  0,
        3,  2,  6,  0, 17, 26, 26, 26, 26, 26, 26, 20,  0, 17, 26, 26, 26, 26, 26, 26, 20,  0,  3,  2,  6,
        1,  0,  4,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0,  1,  0,  4,
        9,  8, 12,  0, 17, 26, 26, 18, 26, 18, 26, 24, 18, 24, 26, 18, 26, 18, 26, 26, 20,  0,  9,  8, 12,
        0,  0,  0,  0, 21,  0,  0, 21,  0, 21,  0,  0,  5,  0,  0, 21,  0, 21,  0,  0, 21,  0,  0,  0,  0,
        0, 27, 26, 26, 20,  0,  0, 21,  0, 21,  0,  3,  0,  6,  0, 21,  0, 21,  0,  0, 17, 26, 26, 30,  0,
        0,  0,  0,  0, 21,  0,  0, 21,  0, 21,  0,  9,  8, 12,  0, 21,  0, 21,  0,  0, 21,  0,  0,  0,  0,
        3,  2,  6,  0, 21,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0, 21,  0,  3,  2,  6,
        9,  8, 12,  0, 21,  0,  0, 21,  0, 17, 18, 18, 26, 18, 18, 20,  0, 21,  0,  0, 21,  0,  9,  8, 12,
        0,  0,  0,  0, 21,  0,  0, 21,  0, 17, 16, 20,  0, 17, 16, 20,  0, 21,  0,  0, 21,  0,  0,  0,  0,
        0, 19, 26, 26, 24, 26, 26, 24, 26, 24, 24, 20,  0, 17, 24, 24, 26, 24, 26, 26, 24, 26, 26, 22,  0,
        0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,
        0, 17, 26, 26, 26, 26, 26, 26, 26, 26, 26, 16, 26, 16, 26, 26, 26, 26, 26, 26, 26, 26, 26, 20,  0,
        0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,
        0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,
        0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,
        0, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 28,  0, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 28,  0,
        0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,

    };
    

    private final short levelData[] = { //level 1
         0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
         0, 19, 26, 26, 26, 26, 26, 26, 26, 26, 18, 18, 18, 18, 18, 26, 26, 26, 26, 26, 26, 26, 26, 22,  0,
         0, 21,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,
         0, 21,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,
         0, 17, 26, 26, 26, 26, 26, 26, 26, 18, 24, 24, 24, 24, 24, 18, 26, 26, 26, 26, 26, 26, 26, 20,  0,
         0, 21,  0,  0,  0,  0,  0,  0,  0, 21,  0,  0,  0,  0,  0, 21,  0,  0,  0,  0,  0,  0,  0, 21,  0,
         0, 25, 26, 26, 18, 26, 26, 18, 18, 20,  0,  0,  0,  0,  0, 17, 18, 18, 26, 26, 18, 26, 26, 28,  0,
         0,  0,  0,  0, 21,  0,  0, 17, 16, 20,  0,  0,  0,  0,  0, 17, 16, 20,  0,  0, 21,  0,  0,  0,  0,
         3,  2,  6,  0, 21,  0,  0, 17, 16, 24, 26, 26, 26, 26, 26, 24, 16, 20,  0,  0, 21,  0,  3,  2,  6,
         1,  0,  4,  0, 21,  0,  0, 17, 20,  0,  0,  0,  0,  0,  0,  0, 17, 20,  0,  0, 21,  0,  1,  0,  4,
         1,  0,  4,  0, 21,  0,  0, 17, 16, 18, 26, 26, 26, 26, 26, 18, 16, 20,  0,  0, 21,  0,  1,  0,  4,
         1,  0,  4,  0, 21,  0,  0, 17, 16, 20,  0,  0,  5,  0,  0, 17, 16, 20,  0,  0, 21,  0,  1,  0,  4,
         1,  0,  4,  0, 21,  0,  0, 17, 16, 20,  0,  3,  0,  6,  0, 17, 16, 20,  0,  0, 21,  0,  1,  0,  4,
         1,  0,  4,  0, 21,  0,  0, 17, 16, 20,  0,  9,  8, 12,  0, 17, 16, 20,  0,  0, 21,  0,  1,  0,  4,
         1,  0,  4,  0, 21,  0,  0, 17, 16, 20,  0,  0,  0,  0,  0, 17, 16, 20,  0,  0, 21,  0,  1,  0,  4,
         9,  8, 12,  0, 21,  0,  0, 17, 24, 24, 26, 26, 26, 26, 26, 24, 24, 20,  0,  0, 21,  0,  9,  8, 12,
         0,  0,  0,  0, 21,  0,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,  0, 21,  0,  0,  0,  0,
         0, 19, 26, 26, 24, 18, 18, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 18, 18, 24, 26, 26, 22,  0,
         0, 21,  0,  0,  0, 17, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 20,  0,  0,  0, 21,  0,
         0, 17, 26, 26, 26, 24, 24, 24, 26, 26, 18, 18, 18, 18, 18, 26, 26, 24, 24, 24, 26, 26, 26, 20,  0,
         0, 21,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,
         0, 21,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,
         0, 17, 18, 18, 18, 18, 18, 18, 18, 18, 16, 16, 32, 16, 16, 18, 18, 18, 18, 18, 18, 18, 18, 20,  0,
         0, 25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28,  0,
         0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
   };


    private int level;//which level we are in

    private final int maxSpeed = 6;//max ghost speed

    private int currentSpeed = 3;//actual speed of ghosts
    private short[] screenData;//contains the current values ​​of the screen, the various levelData
    private Timer timer;//how often the gui updates

    GameSounds sounds;//to manage sounds


    public Model() {

        sounds = new GameSounds();
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        //initGame();
    }
    
    
    private void loadImages() { //uploads images
        down = new ImageIcon("./images/down.gif").getImage();
    	up = new ImageIcon("./images/up.gif").getImage();
    	left = new ImageIcon("./images/left.gif").getImage();
    	right = new ImageIcon("./images/right.gif").getImage();
        ghost = new ImageIcon("./images/ghost.gif").getImage();
        ghost1 = new ImageIcon("./images/ghost1.gif").getImage();
        ghost2 = new ImageIcon("./images/ghost2.gif").getImage();
        ghost3 = new ImageIcon("./images/ghost3.gif").getImage();
        ghost4 = new ImageIcon("./images/ghost4.gif").getImage();
        heart = new ImageIcon("./images/heart.png").getImage();
        life = new ImageIcon("./images/life3.png").getImage();
        seg1 = new ImageIcon("./images/seg1.png").getImage();
        seg2 = new ImageIcon("./images/seg2.png").getImage();
        seg3 = new ImageIcon("./images/seg3.png").getImage();
        seg4 = new ImageIcon("./images/seg4.png").getImage();
        seg5 = new ImageIcon("./images/seg5.png").getImage();
        seg6 = new ImageIcon("./images/seg6.png").getImage();
        seg7 = new ImageIcon("./images/seg7.png").getImage();
        seg8 = new ImageIcon("./images/seg8.png").getImage();
        seg9 = new ImageIcon("./images/seg9.png").getImage();
        seg10 = new ImageIcon("./images/seg10.png").getImage();
        seg11 = new ImageIcon("./images/seg11.png").getImage();
        seg12 = new ImageIcon("./images/seg12.png").getImage();
        seg13 = new ImageIcon("./images/seg13.png").getImage();
        seg14 = new ImageIcon("./images/seg14.png").getImage();
        seg15 = new ImageIcon("./images/seg15.png").getImage();
        seg16 = new ImageIcon("./images/seg16.png").getImage();
        seg17 = new ImageIcon("./images/seg17.png").getImage();
        seg18 = new ImageIcon("./images/seg18.png").getImage();
        seg19 = new ImageIcon("./images/seg19.png").getImage();
        seg20 = new ImageIcon("./images/seg20.png").getImage();
        seg21 = new ImageIcon("./images/seg21.png").getImage();
        seg22 = new ImageIcon("./images/seg22.png").getImage();
        seg23 = new ImageIcon("./images/seg23.png").getImage();
        seg24 = new ImageIcon("./images/seg24.png").getImage();

        titleScreenImage = new ImageIcon("./images/pacman-screen.png").getImage();

        gameOver = new ImageIcon("./images/game-over.png").getImage();

        food = new ImageIcon("./images/food.png").getImage();
        bigDott = new ImageIcon("./images/big-dott.png").getImage();


    }

    private void initVariables(){//inizializes variables
        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(800,800);//indica quanto nero...vedi paintcomponent
        ghost_x = new int [MAX_GHOSTS];
        ghost_y = new int [MAX_GHOSTS];
        ghost_dx = new int [MAX_GHOSTS];
        ghost_dy = new int [MAX_GHOSTS];
        ghostSpeed = new int [MAX_GHOSTS];
        dx = new int[MAX_GHOSTS];
        dy = new int[MAX_GHOSTS];

        timer = new Timer(40, this);//40milliseconds
        timer.restart();
    }




    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {
            checkScared();
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }
    
    
    public void checkScared(){//check if ghosts are blue 
        if (scared)
            scaredcount--;
        if (scaredcount<=0){
            sounds.powerPelletStop();
            scared=false;
            scaredcount = 300;
        }
    }



    private void showIntroScreen(Graphics2D g2d) {//show the intro screen image
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,630,680);
        g2d.drawImage(titleScreenImage,70,30,Color.BLACK,null);
    }




    private void drawScore(Graphics2D g) {//draw lives and score 
    
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score + "  Highscore:" + highscore;
        g.drawString(s, SCREEN_SIZE / 2 + 6, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(life, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }



    private void checkMaze() {//check if pacman ate everything and if yes, it change level

        int i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i] & 48) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {
            if(level==1){
                score += 50;

                N_GHOSTS++;

                if (currentSpeed < maxSpeed) {
                    currentSpeed++;
                }

            initLevel2();
            } else 
            if (level==2) {

                score += 50;

                N_GHOSTS++;
        

                if (currentSpeed < maxSpeed) {
                    currentSpeed++;
                }

                initLevel3();
            }
            if(level==3) {

                score += 50;

                N_GHOSTS++;
        

                if (currentSpeed < maxSpeed) {
                    currentSpeed++;
                }

                initLevel3();
            }   
        }
    }

    
    
    private void death() {//if pacman dies
        sounds.death();
        sounds.nomNomStop();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();}

    	lives--;

        if (lives == 0) {
            inGame = false;
            //gameOver(g2d);
            if(score>highscore){
                PrintWriter out;
                try{
                    out = new PrintWriter("highscore.txt");
                    out.println(score);
                    out.close();
                }
                catch(Exception e)
                {
                }
            }
        }

       continueLevel();
    }


  //show a game-over scren
    /*private void gameOver(Graphics2D g2d){
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,600,600);
        g2d.drawImage(gameOver,70,30,Color.BLACK,null);
          

    }*/



    private void moveGhosts(Graphics2D g2d) {//function that manages ghosts movement

        int pos,pos_x, pos_y;
        int pac_pos, pac_posx, pac_posy;
        int count;

        for (int i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {// make sure that the ghost has finished the 'square' to move into
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);//position of ghost i
                pos_y=pos/25;//y-coordinate
                pos_x=pos-(25*pos_y);//x-coordinate

                pac_pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);//pacman position
                pac_posy=pac_pos/25;//y-coordinate
                pac_posx=pac_pos-(25*pac_posy);//x-coordinate
                
                count = 0;
                //ghosts are smarter only on level 3 and they move to pacman
                if (level==3 && (pos_y==pac_posy) && (pos>pac_pos) && (screenData[pos] & 1) == 0){//ghost on same line of pacman but at its right
                    ghost_dx[i] = -1;
                    ghost_dy[i] = 0;
                }
                else if (level==3 && (pos_y==pac_posy) && (pos<pac_pos) && (screenData[pos] & 4) == 0){//ghost on same line of pacman but at its left
                    ghost_dx[i] = 1;
                    ghost_dy[i] = 0;
                }
                else if (level==3 && (pos_x==pac_posx) && (pos>pac_pos) && (screenData[pos] & 2) == 0){//ghost on same column of pacman but below
                    ghost_dx[i] = 0;
                    ghost_dy[i] = -1;
                }
                else if (level==3 && (pos_x==pac_posx) && (pos<pac_pos) && (screenData[pos] & 8) == 0){//ghost on same column of pacman but above
                    ghost_dx[i] = 0;
                    ghost_dy[i] = 1;
                }
                else{
                    if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {//if there is no obstacle to the left and the ghost is not already moving to the right, it will move to the left. doing so if it enters a tunnel it will move to the end of it
                        dx[count] = -1;
                        dy[count] = 0;
                        count++;
                    }

                    if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {//if there is no obstacle above and the ghost is not already moving below, it will move above
                        dx[count] = 0;
                        dy[count] = -1;
                        count++;
                    }

                    if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {//if there is no obstacle to the right and the ghost is not already moving to the left, it will move to the right
                        dx[count] = 1;
                        dy[count] = 0;
                        count++;
                    }

                    if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {//if there is no obstacle below and the ghost is not already moving above, it will move below
                        dx[count] = 0;
                        dy[count] = 1;
                        count++;
                    }

                    if (count == 0) {

                        if ((screenData[pos] & 15) == 15) {
                            ghost_dx[i] = 0;
                            ghost_dy[i] = 0;
                        } else {
                            ghost_dx[i] = -ghost_dx[i];
                            ghost_dy[i] = -ghost_dy[i];
                        }

                    } else {

                        count = (int) (Math.random() * count);

                        if (count > 3) {
                            count = 3;
                        }

                        ghost_dx[i] = dx[count];
                        ghost_dy[i] = dy[count];
                    }
                }

            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1,i);

            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && inGame) {
                        if(scared){
                            sounds.eatGhost();
                            score = score+5;
                            ghost_y[i] = 13 * BLOCK_SIZE; //start position
                            ghost_x[i] = 12 * BLOCK_SIZE;

                        } else
                            dying = true;
            }
        }
    }

    



    private void drawGhost(Graphics2D g2d, int x, int y, int z) {
        if(!scared){
            if((z%4)==0)
                g2d.drawImage(ghost, x, y, this);
            if((z%4)==1)
                g2d.drawImage(ghost1, x, y, this);
            if((z%4)==2)
                g2d.drawImage(ghost2, x, y, this);
            if((z%4)==3)
                g2d.drawImage(ghost3, x, y, this);
        } else {
            g2d.drawImage(ghost4, x, y, this);
        }
    }
    

        

    private void movePacman() {//manages pacman movement

        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);//pacman position 
            ch = screenData[pos];

            

            if ((ch & 16) == 0)
                sounds.nomNomStop();

            if ((ch & 16) != 0) {//its in a square where can eat
                screenData[pos] = (short) (ch & 15);
                score++;
                sounds.nomNom();
            }
            
            if((ch & 32) != 0){//power pellet
                screenData[pos] = (short) (ch & 15);
                score++;
                scared = true;
                sounds.nomNom();
                sounds.powerPellet();
            }

            if (req_dx != 0 || req_dy != 0) { //check if attached to the wall
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
                else{
                    pacmand_x = 0;
                    pacmand_y = 0;
                }
            }
        } 
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }



    private void drawPacman(Graphics2D g2d) {

        if (req_dx == -1) {
        	g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        } else {
        	g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }



    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(new Color(0,72,251));
                g2d.setStroke(new BasicStroke(5));

                //check if at the sides of the map

                //top edge
                if((y==0) && (levelData[i]==0)){
                    if(x==0){
                        if((levelData[i+25] & 2) != 0 )
                            g2d.drawImage(seg3, x, y, this);
                        if((levelData[i+25] == 0) && (levelData[i+1] != 0))
                            g2d.drawImage(seg4, x, y, this);
                        if((levelData[i+25] == 0) && (levelData[i+1] == 0) && (levelData[i+26] == 0))
                            g2d.drawImage(seg10, x, y, this);
                        if((levelData[i+25] == 0) && (levelData[i+1] == 0) && (levelData[i+26] != 0))
                            g2d.drawImage(seg18, x, y, this);

                    }
                    if(x==(24*BLOCK_SIZE)){
                        if((levelData[i+25] & 2) != 0 )
                            g2d.drawImage(seg2, x, y, this);
                        if((levelData[i+25] == 0) && (levelData[i-1] != 0))
                            g2d.drawImage(seg4, x, y, this);
                        if((levelData[i+25] == 0) && (levelData[i-1] == 0) && (levelData[i+24] == 0))
                            g2d.drawImage(seg11, x, y, this);
                        if((levelData[i+25] == 0) && (levelData[i-1] == 0) && (levelData[i+24] != 0))
                            g2d.drawImage(seg20, x, y, this);
                    }
                    if((x!=0) && (x!=(24*BLOCK_SIZE))){
                        if((levelData[i+1] == 0) && (levelData[i-1] == 0) && (levelData[i+25] != 0))
                            g2d.drawImage(seg1, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-1] == 0) && (levelData[i+25] != 0))
                            g2d.drawImage(seg2, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] != 0) && (levelData[i+25] != 0))
                            g2d.drawImage(seg3, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-1] != 0) && (levelData[i+25] == 0))
                            g2d.drawImage(seg4, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] == 0) && (levelData[i+25] == 0) && (levelData[i+24] == 0))
                            g2d.drawImage(seg9, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] != 0) && (levelData[i+25] == 0) && (levelData[i+26] == 0))
                            g2d.drawImage(seg10, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-1] == 0) && (levelData[i+25] == 0) && (levelData[i+24] == 0))
                            g2d.drawImage(seg11, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] != 0) && (levelData[i+25] == 0) && (levelData[i+26] != 0))
                            g2d.drawImage(seg18, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-1] == 0) && (levelData[i+25] == 0) && (levelData[i+24] != 0))
                            g2d.drawImage(seg20, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] == 0) && (levelData[i+25] == 0) && (levelData[i+24] != 0))
                            g2d.drawImage(seg21, x, y, this);
                    }
                } else if((x==0) && (levelData[i]==0)){//left edge
                    if(y==(24*BLOCK_SIZE)){
                        if((levelData[i+1] == 0) && (levelData[i-25] != 0))
                            g2d.drawImage(seg3, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-25] == 0))
                            g2d.drawImage(seg7, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-25] == 0) && (levelData[i-24] == 0))
                            g2d.drawImage(seg13, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-25] == 0) && (levelData[i-24] != 0))
                            g2d.drawImage(seg19, x, y, this);
                    }
                    if((y!=0) && (y!=(24*BLOCK_SIZE))){
                        if((levelData[i+1] == 0) && (levelData[i-25] != 0) && (levelData[i+25] != 0))
                            g2d.drawImage(seg3, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-25] != 0) && (levelData[i+25] == 0))
                            g2d.drawImage(seg4, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-25] == 0) && (levelData[i+25] != 0))
                            g2d.drawImage(seg7, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-25] != 0) && (levelData[i+25] == 0) && (levelData[i+26] == 0))
                            g2d.drawImage(seg10, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-25] == 0) && (levelData[i+25] != 0) && (levelData[i-24] == 0))
                            g2d.drawImage(seg13, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && (levelData[i-24] == 0))
                            g2d.drawImage(seg15, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0))
                            g2d.drawImage(seg17, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-25] != 0) && (levelData[i+25] == 0) && (levelData[i+26] != 0))
                            g2d.drawImage(seg18, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-25] == 0) && (levelData[i+25] != 0) && (levelData[i-24] != 0))
                            g2d.drawImage(seg19, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && (levelData[i-24] != 0))
                            g2d.drawImage(seg22, x, y, this);
                    }
                } else if((x==(24*BLOCK_SIZE)) && (levelData[i]==0)){//right edge
                    if(y==(24*BLOCK_SIZE)){
                        if((levelData[i-1] == 0) && (levelData[i-25] != 0))
                            g2d.drawImage(seg2, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i-26] != 0))
                            g2d.drawImage(seg5, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-25] == 0))
                            g2d.drawImage(seg7, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i-26] == 0))
                            g2d.drawImage(seg14, x, y, this);
                    }
                    if((y!=0) && (y!=(24*BLOCK_SIZE))){
                        if((levelData[i-1] == 0) && (levelData[i-25] != 0) && (levelData[i+25] != 0))
                            g2d.drawImage(seg2, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-25] != 0) && (levelData[i+25] == 0))
                            g2d.drawImage(seg4, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i+25] != 0) && (levelData[i-26] != 0))
                            g2d.drawImage(seg5, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-25] == 0) && (levelData[i+25] != 0))
                            g2d.drawImage(seg7, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] != 0) && (levelData[i+25] == 0) && (levelData[i+24] == 0))
                            g2d.drawImage(seg11, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i+25] != 0) && (levelData[i-26] == 0))
                            g2d.drawImage(seg14, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && (levelData[i-26] == 0))
                            g2d.drawImage(seg16, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0))
                            g2d.drawImage(seg17, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] != 0) && (levelData[i+25] == 0) && (levelData[i+24] != 0))
                            g2d.drawImage(seg20, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && (levelData[i-26] != 0))
                            g2d.drawImage(seg24, x, y, this);
                    }
                } else if((y==(24*BLOCK_SIZE)) && (levelData[i]==0)){//bottom edge
                    if((x!=0) && (x!=(24*BLOCK_SIZE))){
                        if((levelData[i-1] == 0) && (levelData[i-25] != 0) && (levelData[i+1] == 0))
                            g2d.drawImage(seg1, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] != 0) && (levelData[i+1] != 0))
                            g2d.drawImage(seg2, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-25] != 0) && (levelData[i+1] == 0))
                            g2d.drawImage(seg3, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i+1] != 0) && (levelData[i-26] != 0))
                            g2d.drawImage(seg5, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-25] == 0) && (levelData[i+1] != 0))
                            g2d.drawImage(seg7, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i+1] == 0) && (levelData[i-26] == 0))
                            g2d.drawImage(seg12, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-25] == 0) && (levelData[i+1] == 0) && (levelData[i-24] == 0))
                            g2d.drawImage(seg13, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i+1] != 0) && (levelData[i-26] == 0))
                            g2d.drawImage(seg14, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-25] == 0) && (levelData[i+1] == 0) && (levelData[i-24] != 0))
                            g2d.drawImage(seg19, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-25] == 0) && (levelData[i+1] == 0) && (levelData[i-26] != 0))
                            g2d.drawImage(seg23, x, y, this);
                    }
                }  else { //any other situation
                   
                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && ((levelData[i+25] & 2) != 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0))
                        g2d.drawImage(seg1, x, y, this);
                        
                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && ((levelData[i+25] & 2) != 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0))
                        g2d.drawImage(seg2, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && ((levelData[i+25] & 2) != 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0))
                        g2d.drawImage(seg3, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && (levelData[i+25] == 0) && ((levelData[i-1] & 4) != 0) && ((levelData[i+1] & 1) != 0))
                        g2d.drawImage(seg4, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && ((levelData[i+25] & 2) != 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i-26] != 0))
                        g2d.drawImage(seg5, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) 
                    && (levelData[i-24] != 0) && (levelData[i+24] != 0) && (levelData[i-26] != 0) && (levelData[i+26] != 0))
                        g2d.drawImage(seg6, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && ((levelData[i+25] & 2) != 0) && ((levelData[i-1] & 4) != 0) && ((levelData[i+1] & 1) != 0))
                        g2d.drawImage(seg7, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) 
                    && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-26] == 0) && (levelData[i+26] == 0))
                        g2d.drawImage(seg8, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && (levelData[i+25] == 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) && (levelData[i+24] == 0) && (levelData[i+26] == 0))
                        g2d.drawImage(seg9, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && (levelData[i+25] == 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i+26] == 0))
                        g2d.drawImage(seg10, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && (levelData[i+25] == 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i+24] == 0))
                        g2d.drawImage(seg11, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && ((levelData[i+25] & 2) != 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) && (levelData[i-24] == 0) && (levelData[i-26] == 0))
                        g2d.drawImage(seg12, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && ((levelData[i+25] & 2) != 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i-24] == 0))
                        g2d.drawImage(seg13, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && ((levelData[i+25] & 2) != 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i-26] == 0))
                        g2d.drawImage(seg14, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i-24] == 0) && (levelData[i+26] == 0))
                        g2d.drawImage(seg15, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i+24] == 0) && (levelData[i-26] == 0))
                        g2d.drawImage(seg16, x, y, this);
                    
                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && ((levelData[i-1] & 4) != 0) && ((levelData[i+1] & 1) != 0))
                        g2d.drawImage(seg17, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && (levelData[i+25] == 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i+26] != 0))
                        g2d.drawImage(seg18, x, y, this);
                    
                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && ((levelData[i+25] & 2) != 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i-24] != 0))
                        g2d.drawImage(seg19, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && (levelData[i+25] == 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i+24] != 0))
                        g2d.drawImage(seg20, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-25] & 8) != 0) && (levelData[i+25] == 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) && (levelData[i+24] != 0) && (levelData[i+26] != 0))
                        g2d.drawImage(seg21, x, y, this);
                    
                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i-24] != 0) && (levelData[i+26] != 0))
                        g2d.drawImage(seg22, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && ((levelData[i+25] & 2) != 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) && (levelData[i-24] != 0) && (levelData[i-26] != 0))
                        g2d.drawImage(seg23, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i+24] != 0) && (levelData[i-26] != 0))
                        g2d.drawImage(seg24, x, y, this);


                }

                

                if ((screenData[i] & 16) != 0) { //draw little dott
                    g2d.setColor(new Color(255,250,81));
                    g2d.fillOval(x + 10, y + 10, 4, 4);
                    //g2d.drawImage(food, x, y, this);
               }

               if ((screenData[i] & 32) != 0) { //draw big dott
                g2d.setColor(new Color(255,250,81));
                g2d.fillOval(x + 6, y + 6, 12, 12);
               // g2d.drawImage(bigDott, x, y, this);
           }

                i++;
            }
        }
    }

    private void initGame() {

        level = 1;
    	lives = 3;
        score = 0;
        File file = new File("highscore.txt");
        Scanner sc;
        try
            {
                sc = new Scanner(file);
                highscore = sc.nextInt();
                sc.close();
            }
        catch(Exception e){}
        initLevel();
        
    }

    private void initLevel() {
        sounds.newGame();
        
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        continueLevel();
    }
    
    private void initLevel2() {
        sounds.newGame();

        lives = 3;
        level = 2;
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            levelData[i] = levelData2[i];
            screenData[i] = levelData2[i];
        }

        continueLevel();
    }

    private void initLevel3() {
        sounds.newGame();

        lives = 3;
        level = 3;
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            levelData[i] = levelData3[i];
            screenData[i] = levelData3[i];
        }

        continueLevel();
    }

    private void continueLevel() {

    	int dx = 1;
        int random;

        for (int i = 0; i < N_GHOSTS; i++) {

            ghost_y[i] = 13 * BLOCK_SIZE; //start position
            ghost_x[i] = 12 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;

            ghostSpeed[i] = currentSpeed;
        }

        pacman_x = 12 * BLOCK_SIZE;  //start position
        pacman_y = 15 * BLOCK_SIZE;
        pacmand_x = 0;	//reset direction move
        pacmand_y = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        dying = false;
    }

 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);

        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    //controls
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                } 
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();
                }
            }
        }
}

	
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
		
	}
