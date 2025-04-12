package fr.eurekapoker.parties.infrastructure.auth;

import fr.eurekapoker.parties.application.auth.AuthService;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Override
    public boolean userEstIdentifie() {
        return false;
    }

    @Override
    public boolean estUtilisateur(UtilisateurIdentifie utilisateurIdentifie) {
        return false;
    }

    @Override
    public UtilisateurIdentifie getUtilisateur() {
        return null;
    }
}
