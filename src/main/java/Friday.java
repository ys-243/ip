import java.util.Scanner;

/**
 * Starts the Friday chatbot application.
 */
public class Friday {
    public static void main(String[] args) {
        Task[] tasks = new Task[100];
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
                    System.out.println((i + 1) + "." + tasks[i].toString());
                }
            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5).trim());
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();

                System.out.println("Good! This task done liao: ");
                System.out.println(tasks[taskIndex].toString());

            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7).trim());
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsUndone();

                System.out.println("Nevermind! Can do later: ");
                System.out.println(tasks[taskIndex].toString());

            } else if (input.startsWith("todo ")) {
                String task = input.substring(5).trim();
                System.out.println(separator);
                System.out.println("okay okay, i add " + task + " to the list lor.");
                tasks[taskCount] = new Todo(task);
                taskCount++;
                System.out.println("you have " + taskCount + " tasks lah.");

            } else if (input.startsWith("event ")) {
                String task = input.substring(6).trim();
                System.out.println(separator);
                System.out.println("orh, don't forget to attend ah: ");
                tasks[taskCount] = new Event(task.split("/", 3));
                System.out.println(tasks[taskCount].toString());
                taskCount++;
                System.out.println("you have " + taskCount + " tasks lah.");

            } else if (input.startsWith("deadline ")) {
                String task = input.substring(9).trim();
                System.out.println(separator);
                tasks[taskCount] = new Deadline(task.split("/", 2));
                System.out.println("Remember to finish hor: ");
                System.out.println(tasks[taskCount].toString());
                taskCount++;
                System.out.println("you have " + taskCount + " tasks lah.");

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
