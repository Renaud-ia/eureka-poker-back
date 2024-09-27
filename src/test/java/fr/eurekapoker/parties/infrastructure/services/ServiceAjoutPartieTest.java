package fr.eurekapoker.parties.infrastructure.services;

import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.infrastructure.parties.entites.*;
import fr.eurekapoker.parties.infrastructure.parties.repositories.JoueurRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.PartieRepository;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceAjoutPartie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServiceAjoutPartieTest {

    @Mock
    private PartieRepository partieRepository;

    @Mock
    private JoueurRepository joueurRepository;

    @InjectMocks
    private ServiceAjoutPartie serviceAjoutPartie;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPersisterPartie() {
        // Arrange
        PartiePersistanceDto partieDto = mock(PartiePersistanceDto.class);
        when(partieDto.obtIdUnique()).thenReturn("uniqueId");
        when(partieDto.obtNomRoom()).thenReturn("roomName");
        when(partieDto.obtVariante()).thenReturn("variant");
        when(partieDto.obtTypeTable()).thenReturn("tableType");
        when(partieDto.obtIdParse()).thenReturn(123456789L);
        when(partieDto.obtNomPartie()).thenReturn("partieName");
        when(partieDto.obtNomHero()).thenReturn("heroName");
        when(partieDto.obtDate()).thenReturn(LocalDateTime.now());
        when(partieDto.obtMains()).thenReturn(new ArrayList<>());

        // Act
        serviceAjoutPartie.persisterPartie(partieDto);

        // Assert
        verify(partieRepository, times(1)).save(any(PartieJpa.class));
    }
}