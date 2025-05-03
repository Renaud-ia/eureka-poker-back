package fr.eurekapoker.parties.infrastructure.parties.entites;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Converter
public class RangeMapConverter implements AttributeConverter<Map<String, Float>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Float> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null; // ou "{}" si tu veux une map vide au lieu de null
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sérialisation de la range", e);
        }
    }

    @Override
    public Map<String, Float> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Map.of(); // ou `null` si tu veux explicitement null
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la désérialisation de la range", e);
        }
    }
}