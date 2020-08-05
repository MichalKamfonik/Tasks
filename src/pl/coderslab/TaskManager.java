package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class TaskManager {
    private static String[][] tasks;
    private static final String FILE_NAME = "tasks.csv";

    public static void main(String[] args) {
        readFile();
        boolean exit = false;

        while(!exit) {
            showMenu();
            switch (chooseOption()) {
                case "add" -> addTask();
                case "remove" -> removeTask();
                case "list" -> listTasks();
                case "exit" -> {
                    exit();
                    exit = true;
                }
                default -> System.out.println(ConsoleColors.RED_BOLD
                        + "Command unknown." + ConsoleColors.RESET);
            }
        }
    }
    private static void showMenu() {
        String[] options = {"add", "remove", "list", "exit"};

        System.out.println(ConsoleColors.BLUE
                + "Please select an option:" + ConsoleColors.RESET);
        for (String option: options) {
            System.out.println(option);
        }
    }
    private static void readFile() {
        Path taskFile = Paths.get(FILE_NAME);

        try{
            List<String> readLines = Files.readAllLines(taskFile);
            tasks = new String[readLines.size()][];
            Iterator<String> iter = readLines.iterator();
            for(int i=0; i<tasks.length; i++) {
                tasks[i] = iter.next().split(",");
            }
        }
        catch(IOException e) {
            System.out.println(ConsoleColors.YELLOW_BOLD
                    +"File \"tasks.csv\" not found, no data imported"
                    +ConsoleColors.RESET);
            tasks = new String[0][];
        }
    }
    private static String chooseOption() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }
    private static void listTasks() {
        System.out.println("list");
        for(int i=0; i<tasks.length; i++) {
            System.out.print(i + " :");
            for(String str: tasks[i]) {
                System.out.print(" " + str);
            }
            System.out.println();
        }
    }
    private static void addTask() {
        System.out.println("add");
        Scanner scan = new Scanner(System.in);

        tasks = Arrays.copyOf(tasks,tasks.length+1);
        tasks[tasks.length-1] = new String[3];

        System.out.println("Please add task description");
        tasks[tasks.length-1][0] = scan.nextLine();
        System.out.println("Please add task due date");
        tasks[tasks.length-1][1] = scan.nextLine();
        System.out.println("Is your task is important: true/false");
        tasks[tasks.length-1][2] = scan.nextLine();
    }
    private static void removeTask() {
        System.out.println("remove");
        System.out.println("Please select number to remove.");
        Scanner scan = new Scanner(System.in);
        int chosen = -1;

        while(chosen<0) {
            String scanned = scan.next();
            if (NumberUtils.isParsable(scanned)) {
                chosen = NumberUtils.toInt(scanned);
            }
            if(chosen<0){
                System.out.println(ConsoleColors.RED_BOLD
                        +"Incorrect argument passed. Please give number greater or equal to 0"
                        +ConsoleColors.RESET);
            }
            else {
                try {
                    tasks=ArrayUtils.remove(tasks,chosen);
                    System.out.println(ConsoleColors.GREEN
                            +"Value was successfully deleted."+ConsoleColors.RESET);
                }
                catch(IndexOutOfBoundsException e) {
                    System.out.println(ConsoleColors.RED_BOLD
                            +"There is no such task."+ConsoleColors.RESET);
                    chosen = -1;
                }
            }
        }
    }
    private static void exit() {
        System.out.println("exit");
        writeFile();
        System.out.println(ConsoleColors.RED+"Bye, bye."+ConsoleColors.RESET);
    }
    private static void writeFile() {
        try(FileWriter fileWriter = new FileWriter(FILE_NAME,false)){
            for (String[] task : tasks) {
                for (int i = 0; i < task.length; i++) {
                    fileWriter.append(task[i]);
                    if (i < 2) {
                        fileWriter.append(',');
                    } else {
                        fileWriter.append('\n');
                    }
                }
            }
        }
        catch(IOException e) {
            System.out.println(ConsoleColors.RED_BOLD
                    +"Writing file error. Data was lost."+ConsoleColors.RESET);
        }
    }
}