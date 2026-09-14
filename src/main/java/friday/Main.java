package friday;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Displays Friday's graphical chat interface.
 */
public class Main extends Application {
    private static final double WINDOW_WIDTH = 600;
    private static final double WINDOW_HEIGHT = 500;

    private final Friday friday = new Friday();

    @Override
    public void start(Stage stage) {
        VBox messages = new VBox(10);
        ScrollPane conversation = createConversationArea(messages);
        TextField input = new TextField();
        input.setPromptText("Enter a command...");
        input.setStyle("-fx-background-color: #fffdf5; -fx-background-radius: 10; "
                + "-fx-border-color: #ffd1d6; -fx-border-radius: 10; "
                + "-fx-text-fill: #403840; -fx-prompt-text-fill: #746974; "
                + "-fx-focus-color: #d1eef6; -fx-faint-focus-color: transparent;");
        HBox inputBar = createInputBar(input, messages, conversation);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #fff0b8;");
        root.setCenter(conversation);
        root.setBottom(inputBar);

        stage.setTitle("Friday");
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();
        input.requestFocus();
    }

    private ScrollPane createConversationArea(VBox messages) {
        messages.setPadding(new Insets(10));
        addMessage(messages, "Hello! I'm Friday.\nWhat you want ah?", false);

        ScrollPane conversation = new ScrollPane(messages);
        conversation.setFitToWidth(true);
        conversation.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        conversation.setStyle("-fx-background-color: transparent; -fx-background: #fffdf5;");
        return conversation;
    }

    private HBox createInputBar(TextField input, VBox messages, ScrollPane conversation) {
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-base: #ffd6b3; -fx-background-radius: 10; "
                + "-fx-text-fill: #403840; -fx-focus-color: #d1eef6; "
                + "-fx-faint-focus-color: transparent;");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> handleUserInput(input, messages, conversation));
        input.setOnAction(event -> handleUserInput(input, messages, conversation));

        HBox inputBar = new HBox(10, input, sendButton);
        inputBar.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(input, Priority.ALWAYS);
        return inputBar;
    }

    private void handleUserInput(TextField input, VBox messages, ScrollPane conversation) {
        String userInput = input.getText().trim();
        if (userInput.isEmpty()) {
            return;
        }

        String response = friday.getResponse(userInput);
        addMessage(messages, userInput, true);
        addMessage(messages, response, false);
        input.clear();
        Platform.runLater(() -> conversation.setVvalue(1.0));

        if (userInput.equals("bye")) {
            input.setDisable(true);
            Platform.exit();
        }
    }

    private void addMessage(VBox messages, String text, boolean isUser) {
        Label message = new Label(text);
        message.setWrapText(true);
        message.setMaxWidth(400);
        message.setPadding(new Insets(8, 12, 8, 12));
        message.setStyle(isUser
                ? "-fx-background-color: #d1eef6; -fx-background-radius: 14; -fx-text-fill: #403840;"
                : "-fx-background-color: #ffd1d6; -fx-background-radius: 14; -fx-text-fill: #403840;");

        HBox messageRow = new HBox(message);
        messageRow.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messages.getChildren().add(messageRow);
    }
}
