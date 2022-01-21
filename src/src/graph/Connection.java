package src.graph;

public class Connection {

    private int cost;
    private Node node; //connecting node

    public Connection(Node node){
        this.node = node;
        this.cost = 1; //default cost value
    }

    public Connection(Node node, int cost){
        this.node = node;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Node getNode() {
        return node;
    }
}
