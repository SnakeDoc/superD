package com.vanomaly.superd;

import com.vanomaly.superd.controller.MainWindowController;

import insidefx.undecorator.Undecorator;
import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
//import javafx.scene.Scene;
import javafx.scene.layout.Region;
//import javafx.scene.paint.Color;
import javafx.stage.Stage;
//import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Main extends Application {
    
    private Stage primaryStage;
    
    public static void main(String[] args) {
        
        launch(args);
        
    }
    
    @Override
    public void start(final Stage stage) {
        
        this.primaryStage = stage;
        
        Region root = null;
        try {
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vanomaly/superd/view/MainWindow.fxml"));
            
            fxmlLoader.setController(MainWindowController.getInstance());
            
            root = (Region) fxmlLoader.load();
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
        final UndecoratorScene undecoratorScene = new UndecoratorScene(primaryStage, root);
        
        Node node = root.lookup("#draggableNode");
        undecoratorScene.setAsStageDraggable(primaryStage, node);
        undecoratorScene.setFadeInTransition();
    
        /*
         * Fade transition on window closing request
         */
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                we.consume();   // Do not hide
                undecoratorScene.setFadeOutTransition();
            }
        });
        undecoratorScene.getStylesheets().add("/com/vanomaly/superd/view/application.css");
        primaryStage.setScene(undecoratorScene);
        primaryStage.sizeToScene();
        primaryStage.toFront();
        
        Undecorator undecorator = undecoratorScene.getUndecorator();
            
        // set icons here
            
        // set title
            
        // set close request function
            
        // set resizable
        
        primaryStage.setMinWidth(undecorator.getMinWidth());
        primaryStage.setMinHeight(undecorator.getMinHeight());
        
        primaryStage.show();
        
        /*
        final Undecorator undecorator = new Undecorator(stage, root);
        undecorator.getStylesheets().add("skin/undecorator.css");
        
        Node node = root.lookup("#draggableNode");
        undecorator.setAsStageDraggable(stage, node);
        undecorator.setFadeInTransition();

        Scene scene = new Scene(undecorator);
        
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
    */
        /*
         * Fade transition on window closing request
         *//*
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                we.consume();   // Do not hide
                undecorator.setFadeOutTransition();
            }
        });
        
        stage.setScene(scene);
            
        // set icons here
            
        // set title
            
        // set close request function
            
        // set resizable
            
        stage.show();
        
        stage.setMinWidth(undecorator.getMinWidth());
        stage.setMinHeight(undecorator.getMinHeight());
        */
        
    }
    
    /*``````````````````````````````````````*/
   /* Forwarding to Application Controller */
  /*                                      */
 /*``````````````````````````````````````*/
   @FXML
   public void handleAddButtonAction(final ActionEvent event) {
       //MainWindowController.getInstance().handleAddButtonAction(event);
   }
   
   @FXML
   public void handleActionButtonAction(final ActionEvent event) {
       //MainWindowController.getInstance().handleActionButtonAction(event);
       MainWindowController.handleActionButtonAction(event);
   }

}
