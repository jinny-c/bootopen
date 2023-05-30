package com.example.bootopen.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @description JavaFX 图形化界面
 * 演示：
 * @auth chaijd
 * @date 2023/5/30
 */
public class JavaFxFileUpload2Test extends Application {
    private TextArea fileContentTextArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("File Viewer");

        // 创建一个GridPane布局来安排UI元素
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        // 创建标签和文本区域来显示文件内容
        Label fileContentLabel = new Label("File content:");
        fileContentTextArea = new TextArea();
        fileContentTextArea.setEditable(false);
        fileContentTextArea.setWrapText(true);

        // 将标签和文本区域添加到网格中
        grid.add(fileContentLabel, 0, 0);
        grid.add(fileContentTextArea, 0, 1);

        // 创建一个按钮来打开文件选择器
        Button selectButton = new Button("Select file");
        selectButton.setOnAction(e -> {
            // 创建一个文件选择器对话框
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Java Files", "*.java")
            );

            // 显示文件选择器对话框并等待用户选择文件
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // 如果用户选择了一个文件，则将其内容显示在文本区域中
            if (selectedFile != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String content = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content += line + "\n";
                    }
                    fileContentTextArea.setText(content);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 创建一个按钮，用于清除文本区域中的内容
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> fileContentTextArea.clear());

        // 创建一个按钮，用于关闭应用程序
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> primaryStage.close());

        // 将按钮添加到网格中
        grid.add(selectButton, 0, 0);
        grid.add(clearButton, 1, 1);
        grid.add(closeButton, 0, 2);

        // 创建场景并将其设置为主要舞台的场景
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);

        // 显示主要舞台
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
