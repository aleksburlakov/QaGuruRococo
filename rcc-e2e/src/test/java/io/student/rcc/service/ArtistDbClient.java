package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.repository.ArtistRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.ArtistJson;

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
  public ArtistJson createArtist(ArtistJson artist) {
    return xaTransactionTemplate.execute(() -> ArtistJson.fromEntity(
            artistRepository.create(ArtistEntity.fromJson(artist))
        )
    );
  }

  @Override
  public ArtistJson updateArtist(ArtistJson artist) {
    return xaTransactionTemplate.execute(() -> ArtistJson.fromEntity(
            artistRepository.update(ArtistEntity.fromJson(artist))
        )
    );

//          ArtistEntity updatedArtist = artistRepository.update(ArtistEntity.fromJson(artist));
//          return ArtistJson.fromEntity(updatedArtist);
  }

  @Override
  public Optional<ArtistJson> findArtistById(UUID id) {
    return artistRepository.findById(id)
        .map(ArtistJson::fromEntity);
  }

  @Override
  public Optional<ArtistJson> findArtistByName(String name) {
    return artistRepository.findByName(name)
        .map(ArtistJson::fromEntity);
  }

  @Override
  public List<ArtistJson> findAllArtists() {
    return artistRepository.findAll().stream().map(ArtistJson::fromEntity).collect(Collectors.toList());
  }

  @Override
  public void removeArtist(ArtistJson artistJson) {
    xaTransactionTemplate.execute(() -> {
      artistRepository.remove(ArtistEntity.fromJson(artistJson));
      return null;
    });
  }

  @Override
  public void removeAllArtists() {
    xaTransactionTemplate.execute(() -> {
      artistRepository.removeAll();
      return null;
    });
  }
}
