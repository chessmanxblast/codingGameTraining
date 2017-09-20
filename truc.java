import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

class Factory {
    int _id;
    int _owner;
    int _nbCyborgs;
    int _production;
}
class Troop {
    int _owner;
    int _sourceFactory;
    int _destFactory;
    int _nbCyborgs;
}
class Board {
    List<Factory> _factories;
    List<Troop> _troops;
}

class Player {

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        int factoryCount = in.nextInt(); // the number of factories
        int linkCount = in.nextInt(); // the number of links between factories
        for (int i = 0; i < linkCount; i++) {
            int factory1 = in.nextInt();
            int factory2 = in.nextInt();
            int distance = in.nextInt();
        }

        // game loop
        while (true) {

            Board theBoard = new Board();
            int entityCount = in.nextInt(); // the number of entities (e.g. factories and troops)
            for (int i = 0; i < entityCount; i++) {
                int entityId = in.nextInt();
                String entityType = in.next();
                int arg1 = in.nextInt();
                int arg2 = in.nextInt();
                int arg3 = in.nextInt();
                int arg4 = in.nextInt();
                int arg5 = in.nextInt();

                if (entityType.equals("FACTORY")) {
                    Factory theFactory = new Factory();
                    theFactory._id=entityId;
                    theFactory._owner=arg1;
                    theFactory._nbCyborgs=arg2;
                    theFactory._production=arg3;
                    // System.err.println(theBoard);
                    theBoard._factories = new ArrayList<Factory>();
                    theBoard._factories.add(theFactory);
                    System.err.println("Added factory "+theBoard._factories.size());
                    System.err.println("Factory "+theFactory._id+" of user "+theFactory._owner+" has "+theFactory._nbCyborgs+" cyborgs");
                }
                System.err.println("test");

                // if (entityType=="TROOP ") {}

            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // Any valid action, such as "WAIT" or "MOVE source destination cyborgs"
            System.out.println("WAIT");
        }
    }
}