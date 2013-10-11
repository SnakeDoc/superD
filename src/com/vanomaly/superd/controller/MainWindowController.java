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

package com.vanomaly.superd.controller;

import net.snakedoc.jutils.ConfigException;

import com.vanomaly.superd.Config;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * @author Jason Sipula
 *
 */
public class MainWindowController {
    
    @FXML private TableView table;
    @FXML private TableColumn fileCol;
    @FXML private TableColumn hashCol;
    @FXML private TableColumn sizeCol;
    
    @FXML private Label targetLabel;
    @FXML private TextField targetText;
    
    @FXML private Label delimiterLabel;
    @FXML private TextField delimiterText;
    
    @FXML private Label hashMethodLabel;
    @FXML private Slider hashSlider;
    @FXML private TextField hashMethodDescText;
    
    @FXML private Button addButton;
    @FXML private Button actionButton;
    
    private String CURRENT_HASHALGO;
    
    private MainWindowController() {
    }
    
    private static class MainWindowControllerator {
        private static final MainWindowController INSTANCE = new MainWindowController();
    }
    
    public static MainWindowController getInstance() {
        return MainWindowControllerator.INSTANCE;
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        hashSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                try {
                    setAndUpdateHashAlgo(new_val.intValue());
                } catch (ConfigException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /* targetLabel */
    public String getTargetLabel() {
        return this.targetLabel.getText();
    }
    
    public void setTargetLabel(final String targetLabel) {
        this.targetLabel.setText(targetLabel);
    }
    
    /* targetText */
    public String getTargetText() {
        return this.targetText.getText();
    }
    
    public void setTargetText(final String targetText) {
        this.targetText.setText(targetText);
    }
    
    /* delimiterLabel */
    public String getDelimiterLabel() {
        return this.delimiterLabel.getText();
    }
    
    public void setDelimiterLabel(final String delimiterLabel) {
        this.delimiterLabel.setText(delimiterLabel);
    }
    
    /* delimiterText */
    public String getDelimiterText() {
        return this.delimiterText.getText();
    }
    
    public void setDelimiterText(final String delimiterText) {
        this.delimiterText.setText(delimiterText);
    }
    
    /* hashMethodLabel */
    public String getHashMethodLabel() {
        return this.hashMethodLabel.getText();
    }
    
    public void setHashMethodLabel(final String hashMethodLabel) {
        this.hashMethodLabel.setText(hashMethodLabel);
    }
    
    /* hashSlider */
    public int getHashSlider() {
        return (int) this.hashSlider.getValue();
    }
    
    public void setHashSlider(final int position) {
        this.hashSlider.setValue((double) position);
    }
    
    /* hashMethodDescText */
    public String getHashMethodDescText() {
        return this.hashMethodDescText.getText();
    }
    
    public void setHashMethodDescText(final String hashMethodDescText) {
        this.hashMethodDescText.setText(hashMethodDescText);
    }
    
    /* addButton */
    @FXML
    public void handleAddButtonAction(final ActionEvent event) {
        
        // add directory to target text box
        
    }
    
    /* actionBUtton */
    
    public static void handleActionButtonAction(final ActionEvent event) {
        System.out.println("Click!"); System.out.println(MainWindowController.getInstance().getHashSlider() + " " + MainWindowController.getInstance().getHashMethodLabel());
         
     // perform logical action (start, stop, etc)
        
    }
    
    public void setAndUpdateHashAlgo(final int hashAlgo) throws ConfigException {
        switch(hashAlgo) {
        case 0:
            setAndUpdateHashAlgo(Config.getInstance().getConfig("hashalgo.md5"));
            break;
        case 50:
            setAndUpdateHashAlgo(Config.getInstance().getConfig("hashalgo.sha1"));
            break;
        case 100:
            setAndUpdateHashAlgo(Config.getInstance().getConfig("hashalgo.sha256"));
            break;
        case 150:
            setAndUpdateHashAlgo(Config.getInstance().getConfig("hashalgo.sha512"));
            break;
        default:
            throw new ConfigException("Non supported algorithm!");
        }
    }
    
    public void setAndUpdateHashAlgo(final String hashAlgo) {
        setHashAlgo(hashAlgo);
        updateViewHashAlgo();
    }
    
    private void setHashAlgo(final String hashAlgo) {
        MainWindowController.getInstance().CURRENT_HASHALGO = hashAlgo;
    }
    
    private String getHashAlgo() {
        return MainWindowController.getInstance().CURRENT_HASHALGO;
    }
    
    private void updateViewHashAlgo() {
        this.hashMethodLabel.setText(getHashAlgo());
    }
    
}
