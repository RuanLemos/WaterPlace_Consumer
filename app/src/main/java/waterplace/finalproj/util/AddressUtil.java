package waterplace.finalproj.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import waterplace.finalproj.model.Address;

public class AddressUtil {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static boolean CEPcheck(int CEP) {
        try {
            Future<Boolean> future = executor.submit(() -> {
                int cep = CEP;
                String cepString = String.valueOf(cep);
                String urlString = "https://viacep.com.br/ws/" + cepString + "/json/";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String jsonResponse = response.toString();
                    // Checa se o CEP é válido
                    if (jsonResponse.contains("erro")) {
                        // CEP inválido
                        return false;
                    } else {
                        // CEP válido
                        return true;
                    }
                } else {
                    // Erro na request
                    // Trate o caso de falha na conexão, se necessário
                    return false;
                }
            });

            // Aguarda a conclusão da tarefa assíncrona
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Address getAddressInfo(int CEP) {
        try {
            Future<Address> future = executor.submit(() -> {
                Address address = null;
                int cep = CEP;
                String cepString = String.valueOf(cep);
                String urlString = "https://viacep.com.br/ws/" + cepString + "/json/";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String jsonResponse = response.toString();

                // Parse do JSON de resposta
                JSONObject jsonObject = new JSONObject(jsonResponse);

                // Extrair os dados do CEP
                String logradouro = jsonObject.getString("logradouro");
                String cidade = jsonObject.getString("localidade");
                String bairro = jsonObject.getString("bairro");

                // Criar um objeto Address com os dados do CEP
                address = new Address();
                address.setAvenue(logradouro);
                address.setCity(cidade);
                address.setDistrict(bairro);

                return address;
            });

            // Aguarda a conclusão da tarefa assíncrona
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] geocode(String address) {
        try {
            Future<double[]> future = executor.submit(() -> {
                String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + URLEncoder.encode(address, "UTF-8");

                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String jsonResponse = response.toString();

                JSONArray jsonArray = new JSONArray(jsonResponse);

                if (jsonArray.length() > 0) {
                    JSONObject firstResult = jsonArray.getJSONObject(0);

                    double latitude = firstResult.getDouble("lat");
                    double longitude = firstResult.getDouble("lon");

                    return new double[]{latitude, longitude};
                }

                return null;
            });

            // Aguarda a conclusão da tarefa assíncrona
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
