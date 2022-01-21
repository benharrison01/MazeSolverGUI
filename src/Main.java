import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import src.graph.MazeSolver;
import src.graph.Search;
import src.graph.Graph;
import src.graph.Node;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    VBox root = new VBox();
    Image image;

    public void start(Stage stage) {
        buildAGraph();

        stage.setTitle("Maze Solver");

        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        MenuItem closeItem = new MenuItem("Close");
        closeItem.setDisable(true);
        fileMenu.getItems().addAll(openItem, closeItem);

        Menu tools = new Menu("Tools");
        MenuItem solve = new MenuItem("Solve");
        MenuItem revert = new MenuItem("Revert");
        tools.getItems().addAll(solve, revert);
        solve.setDisable(true);
        revert.setDisable(true);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, tools);
        root.getChildren().add(menuBar);

        ImageView imgView = new ImageView(image);
        root.getChildren().add(imgView);

        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent t){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Image File");
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    loadImageFile(file);
                    imgView.setImage(image);
                    openItem.setDisable(true);
                    closeItem.setDisable(false);
                    solve.setDisable(false);
                    revert.setDisable(false);
                }
            }
        });

        closeItem.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle (ActionEvent t){
                // root.getChildren().remove(indexOfImageView);
                imgView.setImage(null);
                openItem.setDisable(false);
                closeItem.setDisable(true);
                solve.setDisable(true);
                revert.setDisable(true);
            }
        });


        solve.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle (ActionEvent t){
                MazeSolver solver = new MazeSolver(image);
                imgView.setImage(solver.drawPath());
            }
        });

        revert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                imgView.setImage(image);
            }
        });

        stage.setScene(new Scene(root, 500, 500));
        stage.show();
    }

    private void loadImageFile(File file) {
        image = new Image("file:" + file.getPath());
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void buildAGraph() {
        //initialize graph
        Graph graph = new Graph();
        Search s = new Search();

        //create nodes
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");

        //add nodes to graph
        graph.addNodes(new Node[]{A,B,C,D,E,F});

        //create connections, bidirectional connections count as 2 edges.
        graph.addBiDirection(A,B);
        graph.addBiDirection(A,E);
        graph.addBiDirection(E,C);
        graph.addBiDirection(B,D);
        graph.addBiDirection(C,D);
        graph.addBiDirection(D,F);

        //System.out.println(graph.getNumberOfEdges());

        System.out.println("\nSolution: " + s.performAStar(graph, A, F));
    }

}
