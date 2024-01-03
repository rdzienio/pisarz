package pl.gienius.pisarz;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final WriterClient client = new WriterClient();
    private Writer writer;


    List<Book> books = new ArrayList<>();


    private void greeting() {
        System.out.println("### Writer Client app ###");
    }

    public void init() {
        greeting();
        createNewWriter();
        menu();
    }

    private void printMainMenu() {
        greeting();
        System.out.println("1. Add new book");
        System.out.println("2. Show all books");
        System.out.println("3. Block the book");
        System.out.println("4. Unblock the book");
        System.out.println("5. Is the book rented?");
        System.out.println("6. Remove the book");
        System.out.println("7. Update the book");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void menu() {
        while (true) {
            printMainMenu();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    createNewBook();
                    break;
                case 2:
                    printAllBooks();
                    break;
                case 3:
                    blockBookMenu();
                    break;
                case 4:
                    unBlockBookMenu();
                    break;
                case 5:
                    checkBookMenu();
                    break;
                case 6:
                    removeBookMenu();
                    break;
                case 7:
                    updateBookMenu();
                    break;
                case 0: {
                    System.out.println("Exiting...");
                    return;
                }
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createNewWriter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Create new writer! What's your name?");
        String name = scanner.nextLine();
        while (name.isEmpty() || name.isBlank()) {
            System.out.println("Type at least 1 char! What's your name?");
        }
        writer = client.createWriter(name);
        System.out.println("New Writer: " + writer);
    }

    private void createNewBook() {
        Book newBook = new Book();

        newBook.setWriter(writer);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Create new book! What's the title?");
        String title = scanner.nextLine();
        while (title.isEmpty() || title.isBlank()) {
            System.out.println("Type at least 1 char! What's the title?");
        }
        newBook.setTitle(title);

        System.out.println("What's the description?");
        String description = scanner.nextLine();
        while (description.isEmpty() || description.isBlank()) {
            System.out.println("Type at least 1 char! What's the description?");
        }
        newBook.setDescription(description);

        System.out.println("What's the release date? [yyyy-MM-dd]");
        String releaseDate = scanner.nextLine();
        if (releaseDate.isEmpty() || releaseDate.isBlank()) {
            LocalDate startDate = LocalDate.now();
            newBook.setReleaseDate(startDate);
        } else newBook.setReleaseDate(LocalDate.parse(releaseDate));

        Book response = client.addNewBook(newBook, writer.getId());
        if (response != null) {
            books.add(response);
            System.out.println("New book added: " + response);
        } else System.out.println("Something went wrong...");
    }

    private void printAllBooks() {
        books = client.getBooks(writer.getId());
        if (books == null || books.isEmpty())
            System.out.println("No books");
        else {
            for (Book book : books) {
                System.out.println("Id: " + book.getId());
                System.out.println("\tTitle: " + book.getTitle());
                System.out.println("\tRelease date: " + book.getReleaseDate());
                System.out.println("\tDescription: " + book.getDescription());
                System.out.println("\n");
            }
        }
    }

    private void blockBookMenu() {
        Scanner scanner = new Scanner(System.in);
        printAllBooks();
        System.out.println("Which book to block: ");
        Long bookId = scanner.nextLong();
        client.blockBook(bookId, writer.getId());
    }

    private void unBlockBookMenu() {
        Scanner scanner = new Scanner(System.in);
        printAllBooks();
        System.out.println("Which book to unblock: ");
        Long bookId = scanner.nextLong();
        client.unBlockBook(bookId, writer.getId());
    }

    private void checkBookMenu() {
        Scanner scanner = new Scanner(System.in);
        printAllBooks();
        System.out.println("Which book to check: ");
        Long bookId = scanner.nextLong();
        if (client.checkBook(bookId, writer.getId()))
            System.out.println("The book is not rented already");
    }

    private void removeBookMenu() {
        Scanner scanner = new Scanner(System.in);
        printAllBooks();
        System.out.println("Which book to remove: ");
        Long bookId = scanner.nextLong();
        if (client.removeBook(bookId, writer.getId()))
            System.out.println("The book is removed successfully");
        else
            System.out.println("Failed! Something went wrong...");
    }

    private void updateBookMenu() {
        Scanner scanner = new Scanner(System.in);
        printAllBooks();
        System.out.println("Which book to update: ");
        int bookId = scanner.nextInt();
        if (!client.checkBook((long) bookId, writer.getId())) {
            System.out.println("The book is not ready to update");
            return;
        }
        scanner.nextLine();
        System.out.println("chosen book:" + bookId);
        Book bookToUpdate = new Book();
        for (Book book : books) {
            if (book.getId() == bookId) {
                bookToUpdate = book;
                break;
            }
        }
        if (bookToUpdate == null) return;
        System.out.println("Change the title: [press ENTER to skip] " + bookToUpdate.getTitle());
        String newTitle = scanner.nextLine();
        if (newTitle != null && !newTitle.isBlank())
            bookToUpdate.setTitle(newTitle);
        System.out.println("Change the description: [press ENTER to skip] " + bookToUpdate.getDescription());
        String newDescription = scanner.nextLine();
        if (newDescription != null && !newDescription.isBlank())
            bookToUpdate.setDescription(newDescription);
        System.out.println("Change the release date: [press ENTER to skip] " + bookToUpdate.getReleaseDate());
        String newReleaseDate = scanner.nextLine();
        if (newReleaseDate != null && !newReleaseDate.isBlank())
            bookToUpdate.setReleaseDate(LocalDate.parse(newReleaseDate));
        System.out.println("Updated book: " + bookToUpdate);
        if (client.updateBook((long) bookId, bookToUpdate, writer.getId()))
            System.out.println("The book is updated successfully");
        else
            System.out.println("Failed! Something went wrong...");
    }

}
