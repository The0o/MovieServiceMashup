struct MovieDto {
    1: string title,
    2: i16 year,
    3: string visualisationDate,
    4: i16 points
}

exception ServiceMovieNotFoundException {
    1: string msg
}

service MovieService {
    void addMovie(1: MovieDto movie),
    MovieDto findMovieByTitle(1: string title) throws (1: ServiceMovieNotFoundException e),
    list<MovieDto> findMoviesByYear(1: i16 year)
}