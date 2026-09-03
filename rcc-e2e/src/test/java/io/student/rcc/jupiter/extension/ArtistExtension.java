package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.Artist;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.TestData;
import io.student.rcc.service.ArtistClient;
import io.student.rcc.service.ArtistDbClient;
import io.student.rcc.utils.TestDataGenerator;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class ArtistExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ArtistExtension.class);
  private final ArtistClient artistClient = new ArtistDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        Artist.class
    ).ifPresent(
        annotation -> {
          String name = annotation.name().isEmpty()
              ? TestDataGenerator.generateRandomFirstname()
              : annotation.name();
          ArtistJson artist = new ArtistJson(null, name, annotation.biography(), annotation.photo());
          ArtistJson createdArtist = artistClient.createArtist(artist);

          context.getStore(NAMESPACE).put(context.getUniqueId(), createdArtist);
          TestData createdTestData = TestDataExtension.createdTestData();
          TestData newTestData =
              new TestData(
                  createdTestData.user(),
                  createdTestData.userPassword(),
                  createdArtist,
                  createdTestData.museum(),
                  createdTestData.painting()
              );
          TestDataExtension.updateTestData(newTestData);
        }
    );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(Artist.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), ArtistJson.class);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    TestData createdTestData = TestDataExtension.createdTestData();
    ArtistJson createdArtist = createdTestData.artist();
    if (createdArtist != null) {
      artistClient.removeArtist(createdArtist);
    }
  }
}
