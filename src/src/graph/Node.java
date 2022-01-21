package src.graph;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {

    private int heuristic = 0;
    private int g = (int) Double.POSITIVE_INFINITY;
    private int f = (int) Double.POSITIVE_INFINITY;
    private Node previous;
    private ArrayList<Connection> connections = new ArrayList<Connection>();
    private String name = "Unnamed-Node";

    public Node(String name){
        this.name = name;
    }

    public Node(){

    }

    public Node(String name, int heuristic){
        this.name = name;
        this.heuristic = heuristic;
    }

    public Node(int heuristic){
        this.heuristic = heuristic;
    }

    public String toString() {
        return name;
    }

    public void addConnection(Node node){
        removePreexistingConnection(node);
        Connection newConnection = new Connection(node);
        connections.add(newConnection);
    }

    public void addConnection(Node node, int cost){
        removePreexistingConnection(node);
        Connection newConnection = new Connection(node, cost);
        connections.add(newConnection);
    }

    private void removePreexistingConnection(Node node) {
        ArrayList<Connection> connectionsToRemove = new ArrayList<Connection>();

        for (Connection connection : connections) {
            if (connection.getNode() == node) {
                connectionsToRemove.add(connection);
            }
        }

        for (Connection connectionToRemove : connectionsToRemove) {
            connections.remove(connectionToRemove);
        }

    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public ArrayList<Node> getConnectedNodes() {
        ArrayList<Connection> connections = getConnections();
        ArrayList<Node> connectedNodes = new ArrayList<>();
        for (Connection connection : connections) {
            connectedNodes.add(connection.getNode());
        }
        return connectedNodes;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    public int getCostTo(Node connectingNode) {
        for (Connection connection : connections) {
            if (connection.getNode() == connectingNode) {
                return connection.getCost();
            }
        }
        return -1;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public String getName() {
        return name;
    }
}
