package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.api.ConvertisseurPersistanceVersApi;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.imports.ConstructeurPersistence;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.domaine.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InterfacePartiesImplTest {
    @Mock
    FabriqueDependances fabriqueDependancesMock;
    @Mock
    DomaineServiceImport domaineServiceImportMock;
    @Mock
    PersistanceParties persistancePartiesMock;
    @Mock
    PersistanceFichiers persistanceFichiersMock;
    @Mock
    ConstructeurPersistence constructeurPersistenceMock;
    @Mock
    ConvertisseurPersistanceVersApi convertisseurPersistanceVersApi;
    @Mock
    ParametresImport parametresImportMock;

    @BeforeEach
    void constructionDependances() throws ErreurImport {
        MockitoAnnotations.openMocks(this);
        when(fabriqueDependancesMock.obtDomaineServiceImport(anyString(), any())).thenReturn(domaineServiceImportMock);
        when(fabriqueDependancesMock.obtPersistanceParties()).thenReturn(persistancePartiesMock);
        when(fabriqueDependancesMock.obtPersistanceFichiers()).thenReturn(persistanceFichiersMock);
        when(fabriqueDependancesMock.obtConstructeurPersistance(parametresImportMock)).thenReturn(constructeurPersistenceMock);
        when(fabriqueDependancesMock.obtConvertisseurPersistanceVersApi(any())).thenReturn(convertisseurPersistanceVersApi);
    }

    @Test
    void ajoutPartieAppelleLesBonnesDependances() throws ErreurAjoutPartie, ErreurImport {
        String partie = "Partie test";

        InterfacePartiesImpl interfaceParties = new InterfacePartiesImpl(fabriqueDependancesMock);
        interfaceParties.ajouterPartie(partie, parametresImportMock);

        verify(domaineServiceImportMock).lancerImport();
        verify(persistancePartiesMock).ajouterPartie(constructeurPersistenceMock.obtPartie());
        verify(persistanceFichiersMock).enregistrerFichier(eq(partie));
    }

    @Test
    void consultationPartieAppelleLesBonnesMethodes() throws ErreurConsultationPartie, ErreurLectureFichier {
        // given
        String idPartie = "Fake-id-Partie";
        int indexPremiereMain = 0;
        int nombreMains = 10;

        // when
        InterfacePartiesImpl interfaceParties = new InterfacePartiesImpl(fabriqueDependancesMock);
        interfaceParties.consulterMainsParties(idPartie, indexPremiereMain, nombreMains);

        // then
        verify(persistancePartiesMock).recupererPartie(idPartie, indexPremiereMain, nombreMains);

        // todo vérifier qu'il convertit bien les DTO en mockant le retour de persistanceParties
    }
}
