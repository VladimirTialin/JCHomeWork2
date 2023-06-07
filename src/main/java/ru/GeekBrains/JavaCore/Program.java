package ru.GeekBrains.JavaCore;
import java.util.Random;
import java.util.Scanner;
public class Program {
    private static final int WIN_COUNT = 4;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '•';
    private static boolean win;
    private static final Scanner SCANNER = new Scanner(System.in);
    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля
    private static final Random random = new Random();
    private static int fieldSizeX; // Размерность игрового поля
    private static int fieldSizeY; // Размерность игрового поля
    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (gameCheck(DOT_AI, "Компьютер победил!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да)");
            if (!SCANNER.next().equalsIgnoreCase("Y"))
                break;
        }
    }
    /**
     * Инициализация игрового поля
     */
    private static void initialize() {
        // Установим размерность игрового поля
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];
        // Пройдем по всем элементам массива
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                // Проинициализируем все элементы массива DOT_EMPTY (признак пустого поля)
                field[x][y] = DOT_EMPTY;
            }
        }
    }
    /**
     * Отрисовка игрового поля
     * //TODO: Поправить отрисовку игрового поля
     */
    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        }
        System.out.println();

        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print(i + 1 + "|");

            for (int j = 0; j < fieldSizeY; j++)
                System.out.print(field[i][j] + "|");

            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();

    }
    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn() {
        int x, y;
        do {
            System.out.printf("Введите координаты хода X и Y (от 1 до 5) через пробел >>> ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
        win = checkWin(DOT_HUMAN, x, y,WIN_COUNT);
    }
    /**
     * Проверка, ячейка является пустой
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность массива, игрового поля)
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Ход компьютера
     */
    private static void aiTurn() {

        int x, y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
         win= checkWin(DOT_AI,x,y,WIN_COUNT);
    }

    /**
     * Проверка победы
     * TODO: Переработать метод в домашнем задании
     *
     * @param c
     * @return
     */

    static boolean checkWin(char c, int x, int y, int win) {
        return horizontal(x, y, c,win) || vertical(x, y, c,win) ||
                diagonal(x, y, c,win) || reverseDiagonal(x, y, c,win);

    }
    /**
     * Проверка на ничью
     *
     * @return
     */
    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++)
                if (isCellEmpty(x, y)) return false;
        }
        return true;
    }

    /**
     * Метод проверки состояния игры
     *
     * @param c
     * @param str
     * @return
     */
    static boolean gameCheck(char c, String str) {
        if (win) {
            System.out.println(str);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }

        return false; // Игра продолжается
    }
    /**
     * Размер прямой диагонали, заданных координат (Слево-направо)
     */
    static int StraightDiagonalLeftRight(int x, int y) {
        int sizeField = fieldSizeX - x;
        if (fieldSizeX - x > fieldSizeY - y) sizeField = fieldSizeY - y;
        else if (fieldSizeX - x < fieldSizeY - y) sizeField = fieldSizeX - x;
        return sizeField;
    }
    /**
     * Размер обратной диагонали, заданных координат (Слево-направо)
     */
    static int ReverseDiagonalLeftRight(int x, int y) {
        int size = x;
        if (x > y) size = y;
        else if (x < y) size = x;
        return size;
    }
    /**
     * Метод проверки на победу по горизонтали
     *
     * @param x точки по оси Х
     * @param y точка по оси Y
     * @param rate символ игрока
     * @param win размер победной линии
     * @return true - найден комбинация для победы false - комбинация не найдена
     */
    static boolean horizontal(int x, int y, char rate, int win) {
        int count = 0;
        if (y != 0 && y != fieldSizeY - 1) {
            for (int i = y + 1; i < fieldSizeY; i++) {
                if (field[x][i] == rate) count++;
                else break;
            }
            for (; 0 <= y; y--) {
                if (field[x][y] == rate) count++;
                else break;
            }
        }
        else if (y == fieldSizeY - 1) {
            for (; y >= 0; y--) {
                if (field[x][y] == rate) count++;
                else break;
            }
        }
        else if (y == 0) {
            for (; y < fieldSizeY; y++) {
                if (field[x][y] == rate) count++;
                else break;
            }
        }
         return win==count;
    }
    /**
     * Метод проверки на победу по-вертикали
     *
     * @param x точки по оси Х
     * @param y точка по оси Y
     * @param rate символ игрока
     * @param win размер победной линии
     * @return true - найден комбинация для победы false - комбинация не найдена
     */
    static boolean vertical(int x, int y, char rate, int win) {
        int count = 0;
        if (x != 0 && x != fieldSizeX - 1) {
            for (int i = x + 1; i < fieldSizeX; i++) {
                if (field[i][y] == rate) count++;
                else break;
            }
            for (; 0 <= x; x--) {
                if (field[x][y] == rate) count++;
                else break;
            }
        } else if (x == fieldSizeX - 1) {
            for (; x >= 0; x--) {
                if (field[x][y] == rate) count++;
                else break;
            }
        } else if (x == 0) {
            for (; x < fieldSizeX; x++) {
                if (field[x][y] == rate) count++;
                else break;
            }
        }
        return win==count;
    }

    /**
     * Метод проверки на победу прямой диагонали
     *
     * @param x точки по оси Х
     * @param y точка по оси Y
     * @param rate символ игрока
     * @param win размер победной линии
     * @return true - найден комбинация для победы false - комбинация не найдена
     */
    static boolean diagonal(int x, int y, char rate, int win) {
        int count = 0;
        for (int i = 0; i < StraightDiagonalLeftRight(x, y); i++) {
            if (y == 0 || x == 0 || field[x - 1][y - 1] != rate) {
                if (field[x + i][y + i] == rate) count++;
                else break;
            } else if (y == fieldSizeY - 1 || x == fieldSizeX - 1 ||
                    (field[x + 1][y + 1] != rate & field[x - 1][y - 1] == rate)) {
                count = 0;
                for (int j = 0; j <= ReverseDiagonalLeftRight(x, y); j++) {
                    if (field[x - j][y - j] == rate) count++;
                    else break;
                }
            } else {
                count = 0;
                for (int k = 0; k < win / 2; k++) {
                    if (field[x + k][y + k] == rate) count++;
                    else break;
                    if (field[x - k][y - k] == rate) count++;
                    else break;
                }
            }
        }
        return win==count;
    }
    /**
     * Метод проверки на победу обратной диагонали
     *
     * @param x точки по оси Х
     * @param y точка по оси Y
     * @param rate символ игрока
     * @param win размер победной линии
     * @return true - найден комбинация для победы false - комбинация не найдена
     */
    static boolean reverseDiagonal(int x, int y, char rate, int win) {
        int count = 0;
        for (int i = 0; i <= y-x; i++) {
            if (y == fieldSizeY - 1 || x == 0) {
                if (field[x + i][y - i] == rate) count++;
                else break;
            }
        }
        if (x == fieldSizeX - 1 || y == 0) {
            count = 0;
            for (int j = 0; j <= x-y; j = j + 1) {
                if (field[x - j][y + j] == rate) count++;
                else break;
            }
        } else {
            if (x == 0 || y == 0 || x == field.length - 1 || y == fieldSizeY - 1) return false;
            count = 0;
            for (int k = 0; k < win / 2; k++) {
                if (field[x + k][y - k] == rate) count++;
                else break;
                if (field[x - k][y + k] == rate) count++;
                else break;
            }
        }
            return win==count;
        }
}

