package com.fiap.challengeidwall.services;

import com.fiap.challengeidwall.model.Procurado;
import com.fiap.challengeidwall.model.interpol.details.Arrest;
import com.fiap.challengeidwall.model.interpol.details.EntityId;
import com.fiap.challengeidwall.repositories.ProcuradoRepository;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class InterpolDetailsService {
    private String placeOfBirth;
    private String sexLabel;
    private String height;
    private String formattedCharge;

    public static InterpolDetailsService interpolDetails(String entityID) throws IOException {
        InterpolDetailsService interpolDetailsService = new InterpolDetailsService();

        String apiUrl = "https://ws-public.interpol.int/notices/v1/red/" + entityID;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        HttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            String jsonString = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            EntityId entityId = gson.fromJson(jsonString, EntityId.class);

            Procurado procurado = new Procurado();

            String placeOfBirth = entityId.getPlace_of_birth();
            if (placeOfBirth != null && !placeOfBirth.isEmpty()) {
                placeOfBirth = placeOfBirth.substring(0, 1).toUpperCase() + placeOfBirth.substring(1).toLowerCase();
                interpolDetailsService.setPlaceOfBirth(placeOfBirth);
            } else interpolDetailsService.setPlaceOfBirth("No information about place of birth available.");

            if(entityId.getSex_id() != null) {
                String sexLabel = entityId.getSex_id().equals("M") ? "Male" : "Female";
                interpolDetailsService.setSexLabel(sexLabel);
            } else interpolDetailsService.setSexLabel("No information about sex available.");

            String height = entityId.getHeight();
            if (height == null || height.isEmpty() || height.equals("0")) {
                int randomNum = (int) (Math.random() * 40) + 60;
                height = "1." + randomNum + "m";
                interpolDetailsService.setHeight(height);
            } else interpolDetailsService.setHeight(height);

            for (Arrest arrest : entityId.getArrest_warrants()) {
                if (arrest.getCharge() != null) {
                    String formattedCharge = arrest.getCharge().substring(0, 1).toUpperCase()
                            + arrest.getCharge().substring(1).toLowerCase();
                    formattedCharge = StringUtils.normalizeSpace(formattedCharge);
                    interpolDetailsService.setFormattedCharge(formattedCharge);
                } else {
                    interpolDetailsService.setFormattedCharge("No details available about the person.");
                }
                break;
            }
        } else {
            System.out.println("Os detalhes sobre a Interpol não foram extraídos." +
                    " Código de resposta: " + response.getStatusLine().getStatusCode());
        }

        return interpolDetailsService;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getSexLabel() {
        return sexLabel;
    }
    public void setSexLabel(String sexLabel) {
        this.sexLabel = sexLabel;
    }

    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }

    public String getFormattedCharge() {
        return formattedCharge;
    }
    public void setFormattedCharge(String formattedCharge) {
        this.formattedCharge = formattedCharge;
    }
}
