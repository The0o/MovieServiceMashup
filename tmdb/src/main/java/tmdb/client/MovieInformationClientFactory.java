package tmdb.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MovieInformationClientFactory {

    private static final String API_KEY_ENV = "TMDB_API_KEY";
    private static final String ENV_FILE = ".env";

    private static MovieInformationClient client;

    private MovieInformationClientFactory() {
    }

    // Creation paresseuse : la cle API n'est lue qu'au premier appel
    public static synchronized MovieInformationClient getClient() {
        if (client == null) {
            client = new TMDbClientImpl(readApiKey());
        }
        return client;
    }

    // Ordre : variable d'environnement, propriete systeme, puis fichier .env
    private static String readApiKey() {
        String apiKey = System.getenv(API_KEY_ENV);
        if (isBlank(apiKey)) {
            apiKey = System.getProperty("tmdb.api.key");
        }
        if (isBlank(apiKey)) {
            apiKey = readFromEnvFile();
        }
        if (isBlank(apiKey)) {
            throw new IllegalStateException("Cle API TMDb manquante : definir " + API_KEY_ENV
                    + " dans un fichier " + ENV_FILE + " a la racine du projet ou comme variable d'environnement");
        }
        return apiKey.trim();
    }

    // Cherche le .env dans le dossier courant puis dans les dossiers parents
    // (gradle lance les sous-projets depuis leur propre dossier, pas depuis la racine)
    private static String readFromEnvFile() {
        for (Path dir = Path.of("").toAbsolutePath(); dir != null; dir = dir.getParent()) {
            Path envFile = dir.resolve(ENV_FILE);
            if (Files.isRegularFile(envFile)) {
                try {
                    for (String line : Files.readAllLines(envFile)) {
                        String value = parseLine(line.trim());
                        if (value != null) {
                            return value;
                        }
                    }
                } catch (IOException e) {
                    return null;
                }
                return null;
            }
        }
        return null;
    }

    private static String parseLine(String line) {
        if (line.startsWith("export ")) {
            line = line.substring("export ".length()).trim();
        }
        if (line.startsWith("#") || !line.startsWith(API_KEY_ENV + "=")) {
            return null;
        }
        String value = line.substring(API_KEY_ENV.length() + 1).trim();
        if (value.length() >= 2 && (value.startsWith("\"") && value.endsWith("\"")
                || value.startsWith("'") && value.endsWith("'"))) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
