package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    //public int hasKey = 0;
    int standCounter = 0;


    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp); // Vi kaller konstruktøren av super klassen av Player //Nødvendig for at Entity klassen skal få Gamepanel
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();

    }


    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6;
        life = maxLife; //Current life
    }

    public void getPlayerImage() {
    /* Alternativ 1 optimalisert siden episode 11 17:55
    try{
        up1 = ImageIO.read(getClass().getResourceAsStream("/player/up.png"));
        up2 = ImageIO.read(getClass().getResourceAsStream("/player/up2.png"));
        down1 = ImageIO.read(getClass().getResourceAsStream("/player/down.png"));
        down2 = ImageIO.read(getClass().getResourceAsStream("/player/down2.png"));
        left1 = ImageIO.read(getClass().getResourceAsStream("/player/left.png"));
        left2 = ImageIO.read(getClass().getResourceAsStream("/player/left2.png"));
        right1 = ImageIO.read(getClass().getResourceAsStream("/player/right.png"));
        right2 = ImageIO.read(getClass().getResourceAsStream("/player/right2.png"));
    }catch (IOException e) {
        e.printStackTrace();

     */
        // Alternativ 2
        up1 = setup("/player/up");
        up2 = setup("/player/up2");
        down1 = setup("/player/down");
        down2 = setup("/player/down2");
        left1 = setup("/player/left");
        left2 = setup("/player/left2");
        right1 = setup("/player/right");
        right2 = setup("/player/right2");
    }

    public void update() {
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {

            if (keyH.upPressed == true) {
                direction = "up";

            } else if (keyH.downPressed == true) {
                direction = "down";

            } else if (keyH.leftPressed == true) {
                direction = "left";

            } else if (keyH.rightPressed == true) {
                direction = "right";

            }
            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this); //cChecker = collisionChecker

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //CHECK EVENT
            gp.eHandler.checkEvent();

            gp.keyH.enterPressed = false;
            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            spriteCounter++;   // Explaination 19:11 in video // Sprite animation moving.
            if (spriteCounter > 10) { // 22:00 in video Can choose between 1. sprite looping in idle position
                if (spriteNum == 1) { // or 2. stop looping animation unless u make a move [WASD] (currently in this programming) <--
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            standCounter++;
            if (standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {


        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            if (gp.keyH.enterPressed == true) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
                //System.out.println("You are hitting an npc!");
            }

        }
    }

    public void draw(Graphics2D g2) {

        // g2.setColor(Color.white);
        // g2.fillRect(x,y,gp.tileSize,gp.tileSize);
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}

