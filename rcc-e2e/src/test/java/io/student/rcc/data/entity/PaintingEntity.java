package io.student.rcc.data.entity;

import io.student.rcc.model.PaintingJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "painting")
public class PaintingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private ArtistEntity artist;

    @ManyToOne
    @JoinColumn(name = "museum_id", referencedColumnName = "id")
    private MuseumEntity museum;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer()
                .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PaintingEntity that = (PaintingEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode() : getClass().hashCode();
    }

    public static PaintingEntity fromJson(PaintingJson paintingJson) {
        PaintingEntity entity = new PaintingEntity();
        entity.setId(paintingJson.id());
        entity.setTitle(paintingJson.title());
        entity.setDescription(paintingJson.description());
        entity.setContent(paintingJson.content() != null ? paintingJson.content().getBytes() : null);
        entity.setArtist(ArtistEntity.fromJson(paintingJson.artist()));
        entity.setMuseum(MuseumEntity.fromJson(paintingJson.museum()));
        return entity;
    }
}
