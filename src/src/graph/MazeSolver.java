package src.graph;

import java.awt.*;
import java.util.ArrayList;

import javafx.scene.image.PixelReader;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class MazeSolver {

    private Image image;
    private Graph graph = new Graph();

    public MazeSolver(Image image){
        this.image = image;
    }

    private String[][] analyseMapArray(String[][] map) {
        int width = map.length;
        int height = map[0].length;

        //System.out.println(width);
        //System.out.println(height);

        for(int i=1; i<width-1; i++) {
            for(int j=1; j<height-1; j++) {
                //System.out.println("\ni=" + i);
                //System.out.println("j=" + j);
                int horizontalAdjacentPaths = 0;
                int verticalAdjacentPaths = 0;
                if (map[i][j] == "P") {
                    if (map[i + 1][j] == "P") {
                        verticalAdjacentPaths += 1;
                    }
                    if (map[i - 1][j] == "P") {
                        verticalAdjacentPaths += 1;
                    }
                    if (map[i][j + 1] == "P") {
                        horizontalAdjacentPaths += 1;
                    }
                    if (map[i][j - 1] == "P") {
                        horizontalAdjacentPaths += 1;
                    }

                    if (horizontalAdjacentPaths * verticalAdjacentPaths > 0) {
                        //both non zero elements therefore a decision to be made, doesnt consider dead ends
                        map[i][j] = "N";
                        String name = "(" + j + "," + i + ")"; //give name in row major order
                        Node node = new Node(name);
                        graph.addNode(node);
                    }
                }

            }
        }

        /*for (int i=0; i<map.length;i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[j][i]); //print in row major order
            }
            System.out.println();
        }*/
        return map;
    }

    public Color[][] getPixelData() {
        PixelReader pr = image.getPixelReader();
        int rows = (int) image.getHeight();
        int cols = (int) image.getWidth();
        Color[][] pixelData = new Color[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pixelData[i][j] = pr.getColor(i, j);
            }
        }
        return pixelData;
    }

    public String[][] getMapData(Color[][] pixelData) {
        System.out.println(pixelData.length + " x " + pixelData[0].length + " image with cell width of 10 gives " + pixelData.length/10 + " x " + pixelData[0].length/10 + " map");
        String[][] map = new String[pixelData.length/10][pixelData[0].length/10];

        for (int i=0; i<pixelData.length;i=i+10){
            for (int j=0; j<pixelData[i].length;j=j+10) {
                //System.out.println(pixelData[i][j]);
                if (pixelData[i][j].getBlue() == 0 && pixelData[i][j].getRed() == 0 && pixelData[i][j].getGreen() == 0) {
                    map[i/10][j/10] = "W";
                } else {
                    map[i/10][j/10] = "P";
                }
            }
        }

        /*for (int i=0; i<map.length;i++) {
            for (int j = 0; j < map[i].length; j++) {
               System.out.print(map[j][i]); //print in row major order
            }
            System.out.println();
        }*/
        return map;
    }

    private String[][] addStartAndEndNodes(String[][] map) {
        map[0][1] = "N";
        Node node = new Node("(1,0)");
        graph.addNode(node);

        map[map.length-1][map.length -2] = "N";
        String name = "(" + (map.length-2) + "," + (map.length -1) + ")";
        Node node2 = new Node(name);
        graph.addNode(node2);

        return map;
    }

    public Image drawNodes(String[][] mapWithNodes, Color[][] originalPixelArray){
        Color[][] pixelArray = new Color[mapWithNodes.length * 10][mapWithNodes[0].length * 10];
        for (int i=0; i<mapWithNodes.length * 10; i++) {
            for (int j=0; j<mapWithNodes[0].length * 10; j++) {
                if (mapWithNodes[i/10][j/10] == "N"){
                    //System.out.println("i=" + i + ", j=" + j);
                    pixelArray[i][j] = Color.BLUE;
                }
                else {
                    pixelArray[i][j] = originalPixelArray[i][j];
                }
            }
            //System.out.println();
        }

        WritableImage wimg = new WritableImage(pixelArray.length, pixelArray[0].length);
        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) {
                pw.setColor(i, j, pixelArray[i][j]);
            }
        }
        return wimg;
    }

    private void connectNodesInGraph(String[][] map) {
        for (int i=0; i< map.length; i++) {
            Boolean gotFirstNode = false;
            Node node1 = null;
            Node node2 = null;
            int tempI = 0;
            int tempJ = 0;
            for (int j=0; j< map[0].length; j++) {
                if (map[i][j] == "N") {
                    if (gotFirstNode == true) {
                        node2 = graph.findNodeCalled("(" + j + "," + i + ")");
                        System.out.println(node2);
                        graph.addBiDirection(node1, node2, Math.max(Math.abs(j-tempJ),Math.abs(i-tempI)));
                        node1 = node2;
                    }
                    else {
                        node1 = graph.findNodeCalled("(" + j + "," + i + ")");
                        tempI = i;
                        tempJ = j;
                        gotFirstNode = true;
                    }
                }
                else if (map[i][j] == "W") {
                    gotFirstNode = false;
                }
            }
        }
        for (int j=0; j< map.length; j++) {
            Boolean gotFirstNode = false;
            Node node1 = null;
            Node node2 = null;
            int tempI = 0;
            int tempJ = 0;
            for (int i=0; i< map[0].length; i++) {
                if (map[i][j] == "N") {
                    if (gotFirstNode == true) {
                        node2 = graph.findNodeCalled("(" + j + "," + i + ")");
                        System.out.println(node2);
                        graph.addBiDirection(node1, node2, Math.max(Math.abs(j-tempJ),Math.abs(i-tempI)));
                        node1 = node2;
                    }
                    else {
                        node1 = graph.findNodeCalled("(" + j + "," + i + ")");
                        tempI = i;
                        tempJ = j;
                        gotFirstNode = true;
                    }
                }
                else if (map[i][j] == "W") {
                    gotFirstNode = false;
                }
            }
        }
    }

    public Image drawPath() {
        Color[][] originalPixelArray = getPixelData();
        String[][] map = getMapData(originalPixelArray);
        map = addStartAndEndNodes(map);
        String[][] mapWithNodes = analyseMapArray(map);
        connectNodesInGraph(map);
        //Image wimg = drawNodes(mapWithNodes, originalPixelArray);

        ArrayList<Node> Nodes = graph.getNodes();

        //for (Node node : Nodes) {
            //System.out.println(node);
        //}

        Search s = new Search();
        Node start = graph.findNodeCalled("(1,0)");
        String name = "(" + (map.length-2) + "," + (map.length -1) + ")";
        Node goal = graph.findNodeCalled(name);
        System.out.println(graph.toString());
        ArrayList<Node> solution = s.performAStar(graph, start, goal);
        System.out.println(solution);

        Image wimg = drawPathOnImage(solution, originalPixelArray);
        return wimg;
    }

    private Image drawPathOnImage(ArrayList<Node> solution, Color[][] originalPixelArray) {
        String sol = solution.toString();
        System.out.println(sol);
        String path = "[";
        Color[][] pixelArray = new Color[originalPixelArray.length][originalPixelArray[0].length];

        int currentI = -1;
        int currentJ = -1;

        path = path + solution.get(0).toString();
        for (Node node : solution) {

            int i = Integer.parseInt(node.toString().split(",")[0].split("\\(")[1]);
            int j = Integer.parseInt(node.toString().split(",")[1].split("\\)")[0]);
            if (currentI == -1){
                currentI = i;
            }
            if (currentJ == -1){
                currentJ = j;
            }

            if (i > currentI){
                while (i > currentI) {
                    path = path + ", (" + i + "," + j + ")";
                    i = i - 1;
                }
            }
            else if (i < currentI) {
                while (i < currentI) {
                    path = path + ", (" + i + "," + j + ")";
                    i = i + 1;
                }
            }
            else if (j > currentJ) {
                while (j > currentJ) {
                    path = path + ", (" + i + "," + j + ")";
                    j = j - 1;
                }
            }
            else if (j < currentJ) {
                while (j < currentJ) {
                    path = path + ", (" + i + "," + j + ")";
                    j = j + 1;
                }
            }





            currentI = Integer.parseInt(node.toString().split(",")[0].split("\\(")[1]);
            currentJ = Integer.parseInt(node.toString().split(",")[1].split("\\)")[0]);
        }
        path = path + "]";
        System.out.println(path);
        for (int i=0; i<originalPixelArray.length; i++) {
            for (int j=0; j<originalPixelArray[0].length; j++) {
                if (path.contains("(" + j/10 + "," + i/10 + ")")) {
                    pixelArray[i][j] = Color.RED;
                }
                else {
                    pixelArray[i][j] = originalPixelArray[i][j];
                }
            }
            //System.out.println();
        }

        WritableImage wimg = new WritableImage(pixelArray.length, pixelArray[0].length);
        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) {
                pw.setColor(i, j, pixelArray[i][j]);
            }
        }
        return wimg;
    }


}
