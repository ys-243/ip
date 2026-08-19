import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the Friday chatbot application.
 */
public class Friday {
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();
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
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i).toString());
                    }
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    int taskNumber = getTaskNumber(input, "mark", tasks.size());
                    int taskIndex = taskNumber - 1;
                    tasks.get(taskIndex).markAsDone();

                    System.out.println("Good! This task done liao: ");
                    System.out.println(tasks.get(taskIndex).toString());

                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    int taskNumber = getTaskNumber(input, "unmark", tasks.size());
                    int taskIndex = taskNumber - 1;
                    tasks.get(taskIndex).markAsUndone();

                    System.out.println("Nevermind! Can do later: ");
                    System.out.println(tasks.get(taskIndex).toString());

                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    int taskNumber = getTaskNumber(input, "delete", tasks.size());
                    int taskIndex = taskNumber - 1;
                    Task deletedTask = tasks.remove(taskIndex);

                    System.out.println("Okay, I removed this task:");
                    System.out.println(deletedTask.toString());
                    System.out.println("you have " + tasks.size() + " tasks lah.");

                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String task = input.substring("todo".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("todo need description leh.");
                    }
                    System.out.println(separator);
                    System.out.println("okay okay, i add " + task + " to the list lor.");
                    tasks.add(new Todo(task));
                    System.out.println("you have " + tasks.size() + " tasks lah.");

                } else if (input.equals("event") || input.startsWith("event ")) {
                    String task = input.substring("event".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("what event ah?");
                    }
                    int fromIndex = task.indexOf("/from ");
                    int toIndex = task.indexOf("/to ", fromIndex + 6);
                    if (fromIndex < 0 || toIndex < 0) {
                        throw new FridayException("ARE YOU DONE?! write like this lah: event DESCRIPTION /from START /to END");
                    }
                    String description = task.substring(0, fromIndex).trim();
                    String start = task.substring(fromIndex + 6, toIndex).trim();
                    String end = task.substring(toIndex + 4).trim();
                    if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
                        throw new FridayException("Tolong, anevent's description, start, and end cannot be empty lei.");
                    }
                    String[] eventFields = {description, start, end};
                    System.out.println(separator);
                    System.out.println("orh, don't forget to attend ah: ");
                    Task event = new Event(eventFields);
                    tasks.add(event);
                    System.out.println(event.toString());
                    System.out.println("you have " + tasks.size() + " tasks lah.");

                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String task = input.substring("deadline".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("What thing got deadline ah?");
                    }
                    int byIndex = task.indexOf("/by ");
                    if (byIndex < 0) {
                        throw new FridayException("Tolong, write this format: deadline DESCRIPTION /by DATE");
                    }
                    String description = task.substring(0, byIndex).trim();
                    String date = task.substring(byIndex + 4).trim();
                    if (description.isEmpty() || date.isEmpty()) {
                        throw new FridayException("Deadline's description and date must fill up lei.");
                    }
                    String[] deadlineFields = {description, date};
                    System.out.println(separator);
                    Task deadline = new Deadline(deadlineFields);
                    tasks.add(deadline);
                    System.out.println("Remember to finish hor: ");
                    System.out.println(deadline.toString());
                    System.out.println("you have " + tasks.size() + " tasks lah.");

                } else {
                    throw new FridayException("Eh? Sorry i don't understand that bro :-(");
                }
            } catch (FridayException exception) {
                System.out.println(separator);
                System.out.println("SIALA!!! " + exception.getMessage());
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
