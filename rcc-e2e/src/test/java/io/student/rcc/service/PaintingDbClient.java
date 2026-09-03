package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.ArtistEntity;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.entity.PaintingEntity;
import io.student.rcc.data.repository.ArtistRepository;
import io.student.rcc.data.repository.CountryRepository;
import io.student.rcc.data.repository.MuseumRepository;
import io.student.rcc.data.repository.PaintingRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.PaintingJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaintingDbClient implements PaintingClient {
  private static final Config CFG = Config.getInstance();

  private final PaintingRepository paintingRepository = PaintingRepository.getInstance();
  private final ArtistRepository artistRepository = ArtistRepository.getInstance();
  private final MuseumRepository museumRepository = MuseumRepository.getInstance();
  private final CountryRepository countryRepository = CountryRepository.getInstance();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.apiJdbcUrl()
  );

  @Override
  public PaintingJson createPainting(PaintingJson painting) {
    return xaTransactionTemplate.execute(() -> {
      PaintingEntity paintingEntity = PaintingEntity.fromJson(painting);
      paintingEntity.setArtist(getOrCreateArtist(painting.artist()));
      paintingEntity.setMuseum(getOrCreateMuseum(painting.museum()));
      return PaintingJson.fromEntity(paintingRepository.create(paintingEntity));
    });
  }

  @Override
  public PaintingJson updatePainting(PaintingJson painting) {
    return xaTransactionTemplate.execute(() -> {
      PaintingEntity paintingEntity = PaintingEntity.fromJson(painting);
      paintingEntity.setArtist(getOrCreateArtist(painting.artist()));
      paintingEntity.setMuseum(getOrCreateMuseum(painting.museum()));
      return PaintingJson.fromEntity(paintingRepository.update(paintingEntity));
    });
  }

  @Override
  public Optional<PaintingJson> findPaintingById(UUID id) {
    return paintingRepository.findById(id)
        .map(PaintingJson::fromEntity);
  }

  @Override
  public Optional<PaintingJson> findPaintingByTitle(String title) {
    return paintingRepository.findByTitle(title)
        .map(PaintingJson::fromEntity);
  }

  @Override
  public List<PaintingJson> findAllPaintings() {
    return paintingRepository.findAllPainting().stream()
        .map(PaintingJson::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void removePainting(PaintingJson paintingJson) {
    xaTransactionTemplate.execute(() -> {
      paintingRepository.remove(PaintingEntity.fromJson(paintingJson));
      return null;
    });
  }

  private ArtistEntity getOrCreateArtist(ArtistJson artistJson) {
    if (artistJson.name() != null) {
      return artistRepository.findByName(artistJson.name())
          .orElseGet(() -> artistRepository.create(ArtistEntity.fromJson(artistJson)));
    } else if (artistJson.id() != null) {
      return artistRepository.findById(artistJson.id())
          .orElseThrow(() -> new RuntimeException("Artist with id = " + artistJson.id() + " was not found"));
    }
    throw new RuntimeException("Artist id or name is required");
  }

  private MuseumEntity getOrCreateMuseum(MuseumJson museumJson) {
    if (museumJson.title() != null) {
      return museumRepository.findByTitle(museumJson.title())
          .orElseGet(() -> {
            CountryEntity countryEntity = null;
            if (museumJson.country().name() != null) {
              countryEntity = countryRepository.findByName(museumJson.country().name()).get();
            } else if (museumJson.country().id() != null) {
              countryEntity = countryRepository.findById(museumJson.country().id()).get();
            } else {
              throw new RuntimeException("Country id or name is required");
            }
            MuseumEntity museumEntity = MuseumEntity.fromJson(museumJson);
            museumEntity.setCountry(countryEntity);
            return museumRepository.create(museumEntity);
          });
    } else if (museumJson.id() != null) {
      return museumRepository.findById(museumJson.id())
          .orElseThrow(() -> new RuntimeException("Museum with id = " + museumJson.id() + " was not found"));
    }
    throw new RuntimeException("Museum id or title is required");
  }
}
