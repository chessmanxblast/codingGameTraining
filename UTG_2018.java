import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.Math;


//constants

//structures
class Board{
    //copy function

    //isEqual function
    int _myHealth;
    int _myMana;
    int _enemyHealth;
    int _enemyMana;
    Base myBase;
    Base enemyBase;

    List<Entity> _spiders;
    List<Entity> _myHeroes;
    List<Entity> _enemyHeroes;



}

class Entity{
    int _id;
    int _type;
    int _x;
    int _y;
    int _shieldLife;
    int _isControlled;
    int _health;
    int _vx;
    int _vy;
    int _nearBase;
    int _threatFor;
}

class Base{
    int _x;
    int _y;
}
class Action{
    int _x;
    int _y;
    int _spell;
}

//utils
class Utils{
    //measure time since begining of turn and return yes if more than X ms (X constant)
    public static boolean mustAbort() {
        return true; //change that
    }

    //distance between 2 points. maybe change the return type
    public static int distance() {
        return 0;//change that
    }

    //random fuction, return between 0 and iModulo
    public static int getRandom(int iModulo){
        return 0;//change that
    }

    //anticipate 1 turn
    public static void playOneTurn(Board iBoard, Action iAction){

    }

    //evaluation function of 1 player
    public static int  evalPlayer(Board iBoard, boolean isMe){
        return 0;//change that
    }

    public static long evalBoard(Board iBoard){
        long result=0;
        result+=evalPlayer(iBoard, true);
        result-=evalPlayer(iBoard, false);

        return result;
    }
}



// if we do genetic algo
//inspired from https://towardsdatascience.com/introduction-to-genetic-algorithms-including-example-code-e396e98d8bf3

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int baseX = in.nextInt();
        int baseY = in.nextInt();
        int heroesPerPlayer = in.nextInt();

        Board theBoard = new Board();
        Base myBase = new Base();
        Base enemyBase = new Base();
        myBase._x = baseX;
        myBase._y = baseY;
        enemyBase._x =  Math.abs(baseX - 17630);
        enemyBase._y =  Math.abs(baseY - 9000);


        // game loop
        while (true) {

            //initialize lists of your heroes, enemy heroes, spiders
            theBoard._myHeroes = new ArrayList<Entity>();
            theBoard._enemyHeroes = new ArrayList<Entity>();
            theBoard._spiders = new ArrayList<Entity>();

            //define your and enemy's health and mana
            for (int i = 0; i < 2; i++) {
                int health = in.nextInt();
                int mana = in.nextInt();
                if (i==0){
                    theBoard._myHealth = health;
                    theBoard._myMana = mana;
                }
                else{
                    theBoard._enemyHealth = health;
                    theBoard._enemyMana = mana;
                }
            }

            //initialize all entities
            int entityCount = in.nextInt();
            for (int i = 0; i < entityCount; i++) {
                int id = in.nextInt();
                int type = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int shieldLife = in.nextInt();
                int isControlled = in.nextInt();
                int health = in.nextInt();
                int vx = in.nextInt();
                int vy = in.nextInt();
                int nearBase = in.nextInt();
                int threatFor = in.nextInt();

                //create entity with attributes given above, add to appropriate list
                Entity theEntity = new Entity();
                theEntity._id = id;
                theEntity._type = type;
                theEntity._x = x;
                theEntity._y = y;
                theEntity._shieldLife = shieldLife;
                theEntity._isControlled = isControlled;
                theEntity._health = health;
                theEntity._vx = vx;
                theEntity._vy = vy;
                theEntity._nearBase = nearBase;
                theEntity._threatFor = threatFor;
                if (type==0){
                    theBoard._spiders.add(theEntity);
                }
                if (type==1){
                    theBoard._myHeroes.add(theEntity);
                }
                if (type==2){
                    theBoard._enemyHeroes.add(theEntity);
                }

            }
            for (int i = 0; i < heroesPerPlayer; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                System.out.println("WAIT");
            }
        }
    }
}

