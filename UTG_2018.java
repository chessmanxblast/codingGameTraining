import java.util.*;
import java.io.*;
import java.math.*;


//constants

//structures
class Board{
    //copy function

    //isEqual function

}

class Move{
    //copy function

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
    public static void playOneTurn(Board iBoard, Move iMove){

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

        // game loop
        while (true) {
            for (int i = 0; i < 2; i++) {
                int health = in.nextInt();
                int mana = in.nextInt();
            }
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
            }
            for (int i = 0; i < heroesPerPlayer; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                System.out.println("WAIT");
            }
        }
    }
}

