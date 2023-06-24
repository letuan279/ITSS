package org.example.views.screen;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
public class FXMLScreen {
    protected FXMLLoader loader;
    public AnchorPane content;

    public FXMLScreen(String screenPath) throws IOException {
        this.loader = new FXMLLoader();
        this.loader.setController(this);
        this.loader.setLocation(getClass().getResource(screenPath));
        this.content = (AnchorPane) loader.load();
    }
//    public AnchorPane getContent() {
//        return this.content;
//    }
//
//    public FXMLLoader getLoader() {
//        return this.loader;
//    }
//    public void setImage(ImageView imv, String path){
//        File file = new File(path);
//        Image img = new Image(file.toURI().toString());
//        imv.setImage(img);
//    }
}
