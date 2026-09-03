package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.repository.CountryRepository;
import io.student.rcc.data.repository.MuseumRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.MuseumJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MuseumDbClient implements MuseumClient {

  private static final Config CFG = Config.getInstance();

  MuseumRepository museumRepository = MuseumRepository.getInstance();
  CountryRepository countryRepository = CountryRepository.getInstance();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.apiJdbcUrl()
  );

  public MuseumJson createMuseum(MuseumJson museum) {
    return xaTransactionTemplate.execute(() -> {
      MuseumEntity museumEntity = MuseumEntity.fromJson(museum);
      if (museumEntity.getCountry().getName() != null) {
        CountryEntity countryEntity = countryRepository.findByName(museumEntity.getCountry().getName()).get();
        if (countryEntity != null) {
          museumEntity.setCountry(countryEntity);
          museumRepository.create(museumEntity);
        } else {
          throw new RuntimeException("Country with name = " + museumEntity.getCountry().getName() + " was not found");
        }
      } else {
        throw new RuntimeException("Country name is required");
      }
      return MuseumJson.fromEntity(museumEntity);
    });
  }

  @Override
  public MuseumJson updateMuseum(MuseumJson museum) {
    return xaTransactionTemplate.execute(() -> {
      MuseumEntity museumEntity = MuseumEntity.fromJson(museum);
      if (museumEntity.getCountry().getName() != null) {
        CountryEntity countryEntity = countryRepository.findByName(museumEntity.getCountry().getName()).get();
        if (countryEntity != null) {
          museumEntity.setCountry(countryEntity);
          museumEntity = museumRepository.update(MuseumEntity.fromJson(museum));
        } else {
          throw new RuntimeException("Country with name = " + museumEntity.getCountry().getName() + " was not found");
        }
      } else {
        throw new RuntimeException("Country name is required");
      }
      return MuseumJson.fromEntity(museumEntity);
    });
  }

  @Override
  public Optional<MuseumJson> findMuseumById(UUID id) {
    return museumRepository.findById(id).map(MuseumJson::fromEntity);
  }

  @Override
  public Optional<MuseumJson> findMuseumByTitle(String title) {
    return museumRepository.findByTitle(title).map(MuseumJson::fromEntity);
  }

  @Override
  public List<MuseumJson> findAllMuseums() {
    return museumRepository.findAll().stream()
        .map(MuseumJson::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void removeMuseum(MuseumJson museumJson) {
    xaTransactionTemplate.execute(() -> {
      museumRepository.remove(MuseumEntity.fromJson(museumJson));
      return null;
    });
  }

  @Override
  public void removeAllMuseums() {
    xaTransactionTemplate.execute(() -> {
      museumRepository.removeAll();
      return null;
    });
  }
}
