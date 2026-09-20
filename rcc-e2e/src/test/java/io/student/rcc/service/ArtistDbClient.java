package io.student.rcc.service;

import io.qameta.allure.Step;
import io.student.rcc.config.Config;
import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.repository.ArtistRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.ArtistJson;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ArtistDbClient implements ArtistClient {

  private static final Config CFG = Config.getInstance();

  private final ArtistRepository artistRepository = ArtistRepository.getInstance();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.apiJdbcUrl()
  );

  @Override
  @Step("Создать художника в БД")
  public @NonNull ArtistJson createArtist(@NonNull ArtistJson artist) {
    return xaTransactionTemplate.execute(() -> ArtistJson.fromEntity(
            artistRepository.create(ArtistEntity.fromJson(artist))
        )
    );
  }

  @Override
  @Step("Обновить художника в БД")
  public @NonNull ArtistJson updateArtist(@NonNull ArtistJson artist) {
    return xaTransactionTemplate.execute(() -> ArtistJson.fromEntity(
            artistRepository.update(ArtistEntity.fromJson(artist))
        )
    );
  }

  @Override
  @Step("Найти художника в БД по id")
  public @NonNull Optional<ArtistJson> findArtistById(@NonNull UUID id) {
    return artistRepository.findById(id)
        .map(ArtistJson::fromEntity);
  }

  @Override
  @Step("Найти художника в БД по имени")
  public @NonNull Optional<ArtistJson> findArtistByName(@NonNull String name) {
    return artistRepository.findByName(name)
        .map(ArtistJson::fromEntity);
  }

  @Override
  @Step("Найти всех художников в БД")
  public @NonNull List<ArtistJson> findAllArtists() {
    return artistRepository.findAll().stream().map(ArtistJson::fromEntity).collect(Collectors.toList());
  }

  @Override
  @Step("Удалить художника в БД")
  public void removeArtist(@NonNull ArtistJson artistJson) {
    xaTransactionTemplate.execute(() -> {
      artistRepository.remove(ArtistEntity.fromJson(artistJson));
      return null;
    });
  }

  @Override
  @Step("Удалить всех художников в БД")
  public void removeAllArtists() {
    xaTransactionTemplate.execute(() -> {
      artistRepository.removeAll();
      return null;
    });
  }
}
