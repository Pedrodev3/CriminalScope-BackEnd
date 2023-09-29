package com.fiap.challengeidwall.services;

import com.fiap.challengeidwall.model.Procurado;
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

@Service
public class InterpolWebScrapingService {
    @Autowired
    private ProcuradoRepository procuradoRepository;

    public void scrapeAndInsertData() throws IOException {
        String apiUrl =  "https://ws-public.interpol.int/notices/v1/red?resultPerPage=20&page=3";

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        HttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            String jsonString = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            InterpolResponse interpolResponse = gson.fromJson(jsonString, InterpolResponse.class);

            System.out.println("Total: " + interpolResponse.getTotal());
            for (int i = 0; i < interpolResponse.getItems(); i++) {
                InterpolPerson person = interpolResponse.getItems().get(i);
                Procurado procurado = new Procurado();

//                procurado.setProcurado(person.getName());
                System.out.println("Name: " + person.getName());

//                procurado.setDataNascimento(person.getDate_of_birth());
                System.out.println("Date of Birth: " + person.getDate_of_birth());

//                procurado.setNacionalidade(person.getNationalities().get(0));
                System.out.println("Nationalities: " + person.getNationalities().get(0));

//                procurado.setFoto(person.getLinks().getImages().getHref());
                System.out.println("Foto: " + person.getLinks().getImages().getHref());

//                procuradoRepository.save(procurado);
            }
        } else {
            System.out.println("A solicitação não foi bem-sucedida. Código de resposta: " + response.getStatusLine().getStatusCode());
        }
    }
}
