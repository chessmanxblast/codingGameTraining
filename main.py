import sys
import math

class Factory:
    def __init__(self, iD, owner, numCyborgs, production):
        self.iD = iD
        self.owner = owner
        self.numCyborgs = numCyborgs
        self.production = production
    def function(self):
        print("This is a message inside the class.")

class Troop:
    def __init__(self, iD, owner, fromFactory, toFactory, size, ETA):
        self.iD = iD
        self.owner = owner
        self.fromFactory = fromFactory
        self.toFactory = toFactory
        self.size = size
        self.ETA = ETA
    def function(self):
        print("This is a message inside the class.")
        
class Board:
    factories = list()
    troops = list()
    def clean(self):
        del self.factories [:]
        del self.troops [:]
    def getFactory(self,id):
        for tmpfactory in self.factories[:]:
            if tmpfactory.iD == id:
                return tmpfactory
        return -1
        
import collections
graph = collections.defaultdict(dict)


factory_count = int(input())  # the number of factories
link_count = int(input())  # the number of links between factories
for i in range(link_count):
    factory_1, factory_2, distance = [int(j) for j in input().split()]
    graph[factory_1][factory_2]=distance
    graph[factory_2][factory_1]=distance
    

# game loop
while True:
    action = ""
    theBoard = Board()
    entity_count = int(input())  # the number of entities (e.g. factories and troops)
    for i in range(entity_count):
        entity_id, entity_type, arg_1, arg_2, arg_3, arg_4, arg_5 = input().split()
        entity_id = int(entity_id)
        arg_1 = int(arg_1)
        arg_2 = int(arg_2)
        arg_3 = int(arg_3)
        arg_4 = int(arg_4)
        arg_5 = int(arg_5)
        
        if entity_type == "FACTORY":
            newFactory = Factory(entity_id, arg_1, arg_2, arg_3)
            theBoard.factories.append(newFactory)
        else:
            newTroop = Troop(entity_id, arg_1, arg_2, arg_3, arg_4, arg_5)
            theBoard.troops.append(newTroop)

    ConquerFactoryId = -2            
#Build Moves        
    while (ConquerFactoryId != -1):    
    # Lets find our factory with maximum number of cyborgs            
        theNumOfCyborgsOfFactoryWithMaxNumber=0    
        for tmpfactory in theBoard.factories[:]:
            if tmpfactory.owner==1:
                if tmpfactory.numCyborgs > theNumOfCyborgsOfFactoryWithMaxNumber:
                    theNumOfCyborgsOfFactoryWithMaxNumber=tmpfactory.numCyborgs
                    aSourceFactoryID=tmpfactory.iD
    # Lets find a factory to conquer with max cyborgs production but less cyborgs  than our max cyborgs factory        
        ConquerFactoryId=-1
        numCybToSend=0
        theMaxProductionOfNeutralFactoryICanConquer=0    
        for tmpfactory in theBoard.factories[:]:
            if tmpfactory.owner==0 and \
               tmpfactory.production > theMaxProductionOfNeutralFactoryICanConquer and \
               tmpfactory.numCyborgs < theNumOfCyborgsOfFactoryWithMaxNumber:
                theMaxProductionOfNeutralFactoryICanConquer = tmpfactory.production
                ConquerFactoryId=tmpfactory.iD
                numCybToSend= tmpfactory.numCyborgs+1
        if ConquerFactoryId!=-1:
            aSourceFactory=theBoard.getFactory(aSourceFactoryID)
            aSourceFactory.numCyborgs-=numCybToSend
            aTargetFactory=theBoard.getFactory(ConquerFactoryId)
            aTargetFactory.owner=1
            aTargetFactory.numCyborgs=0
            action = action+"MOVE "+str(aSourceFactoryID)+" "+str(ConquerFactoryId)+" "+str(numCybToSend)+";"

    theBoard.clean()
    # Write an action using print
    # To debug: print("Debug messages...", file=sys.stderr)
    print(len(theBoard.factories), file=sys.stderr)
    print("theNumOfCyborgsOfFactoryWithMaxNumber "+str(theNumOfCyborgsOfFactoryWithMaxNumber), file=sys.stderr)
    print("aSourceFactoryID "+str(aSourceFactoryID), file=sys.stderr)
    print("ConquerFactoryId "+str(ConquerFactoryId), file=sys.stderr)
    # Any valid action, such as "WAIT" or "MOVE source destination cyborgs"
    action+="WAIT"
    print(action)
        
