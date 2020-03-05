public class Oxide {
    Double oxygenMolarWeight = 15.9994;
    Double cationMolarWeight;
    String cation;
    Integer cationAmount;
    Integer oxygenAmount;
    Double ppmWeightRatio;

    Oxide(String name) {
        String[] formulaParts = name.split("O");
        setOxygenAmount(formulaParts);
        setCation(formulaParts);
        setCationAmount(formulaParts);
        cationMolarWeight = Store.getMolarWeight(cation);
        setPpmWeightRatio();
        System.out.println(cation);
        System.out.println(cationAmount);
        System.out.println(oxygenAmount);
        System.out.println(ppmWeightRatio);
    }

    void setOxygenAmount(String[] formulaParts) {
        if (formulaParts.length == 1) {
            oxygenAmount = 1;
        }  else {
            oxygenAmount = Integer.valueOf(formulaParts[1]);
        }
    }

    void setCation(String[] formulaParts) {
        cation = formulaParts[0].replaceAll("[0-9]", "");
    }

    void setCationAmount(String[] formulaParts) {
        String numbers = formulaParts[0].replaceAll("[a-zA-Z]", "");
        if (!numbers.equals("")) {
            cationAmount = Integer.valueOf(numbers);
        } else {
            cationAmount = 1;
        }
    }

    void setPpmWeightRatio() {
        ppmWeightRatio = (((cationMolarWeight * cationAmount + oxygenMolarWeight * oxygenAmount) - oxygenMolarWeight * oxygenAmount) * 10000)
                / (cationMolarWeight * cationAmount + oxygenMolarWeight * oxygenAmount);
    }
}
