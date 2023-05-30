package waterplace.finalproj.util;

public class DistanceUtil {
    private static final double EARTH_RADIUS = 6371e3;

    public static double calcDistance(double latUser, double lonUser, double latSup, double lonSup) {
        // Converte as coordenadas de graus para radianos
        double lat1Rad = Math.toRadians(latUser);
        double lon1Rad = Math.toRadians(lonUser);
        double lat2Rad = Math.toRadians(latSup);
        double lon2Rad = Math.toRadians(lonSup);

        // Diferença das latitudes e longitudes
        double difLat = lat2Rad - lat1Rad;
        double difLon = lon2Rad - lon1Rad;

        // Fórmula de Haversine
        double a = Math.sin(difLat / 2) * Math.sin(difLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(difLon / 2) * Math.sin(difLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        // Retorna a distância em quilômetros
        return distance / 1000.0;
    }
}
