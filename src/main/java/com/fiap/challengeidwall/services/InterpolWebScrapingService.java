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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

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

                String name = person.getName();
                if (name != null && !name.isEmpty()) {
                    String finalFormat = Arrays.stream(name.split(" "))
                            .filter(part -> !part.isEmpty())
                            .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
                            .collect(Collectors.joining(" "));
                    procurado.setProcurado(finalFormat);
                } else procurado.setProcurado("No name available.");

                String inputDate = person.getDate_of_birth();
                try {
                    if (inputDate != null) {
                        Date date = new SimpleDateFormat("yyyy/MM/dd").parse(inputDate);
                        String formattedDate = new SimpleDateFormat("MMMM dd, yyyy").format(date);
                        procurado.setDataNascimento(formattedDate);
                    } else procurado.setDataNascimento("No date of birth available.");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (person.getNationalities() != null) {
                    procurado.setNacionalidade(person.getNationalities().get(0));
                } else procurado.setNacionalidade("It does not have nationality.");

                procurado.setStatus("I");

                if (person.get_links() != null && person.get_links().getImages() != null) {
                    procurado.setFoto(person.get_links().getImages().getHref());
                } else procurado.setFoto("No image available for this person.");

                // Comunicação com o serviço de detalhes (terceira API)
                InterpolDetailsService service = new InterpolDetailsService();

                String id = person.getEntity_id();
                try {
                    id = id.replaceAll("/", "-");
                    service = InterpolDetailsService.interpolDetails(id);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }

                procurado.setLugarNascimento(service.getPlaceOfBirth());
                procurado.setSexo(service.getSexLabel());
                procurado.setAltura(service.getHeight());
                procurado.setDetalhes(service.getFormattedCharge());

                procuradoRepository.save(procurado);
            }
        } else {
            System.out.println("A solicitação não foi bem-sucedida. Código de resposta: " + response.getStatusLine().getStatusCode());
        }
    }
}
