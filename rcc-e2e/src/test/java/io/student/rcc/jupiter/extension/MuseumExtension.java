package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.Artist;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.CountryJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.TestData;
import io.student.rcc.service.ArtistClient;
import io.student.rcc.service.ArtistDbClient;
import io.student.rcc.service.MuseumClient;
import io.student.rcc.service.MuseumDbClient;
import io.student.rcc.utils.TestDataGenerator;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class MuseumExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(MuseumExtension.class);
  private final MuseumClient museumClient = new MuseumDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        Museum.class
    ).ifPresent(
        annotation -> {
          String title = annotation.title().isEmpty()
              ? TestDataGenerator.generateRandomLogin()
              : annotation.title();
          String city = annotation.title().isEmpty()
              ? TestDataGenerator.generateRandomCity()
              : annotation.city();
          MuseumJson museumJson = new MuseumJson(
              null,
              title,
              annotation.description(),
              city,
              annotation.photo(),
              new CountryJson(null, annotation.countryName())
          );

          MuseumJson createdMuseum = museumClient.createMuseum(museumJson);

          context.getStore(NAMESPACE).put(context.getUniqueId(), createdMuseum);

          TestData createdTestData = TestDataExtension.createdTestData();
          TestData newTestData =
              new TestData(
                  createdTestData.user(),
                  createdTestData.userPassword(),
                  createdTestData.artist(),
                  createdMuseum,
                  createdTestData.painting()
              );
          TestDataExtension.updateTestData(newTestData);
        }
    );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(Museum.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), MuseumJson.class);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    TestData createdTestData = TestDataExtension.createdTestData();
    MuseumJson createdMuseum = createdTestData.museum();
    if (createdMuseum != null) {
      museumClient.removeMuseum(createdMuseum);
    }
  }
}
