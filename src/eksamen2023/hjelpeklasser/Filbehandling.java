package eksamen2023.hjelpeklasser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
/**
 * For å lage forbindelse til filene, skrive eller lese
 * @author 7040
 */
public class Filbehandling {

	/**
         * Lager en skriveforbindelse
         * @param filnavn
         * @return PrintWriter
         */
	public static PrintWriter lagSkriveforbindelse(String filnavn) {
		try {
			FileWriter filforbindelse = new FileWriter(filnavn);
			BufferedWriter skrivebuffer = new BufferedWriter(filforbindelse);
			PrintWriter skriver = new PrintWriter(skrivebuffer);
			return skriver;
		}catch(Exception e) {return null;}		
	} //slutt metode
        /**
         * Lager en leseforbindelse
         * @param filnavn
         * @return BufferedReader
         */
	public static BufferedReader lagLeseforbindelse(String filnavn) {
		try {
			FileReader filforbindelse = new FileReader(filnavn);
			BufferedReader leser = new BufferedReader(filforbindelse);
			return leser;
		}catch(Exception e) {return null;}
	}

}
