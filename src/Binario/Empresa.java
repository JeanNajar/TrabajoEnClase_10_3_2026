package Binario;

import java.util.Scanner;

public class Empresa {
        
    public static void main(String[] args) {
        Scanner lea=new Scanner(System.in);
        int opcion=0;
        
        System.out.println("****** Menu Principal ******");
        System.out.println("1- Agregar Empleado ");
        System.out.println("2- lista Empleados No Despedidos");
        System.out.println("3- Agregar venta a Empleados");
        System.out.println("4- Pagar Empleado");
        System.out.println("5- Despedir Empleado");
        System.out.println("6- Salir");
        
        System.out.print("escoga una opcion ");
        opcion=lea.nextInt();
    }
    
}
