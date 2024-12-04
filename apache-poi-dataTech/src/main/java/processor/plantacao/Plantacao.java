package processor.plantacao;

public class Plantacao {
    private Integer ano;
    // private Integer municipio;
    private Double quantidadeColhida;
    private Double areaPlantada;
    private Double valorReais;
    private Integer fkFazenda;
    private Integer fazenda_fkEmpresa;
    private Integer fazenda_fkEstadoMunicipio;
    // private Integer tipoCafe;

    public Plantacao(){}

    /*public Integer getMunicipio() {
        return municipio;
    }*/

    public Integer getAno() {
        return ano;
    }

    /*public Integer getTipoCafe() {
        return tipoCafe;
    }*/

    public Double getAreaPlantada() {
        return areaPlantada;
    }

    public Double getQuantidadeColhida() {
        return quantidadeColhida;
    }

    public Double getValorReais() {
        return valorReais;
    }

    /*public void setMunicipio(Integer municipio) {
        this.municipio = municipio;
    }*/

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    /*public void setTipoCafe(Integer tipoCafe) {
        this.tipoCafe = tipoCafe;
    }*/

    public void setAreaPlantada(Double areaPlantada) {
        this.areaPlantada = areaPlantada;
    }

    public void setQuantidadeColhida(Double quantidadeColhida) {
        this.quantidadeColhida = quantidadeColhida;
    }

    public void setValorReais(Double valorReais) {
        this.valorReais = valorReais;
    }

    public Integer getFkFazenda() {
        return fkFazenda;
    }

    public void setFkFazenda(Integer fkFazenda) {
        this.fkFazenda = fkFazenda;
    }

    public Integer getFazenda_fkEmpresa() {
        return fazenda_fkEmpresa;
    }

    public void setFazenda_fkEmpresa(Integer fazenda_fkEmpresa) {
        this.fazenda_fkEmpresa = fazenda_fkEmpresa;
    }

    public Integer getFazenda_fkEstadoMunicipio() {
        return fazenda_fkEstadoMunicipio;
    }

    public void setFazenda_fkEstadoMunicipio(Integer fazenda_fkEstadoMunicipio) {
        this.fazenda_fkEstadoMunicipio = fazenda_fkEstadoMunicipio;
    }

    @Override
    public String toString() {
        return "Plantacao{" +
                "ano=" + ano +
                ", quantidadeColhida=" + quantidadeColhida +
                ", areaPlantada=" + areaPlantada +
                ", valorReais=" + valorReais +
                ", fkFazenda=" + fkFazenda +
                ", fazenda_fkEmpresa=" + fazenda_fkEmpresa +
                ", fazenda_fkEstadoMunicipio=" + fazenda_fkEstadoMunicipio +
                '}';
    }
}
