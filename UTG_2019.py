
import sys
import math
import copy

# Deliver more amadeusium to hq (left side of the map) than your opponent. Use radars to find amadeusium but beware of traps!

# height: size of the map
width, height = [int(i) for i in input().split()]

NONE = -1
ROBOT_ALLY = 0
ROBOT_ENEMY = 1
HOLE = 1
RADAR = 2
TRAP = 3
AMADEUSIUM = 4




class Pos:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def distance(self, pos):
        return abs(self.x - pos.x) + abs(self.y - pos.y)
        
    def changeXYtoAdjacentNonDanger(self,dGrid):
        if dGrid.get_cell(self.x,self.y).danger == True:
            if dGrid.get_cell(self.x,self.y-1).danger == False and y > 0:
                self.y =  self.y - 1
            elif dGrid.get_cell(self.x-1,self.y).danger == False and x > 1:
                self.x =  self.x - 1
            elif dGrid.get_cell(self.x-1,self.y-1).danger == False and x > 1 and y > 0 :
                self.x =  self.x - 1
                self.y =  self.y - 1
            elif dGrid.get_cell(self.x-1,self.y+1).danger == False and x > 1 and y < 14:
                self.x =  self.x - 1
                self.y =  self.y + 1
            elif dGrid.get_cell(self.x+1,self.y-1).danger == False and x < 29 and y > 0 :
                self.x =  self.x + 1
                self.y =  self.y - 1
            elif dGrid.get_cell(self.x+1,self.y).danger == False and x < 29 :
                self.x =  self.x + 1
            elif dGrid.get_cell(self.x,self.y+1).danger == False and y < 14 :
                self.y =  self.y + 1
            elif dGrid.get_cell(self.x+1,self.y+1).danger == False and x < 29 and y < 14 :
                self.x =  self.x + 1
                self.y =  self.y + 1
            
        

IDEALRADARLOCATIONS = [Pos(11,4),Pos(12,11),Pos(17,7),Pos(23,4),Pos(24,11),Pos(29,7),Pos(18,14),Pos(5,11)]

class Entity(Pos):
    def __init__(self, x, y, type, id):
        super().__init__(x, y)
        self.type = type
        self.id = id


class Robot(Entity):
    def __init__(self, x, y, type, id, item):
        super().__init__(x, y, type, id)
        self.item = item
        self.action = "WAIT"

    def is_dead(self):
        return self.x == -1 and self.y == -1
    
    def doAction(self):
        print(self.action)
        
    @staticmethod
    def move(x, y, message=""):
        print(f"MOVE {x} {y} {message}")

    @staticmethod
    def wait(message=""):
        print(f"WAIT {message}")

    @staticmethod
    def dig(x, y, message=""):
        print(f"DIG {x} {y} {message}")

    @staticmethod
    def request(requested_item, message=""):
        if requested_item == RADAR:
            print(f"REQUEST RADAR {message}")
        elif requested_item == TRAP:
            print(f"REQUEST TRAP {message}")
        else:
            raise Exception(f"Unknown item {requested_item}")


class Cell(Pos):
    def __init__(self, x, y, amadeusium, hole):
        super().__init__(x, y)
        self.amadeusium = amadeusium
        self.hole = hole
        self.danger=False

    def has_hole(self):
        return self.hole == HOLE

    def update(self, amadeusium, hole):
        self.amadeusium = amadeusium
        self.hole = hole



class Grid:
    def __init__(self):
        self.cells = []
        for y in range(height):
            for x in range(width):
                self.cells.append(Cell(x, y, 0, 0))

    def get_cell(self, x, y):
        if width > x >= 0 and height > y >= 0:
            return self.cells[x + width * y]
        return None

    def print(self):
        for y in range(15):
            line=""
            for x in range(30):
                line+=self.get_cell(x, y).amadeusium
            print(line, file=sys.stderr)

class Game:
    def __init__(self):
        self.grid = Grid()
        self.my_score = 0
        self.enemy_score = 0
        self.radar_cooldown = 0
        self.trap_cooldown = 0
        self.radars = []
        self.traps = []
        self.my_robots = []
        self.enemy_robots = []

    def reset(self):
        self.radars = []
        self.traps = []
        self.my_robots = []
        self.enemy_robots = []


game = Game()
nbTurn=0;
dangerGrid=Grid()
enemyRadarGrid=Grid()
# game loop
while True:
    nbTurn=nbTurn+1
    # my_score: Players score
    game.my_score, game.enemy_score = [int(i) for i in input().split()]
    for i in range(height):
        inputs = input().split()
        for j in range(width):
            # amadeusium: amount of amadeusium or "?" if unknown
            # hole: 1 if cell has a hole
            amadeusium = inputs[2 * j]
            hole = int(inputs[2 * j + 1])
            game.grid.get_cell(j, i).update(amadeusium, hole)
    # entity_count: number of entities visible to you
    # radar_cooldown: turns left until a new radar can be requested
    # trap_cooldown: turns left until a new trap can be requested
    entity_count, game.radar_cooldown, game.trap_cooldown = [int(i) for i in input().split()]

    game.reset()
    

    for i in range(entity_count):
        # id: unique id of the entity
        # type: 0 for your robot, 1 for other robot, 2 for radar, 3 for trap
        # y: position of the entity
        # item: if this entity is a robot, the item it is carrying (-1 for NONE, 2 for RADAR, 3 for TRAP, 4 for AMADEUSIUM)
        id, type, x, y, item = [int(j) for j in input().split()]

        if type == ROBOT_ALLY:
            game.my_robots.append(Robot(x, y, type, id, item))
        elif type == ROBOT_ENEMY:
            game.enemy_robots.append(Robot(x, y, type, id, item))
        elif type == TRAP:
            game.traps.append(Entity(x, y, type, id))
        elif type == RADAR:
            game.radars.append(Entity(x, y, type, id))

    #game.grid.print()

    # mark the cells we put traps in as danger cells so that we don't blow up our own robots
    for trap in game.traps:
        for cell in game.grid.cells:
            if cell.x == trap.x and cell.y == trap.y:
                dangerGrid.get_cell(cell.x,cell.y).danger = True


    
    currentGame=copy.deepcopy(game)
    
    #looks at how many radars have been placed, uses that to pull the location of the next radar from the list IDEALRADARLOCATIONS and create a position object NEXTRADARPOS
    NUMBEROFRADARS = len(game.radars)
    if NUMBEROFRADARS < len(IDEALRADARLOCATIONS):
        NEXTRADARPOS = Pos(IDEALRADARLOCATIONS[NUMBEROFRADARS].x,IDEALRADARLOCATIONS[NUMBEROFRADARS].y)
        if dangerGrid.get_cell(NEXTRADARPOS.x,NEXTRADARPOS.y).danger == True:
            NEXTRADARPOS.changeXYtoAdjacentNonDanger(dangerGrid)
            
    #calculate closest ore to mine for each robot, if there is a multi-ore and a trap is ready, grab it to place on the multiore
    for robot in range(5):
        g_shortest_dist = 999999
        g_curr_robot = game.my_robots[0]
        g_curr_cell = game.grid.cells[0]
        for i in range(len(game.my_robots)):
            if game.my_robots[i].item == -1 or game.my_robots[i].item == TRAP:
                for cell in game.grid.cells:
                    # print(cell.distance(game.my_robots[i]), file=sys.stderr)

                    if cell.amadeusium != '?' and dangerGrid.get_cell(cell.x,cell.y).danger==False:
                        if cell.distance(game.my_robots[i]) < g_shortest_dist and int(cell.amadeusium) > 0 and game.my_robots[i].action == "WAIT":
                            g_shortest_dist = cell.distance(game.my_robots[i])
                            g_curr_robot = game.my_robots[i]
                            g_curr_cell = cell
        if g_shortest_dist != 999999:
            print("inside 99999 if", file=sys.stderr)
            g_curr_robot.action = "DIG {} {}".format(g_curr_cell.x, g_curr_cell.y)
            g_curr_cell.amadeusium = int(g_curr_cell.amadeusium) -1

            if g_curr_cell.amadeusium > 0 and g_curr_robot.x == 0 and game.radar_cooldown == 0 and g_curr_robot.item != TRAP:
                print("inside trap if", file=sys.stderr)
                g_curr_robot.action = f"REQUEST TRAP"
        print(g_curr_robot.action, file=sys.stderr)
        print("shortest distance", g_shortest_dist, "robot #:", g_curr_robot.id, "robot_x", g_curr_robot.x, "robot_y", g_curr_robot.y, "cur_cell_x", g_curr_cell.x, "cur_cell_y", g_curr_cell.y, file=sys.stderr)
    

    
		
    if nbTurn > 1:
        for i in range(len(game.my_robots)):
            # Write an action using print
            # To debug: print("Debug messages...", file=sys.stderr)

            # WAIT|
            # MOVE x y|REQUEST item

           
            #game.my_robots[i].wait(f"Starter AI {i}")


            #game.my_robots[i].action="MOVE 3 7"
            
            
            #bring back ore if it has some
            if game.my_robots[i].item==AMADEUSIUM:
                game.my_robots[i].action= "MOVE 0 "+ str(game.my_robots[i].y)
           

            #if a robot has radar, drop it at next pos
            elif game.my_robots[i].item == RADAR:
                game.my_robots[i].action = f"DIG {NEXTRADARPOS.x} {NEXTRADARPOS.y} dropping radar {i}"
            # else:
            #     game.my_robots[i].action = f"WAIT waiting for radar to refresh {i}"
            
            for i in range(len(game.grid.cells)):
                currCell=currentGame.grid.cells[i]
                prevCell=previousGame.grid.cells[i]
                
                if (currCell.amadeusium!="?" and prevCell.amadeusium!="?" and currCell.amadeusium!=prevCell.amadeusium) or (currCell.hole!=prevCell.hole):
                    #was there an enemy robot near it?
                    for enemyRobot in previousGame.enemy_robots:
                        if enemyRobot.distance(prevCell)<=1:
                            print(f"cell {currCell.x} {currCell.y} got possibly dug by an enemy", file=sys.stderr)
                            dangerGrid.cells[i].danger=True;
							
    #record changed done by enemy
    if nbTurn>1:
        for i in range(len(game.grid.cells)):
            currCell=currentGame.grid.cells[i]
            prevCell=previousGame.grid.cells[i]
            
            #guess enemy traps
            if (currCell.amadeusium!="?" and prevCell.amadeusium!="?" and currCell.amadeusium!=prevCell.amadeusium) or (currCell.hole!=prevCell.hole):
                #was there an enemy robot near it?
                for enemyRobot in previousGame.enemy_robots:
                    if enemyRobot.distance(prevCell)<=1:
                        print(f"cell {currCell.x} {currCell.y} got possibly dug by an enemy", file=sys.stderr)
                        dangerGrid.cells[i].danger=True;
            
            #guess enemy radars
            #if currCell.hole!=prevCell.hole:
            #    #was there an enemy robot near it?
            #    for enemyRobot in previousGame.enemy_robots:
            #        if enemyRobot.distance(prevCell)<=1:
            #            dangerGrid.cells[i].danger=True;
    
	#if a radar is almost ready, the robot closest to the efficient point at the base should grab it
    if game.radar_cooldown < 2:
        shortestDistanceToRadarHQPoint = 999
        for j in range(5):
            robotdistanceToRadarHQPoint = game.my_robots[j].distance(Pos(0,NEXTRADARPOS.y))
            if robotdistanceToRadarHQPoint < shortestDistanceToRadarHQPoint and game.my_robots[j].item != RADAR and game.my_robots[j].item != TRAP:
                shortestDistanceToRadarHQPoint = robotdistanceToRadarHQPoint 
                closestRobotToRadarHQPoint = j
        game.my_robots[closestRobotToRadarHQPoint].action = f"REQUEST RADAR {j}"
		

	#disrupt people doing a wall
    #first turn, let the free robot closest to center get a trap
    if nbTurn==1:
        distToCenter=99
        robotTakingTrap=game.my_robots[0]
        for i in range(len(game.my_robots)):
            if game.my_robots[i].action=="WAIT" and game.my_robots[i].distance(game.grid.get_cell(0,7))<distToCenter:
                distToCenter=game.my_robots[i].distance(game.grid.get_cell(0,7))
                robotTakingTrap=game.my_robots[i]
        robotTakingTrap.action="REQUEST TRAP"
    #second turn, all free robots dig to their right
    if nbTurn==2:
        for i in range(len(game.my_robots)):
            if game.my_robots[i].action=="WAIT":
                game.my_robots[i].action=f"DIG 1 {game.my_robots[i].y}"
    
    previousGame=copy.deepcopy(currentGame)	
        
            
    game.my_robots[0].doAction()
    game.my_robots[1].doAction()
    game.my_robots[2].doAction()
    game.my_robots[3].doAction()
    game.my_robots[4].doAction()