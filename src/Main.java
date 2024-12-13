// Интерфейс для выполнения различных операций калькулятора
interface Calculation {
    void Calculate(int x, int y); // Метод для выполнения вычислений
}

// Реализация сложения
class Plus implements Calculation {
    @Override
    public void Calculate(int x, int y) {
        System.out.println("калькулятор:" + x + " + " + y + " = " + (x + y));
    }
}

// Реализация вычитания
class Minus implements Calculation {
    @Override
    public void Calculate(int x, int y) {
        System.out.println("калькулятор:" + x + " - " + y + " = " + (x - y));
    }
}

// Класс калькулятора, который использует объекты Calculation
class Calculator {
    private Calculation calculation; // Текущая операция калькулятора

    // Метод для установки операции
    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    // Метод для очистки текущей операции
    public void clearCalculation() {
        this.calculation = null; // Сбрасываем действие
        System.out.println("калькулятор: Действие сброшено");
    }

    // Метод для выполнения операции
    public void executeCalculation(int x, int y) {
        if (calculation != null) {
            calculation.Calculate(x, y); // Выполняем операцию
        } else {
            System.out.println("калькулятор: Жду указаний"); // Если операция не установлена
        }
    }
}

// Абстрактный класс для реализации паттерна Chain of Responsibility
abstract class Notifier {
    private int priority; // Приоритет уведомления
    private Notifier nextNotifier; // Следующий обработчик в цепочке

    public Notifier(int priority) {
        this.priority = priority;
    }

    // Устанавливаем следующий обработчик
    public void setNextNotifier(Notifier nextNotifier) {
        this.nextNotifier = nextNotifier;
    }

    // Метод для передачи уведомления по цепочке
    public void notifyManager(String message, int level) {
        if (level >= priority) {
            write(message); // Если уровень достаточный, пишем сообщение
        }
        if (nextNotifier != null) {
            nextNotifier.notifyManager(message, level); // Передаем сообщение дальше
        }
    }

    // Абстрактный метод для реализации конкретного уведомления
    public abstract void write(String message);
}

// Класс для определения уровней приоритета
class Priority {
    public static final int OK = 1;        // Низкий приоритет
    public static final int smthWrong = 2; // Средний приоритет
    public static final int Problem = 3;   // Высокий приоритет
}

// Уведомление с помощью простого отчета
class SimpleReportNotifier extends Notifier {
    public SimpleReportNotifier(int priority) {
        super(priority);
    }

    @Override
    public void write(String message) {
        System.out.println("Уведомляем с помощью простого отчёта: " + message);
    }
}

// Уведомление с помощью email
class EmailNotifier extends Notifier {
    public EmailNotifier(int priority) {
        super(priority);
    }

    @Override
    public void write(String message) {
        System.out.println("Отправляем Email: " + message);
    }
}

// Уведомление с помощью SMS
class SMSNotifier extends Notifier {
    public SMSNotifier(int priority) {
        super(priority);
    }

    @Override
    public void write(String message) {
        System.out.println("Отправляем SMS менеджеру: " + message);
    }
}

// Интерфейс итератора
interface Iterator {
    boolean hasNext(); // Есть ли следующая глава?
    String next();     // Получить текст следующей главы
}

// Класс книги, содержащей главы
class Book {
    private String[] chapters; // Массив глав
    private int size = 0;      // Текущее количество глав

    public Book(int capacity) {
        chapters = new String[capacity]; // Создаем массив заданной емкости
    }

    // Метод для добавления главы в книгу
    public void addChapter(String chapter) {
        if (size < chapters.length) {
            chapters[size++] = chapter; // Добавляем главу
        } else {
            System.out.println("Книга полна, невозможно добавить главу: " + chapter);
        }
    }

    // Метод для получения итератора
    public Iterator iterator() {
        return new BookIterator(chapters, size); // Передаем массив и размер
    }

    // Вложенный класс итератора
    private class BookIterator implements Iterator {
        private String[] chapters;
        private int size;
        private int index = 0; // Текущий индекс итерации

        public BookIterator(String[] chapters, int size) {
            this.chapters = chapters;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return index < size; // Проверяем, есть ли ещё главы
        }

        @Override
        public String next() {
            return chapters[index++]; // Возвращаем текущую главу и переходим к следующей
        }
    }
}

// Главный класс программы
public class Main {
    public static void main(String[] args) {
        // === Калькулятор ===
        Calculator calculator = new Calculator();
        calculator.executeCalculation(2, 2); // Нет действия, ждем указаний

        calculator.setCalculation(new Plus()); // Устанавливаем операцию сложения
        calculator.executeCalculation(2, 2); // Выполняем сложение

        calculator.setCalculation(new Minus()); // Устанавливаем операцию вычитания
        calculator.executeCalculation(5, 3); // Выполняем вычитание

        calculator.clearCalculation(); // Очищаем действие
        calculator.executeCalculation(4, 4); // Нет действия, ждем указаний


        System.out.println("\n");


        // === Уведомления (Chain of Responsibility) ===
        Notifier reportNotifier = new SimpleReportNotifier(Priority.OK);
        Notifier emailNotifier = new EmailNotifier(Priority.smthWrong);
        Notifier smsNotifier = new SMSNotifier(Priority.Problem);

        // Устанавливаем цепочку ответственности
        reportNotifier.setNextNotifier(emailNotifier);
        emailNotifier.setNextNotifier(smsNotifier);

        // Уведомления с разными уровнями приоритета
        reportNotifier.notifyManager("Всё в порядке", Priority.OK); // Простое уведомление
        reportNotifier.notifyManager("Что-то пошло не так", Priority.smthWrong); // Email и ниже
        reportNotifier.notifyManager("У нас серьёзная проблема!", Priority.Problem); // SMS и ниже


        System.out.println("\n");


        // === Итератор для книги ===
        Book book = new Book(5); // Создаем книгу с емкостью 5 глав

        // Добавляем главы в книгу
        book.addChapter("Глава 1: Пролог");
        book.addChapter("Глава 2: Герой отправляется в путешествие.");
        book.addChapter("Глава 3: Неожиданная встреча.");
        book.addChapter("Глава 4: Битва с драконом.");
        book.addChapter("Глава 5: Возвращение домой.");

        // Получаем итератор для чтения книги
        Iterator iterator = book.iterator();

        System.out.println("Читаем книгу:");
        // Обходим главы книги с помощью итератора
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}