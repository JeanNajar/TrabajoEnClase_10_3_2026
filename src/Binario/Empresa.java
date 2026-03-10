package Binario;

import java.util.Scanner;

public class Empresa {

    public static void main(String[] args) {
        Scanner n = new Scanner(System.in);
        EmpleadoManager manager = new EmpleadoManager();
        int opcion = 0;

        do {
            System.out.println("\n======= MENU PRINCIPAL =======");
            System.out.println("1. Agregar Empleado");
            System.out.println("2. Listar Empleados activos");
            System.out.println("3. Agregar venta al empleado");
            System.out.println("4. Pagar empleado");
            System.out.println("5. Despedir empleado");
            System.out.println("6. Reporte de empleado");
            System.out.println("7. Salir");
            System.out.print("Escoja una opcion: ");
            opcion = n.nextInt();
            n.nextLine();

            try {
                switch (opcion) {

                    case 1:
                        System.out.print("Nombre del empleado: ");
                        String nombre = n.nextLine();
                        System.out.print("Salario: ");
                        double salario = n.nextDouble();
                        n.nextLine();
                        manager.addEmployee(nombre, salario);
                        System.out.println("Empleado agregado correctamente.");
                        break;

                    case 2:
                        System.out.println("\n--- Lista de Empleados Activos ---");
                        manager.employeeList();
                        break;

                    case 3:
                        System.out.print("Codigo del empleado: ");
                        int codeVenta = n.nextInt();
                        System.out.print("Monto de la venta: ");
                        double monto = n.nextDouble();
                        n.nextLine();
                        manager.AddSale(codeVenta, monto);
                        break;

                    case 4:
                        System.out.print("Codigo del empleado a pagar: ");
                        int codePago = n.nextInt();
                        n.nextLine();
                        manager.payEmployee(codePago);
                        break;

                    case 5:
                        System.out.print("Codigo del empleado a despedir: ");
                        int codeDespido = n.nextInt();
                        n.nextLine();
                        boolean resultado = manager.fireEmployee(codeDespido);
                        if (!resultado) {
                            System.out.println("No se pudo despedir al empleado.");
                        }
                        break;

                    case 6:
                        System.out.print("Codigo del empleado: ");
                        int codeReporte = n.nextInt();
                        n.nextLine();
                        manager.printEmployee(codeReporte);
                        break;

                    case 7:
                        System.out.println("Cerrando sistema...");
                        break;

                    default:
                        System.out.println("Opcion no valida, intente de nuevo.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 7);

        n.close();
        System.out.println("Programa cerrado.");
    }
}