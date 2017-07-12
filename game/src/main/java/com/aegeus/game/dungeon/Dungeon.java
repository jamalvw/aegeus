package com.aegeus.game.dungeon;

import com.aegeus.game.Aegeus;
import com.aegeus.game.util.exceptions.DungeonLoadingException;
import com.sk89q.worldedit.CuboidClipboard;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Silvre on 7/10/2017.
 */
public class Dungeon {
    String[][] layout;
    private static transient Aegeus parent = Aegeus.getInstance();
    private transient ThreadLocalRandom random = ThreadLocalRandom.current();
    private File directory;
    private List<CuboidClipboard> starts;
    private List<CuboidClipboard> straights;
    private List<CuboidClipboard> turns;
    private List<CuboidClipboard> trijunctions;
    private List<CuboidClipboard> quadjunctions;
    private List<CuboidClipboard> keys;
    private int length;

    public Dungeon(String directory, int length) throws DungeonLoadingException {
        File temp = new File(Aegeus.getInstance().getDataFolder() + "/dungeons/" + directory + "/");
        this.length = 4;
//        if(!temp.exists() || !temp.isDirectory())   {
//            throw new DungeonLoadingException("The directory selected does not exist or has been corrupted.");
//        }
//        else    {
//            this.directory = temp;
//        }
        parent.getLogger().info("DEPTH FIRST SEARCH");
        dfs();
    }

    public void dfs()    {
        String[][] maze = {
                {"0", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0"}};
        do {
            for (int i = 0; i < maze.length; i++) {
                for (int i1 = 0; i1 < maze[i].length; i1++) {
                    maze[i][i1] = "0";
                }
            }
            int sx, sy, ex, ey;
            //noinspection ControlFlowStatementWithoutBraces
            while (Point2D.distance(sx = random.nextInt(5), sy = random.nextInt(5), ex = random.nextInt(5), ey = random.nextInt(5)) < length || sx == ex || sy == ey)
                ;
            maze[sx][sy] = "S";
            maze[ex][ey] = "E";
            dfsrecursive(sx, sy, maze);
            maze[sx][sy] = "S";
        } while(!validateAndMap(maze));
        printArray(layout);
    }

    private boolean dfsrecursive(int x, int y, String[][] maze)   {
        if(x < 0 || y < 0 || x > 4 || y > 4)    return false;
        if(maze[x][y].equalsIgnoreCase("E")) return true;
        if(maze[x][y].equalsIgnoreCase("P")) return false;
        maze[x][y] = "P";
        if((x < 4 && maze[x + 1][y].equalsIgnoreCase("E")) || (x > 0 && maze[x - 1][y].equalsIgnoreCase("E"))
                || (y < 4 && maze[x][y + 1].equalsIgnoreCase("E")) || (y > 0 && maze[x][y - 1].equalsIgnoreCase("E"))) {
            return true;
        }
        if(nearby(x, y, maze) > 1) {
            maze[x][y] = "0";
            return false;
        }
        boolean pathFound = false;
        switch(random.nextInt(4))    {
            case 0:
                pathFound = dfsrecursive(x + 1, y, maze) || dfsrecursive(x - 1, y, maze) || dfsrecursive(x, y + 1, maze) || dfsrecursive(x, y - 1, maze);
                break;
            case 1:
                pathFound = dfsrecursive(x - 1, y, maze) || dfsrecursive(x, y + 1, maze) || dfsrecursive(x, y - 1, maze) || dfsrecursive(x + 1, y, maze);
                break;
            case 2:
                pathFound = dfsrecursive(x, y + 1, maze) || dfsrecursive(x, y - 1, maze) || dfsrecursive(x + 1, y, maze) || dfsrecursive(x - 1, y, maze);
                break;
            case 3:
                pathFound = dfsrecursive(x, y - 1, maze) || dfsrecursive(x + 1, y, maze) || dfsrecursive(x - 1, y, maze) || dfsrecursive(x, y + 1, maze);
                break;
            default:
                break;
        }
        if(pathFound)    {
            maze[x][y] = "P";
            return true;
        }
        maze[x][y] = "0";
        return false;
    }

    private void bfs()    {
        String[][] maze ={
                {"0","0","0","0","0"},
                {"0","0","0","0","0"},
                {"0","0","0","0","0"},
                {"0","0","0","0","0"},
                {"0","0","0","0","0"}};
        int sx, sy, ex, ey;
        while(Point2D.distance(sx = random.nextInt(5), sy = random.nextInt(5), ex = random.nextInt(5), ey = random.nextInt(5)) < length || sx == ex || sy == ey);
        maze[sx][sy] = "S";
        maze[ex][ey] = "E";
        List<Node> nodes = new ArrayList<>();
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(new Node(sx, sy, null));
        nodes.add(queue.peek());
        while(!queue.isEmpty()) {
            System.out.println(queue.size());
            Node n = queue.poll();
            if(maze[n.getX()][n.getY()].equalsIgnoreCase("E"))  {
                List<Node> path = new ArrayList<>();
                Node step = n.getParent();
                while(step != null) {
                    path.add(step);
                    if(!maze[step.getX()][step.getY()].equalsIgnoreCase("S"))
                        maze[step.getX()][step.getY()] = "P";
                    step = step.getParent();
                }
                printArray(maze);
                return;
            }
            int x = n.getX(), y = n.getY();
            Node child;
            child = new Node(x + 1, y, n);
            if(x < 4 && !nodes.contains(child)) {
                nodes.add(child);
                queue.offer(child);
            }
            child = new Node(x - 1, y, n);
            if(x > 0 && !nodes.contains(child)) {
                nodes.add(child);
                queue.offer(child);
            }
            child = new Node(x, y - 1, n);
            if(y > 0 && !nodes.contains(child)) {
                nodes.add(child);
                queue.offer(child);
            }
            child = new Node(x, y + 1, n);
            if(y < 4 && !nodes.contains(child)) {
                nodes.add(child);
                queue.offer(child);
            }
        }
        printArray(maze);
        return;
    }

    private boolean isValid(String[][] maze)    {
        int count = 0;
        for(String[] arr : maze)
            for(String s : arr)
                if(s.equalsIgnoreCase("P")) count++;
        if(count != 8) return false;
        for (int i = 0; i < maze.length; i++)
            for (int j = 0; j < maze[i].length; j++)
                if(nearby(i,j, maze) > 2) return false;
        return true;
    }

    private boolean validateAndMap(String[][] maze)    {
        if(!isValid(maze)) return false;
        String[][] map = new String[5][5];
        for (int i = 0; i < maze.length; i++)
            map[i] = Arrays.copyOf(maze[i], maze[i].length);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if(maze[i][j].equalsIgnoreCase("P"))    {
                    int surround = nearby(i, j, maze);
                    int count = getDirectionalCount(i, j, maze);
                    if(surround == 2) {
                        if(count == 4)
                            map[i][j] = "V"; //UP DOWN STRAIGHT
                        else if(count == 8)
                            map[i][j] = "H"; //LEFT RIGHT STRAIGHT
                        else if(count == 7)
                            map[i][j] = "D"; //SOUTH EAST TURN
                        else if(count == 9)
                            map[i][j] = "R"; //NORTH EAST TURN
                        else if(count == 5)
                            map[i][j] = "U"; //NORTH WEST TURN
                        else if(count == 3)
                            map[i][j] = "L"; //SOUTH WEST TURN
                    }
                    if(surround == 3)   {
                        if(count == 9)
                            map[i][j] = "S"; //WEST SOUTH EAST JUNCTION
                        if(count == 10)
                            map[i][j] = "E"; //SOUTH EAST NORTH JUNCTION
                        if(count == 11)
                            map[i][j] = "N"; //EAST NORTH WEST JUNCTION
                        if(count == 6)
                            map[i][j] = "W"; //NORTH WEST SOUTH JUNCTION
                    }
                }
            }
        }
        layout = map;
        return true;
    }

    private String getDirection(int x, int y, String[][] maze, Direction d)   {
        if(d == Direction.NORTH && x > 0) return maze[x - 1][y];
        if(d == Direction.SOUTH && x < 4) return maze[x + 1][y];
        if(d == Direction.EAST && y < 4) return maze[x][y + 1];
        if(d == Direction.WEST && y > 0) return maze[x][y - 1];
        return "";
    }


    private int getDirectionalCount(int x, int y, String[][] maze)   {
        int count = 0;
        if(x < 4 && maze[x + 1][y].matches("[PpKkSsEe]")) count += 1; //SOUTH
        if(x > 0 && maze[x - 1][y].matches("[PpKkSsEe]")) count += 3; //NORTH
        if(y > 0 && maze[x][y - 1].matches("[PpKkSsEe]")) count += 2; //WEST
        if(y < 4 && maze[x][y + 1].matches("[PpKkSsEe]")) count += 6; //EAST
        return count;
    }

    private int nearby(int x, int y, String[][] maze)  {
        int count = 0;
        if(x > 0 && maze[x - 1][y].matches("[PpKkSsEe]")) count++;
        if(x < 4 && maze[x + 1][y].matches("[PpKkSsEe]")) count++;
        if(y > 0 && maze[x][y - 1].matches("[PpKkSsEe]")) count++;
        if(y < 4 && maze[x][y + 1].matches("[PpKkSsEe]")) count++;
        return count;
    }

    private void printArray(String[][] array)   {
        for(String[] a : array)  {
            parent.getLogger().info(String.join(" ", a));
        }
    }

    private class Node   {
        private int x,y;
        private Node parent;
        public Node(int x, int y, Node parent)   {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        public Node getParent() {
            return parent;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Node && ((Node) obj).getX() == x && ((Node) obj).getY() == y;
        }
    }

    private enum Direction  {
        NORTH(1),
        SOUTH(1),
        WEST(2),
        EAST(2);

        private int direction;
        Direction(int value)  {
            this.direction = value;
        }

        public int getDirection() {
            return direction;
        }
    }
}