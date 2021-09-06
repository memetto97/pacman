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
    
    private Dimension d; //altezza e larghezza del jpanel
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);//font delle scritte
    private boolean inGame = false; //se il gioco sta andando o no
    private boolean dying = false;//se pacman è vivo o no


    private final int BLOCK_SIZE = 24; //dimensione di un blocco
    private final int N_BLOCKS = 25; //numero di blocchi in larghezza e altezza..625 in totale
    private final int SCREEN_SIZE = BLOCK_SIZE * N_BLOCKS; //dimensione dello schermo
    private final int MAX_GHOSTS = 6;//numero massimo fantasmi
    private final int PACMAN_SPEED = 6;//velocità pacman

    private int N_GHOSTS = 4;//numero fantasmi iniziale
    private int lives, score, highscore;
    private int[] dx, dy;//indicano dove vuole muoversi il fantasma 
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;//ghostx e y sono array con le coordinate dei fantasmi, ghostdx e dy indicano la direzione in cui si stanno per muovere 

    private Image heart, ghost, ghost1, ghost2, ghost3, ghost4;
    private Image up, down, left, right, life, seg1, seg2, seg3, seg4, seg5, seg6, seg7, seg8, seg9, seg10, seg11, seg12, seg13, seg14, seg15, seg16, seg17, seg18, seg19, seg20, seg21, seg22, seg23, seg24, titleScreenImage;
    private Image gameOver;
    private Image food, bigDott;

    private boolean scared = false;//se i fantasmi sono blu o no
    private int scaredcount = 500;//circa 10 secondi, quanto durano i fantasmi blu


    private int pacman_x, pacman_y, pacmand_x, pacmand_y;//pacmanx e y indicano le coordinate di pacman, pacmandx e dy indicano la direzione in cui si stanno per muovere
    private int req_dx, req_dy;//indicano la richiesta di movimento data da chi sta muovendo pacman con le frecce


    //0=blue 1=left border 2=top border 4=rigth border 8=bottom border 16=white dots 32=big dots
    private final short levelData2[] = { //livello 3
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
    
    
    
    private final short levelData1[] = { //livello 2
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
    

    private final short levelData[] = { //livello 1
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

   private int level;//indica che livello siamo

    private final int maxSpeed = 6;//massima velocità dei fantasmi

    private int currentSpeed = 3;//velocità attuale dei fantasmi
    private short[] screenData;//contiene i parameetri attuali dello schermo, i vari levelData
    private Timer timer;//ogni quanto si aggiorna la gui

    GameSounds sounds;//per gestire i suoni


    public Model() {

        sounds = new GameSounds();
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        //initGame();
    }
    
    
    private void loadImages() { //carica le immagini
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

    private void initVariables(){
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
    
    
    public void checkScared(){//controlla per quanto tempo devono rimanere blu i fantasmi
        if (scared)
            scaredcount--;
        if (scaredcount<=0){
            sounds.powerPelletStop();
            scared=false;
            scaredcount = 300;
        }
    }



    private void showIntroScreen(Graphics2D g2d) {//mostra la schermata iniziale
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,630,680);
        g2d.drawImage(titleScreenImage,70,30,Color.BLACK,null);
    }




    private void drawScore(Graphics2D g) {//disegna le vite e lo score
    
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score + "  Highscore:" + highscore;
        g.drawString(s, SCREEN_SIZE / 2 + 6, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(life, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }



    private void checkMaze() {//controlla se pacman ha mangiato tutto

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

            initLevel1();
            }else 
            if (level==2) {

                score += 50;

                N_GHOSTS++;
        

                if (currentSpeed < maxSpeed) {
                    currentSpeed++;
                }

                initLevel2();
            }   
        }
    }

    
    
    private void death() {
        sounds.death();
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



    private void moveGhosts(Graphics2D g2d) {

        int pos,pos_x, pos_y;
        int pac_pos, pac_posx, pac_posy;
        int count;

        for (int i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {//ci si assicura che il fantasma abbia finito il 'quadrato' in cui muoversi
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);//posizione del fantasma sulle 575 disponibili
                pos_y=pos/25;//coordinata y del fantasma
                pos_x=pos-(25*pos_y);//coordinata x del fantasma

                pac_pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
                pac_posy=pac_pos/25;//coordinata y del fantasma
                pac_posx=pac_pos-(25*pac_posy);//coordinata x del fantasma
                
                count = 0;

                if (level==3 && (pos_y==pac_posy) && (pos>pac_pos) && (screenData[pos] & 1) == 0){//stessa riga e fantasma più a destra
                    ghost_dx[i] = -1;
                    ghost_dy[i] = 0;
                }
                else if (level==3 && (pos_y==pac_posy) && (pos<pac_pos) && (screenData[pos] & 4) == 0){//stessa riga e fantasma più a sinistra
                    ghost_dx[i] = 1;
                    ghost_dy[i] = 0;
                }
                else if (level==3 && (pos_x==pac_posx) && (pos>pac_pos) && (screenData[pos] & 2) == 0){//stessa colonna e fantasma più sotto
                    ghost_dx[i] = 0;
                    ghost_dy[i] = -1;
                }
                else if (level==3 && (pos_x==pac_posx) && (pos<pac_pos) && (screenData[pos] & 8) == 0){//stessa colonna e fantasma più sopra
                    ghost_dx[i] = 0;
                    ghost_dy[i] = 1;
                }
                else{
                    if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {//se non c'è ostacolo a sinistra e il fantasma non si sta già muovendo a destra, si muoverà a sinistra. cosi facendo se entra in un tunnel si muoverà fino alla fine di esso
                        dx[count] = -1;
                        dy[count] = 0;
                        count++;
                    }

                    if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {//se non c'è ostacolo sopra e il fantasma non si sta già muovendo sotto, si muoverà sopra.
                        dx[count] = 0;
                        dy[count] = -1;
                        count++;
                    }

                    if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {//se non c'è ostacolo a destra e il fantasma non si sta già muovendo a sinistra, si muoverà a destra.
                        dx[count] = 1;
                        dy[count] = 0;
                        count++;
                    }

                    if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {//se non c'è ostacolo sotto e il fantasma non si sta già muovendo sopra, si muoverà sotto.
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
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1,i);////////////////

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
    

        

    private void movePacman() {

        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);//posizione attuale pacman 
            ch = screenData[pos];

            

            if ((ch & 16) == 0)
                sounds.nomNomStop();

            if ((ch & 16) != 0) {//sono in una casella dove posso mangiare
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

            if (req_dx != 0 || req_dy != 0) { //controllo se sono attaccato alla parete
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

                //controllo se sono sui bordi della mappa

                //sono sul bordo superiore
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
                } else if((x==0) && (levelData[i]==0)){//sono sul bordo sinistro
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
                } else if((x==(24*BLOCK_SIZE)) && (levelData[i]==0)){//sono sul bordo destro
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
                } else if((y==(24*BLOCK_SIZE)) && (levelData[i]==0)){//sono sul bordo inferiore
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
                }  else {
                   
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

                

                if ((screenData[i] & 16) != 0) { 
                    g2d.setColor(new Color(255,250,81));
                    g2d.fillOval(x + 10, y + 10, 4, 4);
                    //g2d.drawImage(food, x, y, this);
               }

               if ((screenData[i] & 32) != 0) { 
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
        
       // N_GHOSTS = 6;
        //currentSpeed = 3;
    }

    private void initLevel() {
        sounds.newGame();
        
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        continueLevel();
    }
    private void initLevel1() {
        sounds.newGame();

        lives = 3;
        level = 2;
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            levelData[i] = levelData1[i];
            screenData[i] = levelData1[i];
        }

        continueLevel();
    }

    private void initLevel2() {
        sounds.newGame();

        lives = 3;
        level = 3;
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            levelData[i] = levelData2[i];
            screenData[i] = levelData2[i];
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
          /*  random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }*/

            ghostSpeed[i] = currentSpeed;//validSpeeds[random];
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
