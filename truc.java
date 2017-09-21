import java.util.*;
import java.io.*;
import java.math.*;

class Board{
    List<Planet> _planets;
    List<Integer> _myPlanets;
    List<Integer> _enemyPlanets;
    List<Integer> _neutralPlanets;
    List<Integer> _assignablePlanets;
}

class Planet{
    int _id;
    int _myUnits;
    int _myTolerance;
    int _otherUnits;
    int _otherTolerance;
    int _canAssign;
    int _owner;
    List<Planet> _neighbors;
    int _numberOfNeighbors;
    int _numberOfAssignableNeigbors;
    int _numberOfNeutralNeighbors;
    int _numberOfFriendNeighbors;
    int _numberOfEnemyNeighbors;
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int planetCount = in.nextInt();
        int edgeCount = in.nextInt();

        Board theBoard = new Board();
        theBoard._planets = new ArrayList<Planet>();

        //create all of the planets with IDs; initialize planets as neutral; create a list of neighbor planets
        //for each planet; add each planet to theBoard's list of planets.
        for (int i = 0; i < planetCount; i++){
            Planet thePlanet = new Planet();
            thePlanet._id = i;
            thePlanet._owner = 0;
            thePlanet._neighbors = new ArrayList<Planet>();
            theBoard._planets.add(thePlanet);
        }

        //for each edge, make neighboring planets add eachother to their list of neighbors
        for (int i = 0; i < edgeCount; i++){
            int planetA = in.nextInt();
            int planetB = in.nextInt();
            theBoard._planets.get(planetA)._neighbors.add(theBoard._planets.get(planetB));
            theBoard._planets.get(planetB)._neighbors.add(theBoard._planets.get(planetA));
        }

        // GAME LOOP
        while (true) {
            //reinitializes each turn the lists of my planets, enemy, neutral planets, and planets I can send units to
            theBoard._myPlanets = new ArrayList<Integer>();
            theBoard._enemyPlanets = new ArrayList<Integer>();
            theBoard._neutralPlanets = new ArrayList<Integer>();
            theBoard._assignablePlanets = new ArrayList<Integer>();

            //define planet parameters
            for (int i = 0; i < planetCount; i++) {
                int myUnits = in.nextInt();
                int myTolerance = in.nextInt();
                int otherUnits = in.nextInt();
                int otherTolerance = in.nextInt();
                int canAssign = in.nextInt();

                theBoard._planets.get(i)._myUnits = myUnits;
                theBoard._planets.get(i)._myTolerance = myTolerance;
                theBoard._planets.get(i)._otherUnits = otherUnits;
                theBoard._planets.get(i)._otherTolerance = otherTolerance;
                theBoard._planets.get(i)._canAssign = canAssign;

                //if I have more units on planet than enemy, make me owner; add ID to list of planets I control
                if (theBoard._planets.get(i)._myUnits > theBoard._planets.get(i)._otherUnits){
                    theBoard._planets.get(i)._owner = 1;
                    theBoard._myPlanets.add(i);
                }

                //if enemy has more units on planet than me, make him owner; add ID to list of planets he controls
                if (theBoard._planets.get(i)._myUnits < theBoard._planets.get(i)._otherUnits){
                    theBoard._planets.get(i)._owner = -1;
                    theBoard._enemyPlanets.add(i);
                }

                //if the owner is still 0, add ID to the list of neutral planets
                if (theBoard._planets.get(i)._owner == 0){
                    theBoard._neutralPlanets.add(i);
                }

                //if we can assign units to the planet, add ID to list of assignable planets
                if (theBoard._planets.get(i)._canAssign == 1){
                    theBoard._assignablePlanets.add(i);
                }

            }

            //define different planet neighbor counts
            for (int i = 0; i < planetCount; i++) {
                //define the number of neighbors each planet has; initialize the other neighbor numbers back to zero
                theBoard._planets.get(i)._numberOfNeighbors = theBoard._planets.get(i)._neighbors.size();
                theBoard._planets.get(i)._numberOfAssignableNeigbors = 0;
                theBoard._planets.get(i)._numberOfNeutralNeighbors = 0;
                theBoard._planets.get(i)._numberOfFriendNeighbors = 0;
                theBoard._planets.get(i)._numberOfEnemyNeighbors = 0;

                //for planet i, loop over all of its neigbors. based on owner and assignability,
                //increment planet i's assignable, neutral, friendly, and enemy neighbor counts
                for (int n = 0; n < theBoard._planets.get(i)._numberOfNeighbors; n++){
                    if (theBoard._planets.get(i)._neighbors.get(n)._canAssign == 1){
                        theBoard._planets.get(i)._numberOfAssignableNeigbors++;
                    }
                    if (theBoard._planets.get(i)._neighbors.get(n)._owner == 0){
                        theBoard._planets.get(i)._numberOfNeutralNeighbors++;
                    }
                    if (theBoard._planets.get(i)._neighbors.get(n)._owner == 1){
                        theBoard._planets.get(i)._numberOfFriendNeighbors++;
                    }
                    if (theBoard._planets.get(i)._neighbors.get(n)._owner == -1){
                        theBoard._planets.get(i)._numberOfEnemyNeighbors++;
                    }
                }
                //System.err.println("planet #" + i + " numNeigh:" + theBoard._planets.get(i)._numberOfNeighbors + " numAss:" + theBoard._planets.get(i)._numberOfAssignableNeigbors + " numNeut:" + theBoard._planets.get(i)._numberOfNeutralNeighbors + " numMy:"+ theBoard._planets.get(i)._numberOfFriendNeighbors + " numEnm:" + theBoard._planets.get(i)._numberOfEnemyNeighbors);
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("0");
            System.out.println("0");
            System.out.println("0");
            System.out.println("0");
            System.out.println("0");
            System.out.println("NONE");
        }
    }
}