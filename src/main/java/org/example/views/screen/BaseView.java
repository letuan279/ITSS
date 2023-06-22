package org.example.views.screen;

import java.io.IOException;
import java.util.Hashtable;

import org.example.controllers.BaseController;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class BaseView extends FXMLScreen{
    private Scene scene;
    protected final Stage stage;
    protected Hashtable<String, String> messages;
    private BaseController bController;

    private BaseView(String screenPath) throws IOException {
        super(screenPath);
        this.stage = new Stage();
    }

    public BaseView(Stage stage, String screenPath) throws IOException {
        super(screenPath);
        this.stage = stage;
    }

    public void show() {
        if (this.scene == null) {
            this.scene = new Scene(this.content);
        }
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    public void setBController(BaseController bController){
        this.bController = bController;
    }

    public BaseController getBController(){
        return this.bController;
    }
}
