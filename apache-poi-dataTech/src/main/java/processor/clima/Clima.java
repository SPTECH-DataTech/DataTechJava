package processor.clima;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Clima {
    private String dataMedicao;
    private Double mediaTemperaturaMinima;
    private Double mediaTemperaturaMaxima;
    private Double umidadeAr;


    public Clima() {

    }

    public Clima(String dataMedicao, Double mediaTemperaturaMinima, Double mediaTemperaturaMaxima, Double umidadeAr) {
        this.dataMedicao = dataMedicao;
        this.mediaTemperaturaMinima = mediaTemperaturaMinima;
        this.mediaTemperaturaMaxima = mediaTemperaturaMaxima;
        this.umidadeAr = umidadeAr;
    }

    public String getDataMedicao() {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd-MMM-yyyy", new Locale("pt", "BR"));  //Para formatar o parâmetro para Date
        SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatoSaida.format(formatoEntrada.parse(dataMedicao));
        } catch (Exception e) {
            System.err.println("Erro ao converter a data: " + dataMedicao);
            return null;
        }

    }

    public void setDataMedicao(String dataMedicao) {
       this.dataMedicao = dataMedicao;
    }

    public Double getMediaTemperaturaMinima() {
        return mediaTemperaturaMinima;
    }

    public void setMediaTemperaturaMinima(Double mediaTemperaturaMinima) {
        this.mediaTemperaturaMinima = mediaTemperaturaMinima;
    }

    public Double getMediaTemperaturaMaxima() {
        return mediaTemperaturaMaxima;
    }

    public void setMediaTemperaturaMaxima(Double mediaTemperaturaMaxima) {
        this.mediaTemperaturaMaxima = mediaTemperaturaMaxima;
    }

    public Double getUmidadeAr() {
        return umidadeAr;
    }

    public void setUmidadeAr(Double umidadeAr) {
        this.umidadeAr = umidadeAr;
    }

    @Override
    public String toString() {
        return "Clima{" +
                "mediaTemperaturaMinima=" + mediaTemperaturaMinima +
                ", mediaTemperaturaMaxima=" + mediaTemperaturaMaxima +
                ", umidadeAr=" + umidadeAr +
                '}';
    }
}
