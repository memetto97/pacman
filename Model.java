
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
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private boolean inGame = false; //se il gioco sta andando
    private boolean dying = false;//se pacman è vivo


    private final int BLOCK_SIZE = 24; //dimensione di un blocco
    private final int N_BLOCKS = 24; //numero di blocchi in larghezza e altezza..576 in totale
    private final int SCREEN_SIZE = BLOCK_SIZE * N_BLOCKS; //dimensione dello schermo
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;

    private int N_GHOSTS = 4;
    private int lives, score, highscore;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;//ghostx e y sono array con le coordinate iniziali dei fantasmi, ghostdx e dy indicano la direzione in cui si muovono 

    private Image heart, ghost, ghost1, ghost2, ghost3;
    private Image up, down, left, right, life, boh, boh2, boh3, boh4, boh5, boh6, boh7, boh8, boh9, boh10, boh11, boh12, boh13, boh14, boh15, boh16, boh17, boh18, boh19, boh20, boh21, boh22, boh23, boh24, titleScreenImage;
    private Image gameOver;


    private int pacman_x, pacman_y, pacmand_x, pacmand_y;//pacmanx e y indicano le coordinate di pacman, pacmandx e dy indicano il delta in cui si stanno per muovere
    private int req_dx, req_dy;//indicano la richiesta di movimento data dall'input di chi sta muovendo pacman

    private final short levelData[] = { 
         0,  0, 19, 18, 18, 18, 18, 18, 22,  0,  0,  0,  0, 19, 18, 22,  0,  0,  0, 19, 22,  0,  0,  0,
         0,  0, 17, 16, 16, 24, 16, 16, 16, 18, 22,  0, 19, 16, 16, 20,  0,  0,  0, 17, 16, 18, 18, 22,
        27, 26, 24, 24, 28,  0, 17, 16, 16, 16, 16, 18, 16, 16, 16, 16, 18, 18, 18, 16, 16, 16, 16, 20,
         0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 28,
        19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 28,  0,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 20,  0,  0,
        17, 16, 16, 16, 24, 16, 16, 16, 16, 20,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 18, 22,
        17, 16, 16, 20,  0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 24, 24, 28,  0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21,  0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 24, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 22,  0, 19, 18, 18, 16, 16, 20,  0, 21,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 20,  0, 17, 16, 16, 16, 24, 28,  0, 29,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
         0,  0,  0, 21,  0, 17, 16, 16, 20,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 24, 28,
        19, 18, 18, 16, 18, 16, 16, 16, 16, 18, 18, 18, 18, 18, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 30,
        25, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,
         0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,
         0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 24, 20,  0,
         0, 25, 16, 16, 16, 16, 16, 24, 24, 24, 20,  0,  0, 17, 16, 16, 16, 16, 16, 16, 28,  0, 29,  0,
         0,  0, 25, 24, 24, 24, 28,  0,  0,  0, 29,  0,  0, 25, 24, 24, 24, 24, 24, 28,  0,  0,  0,  0,

    };
    //0=blue 1=left border 2=top border 4=rigth border 8=bottom border 16=white dots

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;


    public Model() {

        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }
    
    
    private void loadImages() { //carica le immagini
    	down = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/down.gif").getImage();
    	up = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/up.gif").getImage();
    	left = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/left.gif").getImage();
    	right = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/right.gif").getImage();
        ghost = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/ghost.gif").getImage();
        ghost1 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/ghost1.gif").getImage();
        ghost2 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/ghost2.gif").getImage();
        ghost3 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/ghost3.gif").getImage();
        heart = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/heart.png").getImage();
        life = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/life.png").getImage();
        boh = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh.png").getImage();
        boh2 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh2.png").getImage();
        boh3 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh3.png").getImage();
        boh4 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh4.png").getImage();
        boh5 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh5.png").getImage();
        boh6 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh6.png").getImage();
        boh7 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh7.png").getImage();
        boh8 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh8.png").getImage();
        boh9 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh9.png").getImage();
        boh10 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh10.png").getImage();
        boh11 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh11.png").getImage();
        boh12 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh12.png").getImage();
        boh13 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh13.png").getImage();
        boh14 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh14.png").getImage();
        boh15 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh15.png").getImage();
        boh16 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh16.png").getImage();
        boh17 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh17.png").getImage();
        boh18 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh18.png").getImage();
        boh19 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh19.png").getImage();
        boh20 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh20.png").getImage();
        boh21 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh21.png").getImage();
        boh22 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh22.png").getImage();
        boh23 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh23.png").getImage();
        boh24 = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/boh24.png").getImage();

        titleScreenImage = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/pacman-screen.png").getImage();

        gameOver = new ImageIcon("C:/Users/Matteo/Desktop/PIGDM-pacman/images/game-over.png").getImage();


    }

    private void initVariables(){
        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(800,800);//400,400
        ghost_x = new int [MAX_GHOSTS];
        ghost_y = new int [MAX_GHOSTS];
        ghost_dx = new int [MAX_GHOSTS];
        ghost_dy = new int [MAX_GHOSTS];
        ghostSpeed = new int [MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(40, this);//40milliseconds
        timer.restart();
    }




    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {

            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }



    private void showIntroScreen(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,600,600);
        g2d.drawImage(titleScreenImage,70,30,Color.BLACK,null);
    	/*String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN_SIZE)/4, 150);/*/
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



    private void checkMaze() {

        int i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i] & 48) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            score += 50;

            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }

            initLevel();
        }
    }

    private void death() {

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

        int pos;
        int count;

        for (int i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {//ci si assicura che il fantasma abbia finito il 'quadrato' in cui muoversi
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);//posizione del fantasma sulle 225 disponibili

                count = 0;

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

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1,i);////////////////

            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && inGame) {

                dying = true;
            }
        }
    }



    private void drawGhost(Graphics2D g2d, int x, int y, int z) {/////////////
        if((z%4)==0)
            g2d.drawImage(ghost, x, y, this);
        if((z%4)==1)
            g2d.drawImage(ghost1, x, y, this);
        if((z%4)==2)
            g2d.drawImage(ghost2, x, y, this);
        if((z%4)==3)
            g2d.drawImage(ghost3, x, y, this);
        }

        

    private void movePacman() {

        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);//posizione attuale pacman sulle 225 disponibili(da 0 244)
            ch = screenData[pos];//possibili valori inziali 16,17,18,19,20,21,22,24,25,26,28 che in & con 16 fa sempre 16 che a sua volta in & con 15 fa 0

            if ((ch & 16) != 0) {//sono in una casella dove posso mangiare
                screenData[pos] = (short) (ch & 15);
                score++;
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

            /*if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }*/
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
                        if((levelData[i+24] & 2) != 0 )
                            g2d.drawImage(boh3, x, y, this);
                        if((levelData[i+24] == 0) && (levelData[i+1] != 0))
                            g2d.drawImage(boh4, x, y, this);
                        if((levelData[i+24] == 0) && (levelData[i+1] == 0) && (levelData[i+25] == 0))
                            g2d.drawImage(boh10, x, y, this);
                        if((levelData[i+24] == 0) && (levelData[i+1] == 0) && (levelData[i+25] != 0))
                            g2d.drawImage(boh18, x, y, this);

                    }
                    if(x==(23*BLOCK_SIZE)){
                        if((levelData[i+24] & 2) != 0 )
                            g2d.drawImage(boh2, x, y, this);
                        if((levelData[i+24] == 0) && (levelData[i-1] != 0))
                            g2d.drawImage(boh4, x, y, this);
                        if((levelData[i+24] == 0) && (levelData[i-1] == 0) && (levelData[i+23] == 0))
                            g2d.drawImage(boh11, x, y, this);
                        if((levelData[i+24] == 0) && (levelData[i-1] == 0) && (levelData[i+23] != 0))
                            g2d.drawImage(boh20, x, y, this);
                    }
                    if((x!=0) && (x!=(23*BLOCK_SIZE))){
                        if((levelData[i+1] == 0) && (levelData[i-1] == 0) && (levelData[i+24] != 0))
                            g2d.drawImage(boh, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-1] == 0) && (levelData[i+24] != 0))
                            g2d.drawImage(boh2, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] != 0) && (levelData[i+24] != 0))
                            g2d.drawImage(boh3, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-1] != 0) && (levelData[i+24] == 0))
                            g2d.drawImage(boh4, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] == 0) && (levelData[i+24] == 0) && (levelData[i+23] == 0))
                            g2d.drawImage(boh9, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] != 0) && (levelData[i+24] == 0) && (levelData[i+25] == 0))
                            g2d.drawImage(boh10, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-1] == 0) && (levelData[i+24] == 0) && (levelData[i+23] == 0))
                            g2d.drawImage(boh11, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] != 0) && (levelData[i+24] == 0) && (levelData[i+25] != 0))
                            g2d.drawImage(boh18, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-1] == 0) && (levelData[i+24] == 0) && (levelData[i+23] != 0))
                            g2d.drawImage(boh20, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-1] == 0) && (levelData[i+24] == 0) && (levelData[i+23] != 0))
                            g2d.drawImage(boh21, x, y, this);
                    }
                } else if((x==0) && (levelData[i]==0)){//sono sul bordo sinistro
                    if(y==(23*BLOCK_SIZE)){
                        if((levelData[i+1] == 0) && (levelData[i-24] != 0))
                            g2d.drawImage(boh3, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-24] == 0))
                            g2d.drawImage(boh7, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-24] == 0) && (levelData[i-23] == 0))
                            g2d.drawImage(boh13, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-24] == 0) && (levelData[i-23] != 0))
                            g2d.drawImage(boh19, x, y, this);
                    }
                    if((y!=0) && (y!=(23*BLOCK_SIZE))){
                        if((levelData[i+1] == 0) && (levelData[i-24] != 0) && (levelData[i+24] != 0))
                            g2d.drawImage(boh3, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-24] != 0) && (levelData[i+24] == 0))
                            g2d.drawImage(boh4, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-24] == 0) && (levelData[i+24] != 0))
                            g2d.drawImage(boh7, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-24] != 0) && (levelData[i+24] == 0) && (levelData[i+25] == 0))
                            g2d.drawImage(boh10, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-24] == 0) && (levelData[i+24] != 0) && (levelData[i-23] == 0))
                            g2d.drawImage(boh13, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-23] == 0))
                            g2d.drawImage(boh15, x, y, this);
                        if((levelData[i+1] != 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0))
                            g2d.drawImage(boh17, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-24] != 0) && (levelData[i+24] == 0) && (levelData[i+25] != 0))
                            g2d.drawImage(boh18, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-24] == 0) && (levelData[i+24] != 0) && (levelData[i-23] != 0))
                            g2d.drawImage(boh19, x, y, this);
                        if((levelData[i+1] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-23] != 0))
                            g2d.drawImage(boh22, x, y, this);
                    }
                } else if((x==(23*BLOCK_SIZE)) && (levelData[i]==0)){//sono sul bordo destro
                    if(y==(23*BLOCK_SIZE)){
                        if((levelData[i-1] == 0) && (levelData[i-24] != 0))
                            g2d.drawImage(boh2, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i-25] != 0))
                            g2d.drawImage(boh5, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-24] == 0))
                            g2d.drawImage(boh7, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i-25] == 0))
                            g2d.drawImage(boh14, x, y, this);
                    }
                    if((y!=0) && (y!=(23*BLOCK_SIZE))){
                        if((levelData[i-1] == 0) && (levelData[i-24] != 0) && (levelData[i+24] != 0))
                            g2d.drawImage(boh2, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-24] != 0) && (levelData[i+24] == 0))
                            g2d.drawImage(boh4, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i+24] != 0) && (levelData[i-25] != 0))
                            g2d.drawImage(boh5, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-24] == 0) && (levelData[i+24] != 0))
                            g2d.drawImage(boh7, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] != 0) && (levelData[i+24] == 0) && (levelData[i+23] == 0))
                            g2d.drawImage(boh11, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i+24] != 0) && (levelData[i-25] == 0))
                            g2d.drawImage(boh14, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-25] == 0))
                            g2d.drawImage(boh16, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0))
                            g2d.drawImage(boh17, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] != 0) && (levelData[i+24] == 0) && (levelData[i+23] != 0))
                            g2d.drawImage(boh20, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-25] != 0))
                            g2d.drawImage(boh24, x, y, this);
                    }
                } else if((y==(23*BLOCK_SIZE)) && (levelData[i]==0)){//sono sul bordo inferiore
                    if((x!=0) && (x!=(23*BLOCK_SIZE))){
                        if((levelData[i-1] == 0) && (levelData[i-24] != 0) && (levelData[i+1] == 0))
                            g2d.drawImage(boh, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] != 0) && (levelData[i+1] != 0))
                            g2d.drawImage(boh2, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-24] != 0) && (levelData[i+1] == 0))
                            g2d.drawImage(boh3, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i+1] != 0) && (levelData[i-25] != 0))
                            g2d.drawImage(boh5, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-24] == 0) && (levelData[i+1] != 0))
                            g2d.drawImage(boh7, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i+1] == 0) && (levelData[i-25] == 0))
                            g2d.drawImage(boh12, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-24] == 0) && (levelData[i+1] == 0) && (levelData[i-23] == 0))
                            g2d.drawImage(boh13, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i+1] != 0) && (levelData[i-25] == 0))
                            g2d.drawImage(boh14, x, y, this);
                        if((levelData[i-1] != 0) && (levelData[i-24] == 0) && (levelData[i+1] == 0) && (levelData[i-23] != 0))
                            g2d.drawImage(boh19, x, y, this);
                        if((levelData[i-1] == 0) && (levelData[i-24] == 0) && (levelData[i+1] == 0) && (levelData[i-25] != 0))
                            g2d.drawImage(boh23, x, y, this);
                    }
                }  else {
                   
                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && ((levelData[i+24] & 2) != 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0))
                        g2d.drawImage(boh, x, y, this);
                        
                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && ((levelData[i+24] & 2) != 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0))
                        g2d.drawImage(boh2, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && ((levelData[i+24] & 2) != 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0))
                        g2d.drawImage(boh3, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && (levelData[i+24] == 0) && ((levelData[i-1] & 4) != 0) && ((levelData[i+1] & 1) != 0))
                        g2d.drawImage(boh4, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && ((levelData[i+24] & 2) != 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i-25] != 0))
                        g2d.drawImage(boh5, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) 
                    && (levelData[i-23] != 0) && (levelData[i+23] != 0) && (levelData[i-25] != 0) && (levelData[i+25] != 0))
                        g2d.drawImage(boh6, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && ((levelData[i+24] & 2) != 0) && ((levelData[i-1] & 4) != 0) && ((levelData[i+1] & 1) != 0))
                        g2d.drawImage(boh7, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) 
                    && (levelData[i-23] == 0) && (levelData[i+23] == 0) && (levelData[i-25] == 0) && (levelData[i+25] == 0))
                        g2d.drawImage(boh8, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && (levelData[i+24] == 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) && (levelData[i+23] == 0) && (levelData[i+25] == 0))
                        g2d.drawImage(boh9, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && (levelData[i+24] == 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i+25] == 0))
                        g2d.drawImage(boh10, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && (levelData[i+24] == 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i+23] == 0))
                        g2d.drawImage(boh11, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && ((levelData[i+24] & 2) != 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) && (levelData[i-23] == 0) && (levelData[i-25] == 0))
                        g2d.drawImage(boh12, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && ((levelData[i+24] & 2) != 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i-23] == 0))
                        g2d.drawImage(boh13, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && ((levelData[i+24] & 2) != 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i-25] == 0))
                        g2d.drawImage(boh14, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i-23] == 0) && (levelData[i+25] == 0))
                        g2d.drawImage(boh15, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i+23] == 0) && (levelData[i-25] == 0))
                        g2d.drawImage(boh16, x, y, this);
                    
                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && ((levelData[i-1] & 4) != 0) && ((levelData[i+1] & 1) != 0))
                        g2d.drawImage(boh17, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && (levelData[i+24] == 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i+25] != 0))
                        g2d.drawImage(boh18, x, y, this);
                    
                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && ((levelData[i+24] & 2) != 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i-23] != 0))
                        g2d.drawImage(boh19, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && (levelData[i+24] == 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i+23] != 0))
                        g2d.drawImage(boh20, x, y, this);

                    if ((levelData[i] == 0) && ((levelData[i-24] & 8) != 0) && (levelData[i+24] == 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) && (levelData[i+23] != 0) && (levelData[i+25] != 0))
                        g2d.drawImage(boh21, x, y, this);
                    
                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && ((levelData[i-1] & 4) != 0) && (levelData[i+1] == 0) && (levelData[i-23] != 0) && (levelData[i+25] != 0))
                        g2d.drawImage(boh22, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && ((levelData[i+24] & 2) != 0) && (levelData[i-1] == 0) && (levelData[i+1] == 0) && (levelData[i-23] != 0) && (levelData[i-25] != 0))
                        g2d.drawImage(boh23, x, y, this);

                    if ((levelData[i] == 0) && (levelData[i-24] == 0) && (levelData[i+24] == 0) && (levelData[i-1] == 0) && ((levelData[i+1] & 1) != 0) && (levelData[i+23] != 0) && (levelData[i-25] != 0))
                        g2d.drawImage(boh24, x, y, this);


                }

                

                if ((screenData[i] & 16) != 0) { 
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
               }

                i++;
            }
        }
    }

    private void initGame() {

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
        currentSpeed = 3;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        continueLevel();
    }

    private void continueLevel() {

    	int dx = 1;
        int random;

        for (int i = 0; i < N_GHOSTS; i++) {

            ghost_y[i] = 4 * BLOCK_SIZE; //start position
            ghost_x[i] = 4 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

        pacman_x = 7 * BLOCK_SIZE;  //start position
        pacman_y = 11 * BLOCK_SIZE;
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
