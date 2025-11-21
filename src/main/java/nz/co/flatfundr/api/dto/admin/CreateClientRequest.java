package nz.co.flatfundr.api.dto.admin;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public class CreateClientRequest {
    @NotBlank
    private String client_name;

    @NotBlank
    private ArrayList<String> redirect_uris;

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public ArrayList<String> getRedirect_uris() {
        return redirect_uris;
    }

    public void setRedirect_uris(ArrayList<String> redirect_uris) {
        this.redirect_uris = redirect_uris;
    }
}
