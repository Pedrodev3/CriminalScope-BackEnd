package com.fiap.challengeidwall.services;

import com.fiap.challengeidwall.model.*;
import com.fiap.challengeidwall.model.fbi.FBIPerson;
import com.fiap.challengeidwall.model.fbi.FBIResponse;
import com.fiap.challengeidwall.model.fbi.Image;
import com.fiap.challengeidwall.repositories.ProcuradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

@Service
public class FBIWebScrapingService {
    @Autowired
    private ProcuradoRepository procuradoRepository;

    public void scrapeAndInsertData() throws IOException {
        String apiUrl = "https://api.fbi.gov/wanted/v1";

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        HttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            String jsonString = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            FBIResponse fbiResponse = gson.fromJson(jsonString, FBIResponse.class);

            System.out.println("Total: " + fbiResponse.getTotal());
            for (FBIPerson person : fbiResponse.getItems()) {
                Procurado procurado = new Procurado();
                boolean firstImageProcessed = false;

                for (Image image : person.getImages()) {
                    if (!firstImageProcessed) {
                        List<String> datesOfBirthUsed = person.getDates_of_birth_used();

                        procurado.setProcurado(person.getTitle());
                        System.out.println("Title: " + person.getTitle());

                        if (datesOfBirthUsed != null && !datesOfBirthUsed.isEmpty()) {
                            procurado.setDataNascimento(datesOfBirthUsed.get(0));
                            System.out.println("Date of Birth: " + datesOfBirthUsed.get(0));
                        } else {
                            procurado.setDataNascimento("Unknown");
                            System.out.println("Date of Birth: " + "Unknown");
                        }

                        procurado.setNacionalidade(person.getNationality());
                        System.out.println("Nationality: " + person.getNationality());

                        procurado.setFoto(image.getThumb());
                        System.out.println("Thumb: " + image.getOriginal());
                        System.out.println("====================================");

                        firstImageProcessed = true;
                    } else {
                        break;
                    }
                }

                procuradoRepository.save(procurado);
            }
        } else {
            System.out.println("A solicitação não foi bem-sucedida. " + "Código de resposta: " + response.getStatusLine().getStatusCode());
        }
    }
}
