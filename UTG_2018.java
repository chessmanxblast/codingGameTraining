import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.Math;


//constants

//structures
class Board{
    //isEqual function
    int _myHealth;
    int _myMana;
    int _enemyHealth;
    int _enemyMana;
    Base _myBase;
    Base _enemyBase;

    List<Entity> _spiders;
    List<Entity> _myHeroes;
    List<Entity> _enemyHeroes;

    //copy function
    public Board copyBoard() {
        Board copyBoard = new Board();
        copyBoard._myHealth = _myHealth;
        copyBoard._myMana = _myMana;
        copyBoard._enemyHealth = _enemyHealth;
        copyBoard._enemyMana = _enemyMana;

        copyBoard._myBase = _myBase;
        copyBoard._enemyBase = _enemyBase;

        for (int i = 0; i < _spiders.size(); i++) {
            Entity spiderCopy = _spiders.get(i).copyEntity();
            copyBoard._spiders.add(spiderCopy);
        }

        for (int i = 0; i < _myHeroes.size(); i++) {
            Entity heroCopy = _myHeroes.get(i).copyEntity();
            copyBoard._myHeroes.add(heroCopy);
        }

        for (int i = 0; i < _enemyHeroes.size(); i++) {
            Entity heroCopy = _enemyHeroes.get(i).copyEntity();
            copyBoard._enemyHeroes.add(heroCopy);
        }

        return copyBoard;
    }
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
    public Entity copyEntity(){
        Entity copyEntity = new Entity();
        copyEntity._id = _id;
        copyEntity._type = _type;
        copyEntity._x = _x;
        copyEntity._y = _y;
        copyEntity._shieldLife = _shieldLife;
        copyEntity._isControlled = _isControlled;
        copyEntity._health = _health;
        copyEntity._vx = _vx;
        copyEntity._vy = _vy;
        copyEntity._nearBase = _nearBase;
        copyEntity._threatFor = _threatFor;
        return copyEntity;
    }
}

class Base{
    int _x;
    int _y;
}
class Action{
    int _deltaX;
    int _deltaY;
    int _spell;

    Action(){}
    Action(Action iAction){
        this._deltaX=iAction._deltaX;
        this._deltaY=iAction._deltaY;
        this._spell=iAction._spell;
    }
    public static Action getRandomAction(Board iBoard){
        Action result= new Action();
        int deltaX=Utils.getRandom(800);
        int deltaY=Utils.getRandom(800);
        //todo limit to 800 length max
        result._deltaX=deltaX;
        result._deltaY=deltaY;
        return result;
    }
}

//utils
class Utils{

    public static long counter_init = System.currentTimeMillis(); // Timestamp when current turn started in ms
    public static long max_time_before_abort = 50; // Current Timestamp in ms

    // measure time since beginning of turn and return yes if more than X ms (X constant)
    public static boolean mustAbort() {
       if (System.currentTimeMillis()>counter_init+max_time_before_abort) return true; return false;
    }

    //distance between 2 points. maybe change the return type
    public static int distance() {
        return 0;//change that
    }

    //random function, return between 0 and iModulo
    public static int getRandom(int iModulo){
        Random rand = new Random();
        return rand.nextInt(iModulo);
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
//Individual class
class Individual {
    long fitness = 0;
    List<Action> genes;
    int geneLength;
    Board board;

    Individual(){
        fitness = 0;
        genes=new ArrayList<Action>();
        geneLength = 3;
        Board board=new Board();
    }

    Individual(Board iBoard, int iIndexPopulatedIndividual) {
        fitness = 0;
        genes=new ArrayList<Action>();
        geneLength = 3;

        //Set genes randomly for each individual
        board=iBoard.copyBoard();
        //to avoid horizon effect, add fitness of intermediate steps with lower weight
        long cumulatedFitness=0;

        for (int i=0;i<geneLength;i++){
            Action aRandomAction=Action.getRandomAction(board);

            genes.add(aRandomAction);
            Utils.playOneTurn(board,genes.get(i));
            long eval=Utils.evalBoard(board);

            cumulatedFitness+=eval*Math.pow(2, i);
        }

        fitness = cumulatedFitness;
        //most interesting print
        //print();
    }


    Individual copyWithoutBoard() {
        Individual result = new Individual();
        //G_NbIndividualsAnalysed++;
        result.fitness=fitness;
        result.geneLength=geneLength;
        for (int i=0;i<geneLength;i++){
            result.genes.add(new Action(genes.get(i)));
        }
        return result;
    }

    //Calculate fitness
    void calcFitness() {
        long cumulatedFitness=0;
        for(int i=0;i<genes.size();i++){
            Utils.playOneTurn(board,genes.get(i));
            cumulatedFitness+=Utils.evalBoard(board)*Math.pow(2, i);
        }
        fitness=cumulatedFitness;
//print();
    }

    /*void print(string iPrefix=""){
        cerr<<iPrefix<<"if:"<<fitness<<" gl:"<<geneLength<<" g:";
        for(int i=0;i<genes.size();i++){
            cerr<<genes.get(i)._deltaX<<" "<<genes.get(i)._deltaY<<" "<<genes.get(i)._structureToBuild<<" ";
        }

        cerr<<endl;
    }*/
}

//Population class
class Population {

    int popSize;
    List<Individual> individuals;
    long fittest;
    Board board;

    Population(){
        popSize = 21;
        individuals=new ArrayList<Individual>();
        fittest = 0;
        board=new Board();
    }

    //Initialize population
    void initializePopulation(Board iBoard) {
        board=iBoard;
        for (int i = 0; i < popSize && !Utils.mustAbort(); i++) {
            individuals.add(new Individual(board,i));
            //G_NbIndividualsAnalysed++;
        }
    }


    //Get the fittest individual
    Individual getFittest() {
        long maxFit = Long.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.size(); i++) {
            if (maxFit <= individuals.get(i).fitness) {
                maxFit = individuals.get(i).fitness;
                maxFitIndex = i;
            }
        }
        fittest = individuals.get(maxFitIndex).fitness;
        return individuals.get(maxFitIndex);
    }

    //Get the second most fittest individual
    Individual getSecondFittest() {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < individuals.size(); i++) {
            if (individuals.get(i).fitness > individuals.get(maxFit1).fitness) {
                maxFit2 = maxFit1;
                maxFit1 = i;
            }
            else if (individuals.get(i).fitness > individuals.get(maxFit2).fitness) {
                maxFit2 = i;
            }
        }
        return individuals.get(maxFit2);
    }

    //Get index of least fittest individual
    int getLeastFittestIndex() {
        long minFitVal = Long.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individuals.size(); i++) {
            if (minFitVal >= individuals.get(i).fitness) {
                minFitVal = individuals.get(i).fitness;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

}

//Main class
class SimpleDemoGA {

    Population population = new Population();
    Individual fittest;
    Individual secondFittest;
    int generationCount = 0;

    static Action demo(Board iBoard) {
        SimpleDemoGA demo = new SimpleDemoGA();

        //Initialize population
        demo.population.initializePopulation(iBoard);

        //Calculate fitness of each individual is already done within population init. this just initialize the fittest attribute of the pop object
        demo.population.getFittest();

        // cerr<<"Generation: " << demo.generationCount << " Fittest: " << demo.population.fittest<<endl;

        //While we have time
        while (!Utils.mustAbort()) {
            //        while (demo.generationCount<30) {
            ++demo.generationCount;

            //Do selection
            demo.selection();

            //if (demo.generationCount<35 && G_DEBUG)cerr<<"after select:"<<endl;
            //if (demo.generationCount<10)demo.fittest.print();
            //if (demo.generationCount<35 && G_DEBUG)demo.secondFittest.print();
            //Do crossover
            demo.crossover();


            //if (demo.generationCount<35 && G_DEBUG)cerr<<"after crossover:"<<endl;
            //if (demo.generationCount<35 && G_DEBUG)demo.fittest.print();
            //if (demo.generationCount<35 && G_DEBUG)demo.secondFittest.print();

            //Do mutation under a random probability
            if (Utils.getRandom(7) < 5) {
                demo.mutation();
            }

            //if (demo.generationCount<35 && G_DEBUG)cerr<<"after mutation:"<<endl;
            //if (demo.generationCount<35 && G_DEBUG)demo.fittest.print();
            //if (demo.generationCount<35 && G_DEBUG)demo.secondFittest.print();

            //Update fitness values of offspring
            demo.fittest.board=iBoard.copyBoard();
            demo.fittest.calcFitness();
            demo.secondFittest.board=iBoard.copyBoard();
            demo.secondFittest.calcFitness();

            //if (demo.generationCount<35)demo.fittest.print("fittest after offspring ");
            //if (demo.generationCount<35)demo.secondFittest.print("secondFittest after offspring ");

            //Add fittest offspring to population
            demo.addFittestOffspring();

            //if (demo.generationCount<35 && G_DEBUG)cerr<<"new getFittestOffspring():"<<demo.getFittestOffspring().fitness<<endl;

        }

    /*cerr<<"\nSolution found in generation " << demo.generationCount<<endl;
    cerr<<"Fitness: "<<demo.population.getFittest().fitness<<endl;
    cerr<<"Genes: "<<endl;
        for (int i = 0; i < 5; i++) {
    cerr<<demo.population.getFittest().genes.get(i)<<endl;
        }

    cerr<<""<<endl;*/

        return demo.population.getFittest().genes.get(0);
    }

    //Selection
    void selection() {

        //Select the most fittest individual
        fittest = population.getFittest().copyWithoutBoard();

        //Select the second most fittest individual
        secondFittest = population.getSecondFittest().copyWithoutBoard();
    }

    //Crossover
    void crossover() {


        //Select a random crossover point
        //special here: not same point on both
        //CAREFUL: might not work in other Genetic Algo where some Actions cannot be reordered.
        int nbCrossOvers=Utils.getRandom(population.individuals.get(0).geneLength);
        List<Integer> fittestCrossoveredGenes=new ArrayList<Integer>();
        List<Integer> secondFittestCrossoveredGenes=new ArrayList<Integer>();
        int nbTries=0;
        for (int i=0;i<nbCrossOvers;i++) {
            int crossOverPointFittest = -1;
            boolean crossOverPointFittestAlreadyCovered=true;
            nbTries=0;
            while (crossOverPointFittestAlreadyCovered && nbTries<30) {
                crossOverPointFittest = Utils.getRandom(population.individuals.get(0).geneLength);
                nbTries++;
                if(!fittestCrossoveredGenes.contains(crossOverPointFittest)) {
                    crossOverPointFittestAlreadyCovered=false;
                }
            }
            fittestCrossoveredGenes.add(crossOverPointFittest);

            int crossOverPointSecondFittest = -1;
            boolean crossOverPointSecondFittestAlreadyCovered=true;
            nbTries=0;
            while (crossOverPointSecondFittestAlreadyCovered && nbTries<30) {
                crossOverPointSecondFittest = Utils.getRandom(population.individuals.get(0).geneLength);
                nbTries++;
                if(!secondFittestCrossoveredGenes.contains(crossOverPointSecondFittest)) {
                    crossOverPointSecondFittestAlreadyCovered=false;
                }
            }
            secondFittestCrossoveredGenes.add(crossOverPointSecondFittest);

            Action temp = fittest.genes.get(crossOverPointFittest);
            fittest.genes.set(crossOverPointFittest,secondFittest.genes.get(crossOverPointSecondFittest));
            secondFittest.genes.set(crossOverPointSecondFittest,temp);
        }

        //Swap values among parents
        /*int crossOverPoint = Utils.(population.individuals[0].geneLength);
        for (int i = crossOverPoint-1; i >= 0; i--) {
            Action temp = fittest.genes.get(i);
            fittest.genes.get(i) = secondFittest.genes.get(i);
            secondFittest.genes.get(i) = temp;


            //if only 1 of them is a build, cannot go further (would ruin build preconditions most probably by not touching)
            if ((fittest.genes.get(i)._structureToBuild!=K_BUILD_NOTHING
                        && secondFittest.genes.get(i)._structureToBuild==K_BUILD_NOTHING)
                                ||
                    (secondFittest.genes.get(i)._structureToBuild!=K_BUILD_NOTHING
                        && fittest.genes.get(i)._structureToBuild==K_BUILD_NOTHING)) {
                break;
            }
        }*/
    }

    //Mutation
    void mutation() {


        //Select a random mutation point
        int mutationPoint = Utils.getRandom(population.individuals.get(0).geneLength);
        int mutationImpactX = Utils.getRandom(10)-5;
        int mutationImpactY = Utils.getRandom(10)-5;

        //slightly modify values at the mutation point
        /*if (fittest.genes.get(mutationPoint)._structureToBuild!=K_BUILD_NOTHING) {
            fittest.genes.get(mutationPoint)._structureToBuild=Utils.getRandom(6);
        }
        if (fittest.genes.get(mutationPoint)._structureToBuild==K_BUILD_NOTHING) {*/
            fittest.genes.get(mutationPoint)._deltaX+=mutationImpactX;
            fittest.genes.get(mutationPoint)._deltaY+=mutationImpactY;
        /*}*/

        mutationPoint = Utils.getRandom(population.individuals.get(0).geneLength);
        mutationImpactX = Utils.getRandom(10)-5;
        mutationImpactY = Utils.getRandom(10)-5;

        /*if (secondFittest.genes.get(mutationPoint)._structureToBuild!=K_BUILD_NOTHING) {
            secondFittest.genes.get(mutationPoint)._structureToBuild=Utils.getRandom(6);
        }
        if (secondFittest.genes.get(mutationPoint)._structureToBuild==K_BUILD_NOTHING) {*/
            secondFittest.genes.get(mutationPoint)._deltaX+=mutationImpactX;
            secondFittest.genes.get(mutationPoint)._deltaY+=mutationImpactY;
        /*}*/
    }

    //Get fittest offspring
    Individual getFittestOffspring() {
        if (fittest.fitness > secondFittest.fitness) {
            return fittest;
        }
        return secondFittest;
    }


    //Replace least fittest individual from most fittest offspring
    void addFittestOffspring() {


        //Get index of least fit individual
        int leastFittestIndex = population.getLeastFittestIndex();

        //Replace least fittest individual from most fittest offspring
        //getFittestOffspring().print();
        population.individuals.set(leastFittestIndex,getFittestOffspring().copyWithoutBoard());
        //population.individuals.get(leastFittestIndex).print("offspring ");
    }

}

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
        enemyBase._x = Math.abs(baseX - 17630);
        enemyBase._y = Math.abs(baseY - 9000);


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
                if (i == 0) {
                    theBoard._myHealth = health;
                    theBoard._myMana = mana;
                } else {
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
                if (type == 0) {
                    theBoard._spiders.add(theEntity);
                }
                if (type == 1) {
                    theBoard._myHeroes.add(theEntity);
                }
                if (type == 2) {
                    theBoard._enemyHeroes.add(theEntity);
                }

            }

            // initialize counter
            Utils.counter_init = System.currentTimeMillis();

            Action theBestAction=SimpleDemoGA.demo(theBoard);

            for (int i = 0; i < heroesPerPlayer; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");


                if (theBoard._spiders.size()==0){
                    System.out.println("MOVE 4800 1300");
                    System.out.println("MOVE 3500 3500");
                    System.out.println("MOVE 1300 4800");
                }
                else {
                    System.out.println("WAIT");
                }


                System.err.println("Time is: "+ System.currentTimeMillis());
                System.err.println("Must abort: "+Utils.mustAbort());
            }
        }
    }
}