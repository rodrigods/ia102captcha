import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class Classifier {
	
	private static final File TEST_DIR = new File("teste" + File.separator + "captchas");
	private static final File TRAINNING_DIR = new File("treinamento" + File.separator + "caracteres");
	
	
	private static List<String> getGuesses() throws IOException {
		List<String> result = new ArrayList<String>();
		
		for(File captchaFile: TEST_DIR.listFiles()){
			if (!validateFileName(captchaFile)) {
				continue;
			}
			
			BufferedImage image = ImageIO.read(captchaFile);

			List<BufferedImage> symbols = MyImage.getSimbolos(image);
				
			char[] guesses = new char[symbols.size()];

			int index = 0;
			for (BufferedImage symbol : symbols) {
				int max = 0;
				for (File character : TRAINNING_DIR.listFiles()) {
					if (!validateFileName(character)) {
						continue;
					}
					
					BufferedImage symbol2Compare = ImageIO.read(character);
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
			
			result.add(String.valueOf(guesses));
		}

		return result;
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
