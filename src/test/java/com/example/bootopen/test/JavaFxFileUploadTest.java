package com.example.bootopen.test;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @description JavaFX 图形化界面
 * 演示：
 * @auth chaijd
 * @date 2023/5/30
 */
public class JavaFxFileUploadTest extends Application {
    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) {
        Button loadButton = new Button("Load File");
        loadButton.setOnAction(e -> chooseFile());

        textArea = new TextArea();

        VBox vbox = new VBox(loadButton, textArea);
        Scene scene = new Scene(vbox, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            readFile(selectedFile);
        }
    }

    private void readFile(File file) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        textArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
