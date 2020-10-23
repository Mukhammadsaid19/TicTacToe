import java.util.Scanner;

public class TicTacToe {
    private final int SIZE = 3;
    private enum Value {X("X"), O("O"), EMPTY(" ");
        private final String letter;
        Value(String letter){
            this.letter = letter;
        }
        @Override
        public String toString() {
            return letter;
        }
    }
    private final Value[][] board = new Value[SIZE][SIZE];
    private final Scanner scanner = new Scanner(System.in);

    public TicTacToe() {
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                board[i][j] = Value.EMPTY;
            }
        }
    }

    /**
     * Start player vs. player game
     */
    public void humanWithHuman(){
        char player1 = 'X';
        char player2 = 'O';
        int[] coordinate;

        System.out.println("Welcome to Tic-Tac-Toe game!");

        System.out.println("Initial board is empty.");
        System.out.println(toString());

        while (true) {
            System.out.println("Player1 (X), please input your move in x,y format: ");
            coordinate = getCoordinate();
            move(coordinate, player1);
            System.out.println(toString());
            if (isWon(Value.X)){
                System.out.println("Congratulations, Player1 you won!");
                break;
            }

            System.out.println("Player2 (O), please input your move in x,y format: ");
            coordinate = getCoordinate();
            move(coordinate, player2);
            System.out.println(toString());
            if (isWon(Value.O)){
                System.out.println("Congratulations, Player2 you won!");
                break;
            }

            if (isDraw()){
                System.out.println("Good job, guys! Draw!");
                break;
            }
        }
    }

    /**
     * Start player vs. computer game
     */
    public void computerWithHuman() {
        char computer = 'O';
        char player = 'X';
        int[] coordinate;

        System.out.println("Welcome to Tic-Tac-Toe game!");
        System.out.println("Initial board is empty.");
        System.out.println(toString());

        while (true) {

            System.out.println("Please, input your move in x,y format: ");
            coordinate = getCoordinate();
            move(coordinate, player);
            System.out.println(toString());
            if (isWon(Value.X)){
                System.out.println("Congratulations, you won!");
                break;
            }

            if (isDraw()){
                System.out.println("Draw! You're as smart as minimax algorithm!");
                break;
            }

            System.out.println("Computer has made a move: ");
            coordinate = getComputerMove();
            move(coordinate, computer);
            System.out.println(toString());
            if (isWon(Value.O)) {
                System.out.println("Ha-ha, I won!");
                break;
            }
        }
    }


    /**
     * Makes a move on the board depending on the player type: 'X' or 'O'
     * @param coordinate coordinate we need to mark
     * @param playerType player's type: 'X' or 'O'
     */
    private void move(int[] coordinate, char playerType){
        Value currentCell = board[coordinate[0]][coordinate[1]];
        if (currentCell == Value.EMPTY){
            if (playerType == 'X'){
                board[coordinate[0]][coordinate[1]] = Value.X;
            } else {
                board[coordinate[0]][coordinate[1]] = Value.O;
            }
        } else {
            System.out.println("Invalid move. Cell is reserved already.");
            coordinate = getCoordinate();
            move(coordinate, playerType);
        }
    }

    /**
     * Calculates heuristics for a particular state of the board.
     * @return 1 for victory, -1 for loss and 0 for draw statuses.
     */
    private int heuristicsScore() {
        if (isWon(Value.X))
            return -1;
        else if (isWon(Value.O))
            return 1;
        else
            return 0;
    }

    /**
     * Minimax algorithm that takes depth and isMax and calculates
     * optimal move that leads to victory.
     *
     * @param depth stands for how far we are proceeded so far in the game tree
     * @param isMax stands for which level we are solving
     * @return optimal score for a leaf
     */
    private int minimax(int depth, boolean isMax){
        int boardScore = heuristicsScore();

        if (boardScore == 1 || boardScore == -1){
            return boardScore;
        }
        if (isDraw()){
            return 0;
        }

        if (isMax){
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == Value.EMPTY){
                        board[i][j] = Value.O;
                        bestScore = Math.max(bestScore, minimax(depth + 1, !isMax));
                        board[i][j] = Value.EMPTY;
                    }
                }
            }

            return bestScore;
        }

        else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == Value.EMPTY) {
                        board[i][j] = Value.X;
                        bestScore = Math.min(bestScore, minimax(depth + 1, !isMax));
                        board[i][j] = Value.EMPTY;
                    }
                }
            }

            return bestScore;
        }
    }

    /**
     * Calculate the best optimal move by finding maximum
     * score returned by minimax()
     * @return optimal move for a computer
     */
    private int[] getComputerMove(){
        int bestScore = Integer.MIN_VALUE;
        int[] bestCoordinate = new int[2];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == Value.EMPTY){
                    board[i][j] = Value.O;

                    int moveScore = minimax(0, false);
                    board[i][j] = Value.EMPTY;

                    if (bestScore < moveScore){
                        bestCoordinate[0] = i;
                        bestCoordinate[1] = j;
                        bestScore = moveScore;
                    }
                }
            }
        }

        return bestCoordinate;
    }


    /**
     * Determines whether any moves are left in the array, i.e. if there's any draw
     * @return if the game is draw
     */
    private boolean isDraw() {
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (board[i][j] == Value.EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines whether a player won
     * @param playerType stands for either 'X' or 'O'
     * @return if the player won
     */
    private boolean isWon(Value playerType) {
        int counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == playerType){
                    counter++;
                }
            }
            if (counter == SIZE){
                return true;
            } else {
                counter = 0;
            }
        }

        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++){
                if (board[i][j] == playerType){
                    counter++;
                }
            }
            if (counter == SIZE){
                return true;
            } else {
                counter = 0;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (board[i][i] == playerType){
                counter++;
            }
        }

        if (counter == SIZE){
            return true;
        } else {
            counter = 0;
        }

        for (int i = 0; i < SIZE; i++) {
            if (board[i][SIZE - i - 1] == playerType){
                counter++;
            }
        }

        return counter == SIZE;

    }

    /**
     * Determines if coordinate is valid or not
     * @param coordinate takes a coordinate
     * @return is coordinate valid
     */
    private boolean isValid(int[] coordinate){
        return coordinate[0] < SIZE && coordinate[1] < SIZE && coordinate[0] >= 0 && coordinate[1] >= 0;
    }

    /**
     * Gets coordinate from user in a comma separated form: (a, b)
     * @return two-element array of coordinate
     */
    private int[] getCoordinate(){
        String[] input = scanner.nextLine().split(",");
        int x = Integer.parseInt(input[0]);
        int y = Integer.parseInt(input[1]);

        while (!isValid(new int[]{x, y})) {
            System.out.println("Invalid move. Please, try again: ");
            input = scanner.nextLine().split(",");
            x = Integer.parseInt(input[0]);
            y = Integer.parseInt(input[1]);
        }

        return new int[]{x, y};
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();


        for (int i = 0; i < SIZE; i++){
            sb.append("— — — — — — —\n");
            for (int j = 0; j < SIZE; j++){
                sb.append("| ").append(board[i][j].toString()).append(" ");
                if (j == SIZE - 1){
                    sb.append("| ");
                }
            }
            sb.append("\n");
            if (i == SIZE - 1){
                sb.append("— — — — — — —\n");
            }
        }

        return sb.toString();
    }
}
