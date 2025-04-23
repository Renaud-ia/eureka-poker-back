package fr.eurekapoker.parties.infrastructure.parties.entites;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Converter
public class RangeMapConverter implements AttributeConverter<Map<String, Float>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Float> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sérialisation de la range", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Float> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la désérialisation de la range", e);
        }
    }
}