package service;

import com.google.protobuf.MapEntryLite;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.Slack;
import datatech.log.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static service.OperationsCounter.registerFalied;

public class SlackService {
    private static List<String> errorList = new ArrayList<>();
    private static final String TOKEN = System.getenv("TOKEN_SLACK");
    private static final String CHANNEL_ID = System.getenv("CHANNEL_ID");

    public static void sendMessage() {
        try {
            Slack slack = Slack.getInstance();
            MethodsClient methods = slack.methods(TOKEN);

            StringBuilder allErrors = new StringBuilder("⚠️ ATENÃO - ERROS REGISTRADOS NO SISTEMA ⚠️");
            if (!errorList.isEmpty()) {
                for (String error : errorList) {
                    allErrors.append("```\n")
                            .append(error)
                            .append("\n```\n");
                }
            } else {
                allErrors.append("\n\nNenhum erro registrado no sistema.\n");
            }
            allErrors.append("\nResumo de operações:\n")
                    .append("✅ Operações bem-sucedidas: ").append(OperationsCounter.getSuccess()).append("\n")
                    .append("❌ Operações com falha: ").append(OperationsCounter.getFailed()).append("\n");

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(CHANNEL_ID)
                    .text(allErrors.toString())
                    .build();

            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (response.isOk()) {
                System.out.println("Notificação enviada com sucesso para o Slack!");
            } else {
                System.err.println("Erro ao enviar a notificação para o Slack!: " + response.getError());
            }
        } catch (SlackApiException | IOException e) {
            System.err.println("Erro durante a comunicação com a API do Slack: " + e.getMessage());
        }

    }

    public static void errorSlack(Throwable e) {
        StackTraceElement errorLocation = e.getStackTrace()[0];

            String application = "DataTech";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            String description = e.getMessage();
            String tipoExecao = e.getClass().getName();
            String filePath = errorLocation.getFileName();

                String location = String.format(
                    "Método: %s\nClasse: %s\nLinha: %d\nPath: %s\n",
                    errorLocation.getMethodName(),
                    errorLocation.getClassName(),
                    errorLocation.getLineNumber(),
                    filePath
            );

            String errorMessage = String.format(
                    "Aplicação: %s\nData e Hora: %s\nDescrição do Erro: %s\nTipo Exceção: %s\nLocalização:\n%s",
                    application, timestamp, description, tipoExecao, location
            );

            errorList.add(errorMessage);

            registerFalied();

            System.out.println("Erro registrado (SLACK)");
            sendMessage();
    }

    public void listarErros(){
        System.out.println(errorList);
    }

    public void limparListaErros(){
        errorList.clear();
    }



}
