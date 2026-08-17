import java.util.Scanner;

/**
 * Starts the Friday chatbot application.
 */
public class Friday {
    public static void main(String[] args) {

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
            System.out.println("okay okay, i do " + input + " now");
            System.out.println(separator);
            input = scanner.nextLine();
        }
        scanner.close();
        System.out.println("Bye. See you next time lah!");
        System.out.println(separator);
    }
}
