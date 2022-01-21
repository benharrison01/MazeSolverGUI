package src.graph;

import jdk.jfr.Enabled;

import java.util.*;

public class Search {

    public Search(){

    }

    public ArrayList<Node> performAStar(Graph graph, Node start, Node goal) {
        ArrayList<Node> unvistited = new ArrayList<Node>();
        ArrayList<Node> visited = new ArrayList<Node>();
        Boolean finished = false;

        //initialisation
        start.setF(start.getHeuristic());
        start.setG(0);
        unvistited.addAll(graph.getNodes());

        while(!finished) {
            //pick node with lowest f as current node
            Node currentNode = unvistited.get(0);
            for (Node node : unvistited) {
                if (node.getF() < currentNode.getF()) {
                    currentNode = node;
                }
            }
            System.out.println("\nCurrent Node = " + currentNode);

            if (currentNode == goal) {
                System.out.println(currentNode + " == " + goal);
                finished = true;
                visited.add(currentNode);
            }
            else {
                System.out.println(currentNode + " != " + goal);
                //examine unvisited neighbours of this node
                ArrayList<Node> neighbours = currentNode.getConnectedNodes();
                System.out.println(neighbours);
                for (Node neighbour : neighbours) {
                    int newGScore = currentNode.getCostTo(neighbour) + currentNode.getG();
                    if (newGScore < neighbour.getG()) {
                        neighbour.setG(newGScore);
                        neighbour.setF(newGScore + neighbour.getHeuristic());
                        neighbour.setPrevious(currentNode);
                    }
                }
                unvistited.remove(currentNode);
                visited.add(currentNode);
            }
        }

        ArrayList<Node> solution = new ArrayList<>();
        Node backtrackNode = visited.get(visited.size() -1);
        while (backtrackNode != start) {
            solution.add(0, backtrackNode);
            backtrackNode = backtrackNode.getPrevious();
        }
        solution.add(0, start);
        System.out.println("Finished search");
        return solution;

    }


}
