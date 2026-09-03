package io.student.rcc.service;

import io.student.rcc.model.PaintingJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaintingClient {
  PaintingJson createPainting(PaintingJson painting);

  PaintingJson updatePainting(PaintingJson painting);

  Optional<PaintingJson> findPaintingById(UUID id);

  Optional<PaintingJson> findPaintingByTitle(String title);

  List<PaintingJson> findAllPaintings();

  void removePainting(PaintingJson paintingJson);
}
