package processor.plantacao;

public class Plantacao {
    private Integer ano;
    private Integer municipio;
    private Double quantidadeColhida;
    private Integer areaPlantada;
    private Integer valorReais;
    private Integer tipoCafe;

    public Plantacao(){}

    public Integer getMunicipio() {
        return municipio;
    }

    public Integer getAno() {
        return ano;
    }

    public Integer getTipoCafe() {
        return tipoCafe;
    }

    public Integer getAreaPlantada() {
        return areaPlantada;
    }

    public Double getQuantidadeColhida() {
        return quantidadeColhida;
    }

    public Integer getValorReais() {
        return valorReais;
    }

    public void setMunicipio(Integer municipio) {
        this.municipio = municipio;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public void setTipoCafe(Integer tipoCafe) {
        this.tipoCafe = tipoCafe;
    }

    public void setAreaPlantada(Integer areaPlantada) {
        this.areaPlantada = areaPlantada;
    }

    public void setQuantidadeColhida(Double quantidadeColhida) {
        this.quantidadeColhida = quantidadeColhida;
    }

    public void setValorReais(Integer valorReais) {
        this.valorReais = valorReais;
    }

    @Override
    public String toString() {
        return "Plantacao{" +
                "ano=" + ano +
                ", municipio='" + municipio + '\'' +
                ", quantidade_colhida='" + quantidadeColhida + '\'' +
                ", area_plantada=" + areaPlantada +
                ", valor_reais=" + valorReais +
                '}';
    }
}
