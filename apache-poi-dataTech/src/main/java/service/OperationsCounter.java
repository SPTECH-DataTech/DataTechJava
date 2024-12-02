package service;

public class OperationsCounter {
    private static Integer success = 0;
    private static Integer failed = 0;

    public OperationsCounter() {
    }

    public static void registerSuccess() {
        success++;
    }

    public static void registerFalied() {
        failed++;
    }

    public static void exibirRelatorio() {
        System.out.println("Operações bem-sucedidas: " + success);
        System.out.println("Operações com falha: " + failed);
    }

    public static Integer getSuccess() {
        return success;
    }

    public static void setSuccess(Integer success) {
        OperationsCounter.success = success;
    }

    public static Integer getFailed() {
        return failed;
    }

    public static void setFailed(Integer failed) {
        OperationsCounter.failed = failed;
    }
}
