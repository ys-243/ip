package friday;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        TextArea conversation = createConversationArea();
        TextField input = new TextField();
        input.setPromptText("Enter a command...");
        HBox inputBar = createInputBar(input, conversation);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(conversation);
        root.setBottom(inputBar);

        stage.setTitle("Friday");
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();
        input.requestFocus();
    }

    private TextArea createConversationArea() {
        TextArea conversation = new TextArea("Friday: Hello! I'm Friday.\nWhat you want ah?\n\n");
        conversation.setEditable(false);
        conversation.setWrapText(true);
        return conversation;
    }

    private HBox createInputBar(TextField input, TextArea conversation) {
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> handleUserInput(input, conversation));
        input.setOnAction(event -> handleUserInput(input, conversation));

        HBox inputBar = new HBox(10, input, sendButton);
        inputBar.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(input, Priority.ALWAYS);
        return inputBar;
    }

    private void handleUserInput(TextField input, TextArea conversation) {
        String userInput = input.getText().trim();
        if (userInput.isEmpty()) {
            return;
        }

        String response = friday.getResponse(userInput);
        conversation.appendText("You: " + userInput + "\nFriday: " + response + "\n\n");
        input.clear();
        conversation.setScrollTop(Double.MAX_VALUE);

        if (userInput.equals("bye")) {
            input.setDisable(true);
            Platform.exit();
        }
    }
}
