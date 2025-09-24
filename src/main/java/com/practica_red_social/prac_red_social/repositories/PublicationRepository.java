package com.practica_red_social.prac_red_social.repositories;

import com.practica_red_social.prac_red_social.models.entities.mongodb.PublicationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends MongoRepository<PublicationDocument, String> {
    /**
     * Elimina la publicacion con ese ID y EMAIL.
     * @param id
     * @param userEmailDueño
     * @return devuelve la cantidad de documentos que haya borrado para esos parámetros.
     */
    long deleteByIdAndUserEmailDueño(String id, String userEmailDueño);
}
