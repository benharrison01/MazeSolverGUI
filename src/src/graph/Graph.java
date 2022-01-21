package src.graph;

import java.util.ArrayList;

public class Graph {

    private ArrayList<Node> nodes = new ArrayList<Node>();

    public Graph() {
    }

    public void addBiDirection(Node node1, Node node2){
        node1.addConnection(node2);
        node2.addConnection(node1);
    }

    public void addBiDirection(Node node1, Node node2, int cost){
        node1.addConnection(node2, cost);
        node2.addConnection(node1, cost);
    }

    public void addConnection(Node nodeFrom, Node nodeTo){
        nodeFrom.addConnection(nodeTo);
    }

    public void addConnection(Node nodeFrom, Node nodeTo, int cost){
        nodeFrom.addConnection(nodeTo, cost);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public void addNodes(Node[] nodesToAdd){
        for(int i=0;i<nodesToAdd.length;i++){
            nodes.add(nodesToAdd[i]);
        }
    }

    public void addNode(Node nodeToAdd){
            nodes.add(nodeToAdd);
    }

    public int getNumberOfEdges() {
        int noEdges = 0;

        for(Node node: nodes){
           noEdges += node.getConnections().size();
        }

        return noEdges;
    }

    public Connection[] getNodesConnectingToNode(Node node) {
        return (Connection[]) node.getConnections().toArray();
    }

    public Node findNodeCalled(String s) {
        //System.out.println("\nFinding node called: " + s + " in:");
        for (Node node : nodes){
            //System.out.println("Printing node name: " + node.getName());
            if (node.getName().equals(s)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String output = "";
        for (Node node : nodes) {
            ArrayList<Connection> connections = node.getConnections();
            String cons = "";
            for (Connection connection : connections) {
                cons = cons + connection.getNode().toString() + ", ";
            }
            output = output + "Node: " + node + " has connections: " + cons + "\n";
        }
        return output;
    }
}
