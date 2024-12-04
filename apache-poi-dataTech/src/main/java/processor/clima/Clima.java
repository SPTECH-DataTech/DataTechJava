package processor.clima;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static service.SlackService.errorSlack;

public class Clima {
    private String dataMedicao;
    private Double mediaTemperaturaMinima;
    private Double mediaTemperaturaMaxima;
    private Double umidadeAr;
    private String municipio;
    private Integer idFazenda;




    public Clima() {

    }

    public Clima(String dataMedicao, Double mediaTemperaturaMinima, Double mediaTemperaturaMaxima, Double umidadeAr, String municipio) {
        this.dataMedicao = dataMedicao;
        this.mediaTemperaturaMinima = mediaTemperaturaMinima;
        this.mediaTemperaturaMaxima = mediaTemperaturaMaxima;
        this.umidadeAr = umidadeAr;
        this.municipio = municipio;
    }


    public String getDataMedicao() {
        if (dataMedicao == null || dataMedicao.isEmpty()) {
            return "Data não disponível";
        }

        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date data = formatoEntrada.parse(dataMedicao);
            return formatoSaida.format(data);
        } catch (Exception e) {
            System.err.println("Erro ao converter a data: " + dataMedicao);
            return "Data inválida";
        }
    }

    public void setDataMedicao(String dataStr) {
        this.dataMedicao = dataStr;
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

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Integer getIdFazenda() {
        return idFazenda;
    }

    public void setIdFazenda(Integer idFazenda) {
        this.idFazenda = idFazenda;
    }

    @Override
    public String toString() {
        return "Clima{" +
                "dataMedicao='" + dataMedicao + '\'' +
                ", mediaTemperaturaMinima=" + mediaTemperaturaMinima +
                ", mediaTemperaturaMaxima=" + mediaTemperaturaMaxima +
                ", umidadeAr=" + umidadeAr +
                ", municipio='" + municipio + '\'' +
                ", idFazenda=" + idFazenda +
                '}';
    }
}
