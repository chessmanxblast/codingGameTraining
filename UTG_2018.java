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
