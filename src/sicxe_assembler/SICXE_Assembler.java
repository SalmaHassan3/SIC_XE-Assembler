 /*
****ATTACHED FILES****
*code : contains source code
*file: intermediate file, output of pass 1
*format:file containig the format of each opcode
*opcode:contains number corresponding to each opcode
*print: has location counter and object code for each instruction,output of pass 1
*record: has HTME record, output of pass 2
*registers: contains number corresponding to each register
*symtable: symbol table, output of pass 1
*/
package sicxe_assembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import static javafx.application.Platform.exit;

public class SICXE_Assembler {

    static String label = new String();
    static String operand = new String();
    static String opcode = new String();
     static String objOpcode=new String();
    static String address=new String(); 
    static String objLabel=new String();
    static int baseAddress;
    static int baseFlag=0;
    static BufferedReader br = null;
    static FileReader fr = null;
    static int startingAddress;
    static int LOCCTR;
    static int programLength ;
    static int errorFlag=0;
    static int flagEnd=0;
    static String endop=new String();
   static String stlb=new String();
    static List<String> objCodes=new ArrayList<String>();
    static List<String> modi=new ArrayList<String>();
    
    static String readFile(String fileName) {

        String currentLine = new String();
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            currentLine = br.readLine();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
         
        return currentLine;
    }

    static int checkLabel(String label) {
        BufferedReader br = null;
        FileReader fr = null;
        int n = -1;
        try {
            fr = new FileReader("symtable.txt");
            br = new BufferedReader(fr);
            String currentLine = new String();
            while ((currentLine = br.readLine()) != null) {
                String[] splitStr = currentLine.trim().split(",");
                if (label.equalsIgnoreCase(splitStr[0])) {
                    n = 1;
                    objLabel=splitStr[1];
                    break;
                } else {
                    n = 0;
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        return n;
    }

    static int checkOpcode() {
        BufferedReader br = null;
        FileReader fr = null;
        String op="";
        int n = -1;

        try {
            fr = new FileReader("opcode.txt");
            br = new BufferedReader(fr);
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] splitStr = currentLine.trim().split(",");
                if(opcode.startsWith("+")){
                    op=opcode.substring(1);
                if (op.equalsIgnoreCase(splitStr[0])) {
                    n = 1;
                    objOpcode=splitStr[1];
                    break;
                } else {
                    n = 0;
                }
                }
                else{
                    if (opcode.equalsIgnoreCase(splitStr[0])) {
                    n = 1;
                    objOpcode=splitStr[1];
                    break;
                } else {
                    n = 0;
                }
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        return n;
    }

    static void writeFile(String fileName) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);
            String locctr = Integer.toHexString(LOCCTR).toUpperCase();
            String formatStr = "%-20s %-20s %-20s %-20s";
            bw.write(String.format(formatStr, locctr, label, opcode, operand));
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            System.out.println("ERROR");
        }

    }

    static void writeComment(String fileName, String comment) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);
            String formatStr = "%-20s%-20s%-20s ";
            bw.write(String.format(formatStr, "", comment, ""));
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            System.out.println("ERROR");
        }

    }

    static void writeSymbolTable() {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("symtable.txt", true);
            bw = new BufferedWriter(fw);
            bw.write(label);
            bw.write(",");
            bw.write(Integer.toHexString(LOCCTR).toUpperCase());
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            System.out.println("ERROR");
        }

    }

    static String[] splitSpace(String line) {
        String[] splitStr = line.trim().split("\\s+");
        if (splitStr.length == 3) {
            if (!splitStr[0].equals("")) {
                opcode = splitStr[1];
                 operand = splitStr[2];
                 label = splitStr[0];
            }
        } else if (splitStr.length == 2) {
            if (!splitStr[0].equals("")) {
                opcode = splitStr[0];
                 operand = splitStr[1];
                  label = "";
            }
        } else if (splitStr.length == 1){
            if (!splitStr[0].equals("")) {
                opcode = splitStr[0];
                 operand = "";
                  label = "";
            }
        }
        return splitStr;
    }

    static int format() {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader("format.txt");
            br = new BufferedReader(fr);
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] splitStr = currentLine.trim().split(",");
                if (opcode.charAt(0)=='+')
                           return 4;
                else if (opcode.equalsIgnoreCase(splitStr[0])){
                   
                    if(splitStr[1].equals("1"))
                        return 1;
                    else if(splitStr[1].equals("2"))
                        return 2;
                    else if(splitStr[1].equals("3/4"))
                         return 3;
                    
                }
    }        
 
        }catch (IOException e) {System.out.println("ERROR");}
        
        return 0;
}

    static void pass1() {

        String currentline = readFile("code.txt");
        splitSpace(currentline);
        if (opcode.equalsIgnoreCase("START")) {
            startingAddress = Integer.parseInt(operand, 16);
            LOCCTR = startingAddress;
            writeFile("file.txt");
          //if(label.equals(""))
                  //  {
                   //    writeComment("file.txt", "ERROR! Program has no name");
                          //  errorFlag+=1; 
                  //  }
      }else {
            LOCCTR = 0;
        }

        while (!opcode.equalsIgnoreCase("END")) {
            

            try {
                String line = new String();
                line = br.readLine();

                String[] str = splitSpace(line);
                if (str[0].startsWith(".")) {
                    writeComment("file.txt", ".This is a comment");
                } else if (!str[0].equals("")) {
                    writeFile("file.txt");
                    if (str.length == 3) {
                        if (checkLabel(label) == 1) {
                            writeComment("file.txt", "ERROR! Duplicate label");
                            errorFlag+=1;
                        } else if (checkLabel(label) == 0) {
                            writeSymbolTable();
                        }

                    }

                    if (checkOpcode() == 0
                            && !opcode.equalsIgnoreCase("BYTE")
                            && !opcode.equalsIgnoreCase("WORD")
                            && !opcode.equalsIgnoreCase("RESB")
                            && !opcode.equalsIgnoreCase("RESW")
                            && !opcode.equalsIgnoreCase("BASE")
                            && !opcode.equalsIgnoreCase("END")
                            && !opcode.equalsIgnoreCase("START")) {
                        writeComment("file.txt", "ERROR! Invalid opcode");
                        errorFlag+=1;
                    } else
                    if (format() != 0) {
                        if(format()==2){
                             if(operand.contains(",")){
                            String[] operands = operand.split(",");
                            if(checkReg(operands[0]).equals("notfound")||checkReg(operands[1]).equals("notfound"))
                            {writeComment("file.txt", "ERROR! Invalid register name");
                               errorFlag+=1;}        
                            }
                             
                           else{
                          String reg= checkReg(operand);
                             if(reg.equals("notfound"))
                             {  writeComment("file.txt", "ERROR! Invalid register name");
                              errorFlag+=1;}
                           }
                        }
                        LOCCTR += format();
                    } else {
                        if (opcode.equalsIgnoreCase("RESW")) {
                            LOCCTR += 3 * Integer.parseInt(operand);
                        }
                        if (opcode.equalsIgnoreCase("RESB")) {
                            LOCCTR += Integer.parseInt(operand);
                        }
                        if (opcode.equalsIgnoreCase("WORD")) {
                            LOCCTR += 3;
                        }
                        if (opcode.equalsIgnoreCase("BYTE")) {
                            if (operand.charAt(0) == 'C') {
                                LOCCTR += (operand.length()) - 3;
                            } else if (operand.charAt(0) == 'X') {
                                LOCCTR += (operand.length() - 3) / 2;
                            } else {
                                LOCCTR += 1;
                            }
                        }
                    }

                }

            } catch (IOException e) {
                System.out.println("ERROR");
            }

        }
      programLength = LOCCTR - startingAddress; 
    }
    
   static void writeHeader(){
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("record.txt", true);
            bw = new BufferedWriter(fw);
            String start=String.format("%6s",Integer.toHexString(startingAddress)).replace(' ', '0');
            String length=String.format("%6s",Integer.toHexString(programLength)).replace(' ', '0');
            bw.write("H");
            bw.write("^");
            bw.write(label.toUpperCase());
            bw.write("^");
            bw.write(start.toUpperCase());
            bw.write("^");
            bw.write(length.toUpperCase());
            bw.newLine();
            bw.close();
       
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }
   
   
   
     static String checkReg(String reg) {
        BufferedReader br = null;
        FileReader fr = null;
        String n=new String();

        try {
            fr = new FileReader("registers.txt");
            br = new BufferedReader(fr);
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] splitStr = currentLine.trim().split(",");
                if (reg.equalsIgnoreCase(splitStr[0])) {
                    n=splitStr[1];
                    break;
                } else {
                    n ="notfound";
                }

            }
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        return n;
    }
    
     static String[] splitIntermediate(String line) {
        String[] splitStr = line.trim().split("\\s+");
        if (splitStr.length == 4) {
            if (!splitStr[0].equals("")) {
                label = splitStr[1];
                opcode = splitStr[2];
                operand = splitStr[3];
                address = splitStr[0];
            }
        } else if (splitStr.length == 3) {
            if (!splitStr[0].equals("")) {
                opcode = splitStr[1];
                operand = splitStr[2];
                 label = "";
                 address = splitStr[0];
            }
        } else if (splitStr.length == 2){
            if (!splitStr[0].equals("")) {
               address = splitStr[0];
                opcode = splitStr[1];
                 operand = "";
                 label = "";
            }
        }
        return splitStr;
    }
    static void writeTextRecord(String start,int length,String record){
       
         BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("record.txt", true);
            bw = new BufferedWriter(fw);
            bw.write("T");
            bw.write("^");
            bw.write(String.format("%6s", start).replace(' ', '0'));
            bw.write("^");
            bw.write(String.format("%2s", Integer.toHexString(length)).replace(' ', '0'));
            bw.write("^");
            bw.write(record);
            bw.newLine();
            if(flagEnd==1){
             for(int i=0;i<modi.size();i++){
            bw.write("M");
            bw.write("^");
            bw.write(String.format("%6s", modi.get(i)).replace(' ', '0'));
            bw.write("^");
            bw.write("05");
            bw.newLine();
           }
            bw.write("E");
            bw.write("^");  
            bw.write(String.format("%6s", endop).replace(' ', '0'));
            bw.newLine();}      
            
            bw.close();
       
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }
    
    
    
    
    static void print(String print){
       
         BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("print.txt", true);
            bw = new BufferedWriter(fw);
            bw.write(print);
            bw.newLine();
            bw.close();
       
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }
    
    static void pass2(){
        boolean flag=true;
        boolean prevFlag=true;
       
        String record=new String();
        String textRecord=new String();
        String start=new String();
        String temp=new String();
        String formattedAddress;
        int counter=0;
        String currentline = readFile("file.txt");
        splitIntermediate(currentline);
        if (opcode.equalsIgnoreCase("START")) {
            stlb=address;
            writeHeader();
            String objectCode="";
            start=address;
            formattedAddress=String.format("%6s", address).replace(' ', '0');
            String print = "%-20s%-20s%-20s%-20s%-20s%-20s";
           print(String.format(print,counter,formattedAddress,label,opcode,operand,objectCode)); 
        }
        while (!opcode.equalsIgnoreCase("END")) {
                String objectCode="";
            try {
                String line = new String();
                line = br.readLine();

                String[] str = splitIntermediate(line);
                temp=operand;
                if (str[0].startsWith(".")) {
                   continue;
                }
                if(opcode.equalsIgnoreCase("RESW")||opcode.equalsIgnoreCase("RESB")){
                    prevFlag=flag;
                    flag=false;
                     counter++;
                   formattedAddress=String.format("%6s", address).replace(' ', '0');
            String print = "%-20s%-20s%-20s%-20s%-20s%-20s";
           print(String.format(print,counter,formattedAddress,label,opcode,operand,objectCode)); 
                }
                
                else{ prevFlag=flag; flag=true;}
                   
                   
                if (opcode.equalsIgnoreCase("END")  
                || opcode.equalsIgnoreCase("RESB") 
                || opcode.equalsIgnoreCase("RESW"))
                    continue;
                if(checkOpcode()==0&& !opcode.equalsIgnoreCase("BYTE")
                            && !opcode.equalsIgnoreCase("WORD")
                            && !opcode.equalsIgnoreCase("BASE")
                                                               )
                {   counter++;
                    formattedAddress=String.format("%6s", address).replace(' ', '0');
            String print = "%-20s%-20s%-20s%-20s%-20s%-20s";
           print(String.format(print,counter,formattedAddress,label,opcode,operand,objectCode));
                    continue;}
                else{
                    if(opcode.equalsIgnoreCase("BASE")){
                        baseFlag=1;
                        if(checkLabel(operand)==1){
                            baseAddress=Integer.parseInt(objLabel,16);                
                                        }
                        else{
                            baseAddress=Integer.parseInt(operand);
                        }
                    }
                    int format=format();
                    
                    counter++;
                    switch(format)
                    {
                        case 1:
                        {   
                            objectCode=objOpcode;
                            objCodes.add(objOpcode);
                            break;
                        }
                        case 2:
                        {
                           
                            if(operand.contains(",")){
                            String[] operands = operand.split(",");
                                     if(checkReg(operands[0]).equals("notfound")||checkReg(operands[1]).equals("notfound"))
                                             continue;
                              else{
                                   String reg1=checkReg(operands[0]);
                                   String reg2=checkReg(operands[1]);
                                 objCodes.add(objOpcode+reg1+reg2);
                                 objectCode=objOpcode+reg1+reg2;
                              }
                            }
                             
                           else{
                          String reg= checkReg(operand);
                             if(reg.equals("notfound"))
                                  continue;
                             else{
                                 objCodes.add(objOpcode+reg+"0");
                                 objectCode=objOpcode+reg+"0";
                              }
                           }
                      break;
                            }
                        
                        case 3:
                        { 
                            String i="0",n="0",x="0",b="0",p="0",e="0",displacement,subOperand,part2="",part1="";
                        int disp;
                        if(opcode.equalsIgnoreCase("RSUB"))
                        {disp=0;
                        n="1";
                        i="1";
                         }
                        else {
                        if(operand.contains(",")){x="1";
                                   String[] operands = operand.split(",");
                                operand=operands[0];}
                        
                                if(operand.charAt(0)=='#'){
                                 i="1";
                                 subOperand=operand.substring(1);}
                                 
                                
                            
                            else if(operand.charAt(0)=='@'){
                                 n="1";
                                 subOperand=operand.substring(1);}
                            
                              
                            else{i="1";
                                 n="1";
                                subOperand=operand;}
                                
                                if(checkLabel(subOperand)==1)
                                {
                                   
                                    disp=Integer.parseInt(objLabel,16)-(Integer.parseInt(address,16)+3);
                                    if(disp>=-2048&&disp<=2047)
                                        p="1";
                                    else if(baseFlag==1){
                                        disp=Integer.parseInt(objLabel,16)-baseAddress;
                                        if(disp>=0&&disp<=4096)
                                        b="1";
                                        else 
                                            System.out.println("ERROR! Address out of range");
                                            }
                                    else 
                                        System.out.println("'BASE' directive not found");
                                    
                                }
                                else{
          
                                   disp=Integer.parseInt(subOperand);
                                }
                        }    
                     if(disp<0)           
              part2=Integer.toHexString(disp).substring(Integer.toHexString(disp).length()-3,Integer.toHexString(disp).length());
                     else{
                        if(Integer.toHexString(disp).length()==2)
                           part2="0"+Integer.toHexString(disp);
                        else if(Integer.toHexString(disp).length()==1)
                              part2="0"+"0"+Integer.toHexString(disp);
                     }
                   int f = Integer.parseInt(objOpcode, 16);
                  String bin=String.format("%8s", Integer.toBinaryString(f)).replace(' ', '0');
                    bin=bin.substring(0,6);
                   part1=bin+n+i+x+b+p+e;
                   int decimal = Integer.parseInt(part1,2);
                    part1=String.format("%3s", Integer.toString(decimal,16)).replace(' ', '0');
                     objCodes.add(part1+part2);
                     objectCode=part1+part2;
                            break;
                            
                            
                        }
                        case 4:{
                            String subModify;
                            if(operand.charAt(0)=='#'){
                                subModify=operand.substring(1);
                                if(checkLabel(subModify)==1){
                                 int loc=Integer.parseInt(address,16)+1;
                                  modi.add(Integer.toHexString(loc));   
                                }
                            }
                            if(checkLabel(operand)==1){
                            int loc=Integer.parseInt(address,16)+1;
                            modi.add(Integer.toHexString(loc));}
                            
                            String i="0",n="0",x="0",b="0",p="0",e="1",subOperand,part1="",part2="";
                            int disp;
                            
                            if(operand.contains(",")){x="1";
                                   String[] operands = operand.split(",");
                                operand=operands[0];}
                            
                            if(operand.charAt(0)=='#'){
                                 i="1";
                                 subOperand=operand.substring(1);}
                                 
                                
                            
                            else if(operand.charAt(0)=='@'){
                                 n="1";
                                 subOperand=operand.substring(1);}
                            
                              
                            else{i="1";
                                n="1";
                                subOperand=operand;}
                           
                             if(checkLabel(subOperand)==1)
                                {
                                   disp=Integer.parseInt(objLabel,16);
                                }
                             else
                               disp=Integer.parseInt(subOperand);  
                             part2=String.format("%5s", Integer.toHexString(disp)).replace(' ', '0');
                              int f = Integer.parseInt(objOpcode, 16);
                  String bin=String.format("%8s", Integer.toBinaryString(f)).replace(' ', '0');
                    bin=bin.substring(0,6);
                   part1=bin+n+i+x+b+p+e;
                   int decimal = Integer.parseInt(part1,2);
                   part1 = Integer.toString(decimal,16);
                   objCodes.add(part1+part2);
                   objectCode=part1+part2;
                             break;
                        }
                        
                        case 0:
                        {  
                            
                            if(opcode.equalsIgnoreCase("BYTE")){
                                if(operand.startsWith("X")){
                              String split= operand.substring(2,operand.length()-1);
                              objCodes.add(split);
                                objectCode=split;}
                                
                              else  if(operand.startsWith("C")){
                              String split= operand.substring(2,operand.length()-1); 
                              String ascii="";
                              for (char ch : split.toCharArray()) {
                                  ascii += Integer.toHexString(ch).toUpperCase();
                              }
                              objCodes.add(ascii);
                              objectCode=ascii;}
                                
                                else{
                                   System.out.println("byte");
                                int decimal=Integer.parseInt(operand);
                                 String hexa = Integer.toHexString(decimal); 
                                  objCodes.add(hexa);
                                  objectCode=hexa;
                                }
                            }
                            if(opcode.equalsIgnoreCase("WORD")){
                                 int decimal=Integer.parseInt(operand);
                                 String hexa = Integer.toHexString(decimal);
                                 objCodes.add(hexa);
                                 objectCode=hexa;
                             }
                            break;
                        }
                    }}}catch (IOException e) {System.out.println("ERROR");}
             formattedAddress=String.format("%6s", address).replace(' ', '0');
            String print = "%-20s%-20s%-20s%-20s%-20s%-20s";
           print(String.format(print,counter,formattedAddress,label,opcode,temp,objectCode));
           
           if(prevFlag==false&&flag==true){
                 
                 writeTextRecord(start,(record.length())/2,textRecord);
                  record=objectCode;
                  textRecord=objectCode+"^";
                  start=address;
             }
           
           else if(record.length()<=60){
               if((record+objectCode).length()<=60)
               {
                   record=record+objectCode;
                   textRecord=textRecord+objectCode+"^";
                  
               }
               
            
               else{
                   writeTextRecord(start,(record.length())/2,textRecord);
                   record=objectCode;
                   textRecord=objectCode+"^";
                   start=address;
               }
           }
           
                
    }
        
        flagEnd=1;
        if(operand.equalsIgnoreCase("start"))
            endop=stlb;
        else if(checkLabel(operand)==1)
        {
            endop=objLabel;
        }
        else
            endop=operand;
        
        writeTextRecord(start,(record.length())/2,textRecord);
    }
    public static void main(String[] args) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            PrintWriter pw = new PrintWriter("file.txt");
            pw.close();
            fw = new FileWriter("symtable.txt", false);
            bw = new BufferedWriter(fw);
            bw.write("Symbol Table");
            bw.newLine();
            bw.close();
            pw = new PrintWriter("record.txt");
            pw.close();
            pw = new PrintWriter("print.txt");
            pw.close();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        
      pass1();
      if(errorFlag !=0){
          System.out.println(errorFlag+" "+"Errors were found");
          System.exit(0);
      }
      pass2();   
    }

}
