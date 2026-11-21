package Model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConsultarRuc {

    // Reemplaza por tu token de apisperu.com
    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImZjYWxkYXMuZGV2QGdtYWlsLmNvbSIsImp0aSI6ImZjODY3ODM1NDY2MWMwYmEifQ.59liYw9B9FjbTFJicb1ehcey3b4mNpOTO-umj_KYCzA";

    public Cliente consultar(String numero) throws Exception {
        String limpio = numero == null ? "" : numero.trim();
        if (!limpio.matches("\\d{8}|\\d{11}")) {
            throw new IllegalArgumentException("Ingresa un RUC valido de 11 digitos o DNI 8 dígitos válido.");
        }

        boolean esRuc = limpio.length() == 11;
        String endpoint = esRuc
                ? "https://dniruc.apisperu.com/api/v1/ruc/" + limpio + "?token=" + TOKEN
                : "https://dniruc.apisperu.com/api/v1/dni/" + limpio + "?token=" + TOKEN;

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("No se pudo consultar en apisperu.com (HTTP " + response.statusCode() + ")");
        }

        String json = response.body();
        Cliente c = new Cliente();
        c.setRuc(limpio);

        if (esRuc) {
            String razonSocial = extraerString(json, "razonSocial");
            if (razonSocial.isEmpty()) {
                throw new RuntimeException("RUC no encontrado o sin datos.");
            }
            c.setRazonSocial(razonSocial);
            c.setDireccion(extraerString(json, "direccion"));
            c.setEstadoSunat(extraerString(json, "estado"));
            c.setCondicionSunat(extraerString(json, "condicion"));
        } else {
            String nombres = extraerString(json, "nombres");
            String apPat = extraerString(json, "apellidoPaterno");
            String apMat = extraerString(json, "apellidoMaterno");
            String nombreCompleto = (nombres + " " + apPat + " " + apMat).trim().replaceAll("\\s+", " ");
            if (nombreCompleto.isEmpty()) {
                throw new RuntimeException("DNI no encontrado o sin datos.");
            }
            c.setRazonSocial(nombreCompleto);
            c.setDireccion("");
            c.setEstadoSunat("RENIEC");
            c.setCondicionSunat("CONSULTADO");
        }

        return c;
    }

    private String extraerString(String json, String clave) {
        String patron = "\"" + clave + "\"";
        int idx = json.indexOf(patron);
        if (idx == -1) return "";
        int dosPuntos = json.indexOf(":", idx + patron.length());
        if (dosPuntos == -1) return "";

        int i = dosPuntos + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
            i++;
        }
        if (i >= json.length() || json.charAt(i) != '"') {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int j = i + 1; j < json.length(); j++) {
            char ch = json.charAt(j);
            if (ch == '\\' && j + 1 < json.length()) {
                char next = json.charAt(++j);
                if (next == 'u' && j + 4 < json.length()) {
                    String hex = json.substring(j + 1, j + 5);
                    sb.append((char) Integer.parseInt(hex, 16));
                    j += 4;
                } else {
                    sb.append(next);
                }
            } else if (ch == '"') {
                break;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString().trim();
    }
}