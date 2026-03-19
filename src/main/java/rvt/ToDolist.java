package rvt;

public class ToDolist {
    
}
class TodoList {
    private java.util.ArrayList<String> tasks;

    public TodoList() {
        this.tasks = new java.util.ArrayList<>();
    }

    public void add(String task) {
        tasks.add(task);
    }

    public void print() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ": " + tasks.get(i));
        }
    }

    public void remove(int number) {
        if (number >= 1 && number <= tasks.size()) {
            tasks.remove(number - 1);
        }
    }
}

class UserInterface {
    private TodoList todoList;
    private java.util.Scanner scanner;

    public UserInterface(TodoList todoList, java.util.Scanner scanner) {
        this.todoList = todoList;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.print("Command: ");
            String command = scanner.nextLine();

            if (command.equals("stop")) {
                break;
            } else if (command.equals("add")) {
                System.out.print("To add: ");
                String task = scanner.nextLine();
                todoList.add(task);
            } else if (command.equals("list")) {
                todoList.print();
            } else if (command.equals("remove")) {
                System.out.print("Which one is removed? ");
                try {
                    int number = Integer.parseInt(scanner.nextLine());
                    todoList.remove(number);
                } catch (NumberFormatException e) {
                    // ignore invalid numbers
                }
            }
        }
    }
}