import java.util.Scanner;

/**
 * Starts the Friday chatbot application.
 */
public class Friday {
    public static void main(String[] args) {
        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);

        String separator = "____________________________________________________________";

        String banner = " ______    _     _             \n"
                      + "|  ____|  (_)   | |            \n"
                      + "| |__ _ __ _  __| | __ _ _   _\n"
                      + "|  __| '__| |/ _` |/ _` | | | |\n"
                      + "| |  | |  | | (_| | (_| | |_| |\n"
                      + "|_|  |_|  |_|\\__,_|\\__,_|\\__, |\n"
                      + "                          __/ |\n"
                      + "                         |___/ \n";

        System.out.println(separator);
        System.out.print(banner);
        System.out.println("Hello! I'm Friday.");
        System.out.println("What you want ah?");
        System.out.println(separator);

        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            if (input.equals("list")) {
                System.out.println("Here are your tasks:");
                for (int i = 0; i < taskCount; i++) {
                    String status = isDone[i] ? "X" : " ";
                    System.out.println((i + 1) + ".[" + status + "] " + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5).trim());
                int taskIndex = taskNumber - 1;
                isDone[taskIndex] = true;

                System.out.println("Good! This task done liao: ");
                System.out.println(" [X] " + tasks[taskIndex]);
            } else {
                System.out.println(separator);
                System.out.println("okay okay, i add " + input + " to the list lor.");
                tasks[taskCount] = input;
                taskCount++;
            }
            System.out.println(separator);
            input = scanner.nextLine();
        }
        scanner.close();
        System.out.println(separator);
        System.out.println("Bye. See you next time lah!");
        System.out.println(separator);
    }
}
