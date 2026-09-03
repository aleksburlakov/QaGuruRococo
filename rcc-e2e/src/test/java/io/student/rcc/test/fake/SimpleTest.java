package io.student.rcc.test.fake;

import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.entity.PaintingEntity;
import io.student.rcc.data.entity.UserEntity;
import io.student.rcc.model.*;
import io.student.rcc.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Optional;

public class SimpleTest {

  @CsvSource({
      "testUser1, 12345"
  })
  @ParameterizedTest
  void userTest(String username, String password) {
    UsersClient usersClient = new UsersDbClient();
    int beforeCount = usersClient.findAll().size();
    String firstName = "TestName";

    UserJson createdUser = usersClient.createUser(username, password);
    Assertions.assertEquals(username, createdUser.username());

    List<UserJson> users = usersClient.findAll();
    Assertions.assertEquals(beforeCount + 1, users.size());

    Optional<UserJson> optional = usersClient.findUserByUsername(username);
    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals(createdUser.id(), optional.get().id());

    optional = usersClient.findUserById(createdUser.id());
    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals(username, optional.get().username());

    UserEntity userEntity = UserEntity.fromJson(createdUser);
    userEntity.setFirstname(firstName);
    usersClient.updateUser(UserJson.fromEntity(userEntity));
    UserJson updatedUser = usersClient.findUserById(createdUser.id()).get();
    Assertions.assertEquals(firstName, updatedUser.firstname());

    usersClient.removeUser(updatedUser);
    Assertions.assertTrue(usersClient.findUserById(updatedUser.id()).isEmpty());
  }

  @CsvSource({
      "artist1, biography1"
  })
  @ParameterizedTest
  void artistTest(String name, String biography) {
    ArtistClient artistClient = new ArtistDbClient();
    int beforeCount = artistClient.findAllArtists().size();
    String updatedBiography = "updatedBiography";

    ArtistJson createdArtist = artistClient.createArtist(new ArtistJson(null, name, biography, null));
    Assertions.assertEquals(name, createdArtist.name());

    List<ArtistJson> artists = artistClient.findAllArtists();
    Assertions.assertEquals(beforeCount + 1, artists.size());

    Optional<ArtistJson> optional = artistClient.findArtistByName(name);
    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals(createdArtist.id(), optional.get().id());

    optional = artistClient.findArtistById(createdArtist.id());
    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals(name, optional.get().name());

    ArtistEntity artistEntity = ArtistEntity.fromJson(createdArtist);
    artistEntity.setBiography(updatedBiography);
    ArtistJson updatedArtist = artistClient.updateArtist(ArtistJson.fromEntity(artistEntity));
    Assertions.assertEquals(updatedBiography, updatedArtist.biography());

    artistClient.removeArtist(updatedArtist);
    Assertions.assertTrue(artistClient.findArtistById(updatedArtist.id()).isEmpty());
  }

  @CsvSource({
      "Лувр, Большой музей, Париж, Франция"
  })
  @ParameterizedTest
  void museumTest(String title, String description, String city, String country) {
    MuseumClient museumClient = new MuseumDbClient();
    int beforeCount = museumClient.findAllMuseums().size();
    String updatedDescription = "Очень большой музей";

    CountryJson countryJson = new CountryJson(null, country);
    MuseumJson museumJson = new MuseumJson(null, title, description, city, null, countryJson);

    museumJson = museumClient.createMuseum(museumJson);
    Assertions.assertEquals(title, museumJson.title());

    List<MuseumJson> museums = museumClient.findAllMuseums();
    Assertions.assertEquals(beforeCount + 1, museums.size());

    Optional<MuseumJson> optional = museumClient.findMuseumByTitle(title);
    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals(museumJson.id(), optional.get().id());

    optional = museumClient.findMuseumById(museumJson.id());
    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals(title, optional.get().title());

    MuseumEntity museumEntity = MuseumEntity.fromJson(museumJson);
    museumEntity.setDescription(updatedDescription);
    MuseumJson updatedMuseum = museumClient.updateMuseum(MuseumJson.fromEntity(museumEntity));
    Assertions.assertEquals(updatedDescription, updatedMuseum.description());

    museumClient.removeMuseum(updatedMuseum);
    Assertions.assertTrue(museumClient.findMuseumById(museumJson.id()).isEmpty());
  }

  @CsvSource({
      "Mona Lisa, Портрет женщины, Леонардо Да Винчи, Сложная биография, Лувр, Париж, Франция"
  })
  @ParameterizedTest
  void paintingTest(String title, String description, String artistName, String artistBiography,
                    String museumTitle, String museumCity, String museumCountry) {
    PaintingClient paintingClient = new PaintingDbClient();
    int beforeCount = paintingClient.findAllPaintings().size();
    String updatedDescription = "Портрет загадочной женщины";

    ArtistJson artistJson = new ArtistJson(null, artistName, artistBiography, null);
    CountryJson countryJson = new CountryJson(null, museumCountry);
    MuseumJson museumJson = new MuseumJson(null, museumTitle, null, museumCity, null, countryJson);
    PaintingJson paintingJson = new PaintingJson(null, title, description, artistJson, museumJson, null);

    paintingJson = paintingClient.createPainting(paintingJson);
    Assertions.assertEquals(title, paintingJson.title());

    List<PaintingJson> paintings = paintingClient.findAllPaintings();
    Assertions.assertEquals(beforeCount + 1, paintings.size());

    Optional<PaintingJson> optional = paintingClient.findPaintingByTitle(title);
    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals(paintingJson.id(), optional.get().id());

    optional = paintingClient.findPaintingById(paintingJson.id());
    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals(title, optional.get().title());

    PaintingEntity paintingEntity = PaintingEntity.fromJson(paintingJson);
    paintingEntity.setDescription(updatedDescription);
    PaintingJson updatedPainting = paintingClient.updatePainting(PaintingJson.fromEntity(paintingEntity));
    Assertions.assertEquals(updatedDescription, updatedPainting.description());

    paintingClient.removePainting(updatedPainting);
    Assertions.assertTrue(paintingClient.findPaintingById(paintingJson.id()).isEmpty());
  }
}
