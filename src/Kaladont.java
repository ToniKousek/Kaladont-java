import jdk.jshell.spi.ExecutionControl;
import java.util.*;

public class Kaladont {
    // to fix the player 0, you have to go to the 20th line, to the comment
    final static String script = "Hello to Kaladont.\nTo play the game, you have to bla bla bla bla bla\nPlayer 0 being the first player is a feature, because I think like a programmer all the time, and the first number is always zero";
    private static final Scanner scanner = new Scanner(System.in);

    private static final ArrayList<String> wordsGuessed = new ArrayList<>();
    private static String previousLastLetters;

    public static void main(String[] args) throws ExecutionControl.NotImplementedException {
        int playerCount = startGame();
        Player[] players = new Player[playerCount];

        for (int i = 0; i < playerCount; i++) {
            // players[i] = new Player(i+1); // for the first player being 'Player 0'
            players[i] = new Player(i);

        }
        int numPlayersPlaying = players.length;

        // add '.clone()' to the end for future update of some players leaving after rounds
        Player[] playersPlaying = players;

        // game loop
        while (true) {
            // go through each player
            for (int i = 0; i < playerCount; i++) {
                // if the player is not playing, skip him/her
                if (!playersPlaying[i].playing) continue;

                // if the player won
                if (numPlayersPlaying <= 1) {
                    // if they want to continue
                    if (won(playersPlaying[i], playersPlaying)) {

                        System.out.println("Play again!");

                        // set all players to playing
                        for (int j = 0; j < playerCount; j++) {
                            playersPlaying[j].playing = true;
                        }
                        numPlayersPlaying = players.length;
                        // reset so the first word can be anything
                        previousLastLetters = null;
                        // break to the game loop, so the player[0] goes first
                        break;
                    } else {
                        System.out.println("Exiting!");
                        return;
                    }
                }

                /*
                 Ovo ste pitali hoće li raditi, radi pa sam ostavio iako mislim da je bolje koristiti flag  unutar uvjeta za 'while' loop
                 Ovo je nešto što se zove 'label' https://en.wikipedia.org/wiki/Label_(computer_science)
                 Koristi se za 'goto' statements https://youtu.be/AKJhThyTmQw?si=8PBTzpw61-RJEHmY
                 Ne sviđa mi se koristiti ovaj način zbog videa gore
                */
                loop:
                while (true) {
                    // get the word from a player
                    String word = getWord(playersPlaying[i].getPlayerId());

                    // checkWord(word) returns an enum CheckWordReturn
                    switch (checkWord(word)) {
                        case Short -> System.out.println("Please input a word with more than two letters!");
                        case AlreadyGuessed ->
                                System.out.println("You already guessed the word! You can't repeat yourself or other players!");
                        case LastFalse ->
                                System.out.println("Your first two letters didn't match '" + previousLastLetters + "'!");

                        case Kaladont -> {
                            System.out.println("You lost!");
                            // Setting to false so the player can be skipped all next rounds
                            playersPlaying[i].playing = false;
                            numPlayersPlaying--;
                            break loop;

                        }
                        case Ok -> {
                            System.out.println("Ok!");
                            previousLastLetters = word.substring(word.length() - 2);

                            wordsGuessed.add(word);
                            break loop;
                        }
                        default -> throw new ExecutionControl.NotImplementedException("Return value not created!");

                    }
                }
            }
        }
    }


    private static CheckWordReturn checkWord(String word) {
        if (word.length() < 2) return CheckWordReturn.Short;
        if (word.endsWith("ka")) return CheckWordReturn.Kaladont;
        if (previousLastLetters == null) return CheckWordReturn.Ok; // if it's the first word in the game
        if (wordsGuessed.contains(word)) return CheckWordReturn.AlreadyGuessed;
        if (word.substring(0, 2).equals(previousLastLetters)) return CheckWordReturn.Ok;
        return CheckWordReturn.LastFalse; // can be written like 'else ...;'
    }

    private static String getWord(int playerId) {
        System.out.println("Player " + playerId + ": ");
        String in = scanner.next();
        if (in.contains(" ")) {
            System.out.println("Please input one word");
            return getWord(playerId);
        }
        // https://stackoverflow.com/a/5238524/21512548
        if (!in.matches("[a-zA-Z]+")) {
            System.out.println("Please input a word");
            return getWord(playerId);
        }
        return in.toLowerCase();
    }

    private static int startGame(){
        System.out.println(script);
        while (true) {
            System.out.println("How many players do you want (more than 2 inclusive): ");
            int inNum;
            String in = scanner.next();
            if (in.matches("[0-9]+")) {
                inNum = Integer.parseInt(in);
                if (inNum >= 2) return inNum;
            }


        }
    }

    private static boolean won(Player player, Player[] players) {
        System.out.println("Player " + player.getPlayerId() + " won!\nCongratulations!");
        player.won();
        printWinners(players);
        while (true) {
            System.out.println("Do you want to continue (y,n): ");
            char in = scanner.next().charAt(0);
            if (in == 'n') return false;
            if (in == 'y') return true;
            else System.out.println("Usage: y = yes, n = no");
        }
    }

    private static void printWinners(Player[] players) {
        for (Player player : players) {
            System.out.println("Player " + player.getPlayerId() + " has " + player.wins + (player.wins == 1 ? " wins" : " win"));
        }
    }
}

