import java.util.*;
import java.io.*;
import java.math.*;

class Board{
    List<Planet> _planets;
    List<int> _myPlanets;
    List<int> _enemyPlanets;
    List<int> _neutralPlanets;
    List<int> _assignablePlanets;
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
    int _distanceFromClosestEnemy;
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int planetCount = in.nextInt();
        int edgeCount = in.nextInt();

        Board theBoard = new Board();
        theBoard._planets = new ArrayList<Planet>();

        for (int i = 0; i < planetCount; i++){
            Planet thePlanet = new Planet();
            thePlanet._id = i;
            thePlanet._owner = 0;
            thePlanet._neighbors = new ArrayList<Planet>();
            theBoard._planets.add(thePlanet);
        }

        for (int i = 0; i < edgeCount; i++) {
            int planetA = in.nextInt();
            int planetB = in.nextInt();
            theBoard._planets.get(planetA)._neighbors.add(theBoard._planets.get(planetB));
            theBoard._planets.get(planetB)._neighbors.add(theBoard._planets.get(planetA));
        }



        // game loop
        while (true) {
            //reinitializes each turn the lists of my planets, enemy, neutral planets, and planets i can send units to
            theBoard._myPlanets = new ArrayList<int>();
            theBoard._enemyPlanets = new ArrayList<int>();
            theBoard._neutralPlanets = new ArrayList<int>();
            theBoard._assignablePlanets = new ArrayList<int>();

            for (int i = 0; i < planetCount; i++) {
                int myUnits = in.nextInt();
                int myTolerance = in.nextInt();
                int otherUnits = in.nextInt();
                int otherTolerance = in.nextInt();
                int canAssign = in.nextInt();

                //define planet parameters
                theBoard._planets.get(i)._myUnits = myUnits;
                theBoard._planets.get(i)._myTolerance = myTolerance;
                theBoard._planets.get(i)._otherUnits = otherUnits;
                theBoard._planets.get(i)._otherTolerance = otherTolerance;
                theBoard._planets.get(i)._canAssign = canAssign;

                //if I have more units on planet that him, I am owner; add ID to list of planets I control
                if (theBoard._planets.get(i)._myUnits > theBoard._planets.get(i)._otherUnits){
                    theBoard._planets.get(i)._owner = 1;
                    theBoard._myPlanets.add(i);
                }

                //if he has more units on planet, he is owner; add ID to list of planets he controls
                if (theBoard._planets.get(i)._myUnits < theBoard._planets.get(i)._otherUnits){
                    theBoard._planets.get(i)._owner = -1;
                    theBoard._enemyPlanets.add(i);
                }

                //if the owner is still 0, add ID to the list of neutral planets
                if (theBoard._planets.get(i)._owner == 0) {
                    theBoard._neutralPlanets.add(i);
                }

                //if we can assign units to the planet, add ID to list of assignable planets
                if (theBoard._planets.get(i)._canAssign == 1){
                    theBoard._assignablePlanets.add(i);
                }

            }

            //calculate distances from enemy
            for (int i = 0; i < planetCount; i++) {
                //reinit
                if (theBoard._planets.get(i)._owner == -1) {
                    theBoard._planets.get(i)._distanceFromClosestEnemy = 0;
                }
                else {
                    //-1 here means "not yet calculated"
                    theBoard._planets.get(i)._distanceFromClosestEnemy = -1;
                }
            }
            //now, increment by increment, let's populate the neighbours
            boolean stillSomePlanetToCheck=true;
            //each loop we will only allocate this distance
            int currentDistance=1;
            while (stillSomePlanetToCheck) {
                stillSomePlanetToCheck=false;
                System.err.println("checking round");
                //those not yet calculated that are neighbour of a calculated get's its distance+1
                for (int i = 0; i < planetCount; i++) {
                    Planet planetToCheck=theBoard._planets.get(i);
                    if (planetToCheck._distanceFromClosestEnemy == -1) {
                        stillSomePlanetToCheck=true;
                        int minNeighborsDist=10000;
                        for (int j = 0; j < planetToCheck._neighbors.size(); j++) {
                            if (planetToCheck._neighbors.get(j)._distanceFromClosestEnemy!=-1
                                        && planetToCheck._neighbors.get(j)._distanceFromClosestEnemy<minNeighborsDist) {
                                minNeighborsDist=planetToCheck._neighbors.get(j)._distanceFromClosestEnemy;
                            }
                        }
                        if (minNeighborsDist!=10000
                                && currentDistance==minNeighborsDist+1) {
                            planetToCheck._distanceFromClosestEnemy = currentDistance;
                            System.err.println(i + " is at distance " + theBoard._planets.get(i)._distanceFromClosestEnemy + " from ennemy");
                        }
                    }
                }
                currentDistance++;
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