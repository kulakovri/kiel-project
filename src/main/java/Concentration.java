import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Concentration {
    public static Map<String, Double> getElementWeight() {
        Map<String, Double> molarWeightsElements = new HashMap<>();
        String unparsedMolarWeights =
                "'O': 15.9994, 'Na': 22.99, 'Mg': 24.305, 'Al': 26.9815, " +
                        "'Si': 28.086, 'P': 30.9738, 'K': 39.098, 'Ca': 40.078," +
                        " 'Ti': 47.867, 'V': 50.9415, 'Cr': 51.996, 'Mn': 54.938, " +
                        "'Fe': 55.845, 'Ni': 58.693, 'Sr': 103.62";
        String[] elementsAndWeights = unparsedMolarWeights.split(", ");
        for (String onePair : elementsAndWeights) {
            String elementName = StringUtils.substringBetween(onePair, "'", "'");
            Double elementMolarWeight = Double.valueOf(StringUtils.substringAfter(onePair, " "));
            molarWeightsElements.put(elementName, elementMolarWeight);
        }
        return molarWeightsElements;
    }

    public static Map<String, Double> getSphPpmMap() {
        Map<String, Double> sphValues = new HashMap<>();
        String ppmValuesPlain = "Al: 156129.0 ppm Ba: 121.9 ppm Ca: 87621.0 ppm " +
                "Cr: 0.1 ppm Ni: 0.0 ppm Cu: 3.2 ppm Dy: 0.1 ppm Eu: 0.4 ppm Fe: 2876.0 ppm " +
                "Ce: 2.5 ppm Co: 0.5 ppm Ga: 14.9 ppm Gd: 0.1 ppm K: 2324.0 ppm La: 1.4 ppm Li: 8.2 ppm " +
                "Mg: 663.0 ppm Mn: 77.0 ppm Na: 33235.0 ppm Nd: 1.1 ppm P: 48.7 ppm " +
                "Pb: 0.3 ppm Pr: 0.3 ppm Rb: 0.35 ppm Sc: 0.2 ppm Si: 248911.0 ppm " +
                "Sm: 0.2 ppm Sr: 1024.0 ppm Ti: 295.13 ppm V: 1.7 ppm Y: 0.26 ppm Zn: 1.4";
        String[] ppmValuesList = ppmValuesPlain.split(" ppm ");
        for (String ppmValue : ppmValuesList) {
            String elementName = StringUtils.substringBefore(ppmValue, ":");
            String weightString = StringUtils.substringAfterLast(ppmValue, " ");
            sphValues.put(elementName, Double.valueOf(weightString));
        }
        return sphValues;
    }
}
