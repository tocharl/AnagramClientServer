package atl.client.g49582.view;

import atl.client.g49582.exception.ViewException;
import java.util.Scanner;

/**
 *
 * @author game
 */
public class View {
    /**
     * Prints a text in a console. If you want to indent the text with 2 blank
     * spaces, you just update this method instead of all the display method.
     *
     * @param text text to print in the console.
     */
    static final void print(String text) {
        System.out.println(text);
    }

    /**
     * The user can give his input from this scanner.
     */
    private final Scanner input;

    /**
     * Constructs the console view. The input used is the input stream connected
     * to the console (System.in).
     *
     */
    public View() {
        input = new Scanner(System.in, "UTF-8");

    }

    /**
     * Prints a welcome message.
     */
    public void initialize() {
        print("Bienvenue au jeu d'anagramme");
    }

    /**
     * Prints a farewell message.
     */
    public void quit() {
        print("\nBye Bye\n");
    }

    /**
     * Prints an error message.
     *
     * @param message the error message.
     */
    public void displayError(String message) {
        print("\nErreur : " + message);
    }

    /**
     * Prints the help message.
     */
    public void displayHelp() {
        print("\nUsage :");
        print("\ttaper check <word> pour proposer votre réponse");
        print("\ttaper pass pour passer le mot courant");
        print("\ttaper quit pour arrêter le programme");
        print("\n");

    }

    /**
     * Asks a command from the user.
     *
     * @return the request of the user.
     *
     * @throws ViewException if the Scanner is not initialized.
     */
    public String askCommand() throws ViewException {
        if (input == null) {
            throw new ViewException("Aucun input Scanner déclaré");
        }
        return input.nextLine();
    }
    
    public boolean defaultHost() throws ViewException {
        print("Tapé oui si vous voulez entrer une ip host spécifique");
        return !askCommand().equals("oui");
    }
    
    public boolean defaultPort() throws ViewException {
        print("Tapé oui si vous voulez entrer un numéro de port spécifique");
        return !askCommand().equals("oui");
    }
    
    public String askPort() throws ViewException {
        print("Numéro de port ");
        return askCommand();
    }
    
    public String askHost() throws ViewException {
        print("Adresse ip du serveur host ");
        return askCommand();
    }
    
    public void start() {
        print("Tapé start pour revoir l'anagram a résoudre");
    }
    
    public String askLogin() throws ViewException {
        print("Entrez votre login : ");
        return askCommand();
    }

    /**
     * Prints the scrambled word given and the amount of proposal send by the
     * player. The user has to find the original word.
     *
     * @param scrambledWord the scrambled version of the word.
     * @param nbProposal the amout of proposal for the current word.
     */
    public void displayScrambledWord(String scrambledWord, int nbProposal) {
        print("Quel est l'anagramme de " + scrambledWord);
        print(nbProposal + " proposition(s) pour ce mot.");
    }

    /**
     * Prints some congratulations if the user wins.
     */
    public void displayCongratulations() {
        print("Bravo !! ");
    }

    /**
     * Prints some comfort message if the user fails.
     */
    public void displayTryAgain() {
        print("Mauvaise réponse. Essaie encore ");
    }

    /**
     * Prints the answer of the anagram game.
     *
     * @param answer the answer of the anagram game.
     */
    public void displayAnswer(String answer) {
        print("La réponse est " + answer);
    }

    /**
     * Prints some statistics about the game.
     *
     * @param nbWords the amount of words to play.
     * @param nbRemainingWords the amount of remaining words to play.
     * @param nbSolvedWords the amount of solved words.
     * @param nbUnsolvedWords the amount of unsolved words.
     */
    public void displayStatistics(int nbWords, int nbRemainingWords, int nbSolvedWords, int nbUnsolvedWords) {
        print("Il reste " + nbRemainingWords + " mot(s) à deviner sur les " + nbWords + " mots disponibles");
        print("Vous avez trouvé " + nbSolvedWords + " mot(s) et échoué sur " + nbUnsolvedWords + " mot(s) ");
    }
}
