package tmdb.dto;

import java.net.URL;

public class CharacterDto {

    private String characterName;
    private URL imgUrl;
    private String actorName;
    private String birthday;
    private String deathday = null;
    private String placeOfBirth;

    public CharacterDto() {
    }

    public CharacterDto(String characterName, URL imgUrl, String actorName,
                        String birthday, String deathday, String placeOfBirth) {
        setCharacterName(characterName);
        setImgUrl(imgUrl);
        setActorName(actorName);
        setBirthday(birthday);
        setDeathday(deathday);
        setPlaceOfBirth(placeOfBirth);
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public URL getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(URL imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @Override
    public String toString() {
        return characterName + " (" + actorName
                + (birthday != null ? ", ne(e) le " + birthday : "")
                + (deathday != null ? ", decede(e) le " + deathday : "")
                + (placeOfBirth != null ? ", " + placeOfBirth : "") + ")";
    }
}
