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
    int _distanceFromClosestEnemy;
    int _ranking; // the higher the value, the best to send unit to
    int _nbNeededUnitsToFeelSafe;
    int _numberOfNeighbors;
    int _numberOfAssignableNeigbors;
    int _numberOfNeutralNeighbors;
    int _numberOfVulnerableNeutralNeighbors;
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
                if (theBoard._planets.get(i)._myUnits == theBoard._planets.get(i)._otherUnits){
                    theBoard._neutralPlanets.add(i);
                    theBoard._planets.get(i)._owner = 0;
                }

                //if we can assign units to the planet, add ID to list of assignable planets
                if (theBoard._planets.get(i)._canAssign == 1){
                    theBoard._assignablePlanets.add(i);
                }

            }

            //calculate distances from enemy
            for (int i = 0; i < planetCount; i++) {
                //reset _distanceFromClosestEnemy
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
//                System.err.println("checking round");
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
//                            System.err.println(i + " is at distance " + theBoard._planets.get(i)._distanceFromClosestEnemy + " from ennemy");
                        }
                    }
                }
                currentDistance++;
            }


            //define different planet neighbor counts
            for (int i = 0; i < planetCount; i++) {
                //define the number of neighbors each planet has; initialize the other neighbor numbers back to zero
                theBoard._planets.get(i)._numberOfNeighbors = theBoard._planets.get(i)._neighbors.size();
                theBoard._planets.get(i)._numberOfAssignableNeigbors = 0;
                theBoard._planets.get(i)._numberOfNeutralNeighbors = 0;
                theBoard._planets.get(i)._numberOfFriendNeighbors = 0;
                theBoard._planets.get(i)._numberOfVulnerableNeutralNeighbors = 0;
                theBoard._planets.get(i)._numberOfEnemyNeighbors = 0;

                //for planet i, loop over all of its neigbors. based on owner and assignability,
                //increment planet i's assignable, neutral, friendly, and enemy neighbor counts
                for (int n = 0; n < theBoard._planets.get(i)._numberOfNeighbors; n++){
                    if (theBoard._planets.get(i)._neighbors.get(n)._canAssign == 1){
                        theBoard._planets.get(i)._numberOfAssignableNeigbors++;
                    }
                    if (theBoard._planets.get(i)._neighbors.get(n)._owner == 0){
                        theBoard._planets.get(i)._numberOfNeutralNeighbors++;
                        if (theBoard._planets.get(i)._neighbors.get(n)._distanceFromClosestEnemy==1) {
                            theBoard._planets.get(i)._numberOfVulnerableNeutralNeighbors++;
                            if (i==27){System.err.println("toto 7 ");}
                        }
                    }
                    if (theBoard._planets.get(i)._neighbors.get(n)._owner == 1){
                        theBoard._planets.get(i)._numberOfFriendNeighbors++;
                        if (i==27){System.err.println("toto 6 ");}
                    }
                    if (theBoard._planets.get(i)._neighbors.get(n)._owner == -1){
                        theBoard._planets.get(i)._numberOfEnemyNeighbors++;
                    }
                }
                //System.err.println("planet #" + i + " numNeigh:" + theBoard._planets.get(i)._numberOfNeighbors + " numAss:" + theBoard._planets.get(i)._numberOfAssignableNeigbors + " numNeut:" + theBoard._planets.get(i)._numberOfNeutralNeighbors + " numMy:"+ theBoard._planets.get(i)._numberOfFriendNeighbors + " numEnm:" + theBoard._planets.get(i)._numberOfEnemyNeighbors);
            }


            //check if we already encountered the enemy
            int closestDistanceBetweenUsAndEnemy=10000;
            for (int i = 0; i < planetCount; i++) {
                if (theBoard._planets.get(i)._owner == 1
                        && theBoard._planets.get(i)._distanceFromClosestEnemy <closestDistanceBetweenUsAndEnemy){
                    closestDistanceBetweenUsAndEnemy=theBoard._planets.get(i)._distanceFromClosestEnemy;
                }
            }

            // get the ranking of each planet
            for (int i = 0; i < planetCount; i++) {
                //reset _ranking -
                theBoard._planets.get(i)._ranking=0;
                theBoard._planets.get(i)._nbNeededUnitsToFeelSafe=0;


                if (theBoard._planets.get(i)._owner==0) {
                    if (i==17){System.err.println("toto 1");}
                    for (int j = 0; j < theBoard._planets.get(i)._neighbors.size(); j++) {
                        Planet neighbor = theBoard._planets.get(i)._neighbors.get(j);
                        if (i==17){System.err.println("toto 2 "+neighbor._id+" "+neighbor._owner);}
                        if (i==17){System.err.println("toto 4 "+neighbor._id+" "+neighbor._numberOfVulnerableNeutralNeighbors);}
                        if (i==17){System.err.println("toto 5 "+neighbor._id+" "+neighbor._numberOfFriendNeighbors);}
                        if (neighbor._owner==1
                                && neighbor._numberOfVulnerableNeutralNeighbors>neighbor._numberOfFriendNeighbors) {
                            if (i==17){System.err.println("toto 3");}
                            theBoard._planets.get(i)._ranking = 50;
                        }
                    }
                }

                if (closestDistanceBetweenUsAndEnemy>1
                        && theBoard._planets.get(i)._owner==0) {
                    // the closest the distance from enemy, the better, while we are in the attack phase
                    theBoard._planets.get(i)._ranking = 40-theBoard._planets.get(i)._distanceFromClosestEnemy;
                }
                else if (closestDistanceBetweenUsAndEnemy==1
                        && theBoard._planets.get(i)._owner==0
                        && theBoard._planets.get(i)._distanceFromClosestEnemy==2) {
                    //dist 2 is support
                    theBoard._planets.get(i)._ranking = 40;
                }
                else if (closestDistanceBetweenUsAndEnemy==1
                        && theBoard._planets.get(i)._owner==0
                        && theBoard._planets.get(i)._distanceFromClosestEnemy==1) {
                    theBoard._planets.get(i)._ranking = 30;
                }
                else if (closestDistanceBetweenUsAndEnemy==1
                        && theBoard._planets.get(i)._owner==1
                        && theBoard._planets.get(i)._distanceFromClosestEnemy==1) {
                    theBoard._planets.get(i)._ranking = 20;
                }

                if (theBoard._planets.get(i)._numberOfFriendNeighbors>=theBoard._planets.get(i)._numberOfEnemyNeighbors
                        && theBoard._planets.get(i)._myUnits<=theBoard._planets.get(i)._otherUnits+5
                        && theBoard._planets.get(i)._distanceFromClosestEnemy==1) {
                    theBoard._planets.get(i)._ranking+=10;
                    theBoard._planets.get(i)._nbNeededUnitsToFeelSafe=theBoard._planets.get(i)._otherUnits+6-theBoard._planets.get(i)._myUnits;
                    System.err.println("Planet " + i +" needs help: "+theBoard._planets.get(i)._nbNeededUnitsToFeelSafe);
                }

                System.err.println("Planet " + i +" has a ranking of "+theBoard._planets.get(i)._ranking);
            }
           // System.err.println(" we have " + theBoard._bestPlanetsToTarget.size()+" good planets to target");

            // get the top 5 planets to send units to
            // ArrayUtils arrayUtils=new ArrayUtils();
            ArrayList<Integer> bestPlanetToTarget = new ArrayList<Integer>();
            for (int k=0; k<5; k++) {
                int maxRankingSoFar=-1000000;
                int planetToAdd = -1;
                for (int i = 0; i < planetCount; i++) {
                    if (theBoard._planets.get(i)._ranking>maxRankingSoFar
                            && theBoard._planets.get(i)._canAssign>0) {
                        //
                        if (!bestPlanetToTarget.contains(i)) {
                            planetToAdd=i;
                            maxRankingSoFar=theBoard._planets.get(i)._ranking;
                        }
                    }
                }
                if (planetToAdd!=-1) {
                    bestPlanetToTarget.add(planetToAdd);
                }
            }

            if (bestPlanetToTarget.size()==0) {
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("NONE");
            }
            else {
                int topBestPlanetToTarget = bestPlanetToTarget.get(0);
                System.err.println("topBestPlanetToTarget : " + topBestPlanetToTarget + " at distance " + theBoard._planets.get(topBestPlanetToTarget)._distanceFromClosestEnemy);


                if (theBoard._planets.get(topBestPlanetToTarget)._distanceFromClosestEnemy > 2
                        && closestDistanceBetweenUsAndEnemy > 1) {
                    //if closest planet from enemy is still far from enemy (closestDistance>1), move the fastest possible to it using spread
                    System.out.println(topBestPlanetToTarget);
                    System.out.println(topBestPlanetToTarget);
                    System.out.println(topBestPlanetToTarget);
                    System.out.println(topBestPlanetToTarget);
                    System.out.println(topBestPlanetToTarget);
                    System.out.println(topBestPlanetToTarget);
                } else {

                    int nbUnitsAlreadySent = 0;
                    // send units to each planet until the planet is safe
                    for (int k = 0; k < bestPlanetToTarget.size(); k++) {
                        Planet theCurrentPlanet = theBoard._planets.get(bestPlanetToTarget.get(k));

                        if (theCurrentPlanet._nbNeededUnitsToFeelSafe > 0) {
                            while (theCurrentPlanet._nbNeededUnitsToFeelSafe > 0
                                    && nbUnitsAlreadySent < 5) {
                                System.out.println(bestPlanetToTarget.get(k));

                                nbUnitsAlreadySent++;
                                theCurrentPlanet._nbNeededUnitsToFeelSafe--;
                            }
                        } else {
                            if (nbUnitsAlreadySent < 5) {
                                System.out.println(bestPlanetToTarget.get(k));
                                nbUnitsAlreadySent++;
                            }
                        }
                    }
                    // send remaining units (if any left)
                    for (int k = nbUnitsAlreadySent; k < 5; k++) {
                        System.out.println(bestPlanetToTarget.get(0));
                        nbUnitsAlreadySent++;
                    }
                    System.out.println("NONE");
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

           // System.out.println("0");
           // System.out.println("0");
           // System.out.println("0");
           // System.out.println("0");
           // System.out.println("0");
        }
    }
}