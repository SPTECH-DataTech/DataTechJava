package processor.estadoMunicipio;

public class EstadoMunicipio {

    private Integer idUf;
    private Integer idMunicipio;
    private String estado;
    private String municipio;

    public EstadoMunicipio() {    }

    public EstadoMunicipio(Integer idUf, Integer idMunicipio, String estado, String municipio) {
        this.idUf = idUf;
        this.idMunicipio = idMunicipio;
        this.estado = estado;
        this.municipio = municipio;
    }

    public Integer getIdUf() {
        return idUf;
    }

    public void setIdUf(Integer idUf) {
        this.idUf = idUf;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @Override
    public String toString() {
        return "EstadoMunicipio{" +
                "idUf=" + idUf +
                ", idMunicipio=" + idMunicipio +
                ", estado='" + estado + '\'' +
                ", municipio='" + municipio + '\'' +
                '}';
    }
}
