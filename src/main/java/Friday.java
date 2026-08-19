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
            try {
                if (input.equals("list")) {
                    System.out.println("Here are your tasks:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i].toString());
                    }
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    int taskNumber = getTaskNumber(input, "mark", taskCount);
                    int taskIndex = taskNumber - 1;
                    tasks[taskIndex].markAsDone();

                    System.out.println("Good! This task done liao: ");
                    System.out.println(tasks[taskIndex].toString());

                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    int taskNumber = getTaskNumber(input, "unmark", taskCount);
                    int taskIndex = taskNumber - 1;
                    tasks[taskIndex].markAsUndone();

                    System.out.println("Nevermind! Can do later: ");
                    System.out.println(tasks[taskIndex].toString());

                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    int taskNumber = getTaskNumber(input, "delete", taskCount);
                    int taskIndex = taskNumber - 1;
                    Task deletedTask = tasks[taskIndex];

                    // Close the gap in the array so the remaining task numbers stay consecutive.
                    for (int i = taskIndex; i < taskCount - 1; i++) {
                        tasks[i] = tasks[i + 1];
                    }
                    tasks[taskCount - 1] = null;
                    taskCount--;

                    System.out.println("Okay, I removed this task:");
                    System.out.println(deletedTask.toString());
                    System.out.println("you have " + taskCount + " tasks lah.");

                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String task = input.substring("todo".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("The description of a todo cannot be empty.");
                    }
                    System.out.println(separator);
                    System.out.println("okay okay, i add " + task + " to the list lor.");
                    tasks[taskCount] = new Todo(task);
                    taskCount++;
                    System.out.println("you have " + taskCount + " tasks lah.");

                } else if (input.equals("event") || input.startsWith("event ")) {
                    String task = input.substring("event".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("The description of an event cannot be empty.");
                    }
                    int fromIndex = task.indexOf("/from ");
                    int toIndex = task.indexOf("/to ", fromIndex + 6);
                    if (fromIndex < 0 || toIndex < 0) {
                        throw new FridayException("Please specify the event as: event DESCRIPTION /from START /to END");
                    }
                    String description = task.substring(0, fromIndex).trim();
                    String start = task.substring(fromIndex + 6, toIndex).trim();
                    String end = task.substring(toIndex + 4).trim();
                    if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
                        throw new FridayException("An event's description, start, and end cannot be empty.");
                    }
                    String[] eventFields = {description, start, end};
                    System.out.println(separator);
                    System.out.println("orh, don't forget to attend ah: ");
                    tasks[taskCount] = new Event(eventFields);
                    System.out.println(tasks[taskCount].toString());
                    taskCount++;
                    System.out.println("you have " + taskCount + " tasks lah.");

                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String task = input.substring("deadline".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("The description of a deadline cannot be empty.");
                    }
                    int byIndex = task.indexOf("/by ");
                    if (byIndex < 0) {
                        throw new FridayException("Please specify the deadline as: deadline DESCRIPTION /by DATE");
                    }
                    String description = task.substring(0, byIndex).trim();
                    String date = task.substring(byIndex + 4).trim();
                    if (description.isEmpty() || date.isEmpty()) {
                        throw new FridayException("A deadline's description and date cannot be empty.");
                    }
                    String[] deadlineFields = {description, date};
                    System.out.println(separator);
                    tasks[taskCount] = new Deadline(deadlineFields);
                    System.out.println("Remember to finish hor: ");
                    System.out.println(tasks[taskCount].toString());
                    taskCount++;
                    System.out.println("you have " + taskCount + " tasks lah.");

                } else {
                    throw new FridayException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (FridayException exception) {
                System.out.println(separator);
                System.out.println("OOPS!!! " + exception.getMessage());
            }

            System.out.println(separator);
            input = scanner.nextLine();
        }
        scanner.close();
        System.out.println(separator);
        System.out.println("Bye. See you next time lah!");
        System.out.println(separator);
    }

    /**
     * Extracts and validates the task number supplied to a numbered command.
     *
     * @param input full command entered by the user
     * @param command command name, such as mark, unmark, or delete
     * @param taskCount number of tasks currently stored
     * @return the one-based task number
     * @throws FridayException if the number is missing, invalid, or out of range
     */
    private static int getTaskNumber(String input, String command, int taskCount)
            throws FridayException {
        String numberText = input.substring(command.length()).trim();
        if (numberText.isEmpty()) {
            throw new FridayException("Please specify a task number.");
        }

        try {
            int taskNumber = Integer.parseInt(numberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new FridayException("That task number does not exist.");
            }
            return taskNumber;
        } catch (NumberFormatException exception) {
            throw new FridayException("The task number must be a whole number.");
        }
    }
}
