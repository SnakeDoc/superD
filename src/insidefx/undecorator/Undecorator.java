/*
 * BSD
 * Copyright (c) 2013, Arnaud Nouard
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the In-SideFX nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package insidefx.undecorator;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * This class, with the UndecoratorController, is the central class for the
 * decoration of Transparent Stages. The Stage Undecorator TODO: Themes, title
 * bar, accelerator, stage icons
 */
public class Undecorator extends StackPane {

    static public int SHADOW_WIDTH = 15;
    static public int SAVED_SHADOW_WIDTH = 15;
    static public int RESIZE_PADDING = 7;
    static public int FEEDBACK_SIZE = 60;
    static public int FEEDBACK_STROKE = 4;
    public static final Logger LOGGER = Logger.getLogger("Undecorator");
    public static ResourceBundle LOC;
    StageStyle stageStyle;
    @FXML
    private Button menu;
    @FXML
    private Button close;
    @FXML
    private Button maximize;
    @FXML
    private Button minimize;
    @FXML
    private Button resize;
    @FXML
    private Button fullscreen;
    MenuItem maximizeMenuItem;
    CheckMenuItem fullScreenMenuItem;
    Region clientArea;
    Pane stageDecoration = null;
    Rectangle shadowRectangle;
    Pane glassPane;
    Rectangle dockFeedback;
    ParallelTransition parallelTransition;
    DropShadow dsFocused;
    DropShadow dsNotFocused;
    UndecoratorController undecoratorController;
    Stage stage;
    Rectangle resizeRect;
    SimpleBooleanProperty maximizeProperty;
    SimpleBooleanProperty minimizeProperty;
    SimpleBooleanProperty closeProperty;
    SimpleBooleanProperty fullscreenProperty;
    String backgroundStyleClass = "undecorator-background";
    TranslateTransition fullscreenButtonTransition;

    public SimpleBooleanProperty maximizeProperty() {
        return maximizeProperty;
    }

    public SimpleBooleanProperty minimizeProperty() {
        return minimizeProperty;
    }

    public SimpleBooleanProperty closeProperty() {
        return closeProperty;
    }

    public SimpleBooleanProperty fullscreenProperty() {
        return fullscreenProperty;
    }

    public Undecorator(Stage stage, Region root) {
        this(stage, root, "stagedecoration.fxml", StageStyle.UNDECORATED);
    }

    public Undecorator(Stage stag, Region clientArea, String stageDecorationFxml, StageStyle st) {
        this.stage = stag;
        this.clientArea = clientArea;

        setStageStyle(st);
        loadConfig();

        // Properties 
        maximizeProperty = new SimpleBooleanProperty(false);
        maximizeProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                getController().maximizeOrRestore();
            }
        });
        minimizeProperty = new SimpleBooleanProperty(false);
        minimizeProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                /*
                 * Transition
                 */
                /* FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), Undecorator.this);
                 fadeTransition.setToValue(0);
                 fadeTransition.play();
                 fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent t) {*/

                getController().minimize();
                /*    }
                 });*/
            }
        });

        closeProperty = new SimpleBooleanProperty(false);
        closeProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                getController().close();
            }
        });
        fullscreenProperty = new SimpleBooleanProperty(false);
        fullscreenProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                getController().setFullScreen(!stage.isFullScreen());
            }
        });

        // The controller
        undecoratorController = new UndecoratorController(this);

        undecoratorController.setAsStageDraggable(stage, clientArea);

        // radius, spread, offsets
        dsFocused = new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, SHADOW_WIDTH, 0.1, 0, 0);
        dsNotFocused = new DropShadow(BlurType.THREE_PASS_BOX, Color.DARKGREY, SHADOW_WIDTH, 0, 0, 0);

        shadowRectangle = new Rectangle();

        // UI part of the decoration
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(stageDecorationFxml));
//            fxmlLoader.setController(new StageDecorationController(this));
            fxmlLoader.setController(this);
            stageDecoration = (Pane) fxmlLoader.load();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Decorations not found", ex);
        }
        initDecoration();

        /*
         * Resize rectangle
         */
        resizeRect = new Rectangle();
        resizeRect.setFill(null);
        resizeRect.setStrokeWidth(RESIZE_PADDING);
        resizeRect.setStrokeType(StrokeType.INSIDE);
        resizeRect.setStroke(Color.TRANSPARENT);
        undecoratorController.setStageResizableWith(stage, resizeRect, RESIZE_PADDING, SHADOW_WIDTH);

        glassPane = new Pane();
        glassPane.setMouseTransparent(true);
        buildDockFeedback();

        // TODO: how to programmatically get css values? wait for JavaFX custom CSS
        shadowRectangle.getStyleClass().add(backgroundStyleClass);
        // Do not intercept mouse events on stage's drop shadow
        shadowRectangle.setMouseTransparent(true);

        // Add all layers
        super.getChildren().addAll(shadowRectangle, clientArea, stageDecoration, resizeRect, glassPane);

        /*
         * Focused stage
         */
        stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                setShadowFocused(t1.booleanValue());
            }
        });
        /*
         * Fullscreen
         */
        if (fullscreen != null) {
            fullscreen.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    if (stage.isFullScreen()) {
                        fullscreen.setOpacity(1);
                    }
                }
            });

            fullscreen.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    if (stage.isFullScreen()) {
                        fullscreen.setOpacity(0.2);
                    }
                }
            });

            stage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean fullscreenState) {
                    setShadow(!fullscreenState.booleanValue());
                    fullScreenMenuItem.setSelected(fullscreenState.booleanValue());
                    maximize.setVisible(!fullscreenState.booleanValue());
                    minimize.setVisible(!fullscreenState.booleanValue());
                    resize.setVisible(!fullscreenState.booleanValue());
                    if (fullscreenState.booleanValue()) {
                        // String and icon
                        fullscreen.getStyleClass().add("decoration-button-unfullscreen");
                        fullscreen.setTooltip(new Tooltip(LOC.getString("Restore")));

                        undecoratorController.saveFullScreenBounds();
                        if (fullscreenButtonTransition != null) {
                            fullscreenButtonTransition.stop();
                        }
                        // Animate the change
                        fullscreenButtonTransition = TranslateTransitionBuilder.create()
                                .duration(Duration.millis(3000))
                                .toX(66)
                                .node(fullscreen)
                                .onFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                fullscreenButtonTransition = null;
                            }
                        })
                                .build();
                        fullscreenButtonTransition.play();
                        fullscreen.setOpacity(0.2);
                    } else {
                        // String and icon
                        fullscreen.getStyleClass().remove("decoration-button-unfullscreen");
                        fullscreen.setTooltip(new Tooltip(LOC.getString("FullScreen")));

                        undecoratorController.restoreFullScreenSavedBounds(stage);
                        fullscreen.setOpacity(1);
                        if (fullscreenButtonTransition != null) {
                            fullscreenButtonTransition.stop();
                        }
                        // Animate the change
                        fullscreenButtonTransition = TranslateTransitionBuilder.create()
                                .duration(Duration.millis(1000))
                                .toX(0)
                                .node(fullscreen)
                                .onFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                fullscreenButtonTransition = null;
                            }
                        })
                                .build();

                        fullscreenButtonTransition.play();
                    }

                }
            });
        }
        computeAllSizes();
    }

    /**
     * Init the minimum/pref/max size in order to be reflected in the primary
     * stage
     */
    private void computeAllSizes() {
        double minWidth = minWidth(getHeight());
        setMinWidth(minWidth);
        double minHeight = minHeight(getWidth());
        setMinHeight(minHeight);

        double prefHeight = prefHeight(getWidth());
        setPrefHeight(prefHeight);
        double prefWidth = prefWidth(getHeight());
        setPrefWidth(prefWidth);

        double maxWidth = maxWidth(getHeight());
        if (maxWidth > 0) {
            setMaxWidth(maxWidth);
        }
        double maxHeight = maxHeight(getWidth());
        if (maxHeight > 0) {
            setMaxHeight(maxHeight);
        }
    }
    /*
     * The sizing is based on client area's bounds.
     */

    @Override
    protected double computePrefWidth(double d) {
        return clientArea.getPrefWidth() + SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
    }

    @Override
    protected double computePrefHeight(double d) {
        return clientArea.getPrefHeight() + SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
    }

    @Override
    protected double computeMaxHeight(double d) {
        return clientArea.getMaxHeight() + SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
    }

    @Override
    protected double computeMinHeight(double d) {
        double d2 = super.computeMinHeight(d);
        d2 += SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
        return d2;
    }

    @Override
    protected double computeMaxWidth(double d) {
        return clientArea.getMaxWidth() + SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
    }

    @Override
    protected double computeMinWidth(double d) {
        double d2 = super.computeMinWidth(d);
        d2 += SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
        return d2;
    }

    public void setStageStyle(StageStyle st) {
        stageStyle = st;
    }

    public StageStyle getStageStyle() {
        return stageStyle;
    }

    /**
     * Activate fade in transition on showing event
     */
    public void setFadeInTransition() {
        super.setOpacity(0);
        stage.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1.booleanValue()) {
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), Undecorator.this);
                    fadeTransition.setToValue(1);
                    fadeTransition.play();
                }
            }
        });
    }

    /**
     * Launch the fade out transition. Must be invoked when the
     * application/window is supposed to be closed
     */
    public void setFadeOutTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), Undecorator.this);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.hide();
            }
        });
    }

    public void removeDefaultBackgroundStyleClass() {
        shadowRectangle.getStyleClass().remove(backgroundStyleClass);
    }

    public Rectangle getBackground() {
        return shadowRectangle;
    }

    /**
     * Manage buttons and menu items
     */
    public void initDecoration() {
        MenuItem minimizeMenuItem = null;
        // Menu
        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);
        
        // set drop-down menu to be square cornered instead of 3-corner rounded
        //contextMenu.setStyle("-fx-background-radius:0.0;");
        
        if (maximize != null) { // Utility Stage type
            maximizeMenuItem = new MenuItem(LOC.getString("Maximize"));
            maximizeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    maximizeProperty().set(!maximizeProperty().get());
                    contextMenu.hide(); // Stay stuck on screen
                }
            });
            contextMenu.getItems().add(maximizeMenuItem);
        }
        
        if (minimize != null) { // Utility Stage
            minimizeMenuItem = new MenuItem(LOC.getString("Minimize"));
            minimizeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    minimizeProperty().set(!minimizeProperty().get());
                }
            });
            contextMenu.getItems().addAll(minimizeMenuItem, new SeparatorMenuItem());
        }

        // Fullscreen
        if (stageStyle != StageStyle.UTILITY) {
            fullScreenMenuItem = new CheckMenuItem(LOC.getString("FullScreen"));
            fullScreenMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    // fake
                    //maximizeProperty().set(!maximizeProperty().get());
                    undecoratorController.setFullScreen(!stage.isFullScreen());
                }
            });

            contextMenu.getItems().addAll(fullScreenMenuItem, new SeparatorMenuItem());
        }

        // Close
        MenuItem closeMenuItem = new MenuItem(LOC.getString("Close"));
        closeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                closeProperty().set(!closeProperty().get());
            }
        });

        contextMenu.getItems().add(closeMenuItem);

        menu.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (contextMenu.isShowing()) {
                    contextMenu.hide();
                } else {
                    contextMenu.show(menu, Side.BOTTOM, 0, 0);
                }
            }
        });

        // Close button
        close.setTooltip(new Tooltip(LOC.getString("Close")));
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                closeProperty().set(!closeProperty().get());
            }
        });

        // Maximize button
        // If changed via contextual menu
        maximizeProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                Tooltip tooltip = maximize.getTooltip();
                if (tooltip.getText().equals(LOC.getString("Maximize"))) {
                    tooltip.setText(LOC.getString("Restore"));
                    maximizeMenuItem.setText(LOC.getString("Restore"));
                    maximize.getStyleClass().add("decoration-button-restore");
                    resize.setVisible(false);
                } else {
                    tooltip.setText(LOC.getString("Maximize"));
                    maximizeMenuItem.setText(LOC.getString("Maximize"));
                    maximize.getStyleClass().remove("decoration-button-restore");
                    resize.setVisible(true);
                }
            }
        });

        if (maximize != null) { // Utility Stage
            maximize.setTooltip(new Tooltip(LOC.getString("Maximize")));
            maximize.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    maximizeProperty().set(!maximizeProperty().get());
                }
            });
        }
        if (fullscreen != null) { // Utility Stage
            fullscreen.setTooltip(new Tooltip(LOC.getString("FullScreen")));
            fullscreen.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    fullscreenProperty().set(!fullscreenProperty().get());
                }
            });
        }

        // Minimize button
        if (minimize != null) { // Utility Stage
            minimize.setTooltip(new Tooltip(LOC.getString("Minimize")));
            minimize.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    minimizeProperty().set(!minimizeProperty().get());
                }
            });
        }
    }

    /**
     * Bridge to the controller to enable the specified node to drag the stage
     *
     * @param stage
     * @param node
     */
    public void setAsStageDraggable(Stage stage, Node node) {
        undecoratorController.setAsStageDraggable(stage, node);
    }

    protected void setShadow(boolean shadow) {
        // Already removed?
        if (!shadow && shadowRectangle.getEffect() == null) {
            return;
        }
        // From fullscreen to maximize situation
        if (shadow && maximizeProperty.get()) {
            return;
        }
        if (!shadow) {
            shadowRectangle.setEffect(null);
            SAVED_SHADOW_WIDTH = SHADOW_WIDTH;
            SHADOW_WIDTH = 0;
        } else {
            shadowRectangle.setEffect(dsFocused);
            SHADOW_WIDTH = SAVED_SHADOW_WIDTH;
        }
    }

    /**
     * Set on/off the stage shadow effect
     *
     * @param b
     */
    protected void setShadowFocused(boolean b) {
        if (b) {
            shadowRectangle.setEffect(dsFocused);
        } else {
            shadowRectangle.setEffect(dsNotFocused);
        }
    }

    /**
     * Set the layout of different layers of the stage
     */
    @Override
    public void layoutChildren() {
        Bounds b = super.getLayoutBounds();
        double w = b.getWidth();
        double h = b.getHeight();
        ObservableList<Node> list = super.getChildren();
        for (Node node : list) {
            if (node == shadowRectangle) {
                shadowRectangle.setWidth(w - SHADOW_WIDTH * 2);
                shadowRectangle.setHeight(h - SHADOW_WIDTH * 2);
                shadowRectangle.setX(SHADOW_WIDTH);
                shadowRectangle.setY(SHADOW_WIDTH);
            } else if (node == stageDecoration) {
                stageDecoration.resize(w - SHADOW_WIDTH * 2, h - SHADOW_WIDTH * 2);
                stageDecoration.setLayoutX(SHADOW_WIDTH);
                stageDecoration.setLayoutY(SHADOW_WIDTH);
            } else if (node == resizeRect) {
                resizeRect.setWidth(w - SHADOW_WIDTH * 2);
                resizeRect.setHeight(h - SHADOW_WIDTH * 2);
                resizeRect.setLayoutX(SHADOW_WIDTH);
                resizeRect.setLayoutY(SHADOW_WIDTH);
            } else {
                node.resize(w - SHADOW_WIDTH * 2 - RESIZE_PADDING * 2, h - SHADOW_WIDTH * 2 - RESIZE_PADDING * 2);
                node.setLayoutX(SHADOW_WIDTH + RESIZE_PADDING);
                node.setLayoutY(SHADOW_WIDTH + RESIZE_PADDING);
            }
        }
    }

    public int getShadowBorderSize() {
        return SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
    }

    public UndecoratorController getController() {
        return undecoratorController;
    }

    public Stage getStage() {
        return stage;
    }

    protected Pane getGlassPane() {
        return glassPane;
    }

    public void addGlassPane(Node node) {
        glassPane.getChildren().add(node);
    }

    public void removeGlassPane(Node node) {
        glassPane.getChildren().remove(node);
    }

    void buildDockFeedback() {
        dockFeedback = new Rectangle();
        dockFeedback.setStroke(Color.GRAY);
        dockFeedback.setArcHeight(2);
        dockFeedback.setArcWidth(2);
        dockFeedback.setStrokeWidth(FEEDBACK_STROKE);
        dockFeedback.setFill(null);
        dockFeedback.setOpacity(0);
        dockFeedback.setVisible(false);
        addGlassPane(dockFeedback);
    }

    /**
     * Activate dock feedback on screen's bounds
     *
     * @param x
     * @param y
     */
    public void setDockFeedbackVisible(double x, double y, double width, double height) {


        dockFeedback.setVisible(true);

        dockFeedback.setLayoutX(x);
        dockFeedback.setLayoutY(y);
        dockFeedback.setWidth(width);
        dockFeedback.setHeight(height);



        FadeTransition fadeTransition = FadeTransitionBuilder.create()
                .duration(Duration.millis(100))
                .node(dockFeedback)
                .fromValue(0)
                .toValue(1)
                .autoReverse(true)
                .cycleCount(4)
                .build();

        ScaleTransition scaleTransition = ScaleTransitionBuilder.create()
                .duration(Duration.millis(400))
                .node(dockFeedback)
                .fromX(0.4)
                .fromY(0.4)
                .toX(1)
                .toY(1)
                .build();

        parallelTransition = new ParallelTransition(dockFeedback);
        parallelTransition.getChildren().addAll(fadeTransition, scaleTransition);
        parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                dockFeedback.setVisible(false);
            }
        });
        parallelTransition.play();
    }

    public void setDockFeedbackUnVisible() {
        if (parallelTransition != null) {
            dockFeedback.setVisible(false);
            parallelTransition.stop();
        }
    }

    static void loadConfig() {
        Properties prop = new Properties();

        try {
            prop.load(Undecorator.class.getClassLoader().getResourceAsStream("skin/undecorator.properties"));
            SHADOW_WIDTH = Integer.parseInt(prop.getProperty("window-shadow-width"));
            RESIZE_PADDING = Integer.parseInt(prop.getProperty("window-resize-padding"));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error while loading confguration flie", ex);
        }
        LOC = ResourceBundle.getBundle("insidefx/undecorator/resources/localization", Locale.getDefault());

    }
}
