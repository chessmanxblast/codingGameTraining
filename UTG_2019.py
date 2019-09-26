import sys
import math

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
IDEALRADARLOCATIONS = [[5,7],[11,4],[12,11],[17,7],[23,4],[24,11],[29,7]]



class Pos:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def distance(self, pos):
        return abs(self.x - pos.x) + abs(self.y - pos.y)


class Entity(Pos):
    def __init__(self, x, y, type, id):
        super().__init__(x, y)
        self.type = type
        self.id = id


class Robot(Entity):
    def __init__(self, x, y, type, id, item):
        super().__init__(x, y, type, id)
        self.item = item

    def is_dead(self):
        return self.x == -1 and self.y == -1

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

# game loop
while True:
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
    
    #looks at how many radars have been placed, uses that to pull the location of the next radar from the list IDEALRADARLOCATIONS and create a position object NEXTRADARPOS
    NUMBEROFRADARS = len(game.radars)
    NEXTRADARXY = IDEALRADARLOCATIONS[NUMBEROFRADARS]
    NEXTRADARPOS = Pos(NEXTRADARXY[0],NEXTRADARXY[1])
    print(NUMBEROFRADARS, file=sys.stderr)
    print(NEXTRADARPOS.x, file=sys.stderr)
    print(NEXTRADARPOS.y, file=sys.stderr)

    for i in range(len(game.my_robots)):
        # Write an action using print
        # To debug: print("Debug messages...", file=sys.stderr)

        # WAIT|
        # MOVE x y|REQUEST item
        
        # if radar will spawn within 2 turns, get guy
        if game.radar_cooldown < 2 and game.my_robots[i].item != RADAR:
            game.my_robots[i].action = ("REQUEST RADAR {i}")
        elif game.my_robots[i].item == RADAR:
            game.my_robots[i].action = "DIG {NEXTRADARPOS.x},{NEXTRADARPOS.y} dropping radar {i}"
        else:
            game.my_robots[i].action = "WAIT waiting for radar to refresh {i}"
    game.my_robots[0].doAction
    game.my_robots[1].doAction
    game.my_robots[2].doAction
    game.my_robots[3].doAction
    game.my_robots[4].doAction



        