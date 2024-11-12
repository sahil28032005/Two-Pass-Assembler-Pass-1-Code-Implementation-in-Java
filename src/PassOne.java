import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassOne {
    //firstly create maps for symboltable and opcodeTable and list for intermediate code
	private Map<String,Integer> symbolTable;
	private Map<String,String> opcodeTable;
	private List<String> intermediate;
	private int locationCounter;
	
	//crete consructor
	
	public PassOne() {
		symbolTable=new HashMap<>();
		opcodeTable=new HashMap<>();
		intermediate=new ArrayList<>();
		locationCounter=0;
		
		//inatialize with some sample opcodes for identity=fy their existanc in asssembly input program
		opcodeTable.put("LOAD","01");
		opcodeTable.put("STORE","02");
		opcodeTable.put("ADD","03");
		opcodeTable.put("SUB","04");
		opcodeTable.put("JMP","05");

	}
	public static void main(String[] args){
		// TODO Auto-generated method stub
		PassOne p1=new PassOne();
		try {
			p1.passOne("src/input.asm");
			p1.writeSymbolTable("symboltable.txt");
			p1.writeIntermediateCode("intermediate.txt");
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		
		
	}
	
	//MAKE METHOD FO PASSONE
    public void passOne(String inpFilePath) throws Exception{
    	System.out.println("insdie pass1");
    	 try(BufferedReader reader=new BufferedReader(new FileReader(inpFilePath))){
    		 String line;
    		 while((line=reader.readLine()) != null) {
    			 //give that liine to process
    			 processLine(line);    		 }
    	 }
    }	
    
    public void processLine(String line) {
    	 //we have line of input assembly classify it
    	
//    	1)idetify not empty and not comment
    	
    	if(line.isEmpty() || line.startsWith(";")) {
    		return;
    	}
    	
    	//split parts in array format by spaces
    	String splitenLine[]=line.split("\\s+");
    	
    	//now for that line we have label opcode and operand
    	String label=null,opcode,operand=null;
    	
    	//now time to seperate each in this variabbles accordint to length they also vary
    	
//    	START LOAD 1000
//    	      LOAD 1000
//    	            ADD
    	if(splitenLine.length==3) {
    		label=splitenLine[0];
    		opcode=splitenLine[1];
    		operand=splitenLine[2];
    	}
        else if (splitenLine.length==2) {
        opcode = splitenLine[0];       // First part is opcode
        operand = splitenLine[1];      // Second part is operand
       }
       else {
        opcode =splitenLine[0];       // Only opcode in the line
       }
    	
    	//now handle that labels and opcodes
    	System.out.println("current vals"+label+"\t"+opcode+"\t"+operand);
    	//label
    	if(label!=null && !symbolTable.containsKey(label)) {
    		symbolTable.put(label,locationCounter);
    	}
    	
    	//process opcode
    	if(opcodeTable.containsKey(opcode)) {
    		intermediate.add(locationCounter+"\t"+label+"\t"+opcode+"\t"+operand);
    		locationCounter++;
    	}
    	else if(opcode.equals("START")){
    			locationCounter=(operand!=null)? Integer.parseInt(operand):0;
    	}
    	else if(opcode.equals("END")) {
    		intermediate.add(locationCounter+"\t"+opcode);
    	}
    	else {
            System.err.println("Error: Unknown opcode " + opcode);
        }
    	
    }
    
    public void writeSymbolTable(String filePath) throws Exception {
    	try(BufferedWriter writer=new BufferedWriter(new FileWriter(filePath))){
    		writer.write("Symbol\t Address\n");
    		for(Map.Entry<String, Integer> entry:symbolTable.entrySet()) {
    			//write each entry in writer file
    			writer.write(entry.getKey()+"\t"+entry.getValue()+"\n");
    		}
    	}
    }
    
    private void writeIntermediateCode(String filePath) throws Exception {
    	
    	try(BufferedWriter writer=new BufferedWriter(new FileWriter(filePath))){
    		 writer.write("Location\tLabel\tOpcode\tOperand\n");
    		for(String entry:intermediate) {
    			writer.write(entry + "\n");
    		}
    	}
    }

}
