package tmdb.dto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieInfoDto {

    private String title;
    private List<String> genres = new ArrayList<>();
    private URL posterPath;
    private Integer year;
    private List<CharacterDto> characters = new ArrayList<>();

    public MovieInfoDto() {
    }

    public MovieInfoDto(String title, List<String> genres, URL posterPath, Integer year, List<CharacterDto> characters) {
        setTitle(title);
        setGenres(genres);
        setPosterPath(posterPath);
        setYear(year);
        setCharacters(characters);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public URL getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(URL posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<CharacterDto> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharacterDto> characters) {
        this.characters = characters;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Titre   : ").append(title).append('\n')
          .append("Annee   : ").append(year).append('\n')
          .append("Genres  : ").append(String.join(", ", genres)).append('\n')
          .append("Affiche : ").append(posterPath).append('\n')
          .append("Personnages :");
        for (CharacterDto character : characters) {
            sb.append("\n  - ").append(character);
        }
        return sb.toString();
    }
}
