import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class Queen8 extends Application {
    static int N = 8;                 // 皇后数量
    static int[] chess;               // 棋盘状态
    static ArrayList<int[]> solutions = new ArrayList<>();  // 所有解

    // 检查当前摆放是否合法
    static boolean check(int step) {
        for (int i = 0; i < step; i++) {
            if (chess[i] == chess[step] ||
                    Math.abs(chess[i] - chess[step]) == Math.abs(step - i)) {
                return false;
            }
        }
        return true;
    }

    // 回溯求解
    static void dfs(int step) {
        if (step == N) {
            solutions.add(chess.clone());
            return;
        }
        for (int i = 0; i < N; i++) {
            chess[step] = i;
            if (check(step)) {
                dfs(step + 1);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 弹窗输入皇后数
        TextInputDialog dialog = new TextInputDialog("8");
        dialog.setTitle("皇后个数选择");
        dialog.setHeaderText("请输入皇后个数 N");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            try {
                int input = Integer.parseInt(s.trim());
                if(input < 4 || input > 20) {
                    N = 8;
                } else {
                    N = input;
                }
            } catch (Exception e) {
                N = 8;
            }
        });

        chess = new int[N];
        solutions.clear();
        dfs(0);

        // 无解则退出
        if (solutions.size() == 0) {
            System.out.println(N + "皇后无解");
            return;
        }

        int boardSize = Math.max(500, N*60 + 100);
        int paneSize = Math.max(400, N*60 + 150);

        Scene scene = new Scene(new QueenPane(), boardSize, paneSize);
        primaryStage.setScene(scene);
        primaryStage.setTitle(N + " 皇后问题");
        primaryStage.show();
    }
}

class QueenPane extends Pane {
    static int idx = 0;               // 当前解序号
    int boardStartX = 50, boardStartY = 50;
    int step = 60;                    // 格子的像素边长
    int N;

    QueenPane() {
        this.N = Queen8.N;
        int paneWidth = Math.max(500, N*60 + 100);
        int paneHeight = Math.max(400, N*60 + 150);
        step = Math.min((paneWidth-100)/N, (paneHeight-150)/N);
        boardStartX = 50;
        boardStartY = 50;
        drawBoard();
        drawQueens(Queen8.solutions.get(idx));

        Button button = new Button("next");
        button.setFont(Font.font(30));
        button.setLayoutX(boardStartX+step*N+20);
        button.setLayoutY(boardStartY);
        getChildren().add(button);
        button.setOnAction(e -> {
            getChildren().removeIf(node -> node instanceof ImageView);
            idx = (idx+1) % Queen8.solutions.size();
            drawQueens(Queen8.solutions.get(idx));
        });
    }

    void drawBoard() {
        // 画格子线
        for (int i = 0; i <= N; i++) {
            Line line = new Line(boardStartX, boardStartY + step * i,
                    boardStartX + step * N, boardStartY + step * i);
            getChildren().add(line);

            line = new Line(boardStartX + step * i, boardStartY,
                    boardStartX + step * i, boardStartY + step * N);
            getChildren().add(line);
        }
    }

    void drawQueens(int[] data) {
        for (int i = 0; i < data.length; i++) {
            ImageView imageView = new ImageView("queen.png");
            imageView.setFitHeight(step);
            imageView.setFitWidth(step);
            imageView.setX(boardStartX + step * data[i]);
            imageView.setY(boardStartY + step * i);
            getChildren().add(imageView);
        }
    }
}