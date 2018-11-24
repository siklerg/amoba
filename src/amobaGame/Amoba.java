package amobaGame;

import java.util.Random;
import java.util.Scanner;

public class Amoba {

    Scanner scanner = new Scanner(System.in);
    Random generator = new Random();
    private static boolean pcMoved;
    private char[][] board = new char[3][3];
    private int a = 0, b = 0; // koordináták

    public Amoba() {
        init();
    }

    // játék *****************************************************
    public void play() {
        boolean gameOver = false; // jelzi, ha van nyertes
        String winner = ""; // ez tárolja ki a nyertes
        int counter = 0; // lépések számlálója - 9 kör lehet max

        System.out.println("W e l c o m e !");
        System.out.println();
        drawBoard();

        while ((gameOver == false) && (counter != 9)) {
            // X játékos köre ***********************************
            System.out.println("Player turn (X)");
            getCoordinates();
            while (!(board[a - 1][b - 1] == '_')) { // vizsgáljuk - plusz pontért :) - hogy foglalt-e a mező, ha igen, akkor újra bekérünk. Meg megint...
                System.out.println();
                System.out.println("That place is not EMPTY! Choose another one!");
                getCoordinates();
            }
            board[a - 1][b - 1] = 'X'; // beírjuk a karaktert a táblába
            counter++;
            drawBoard();

            if (isCharacterWinner('X')) { // megnézzük, hogy győztünk-e?
                gameOver = true;
                winner = "Player"; // letárolja a győztest
                continue; // ha van gyöztes, ugrik a ciklus elejére, és kilépünk a gameOver=true miatt
            }
            if (counter == 9) {
                continue;
            }

            // O PC köre ***********************************
            System.out.println("PC turn (O)");
            pcMoved = false;
            attack(); // első körben megnézi, hogy van-e győztes hely
            if (!pcMoved) {
                defend(); // második körben megnézi kell-e védekezni valahol
                if (!pcMoved) {
                    getCoordinatesComputer(); // keres egy üres helyet
                    while (!(board[a][b] == '_')) { // vizsgáljuk - plusz pontért :) - hogy foglalt-e a mező, ha igen, akkor újra bekérünk. Meg megint...
                        getCoordinatesComputer();
                    }
                }
            }

            board[a][b] = 'O'; // beírjuk a karaktert
            counter++;
            drawBoard();

            if (isCharacterWinner('O')) { // megnézzük, hogy győztünk-e?
                gameOver = true; // ha volt győztes, akkor a ciklus elejére lépve megáll
                winner = "PC"; // letárolja a győztest
            }
        }
        // játék vége ********************************************************
        System.out.println("Game Over!");
        if (winner == "") {
            System.out.println("D r a w!");
        } else {
            System.out.println("W i n n e r: " + winner);
        }
    }

    private void getCoordinates() { // kivétel kezelés
        System.out.print("Row? ");
        a = scanner.nextInt();
        while ((a<1)||(a>3)){
            System.out.println("The number must be 1 or 2 or 3!");
            System.out.print("Row? ");
            a = scanner.nextInt();
        }
        System.out.print("Column:? ");
        b = scanner.nextInt();
        while ((b<1)||(b>3)){
            System.out.println("The number must be 1 or 2 or 3!");
            System.out.print("Column:? ");
            b = scanner.nextInt();
        }
    }


    private void getCoordinatesComputer() {
        a = generator.nextInt(3);
        b = generator.nextInt(3);
    }

    private void attack() { // megnézi van-e olyan sor vagy oszlop vagy átló, ahol már van két 'O'
        int countX = 0;
        int countO;
        int k = 0;
        pcMoved = false;
        while ((k < 3) && (!pcMoved)) {// sorok vizsgálata
            countX = 0;
            countO = 0;
            for (int j = 0; j < board[k].length; j++) {
                if (board[k][j] == 'O') {
                    countO++;
                }
                if (board[k][j] == 'X') {
                    countX++;
                }
            }
            if ((countO == 2) && (countX == 0)) { // ha van két 'O' és a harmadik nem 'X', akkor megkeressük az üres helyet
                if (board[k][0] == '_') {
                    a = k;
                    b = 0;
                    pcMoved = true; // ha bárhol talál, akkor letárol, és ezen keresztül tudjuk hogy mást nem kell futtatni (lásd alább)
                } else if (board[k][1] == '_') {
                    a = k;
                    b = 1;
                    pcMoved = true;
                } else if (board[k][2] == '_') {
                    a = k;
                    b = 2;
                    pcMoved = true;
                }
            }
            k++;
        }
        if (!pcMoved) { // ha már lépett , akkor itt nem fut tovább
            k = 0;
            while ((k < 3) && (!pcMoved)) { // oszlopok vizsgálata, ha nem volt megfelelő sor
                countX = 0;
                countO = 0;
                for (int j = 0; j < board[k].length; j++) {
                    if (board[j][k] == 'O') {
                        countO++;
                    }
                    if (board[j][k] == 'X') {
                        countX++;
                    }
                }
                if ((countO == 2) && (countX == 0)) { // ha van két 'O' és a harmadik nem 'X', akkor megkeressük az üres helyet
                    if (board[0][k] == '_') {
                        a = 0;
                        b = k;
                        pcMoved = true;
                    } else if (board[1][k] == '_') {
                        a = 1;
                        b = k;
                        pcMoved = true;
                    } else if (board[2][k] == '_') {
                        a = 2;
                        b = k;
                        pcMoved = true;
                    }
                }
                k++;
            }
            if (!pcMoved) { // ha már lépett , akkor itt nem fut tovább
                if ((board[0][0] == 'O') && (board[1][1] == 'O') && (board[2][2])=='_') {// egyik átló vizsgálata
                    a = 2;
                    b = 2;
                    pcMoved = true;
                } else if ((board[0][0] == 'O') && (board[2][2] == 'O') && (board[1][1])=='_') {
                    a = 1;
                    b = 1;
                    pcMoved = true;
                } else if ((board[2][2] == 'O') && (board[1][1] == 'O') && (board[0][0])=='_') {
                    a = 0;
                    b = 0;
                    pcMoved = true;
                }
                if (!pcMoved) { // ha már lépett , akkor itt nem fut tovább
                    if ((board[2][0] == 'O') && (board[1][1] == 'O') && (board[0][2])=='_') {// másik átló
                        a = 0;
                        b = 2;
                        pcMoved = true;
                    } else if ((board[1][1] == 'O') && (board[0][2] == 'O') && (board[2][0])=='_') {
                        a = 2;
                        b = 0;
                        pcMoved = true;
                    } else if ((board[0][2] == 'O') && (board[2][0] == 'O') && (board[1][1])=='_') {
                        a = 1;
                        b = 1;
                        pcMoved = true;
                    }
                }
            }
        }
    }

    private void defend() {
        int countX = 0;
        int countO;
        int k = 0;
        pcMoved = false;
        while ((k < 3) && (!pcMoved)) {// sorok vizsgálata
            countX = 0;
            countO = 0;
            for (int j = 0; j < board[k].length; j++) {
                if (board[k][j] == 'X') {
                    countX++;
                }
                if (board[k][j] == 'O') {
                    countO++;
                }
            }
            if ((countX == 2) && (countO == 0)) { // ha van két 'X' és a harmadik nem 'O', akkor megkeressük az üres helyet
                if (board[k][0] == '_') {
                    a = k;
                    b = 0;
                    pcMoved = true;
                } else if (board[k][1] == '_') {
                    a = k;
                    b = 1;
                    pcMoved = true;
                } else if (board[k][2] == '_') {
                    a = k;
                    b = 2;
                    pcMoved = true;
                }
            }
            k++;
        }
        if (!pcMoved) { // ha már lépett , akkor itt nem fut tovább
            k = 0;
            while ((k < 3) && (!pcMoved)) { // oszlopok vizsgálata, ha nem volt megfelelő sor
                countX = 0;
                countO = 0;
                for (int j = 0; j < board[k].length; j++) {
                    if (board[j][k] == 'X') {
                        countX++;
                    }
                    if (board[j][k] == 'O') {
                        countO++;
                    }
                }
                if ((countX == 2) && (countO == 0)) { // ha van két 'X' és a harmadik nem 'O', akkor megkeressük az üres helyet
                    if (board[0][k] == '_') {
                        a = 0;
                        b = k;
                        pcMoved = true;
                    } else if (board[1][k] == '_') {
                        a = 1;
                        b = k;
                        pcMoved = true;
                    } else if (board[2][k] == '_') {
                        a = 2;
                        b = k;
                        pcMoved = true;
                    }
                }
                k++;
            }
            if (!pcMoved) { // ha már lépett , akkor itt nem fut tovább
                if ((board[0][0] == 'X') && (board[1][1] == 'X') && (board[2][2])=='_') {// egyik átló vizsgálata
                    a = 2;
                    b = 2;
                    pcMoved = true;
                } else if ((board[0][0] == 'X') && (board[2][2] == 'X') && (board[1][1])=='_') {
                    a = 1;
                    b = 1;
                    pcMoved = true;
                } else if ((board[2][2] == 'X') && (board[1][1] == 'X') && (board[0][0])=='_') {
                    a = 0;
                    b = 0;
                    pcMoved = true;
                }
                if (!pcMoved) { // ha már lépett , akkor itt nem fut tovább
                    if ((board[2][0] == 'X') && (board[1][1] == 'X') && (board[0][2])=='_') {// másik átló
                        a = 0;
                        b = 2;
                        pcMoved = true;
                    } else if ((board[1][1] == 'X') && (board[0][2] == 'X') && (board[2][0])=='_') {
                        a = 2;
                        b = 0;
                        pcMoved = true;
                    } else if ((board[0][2] == 'X') && (board[2][0] == 'X') && (board[1][1])=='_') {
                        a = 1;
                        b = 1;
                        pcMoved = true;
                    }
                }
            }
        }
    }

    private void drawBoard() {
        System.out.println();
        System.out.println("  1 2 3");
        for (int i = 0; i < board.length; ++i) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < board[i].length; ++j) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void init() {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                board[i][j] = '_';
            }
        }
    }

    public boolean isCharacterWinner(char symbol) {
        return checkRow(symbol) || checkColumn(symbol)
                || checkDiagonalLeftToRight(symbol)
                || checkDiagonalRightToLeft(symbol);
    }

    private boolean checkRow(char symbol) {
        boolean check = false;
        for (int i = 0; i < board.length; ++i) {
            if ((board[i][0] == symbol) && (board[i][1] == symbol) && (board[i][2] == symbol)) {
                check = true;
                break;
            }
        }
        return check;
    }

    private boolean checkColumn(char symbol) {
        boolean check = false;
        for (int i = 0; i < board[0].length; ++i) {
            if ((board[0][i] == symbol) && (board[1][i] == symbol) && (board[2][i] == symbol)) {
                check = true;
                break;
            }
        }
        return check;
    }

    private boolean checkDiagonalLeftToRight(char symbol) {
        boolean check = false;
        if ((board[0][0] == symbol) && (board[1][1] == symbol) && (board[2][2] == symbol)) {
            check = true;
        }
        return check;
    }

    private boolean checkDiagonalRightToLeft(char symbol) {
        boolean check = false;
        if ((board[2][0] == symbol) && (board[1][1] == symbol) && (board[0][2] == symbol)) {
            check = true;
        }
        return check;
    }
}