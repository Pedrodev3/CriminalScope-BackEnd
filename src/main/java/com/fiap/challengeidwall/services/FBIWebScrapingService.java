package com.fiap.challengeidwall.services;

import com.fiap.challengeidwall.model.*;
import com.fiap.challengeidwall.model.fbi.FBIPerson;
import com.fiap.challengeidwall.model.fbi.FBIResponse;
import com.fiap.challengeidwall.model.fbi.Image;
import com.fiap.challengeidwall.repositories.ProcuradoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

                        String title = person.getTitle();
                        if (title != null && !title.isEmpty()) {
                            String finalFormat = Arrays.stream(title.split(" "))
                                    .filter(part -> !part.isEmpty())
                                    .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
                                    .collect(Collectors.joining(" "));
                            procurado.setProcurado(finalFormat);
                        } else procurado.setProcurado("No name available.");

                        if (datesOfBirthUsed != null && !datesOfBirthUsed.isEmpty()) {
                            procurado.setDataNascimento(datesOfBirthUsed.get(0));
                        } else {
                            int randomYear = 1950 + new Random().nextInt(51);
                            int randomMonth = 1 + new Random().nextInt(12);
                            int randomDay = 1 + new Random().nextInt(28);

                            String[] months = {
                                    "January", "February", "March", "April", "May", "June",
                                    "July", "August", "September", "October", "November", "December"
                            };

                            String finalDate = months[randomMonth - 1] + " " + randomDay + ", " + randomYear;
                            procurado.setDataNascimento(finalDate);
                        }

                        String[] nationalities = {
                                "American", "British", "French", "German", "Japanese",
                                "Canadian", "Australian", "Italian", "Spanish", "Chinese",
                                "Swedish", "Mexican", "Indian", "Brazilian", "Russian"
                        };

                        String[][] cities = {
                                {"New York", "Los Angeles", "Chicago", "Miami", "San Francisco"},
                                {"London", "Manchester", "Birmingham", "Liverpool", "Glasgow"},
                                {"Paris", "Marseille", "Lyon", "Toulouse", "Nice"},
                                {"Berlin", "Munich", "Hamburg", "Cologne", "Frankfurt"},
                                {"Tokyo", "Osaka", "Kyoto", "Nagoya", "Yokohama"},
                                {"Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa"},
                                {"Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide"},
                                {"Rome", "Milan", "Naples", "Turin", "Florence"},
                                {"Madrid", "Barcelona", "Valencia", "Seville", "Bilbao"},
                                {"Beijing", "Shanghai", "Guangzhou", "Chengdu", "Shenzhen"},
                                {"Stockholm", "Gothenburg", "Malmö", "Uppsala", "Linköping"},
                                {"Mexico City", "Guadalajara", "Monterrey", "Puebla", "Tijuana"},
                                {"Mumbai", "Delhi", "Bangalore", "Kolkata", "Chennai"},
                                {"Sao Paulo", "Rio de Janeiro", "Brasília", "Salvador", "Fortaleza"},
                                {"Moscow", "Saint Petersburg", "Novosibirsk", "Yekaterinburg", "Kazan"}
                        };

                        Random random = new Random();
                        int nationalityIndex = random.nextInt(nationalities.length);
                        String nationality = nationalities[nationalityIndex];

                        String[] selectedCities = cities[nationalityIndex];
                        int cityIndex = random.nextInt(selectedCities.length);
                        String city = selectedCities[cityIndex];

                        if (person.getNationality() != null && !person.getNationality().isEmpty()) {
                            procurado.setNacionalidade(person.getNationality());
                        } else procurado.setNacionalidade(nationality);

                        if (person.getPlace_of_birth() != null) {
                            procurado.setLugarNascimento(person.getPlace_of_birth());
                            System.out.println("Place of Birth: " + person.getPlace_of_birth());
                        } else procurado.setLugarNascimento(city);

                        if (person.getSex() != null) {
                            procurado.setSexo(person.getSex());
                        } else procurado.setSexo("Unknown");

                        if (person.getHeight_max() != null && person.getHeight_min() != null) {
                            int heightMax = Integer.parseInt(person.getHeight_max());
                            int heightMin = Integer.parseInt(person.getHeight_min());
                            int height = (heightMax + heightMin) / 2;
                            String addedLetters = "1." + height + "m";

                            procurado.setAltura(addedLetters);
                        } else {
                            int randomNum = (int) (Math.random() * 40) + 60;
                            String height = "1." + randomNum + "m";
                            procurado.setAltura(height);
                        }

                        procurado.setStatus("F");

                        procurado.setFoto(image.getThumb());

                        String details = person.getDetails();
                        if (details != null && !details.isEmpty()) {
                            details = StringUtils.normalizeSpace(details.replaceAll("<p>", "").replaceAll("</p>", ""));

                            procurado.setDetalhes(details);
                        } else procurado.setDetalhes("No details available about the person.");

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
