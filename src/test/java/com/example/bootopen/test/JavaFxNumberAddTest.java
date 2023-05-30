package com.example.bootopen.test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;

/**
 * @description JavaFX 图形化界面
 * 演示：两个输入，一个返回
 * @auth chaijd
 * @date 2023/5/30
 */
public class JavaFxNumberAddTest extends Application {
    private TextField num1Field;
    private TextField num2Field;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 创建一个网格布局
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // 创建两个文本框和一个标签
        num1Field = new TextField();
        num2Field = new TextField();
        resultLabel = new Label();

        // 将它们添加到网格中
        grid.add(new Label("Number 1:"), 0, 0);
        grid.add(num1Field, 1, 0);
        grid.add(new Label("Number 2:"), 0, 1);
        grid.add(num2Field, 1, 1);
        grid.add(resultLabel, 1, 2);

        // 创建一个按钮
        Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // 获取输入的数字
                double num1 = Double.parseDouble(num1Field.getText());
                double num2 = Double.parseDouble(num2Field.getText());

                // 计算它们的和
                double result = num1 + num2;

                // 显示结果
                resultLabel.setText(Double.toString(result));
            }
        });

        // 将按钮添加到网格中
        grid.add(addButton, 0, 2);

        // 创建一个场景并将其设置到主舞台上
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setTitle("Addition Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
