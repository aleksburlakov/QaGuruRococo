package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.Painting;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.PaintingJson;
import io.student.rcc.model.TestData;
import io.student.rcc.service.PaintingClient;
import io.student.rcc.service.PaintingDbClient;
import io.student.rcc.utils.TestDataGenerator;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class PaintingExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingExtension.class);
  private final PaintingClient paintingClient = new PaintingDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        Painting.class
    ).ifPresent(
        annotation -> {
          TestData testData = TestDataExtension.createdTestData();

          ArtistJson createdArtist = testData.artist();
          if (createdArtist == null) {
            throw new RuntimeException("Для создания сущности artist необходимо добавить аннотацию @Artist перед аннотацией @Painting");
          }

          MuseumJson createdMuseum = testData.museum();
          if (createdMuseum == null) {
            throw new RuntimeException("Для создания сущности museum необходимо добавить аннотацию @Museum перед аннотацией @Painting");
          }
          String title = annotation.title().isEmpty()
              ? "Painting_" + TestDataGenerator.generateRandomWord()
              : annotation.title();

          PaintingJson painting = new PaintingJson(
              null,
              title,
              annotation.description(),
              createdArtist,
              createdMuseum,
              annotation.content()
          );
          PaintingJson createdPainting = paintingClient.createPainting(painting);
          context.getStore(NAMESPACE).put(context.getUniqueId(), createdPainting);
          TestData newTestData =
              new TestData(
                  testData.user(),
                  testData.userPassword(),
                  createdArtist,
                  createdMuseum,
                  createdPainting
              );
          TestDataExtension.updateTestData(newTestData);
        }
    );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(Painting.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), PaintingJson.class);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    TestData createdTestData = TestDataExtension.createdTestData();
    PaintingJson createdPainting = createdTestData.painting();
    if (createdPainting != null) {
      paintingClient.removePainting(createdPainting);
    }
  }
}
