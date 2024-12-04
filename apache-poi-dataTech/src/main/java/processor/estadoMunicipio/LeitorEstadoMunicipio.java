package processor.estadoMunicipio;

import datatech.log.Log;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import processor.LeitorArquivos;
import writer.ConexaoBanco;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeitorEstadoMunicipio extends LeitorArquivos {

    private static final String aplicacao = "Leitor Estado Municipio" ;
    private static final ConexaoBanco conexaoBanco = new ConexaoBanco();
    private static final List logs = new ArrayList<>();

    public LeitorEstadoMunicipio() {
        super(aplicacao, conexaoBanco, logs);
    }

    @Override
    public List extrairDados(String nomeArquivo, InputStream arquivo) {
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new XSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<EstadoMunicipio> estadoMunicipios = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() < 6) {
                    System.out.println("\nLendo cabeçalho");

                    System.out.println("--------------------");
                    continue;
                }

                EstadoMunicipio estadoMunicipio = new EstadoMunicipio();
                estadoMunicipio.setIdUf((int) row.getCell(0).getNumericCellValue());
                estadoMunicipio.setEstado(row.getCell(1).getStringCellValue());
                estadoMunicipio.setIdMunicipio((int) row.getCell(2).getNumericCellValue());
                estadoMunicipio.setMunicipio(row.getCell(3).getStringCellValue());

                estadoMunicipios.add(estadoMunicipio);
            }
            workbook.close();

            Log log = new Log("OK", this.aplicacao + " ", LocalDateTime.now(), " Leitura do arquivo finalizada");
            logs.add(log);
            System.out.println("\nLeitura do arquivo finalizada\n");
//            conexao.inserirLogNoBanco(logFimLeitura);

            return estadoMunicipios;

        } catch (IOException e) {
            Log log = new Log("ERRO", this.aplicacao + " ", LocalDateTime.now(), "Erro ao ler o arquivo" + e.getMessage());
            System.out.println("Erro ao ler o arquivo" + e.getMessage());
            logs.add(log);

//            conexao.inserirLogNoBanco(log);

            throw new RuntimeException(e);
        }
    }

    private LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
