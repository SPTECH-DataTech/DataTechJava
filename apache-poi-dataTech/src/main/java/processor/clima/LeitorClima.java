package processor.clima;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import processor.LeitorArquivos;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static service.SlackService.errorSlack;

public class LeitorClima extends LeitorArquivos {

    public LeitorClima() {}

    @Override
    public List<Clima> extrairDados(String nomeArquivo, InputStream arquivo) {
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new XSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);
            List<Clima> climasExtraidos = new ArrayList<>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {
                Clima clima = new Clima();

                if (row.getRowNum() < 11) {

                    System.out.println("\nLendo cabeçalho");

                    for (int i = 0; i < 4; i++) {
                        Cell cell = row.getCell(i);

                        if (row.getRowNum() == 0) {
                            clima.setMunicipio(row.getCell(0).getStringCellValue());
                        }

                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String coluna = cell.getStringCellValue();
                            System.out.println("Coluna " + i + ": " + coluna);
                        } else {
                            System.out.println("Coluna " + i + ": não é string.");
                        }

                    }

                    System.out.println("--------------------");
                    continue;

                }



                Cell cellDataMedicao = row.getCell(0);
                if (cellDataMedicao != null) {
                    clima.setDataMedicao(cellDataMedicao.toString());
                } else {
                    clima.setDataMedicao("Data não disponível");
                }

                clima.setMediaTemperaturaMaxima(lerValor(row.getCell(1)));
                clima.setMediaTemperaturaMinima(lerValor(row.getCell(2)));
                clima.setUmidadeAr(lerValor(row.getCell(3)));

                climasExtraidos.add(clima);
            }

            // Fechando o workbook após a leitura
            workbook.close();

            System.out.println("\nLeitura do arquivo finalizada\n");
            return climasExtraidos;
        } catch (IOException e) {
            errorSlack(e);
            throw new RuntimeException(e);
        }
    }

    public Double lerValor(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().replace(",", "."));
                } catch (NumberFormatException e) {
                    errorSlack(e);
                    System.err.println("Erro ao converter para Double: " + e.getMessage());
                    throw new Error("Erro ao converter para Double (Leitor Clima): " + e.getMessage(), e);
                    // retutn 0.0;
                }
            default:
                return 0.0;
        }
    }
}
