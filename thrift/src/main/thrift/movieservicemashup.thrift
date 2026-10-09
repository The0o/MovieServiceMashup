namespace java fr.univangers.movieservice.thrift

/**
 * DTO représentant un film vu par l'utilisateur
 */
struct MovieDto {
    /** Titre du film (sert d'identifiant) */
    1: string title,

    /** Année de sortie du film */
    2: i16 year,

    /** Date de visionnage au format ISO AAAA-MM-JJ ; vide si inconnue */
    3: string visualisationDate,

    /** Note attribuée au film par l'utilisateur, entre 0 et 10 */
    4: i16 points
}

/**
 * Exception levée lorsqu'un film demandé est introuvable
 */
exception ServiceMovieNotFoundException {
    /** Message d'erreur */
    1: string msg
}

/**
 * Service RPC de gestion et de recherche des films
 */
service MovieService {
    /**
     * Ajoute un film au catalogue, ou met à jour celui qui porte déjà ce titre.
     * Une date de visionnage mal formée provoque une erreur (TException).
     *
     * @param movie le film à ajouter ou à mettre à jour
     */
    void addMovie(1: MovieDto movie),

    /**
     * Recherche un film à partir de son titre
     *
     * @param title le titre du film recherché
     * @return le film correspondant
     * @throws ServiceMovieNotFoundException si aucun film ne porte ce titre
     */
    MovieDto findMovieByTitle(1: string title) throws (1: ServiceMovieNotFoundException e),

    /**
     * Récupère la liste des films vus au cours d'une année donnée
     *
     * @param year l'année de visionnage
     * @return la liste des films correspondants, ou une liste vide si aucun résultat
     */
    list<MovieDto> findMoviesByYear(1: i16 year)
}