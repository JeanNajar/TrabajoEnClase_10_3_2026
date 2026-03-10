package Binario;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {

    private RandomAccessFile rcods, remps;

    public EmpleadoManager() {
        try {
            File mf = new File("company");
            mf.mkdir();
            rcods = new RandomAccessFile("company/codigos.emp", "rw");
            remps = new RandomAccessFile("company/empleados.emp", "rw");
            initCode();

        } catch (IOException e) {
            System.out.println("Error!");
        }

    }

    private void initCode() throws IOException {
        if (rcods.length() == 0) {
            rcods.writeInt(1);
        }
    }

    private int getCode() throws IOException {
        rcods.seek(0);
        int code = rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(code + 1);
        return code;
    }

    public void addEmployee(String name, double salary) throws IOException {
        remps.seek(remps.length());
        int code = getCode();
        remps.writeInt(code);
        remps.writeUTF(name);
        remps.writeDouble(salary);
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        remps.writeLong(0);
        // crear folder
        createEmployeeFolders(code);

    }

    private String employeeFolder(int code) {
        return "company/empleado" + code;
    }

    private RandomAccessFile salesFileFor(int code) throws IOException {
        String dirPadre = employeeFolder(code);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String dir = dirPadre + "/ventas" + year + ".emp";
        return new RandomAccessFile(dir, "rw");

    }

    /*
    Formato VentasYea.emp
    double saldo
    boolean estadoPago
    
     */
    private void createYearSaleFileFor(int code) throws IOException {
        RandomAccessFile rventa = salesFileFor(code);
        if (rventa.length() == 0) {
            for (int mes = 0; mes < 12; mes++) {
                rventa.writeDouble(0);
                rventa.writeBoolean(false);
            }

        }

    }

    private void createEmployeeFolders(int code) throws IOException {
        File dir = new File(employeeFolder(code));
        dir.mkdir();
        createYearSaleFileFor(code);
    }

    public void employeeList() throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int code = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            Date dateH = new Date(remps.readLong());
            if (remps.readLong() == 0) {
                System.out.println(code + " - " + name + " - " + salary + "$ - " + dateH);
            }
        }

    }
    private boolean isEmployeeActive(int code) throws IOException{
        remps.seek(0);
        while(remps.getFilePointer() < remps.length()){
            int cod=remps.readInt();
            long pos=remps.getFilePointer();
            remps.readUTF();
            remps.skipBytes(16);
            if(remps.readLong()==0 && cod==code){
                return true;
            }
        }
        return false;
    }
    
    public boolean fireEmployee(int code) throws IOException{
        if (isEmployeeActive(code)){
            String name= remps.readUTF();
            remps.skipBytes(16);
            remps.writeLong(new Date().getTime());
            System.out.println("Despidiendo a: "+name);
            return true;
        }
        return false;
    }
    
    public void AddSale(int code, double ven) throws IOException{
        if(isEmployeeActive(code)){
            RandomAccessFile sales= salesFileFor(code);
            int pos =(Calendar.getInstance().get(Calendar.MONTH))*9;
            sales.seek(pos);
            double monto= sales.readDouble();
            sales.seek(pos);
            sales.writeDouble(ven+monto);
            
        }
        System.out.println("");
    }
    
    private RandomAccessFile billsFileFor(int code) throws IOException {
        String dirPadre = employeeFolder(code);
        String dir = dirPadre + "/recibos.emp";
             return new RandomAccessFile(dir, "rw");
    }
    
    
    public boolean isEmployeePayed(int code) throws IOException {
        RandomAccessFile sales = salesFileFor(code);
        int mes = Calendar.getInstance().get(Calendar.MONTH);
        long pos = (long) mes * 9; 
        sales.seek(pos);
        sales.skipBytes(8);  
        boolean pagado = sales.readBoolean();
        sales.close();
        return pagado;
    }

    
    
        public void payEmployee(int code) throws IOException {
      
        if (!isEmployeeActive(code)) {
            System.out.println("No se pudo pagar");
            return;
        }
          if (isEmployeePayed(code)) {
            System.out.println("No se pudo pagar");
            return;
        }

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int mes  = Calendar.getInstance().get(Calendar.MONTH);
 
        
        String empName   = "";
        double empSalary = 0;

        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int cod      = remps.readInt();
            String name  = remps.readUTF();
            double sal   = remps.readDouble();
            remps.skipBytes(8);
            long fired   = remps.readLong();
            if (cod == code && fired == 0) {
                empName   = name;
                empSalary = sal;
                break;
            }
          }
        
        RandomAccessFile sales = salesFileFor(code);
        long posVentas = (long) mes * 9;
        sales.seek(posVentas);
        double ventas = sales.readDouble(); 
        
        double sueldo    = empSalary + (ventas * 0.10);
        double deduccion = sueldo * 0.035;
        double total     = sueldo - deduccion;

        
        RandomAccessFile bills = billsFileFor(code);
        bills.seek(bills.length());
        bills.writeLong(new Date().getTime());  
        bills.writeDouble(sueldo);
        bills.writeDouble(deduccion);
        bills.writeInt(year);
        bills.writeInt(mes);
        bills.close();
        
        sales.writeBoolean(true);
        sales.close();

        System.out.printf("Empleado %s se le pago Lps. %.2f%n", empName, total);
    
  }
        
         public void printEmployee(int code) throws IOException {
  
        String empName    = "";
        double empSalary  = 0;
        Date   empHireDate = null;
        boolean found     = false;

        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int    cod      = remps.readInt();
            String name     = remps.readUTF();
            double sal      = remps.readDouble();
            Date   hireDate = new Date(remps.readLong());
            long   fired    = remps.readLong();

            if (cod == code) {
                empName     = name;
                empSalary   = sal;
                empHireDate = hireDate;
                found       = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Empleado no encontrado.");
            return;
        }
        
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(empHireDate);
        String fechaStr = String.format("%02d/%02d/%04d",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));

        System.out.println("Codigo: "              + code);
        System.out.println("Nombre: "              + empName);
        System.out.println("Salario: "             + empSalary);
        System.out.println("Fecha de contratacion: " + fechaStr);
        
        
         RandomAccessFile sales = salesFileFor(code);
        sales.seek(0);
        double totalVentas = 0;

        for (int m = 0; m < 12; m++) {
            double venta = sales.readDouble();
            sales.readBoolean();   
            System.out.println("Mes " + (m + 1) + " : " + venta);
            totalVentas += venta;
        }
        sales.close();
        
         System.out.println("Total de ventas del año: " + totalVentas);
         
           RandomAccessFile bills = billsFileFor(code);
        // suma de las cosas, long(8) + double(8) + double(8) + int(4) + int(4) = 32 bytes
        long totalRecibos = bills.length() / 32;
        bills.close();

        System.out.println("Total de pagos realizados: " + totalRecibos);
     }
         
}