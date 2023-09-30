package com.fiap.challengeidwall.services;

import com.fiap.challengeidwall.model.Procurado;
import com.fiap.challengeidwall.model.Status;
import com.fiap.challengeidwall.model.interpol.InterpolPerson;
import com.fiap.challengeidwall.model.interpol.InterpolResponse;
import com.fiap.challengeidwall.repositories.ProcuradoRepository;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class InterpolWebScrapingService {
    @Autowired
    private ProcuradoRepository procuradoRepository;

    public void scrapeAndInsertData() throws IOException {
        String apiUrl = "https://ws-public.interpol.int/notices/v1/red?resultPerPage=20&page=3";

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        HttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            String jsonString = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            InterpolResponse interpolResponse = gson.fromJson(jsonString, InterpolResponse.class);

            for (InterpolPerson person : interpolResponse.get_embedded().getNotices()) {
                Procurado procurado = new Procurado();

                procurado.setProcurado(person.getName());
                System.out.println("Name: " + person.getName());

                procurado.setDataNascimento(person.getDate_of_birth());
                System.out.println("Date of Birth: " + person.getDate_of_birth());

                procurado.setNacionalidade(person.getNationalities().get(0));
                if (person.getNationalities() != null) {
                    System.out.println("Nationalities: " + person.getNationalities().get(0));
                } else {
                    System.out.println("It does not have nationality.");
                }

                procurado.setStatus("I");

                procurado.setFoto(person.get_links().getImages().getHref());
                if (person.get_links() != null && person.get_links().getImages() != null) {
                    System.out.println("Foto: " + person.get_links().getImages().getHref());
                } else {
                    System.out.println("No image link available for this person.");
                }
                System.out.println("--------------------------------");

                procuradoRepository.save(procurado);
            }


        } else {
            System.out.println("A solicitação não foi bem-sucedida. Código de resposta: " + response.getStatusLine().getStatusCode());
        }
    }
}
