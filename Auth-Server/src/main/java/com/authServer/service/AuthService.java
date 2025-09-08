package com.authServer.service;

import com.authServer.dtos.SignupRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-username}")
    private String adminUserName;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    //1. Get a Keycloak client logged in as admin
    private Keycloak getAdminKeyCloak(){
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("admin-cli")
                .username(adminUserName)
                .password(adminPassword)
                .build();
    }

    //2. Signup new user and assign role
    public void signup(SignupRequest request){
        Keycloak keycloak = getAdminKeyCloak();

        //Create new user
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(true);

        //Add password
        CredentialRepresentation userPassword = new CredentialRepresentation();
        userPassword.setTemporary(false);
        userPassword.setType(CredentialRepresentation.PASSWORD);
        userPassword.setValue(request.getPassword());
        user.setCredentials(Collections.singletonList(userPassword));

        //Send response to keycloak
        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
        }

        //Get newly userId
        String userId = CreatedResponseUtil.getCreatedId(response);

        //Assign role
        String role;
        if(request.getRole() == null) {
            role = "USER";
        }else{
            role=request.getRole();
        }

        RoleRepresentation setRole = keycloak.realm(realm).roles()
                .get(role)
                .toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(setRole));
    }
}
