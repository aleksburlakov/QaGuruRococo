package io.student.rcc.model;

public record TestData(
    UserJson user,
    String userPassword,
    ArtistJson artist,
    MuseumJson museum,
    PaintingJson painting
) {

  public TestData(UserJson user, String userPassword) {
    this(user, userPassword, null, null, null);
  }

  public TestData(UserJson user, String userPassword, ArtistJson artist, MuseumJson museum, PaintingJson painting) {
    this.user = user;
    this.userPassword = userPassword;
    this.artist = artist;
    this.museum = museum;
    this.painting = painting;
  }
}
