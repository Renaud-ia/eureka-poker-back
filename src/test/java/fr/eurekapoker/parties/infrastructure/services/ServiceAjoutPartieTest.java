package fr.eurekapoker.parties.infrastructure.services;

import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.builders.UtilisateurIdentifieTestBuilder;
import fr.eurekapoker.parties.infrastructure.parties.entites.*;
import fr.eurekapoker.parties.infrastructure.parties.repositories.JoueurRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.PartieRepository;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceAjoutPartie;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceUtilisateur;
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

    @Mock
    private ServiceUtilisateur serviceUtilisateur;

    @InjectMocks
    private ServiceAjoutPartie serviceAjoutPartie;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}