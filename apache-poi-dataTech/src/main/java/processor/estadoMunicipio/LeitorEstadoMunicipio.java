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

    String aplicacao = "LeitorEstadoMunicipio";

    public LeitorEstadoMunicipio() {
    }

    @Override
    public List<EstadoMunicipio> extrairDados(String nomeArquivo, InputStream arquivo) {
        try {
            Log logInicioLeitura = new Log(this.aplicacao + " ", LocalDateTime.now(), " Iniciando leitura do arquivo %s\n".formatted(nomeArquivo));
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));
            getConexao().inserirLogNoBanco(logInicioLeitura);

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new XSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<EstadoMunicipio> estadoMunicipios = new ArrayList<>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {

                if (row.getRowNum() < 7) {
                    System.out.println("\nLendo cabeçalho");

                    for (int i = 0; i < 4; i++) {
                        String coluna = row.getCell(i).getStringCellValue();
                        System.out.println("Coluna " + i + ": " + coluna);
                    }

                    System.out.println("--------------------");
                    continue;
                }

                // Extraindo valor das células e criando objeto plantação

                EstadoMunicipio estadoMunicipio = new EstadoMunicipio();
                estadoMunicipio.setIdUf((int) row.getCell(0).getNumericCellValue());
                estadoMunicipio.setEstado(row.getCell(1).getStringCellValue());
                estadoMunicipio.setIdMunicipio((int) row.getCell(2).getNumericCellValue());
                estadoMunicipio.setMunicipio(row.getCell(3).getStringCellValue());

                estadoMunicipios.add(estadoMunicipio);
            }

            // Fechando o workbook após a leitura
            workbook.close();

            Log logFimLeitura = new Log(this.aplicacao + " ", LocalDateTime.now(), " Leitura do arquivo finalizada");
            System.out.println("\nLeitura do arquivo finalizada\n");
            getConexao().inserirLogNoBanco(logFimLeitura);

            return estadoMunicipios;

        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), "Erro ao ler o arquivo" + e.getMessage());
            System.out.println("Erro ao ler o arquivo" + e.getMessage());
            getConexao().inserirLogNoBanco(log);
            throw new RuntimeException(e);
        }
    }

    public LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }




}
