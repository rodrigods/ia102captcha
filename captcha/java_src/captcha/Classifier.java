package captcha;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class Classifier {
	
	private static final File TEST_DIR = new File("teste" + File.separator + "captchas");
	private static final File TRAINNING_DIR = new File("treinamento" + File.separator + "caracteres");
	private static final String CURRENT_DIRECTORY = System.getProperty("user.dir") + File.separator + "workspaceP1" + File.separator + "captcha" + File.separator;
	
	
	private static List<String> getGuesses() throws IOException {
		List<String> result = new ArrayList<String>();
		
		for(File captchaFile: TEST_DIR.listFiles()){
			if (!validateFileName(captchaFile)) {
				continue;
			}
			
			String guesses = guessCaptcha(captchaFile);
			
			
			
			result.add(String.valueOf(guesses));
		}

		return result;
	}
	public static String guessCaptcha(String nameFile) throws IOException {
		String fullNameFile =  CURRENT_DIRECTORY + TEST_DIR + File.separator + nameFile;
		
		File captchaFile = new File(fullNameFile);
		return guessCaptcha(captchaFile);
	}
	
	public static String guessCaptcha(File captchaFile) {
		
		
		BufferedImage image;
		List<BufferedImage> symbols = new ArrayList<BufferedImage>();
		try {
			image = ImageIO.read(captchaFile);
			symbols = MyImage.getSimbolos(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
			
		char[] guesses = new char[symbols.size()];

		int index = 0;
		for (BufferedImage symbol : symbols) {
			int max = 0;
			String fullNameFile =  CURRENT_DIRECTORY + TRAINNING_DIR;
			File TRAINNING_DIR_File = new File(fullNameFile);
			for (File character : TRAINNING_DIR_File.listFiles()) {
				if (!validateFileName(character)) {
					continue;
				}
				
				BufferedImage symbol2Compare = null;
				try {
					symbol2Compare = ImageIO.read(character);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int out = 0;
				
				for (int i = 0; i < 20; i++) {
					for (int j = 0; j < 20; j++) {
						if (symbol.getRGB(i, j) == symbol2Compare.getRGB(i, j)) {
							out++;
						}
					}
				}
					
				if (out > max) {
					guesses[index] = character.getName().charAt(0);
					max = out;
				}
			}
				
			index++;
		}
		String a = toString(guesses);
		return a;
	}


	private static String toString(char[] guesses) {
		String string = "";
	for(int i = 0; i< guesses.length; i++){
		string += guesses[i];
	}
	return string;
	}
	private static boolean validateFileName(File file) {
		return Character.isLetterOrDigit(file.getName().charAt(0));
	}


	public static void main(String[] args) {
		
		List<String> guesses;
		try {
			guesses = getGuesses();
			
			System.out.println("#####");
			
			for (String guess : guesses) {
				System.out.println(guess);
			}
			
			System.out.println("#####");
		} catch (IOException e) {
			System.out.println("Error on computing guesses");
		}
	}
}
