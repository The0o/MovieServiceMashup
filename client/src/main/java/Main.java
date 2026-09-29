import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Que voulez vous faire ?\nPour trouver un film, tapez 1.\nPour ajouter un film, tapez 2.\n");
            while (!scanner.hasNextInt()) {
                System.out.println("Veuillez entrer un chiffre\n");
                scanner.next();
            }
            int optionChoisi = scanner.nextInt();
            if (optionChoisi != 1 && optionChoisi != 2) {
                System.out.println("Chiffre non valide, seul les options 1 et 2 sont valides\n");
            }
            if (optionChoisi == 1) {
                System.out.println("Vous voulez trouvez un film. Veuillez indiquer le nom du film.\n");
                String nomFilm = scanner.next();
                //fonction pour trouver le nom du film, à brancher apres l'exo 4
            }
            if (optionChoisi == 2) {
                System.out.println("Vous voulez ajouter un film\nVeuillez d'abord indiquer le nouveau nom du film\n");
                String nomFilm = scanner.next();
                System.out.println("Maintenant, l'année du film\n");
                String anneeFilm = scanner.next();
                System.out.println("Maintenant, la date de la visualisation de ce film\n");
                String dateVisualisationFilm = scanner.next();
                System.out.println("Ainsi que sa note\n");
                String noteFilm = scanner.next();
                //fonction pour ajouter le film, à brancher apres l'exo 2
            }
        }
    }
}