/*******************************************************************************
 *  Copyright 2013 Jason Sipula                                                *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package com.vanomaly.superd.view;

import insidefx.undecorator.Undecorator;
import insidefx.undecorator.UndecoratorScene;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Jason Sipula
 *
 */
public class ThemedStageFactory {
    public static Stage getNewThemedDialogStage() {
        Stage newStage = new Stage();
        Region root = new Region();
        final UndecoratorScene undecoratorScene = new UndecoratorScene(newStage, root);
        newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                we.consume();   // Do not hide
                undecoratorScene.setFadeOutTransition();
            }
        });
        undecoratorScene.getStylesheets().add("/com/vanomaly/superd/view/application.css");
        newStage.setScene(undecoratorScene);
        newStage.sizeToScene();
        newStage.toFront();
        
        Undecorator undecorator = undecoratorScene.getUndecorator();
        
        newStage.setMinWidth(undecorator.getMinWidth());
        newStage.setMinHeight(undecorator.getMinHeight());
        return newStage;
    }
}
