package io.student.rcc.service.impl;

import io.qameta.allure.Step;
import io.student.rcc.config.Config;
import io.student.rcc.data.entity.CountryEntity;
import io.student.rcc.data.entity.MuseumEntity;
import io.student.rcc.data.repository.CountryRepository;
import io.student.rcc.data.repository.MuseumRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.service.MuseumClient;
import org.jspecify.annotations.NonNull;

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

  @Override
  @Step("Создать музей в БД")
  public @NonNull MuseumJson createMuseum(@NonNull MuseumJson museum) {
    return xaTransactionTemplate.execute(() -> {
      MuseumEntity museumEntity = MuseumEntity.fromJson(museum);
      if (museumEntity.getCountry().getName() != null) {
        Optional<CountryEntity> countryEntity = countryRepository.findByName(museumEntity.getCountry().getName());
        if (countryEntity.isPresent()) {
          museumEntity.setCountry(countryEntity.get());
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
  @Step("Обновить музей в БД")
  public @NonNull MuseumJson updateMuseum(@NonNull MuseumJson museum) {
    return xaTransactionTemplate.execute(() -> {
      MuseumEntity museumEntity = MuseumEntity.fromJson(museum);
      if (museumEntity.getCountry().getName() != null) {
        Optional<CountryEntity> countryEntity = countryRepository.findByName(museumEntity.getCountry().getName());
        if (countryEntity.isPresent()) {
          museumEntity.setCountry(countryEntity.get());
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
  @Step("Найти музей в БД по id")
  public @NonNull Optional<MuseumJson> findMuseumById(@NonNull UUID id) {
    return museumRepository.findById(id).map(MuseumJson::fromEntity);
  }

  @Override
  @Step("Найти музей в БД по названию")
  public @NonNull Optional<MuseumJson> findMuseumByTitle(@NonNull String title) {
    return museumRepository.findByTitle(title).map(MuseumJson::fromEntity);
  }

  @Override
  @Step("Найти все музеи в БД")
  public @NonNull List<MuseumJson> findAllMuseums() {
    return museumRepository.findAll().stream()
        .map(MuseumJson::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  @Step("Удалить музей в БД")
  public void removeMuseum(@NonNull MuseumJson museumJson) {
    xaTransactionTemplate.execute(() -> {
      museumRepository.remove(MuseumEntity.fromJson(museumJson));
      return null;
    });
  }

  @Override
  @Step("Удалить все музеи в БД")
  public void removeAllMuseums() {
    xaTransactionTemplate.execute(() -> {
      museumRepository.removeAll();
      return null;
    });
  }
}
