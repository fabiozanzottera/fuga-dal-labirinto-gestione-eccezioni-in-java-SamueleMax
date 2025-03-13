package org.example;

import java.util.Random;
import java.util.Scanner;

// Eccezione personalizzata per movimenti fuori dai limiti
class OutOfBoundsException extends Exception {
    public OutOfBoundsException(String message) {
        super(message);
    }
}

// Eccezione personalizzata per collisione con muri
class WallCollisionException extends Exception {
    public WallCollisionException(String message) {
        super(message);
    }
}

public class MazeEscape {
    // Dichiarazione della matrice del labirinto
    private static char[][] LABIRINTO = {
        { 'P', '.', '#', '.', '.' },
        { '#', '.', '#', '.', '#' },
        { '.', '.', '.', '#', '.' },
        { '#', '#', '.', '.', '.' },
        { '#', '.', '#', '#', 'E' }
    };

    // Coordinate iniziali del giocatore
    private static int playerX = 0;
    private static int playerY = 0;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean escaped = false;
        int mosse = 0;

        generaLabirintoCasuale();
        System.out.println("Benvenuto in Maze Escape! Trova l'uscita ('E').");

        while (!escaped) {
            printMaze();
            System.out.print("Muoviti (W = su, A = sinistra, S = giù, D = destra): ");
            char move = scanner.next().toUpperCase().charAt(0);

            try {
                // Incrementare il contatore di mosse
                mosse++;
                // Chiamare il metodo per muovere il giocatore
                movePlayer(move);
                // Verificare se ha raggiunto l'uscita e terminare il gioco
                if (playerX == LABIRINTO.length - 1 && playerY == LABIRINTO[0].length - 1) {
                    escaped = true;
                    System.out.println("Hai trovato l'uscita! Congratulazioni!");
                    System.out.println("Hai impiegato " + mosse + " mosse.");
                }
            } catch (OutOfBoundsException | WallCollisionException e) {
                // Stampare il messaggio di errore dell'eccezione
                System.out.println(e.getMessage());
            }
        }

        scanner.close();
    }

    public static void generaLabirintoCasuale() {
        LABIRINTO = new char[5][5];
        Random random = new Random();

        // Riempi di muri
        for (int i = 0; i < LABIRINTO.length; i++) {
            for (int j = 0; j < LABIRINTO[i].length; j++) {
                LABIRINTO[i][j] = '#';
            }
        }

        // Crea un percorso valido
        int x = 0;
        int y = 0;
        while (x < LABIRINTO.length - 1 || y < LABIRINTO[0].length - 1) {
            LABIRINTO[x][y] = '.';

            // Decidi se muoverti a destra, sinistra, su o giù
            int direzione = random.nextInt(3);
            switch (direzione) {
                case 0:
                    if (x < LABIRINTO.length - 1) {
                        x++;
                    }
                    break;
                case 1:
                    if (y < LABIRINTO[0].length - 1) {
                        y++;
                    }
                    break;
                case 2:
                    if (x > 0) {
                        x--;
                    }
                    break;
            }
        }

        LABIRINTO[0][0] = 'P';
        LABIRINTO[LABIRINTO.length - 1][LABIRINTO[0].length - 1] = 'E';
    }

    /**
     * Metodo per spostare il giocatore all'interno del labirinto
     * Deve controllare:
     * - Se il movimento è fuori dai limiti → lancia OutOfBoundsException
     * - Se il movimento porta su un muro ('#') → lancia WallCollisionException
     * - Se il movimento è valido, aggiornare la posizione
     */
    private static void movePlayer(char direction) throws OutOfBoundsException, WallCollisionException {
        // Dichiarare nuove variabili per la posizione dopo il movimento
        int newX = playerX;
        int newY = playerY;
        
        // Switch-case per aggiornare le nuove coordinate in base alla direzione
        switch (direction) {
            case 'W':
                newX--;
                break;
            case 'A':
                newY--;
                break;
            case 'S':
                newX++;
                break;
            case 'D':
                newY++;
                break;
            default:
                return;
        }
        
        // Controllare se il movimento è fuori dalla matrice e lanciare OutOfBoundsException
        if (newX < 0 || newX >= LABIRINTO.length || newY < 0 || newY >= LABIRINTO[0].length) {
            throw new OutOfBoundsException("Movimento fuori dai limiti!");
        }
        
        // Controllare se il movimento porta su un muro e lanciare WallCollisionException
        if (LABIRINTO[newX][newY] == '#') {
            throw new WallCollisionException("Hai colpito un muro!");
        }
        
        // Aggiornare la matrice con la nuova posizione del giocatore
        LABIRINTO[playerX][playerY] = '.';
        LABIRINTO[newX][newY] = 'P';
        playerX = newX;
        playerY = newY;
    }

    /**
     * Metodo per stampare il labirinto attuale
     */
    private static void printMaze() {
        // Stampare la matrice riga per riga
        for (int i = 0; i < LABIRINTO.length; i++) {
            for (int j = 0; j < LABIRINTO[i].length; j++) {
                System.out.print(LABIRINTO[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
