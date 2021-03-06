

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class MyImage {

	public static final int LARGURA_SIMBOLO = 20;
	public static final int ALTURA_SIMBOLO = 20;

	private static int LARANJA = -34816;

	private static int melhorX1, melhorX2, melhorY1, melhorY2;

	public static List<BufferedImage> getSimbolos(BufferedImage captchaImg) throws IOException {
		isolaCorLaranja(captchaImg);
		Coordenada pontoLarajna = null;

		List<BufferedImage> simbolos = new ArrayList<BufferedImage>();

		while(verificaIouJ(captchaImg));
		
		while((pontoLarajna = getNextCoordenadaLaranja(captchaImg)) != null){
			Dimensao nextDimensao = getNextDimensao(captchaImg, pontoLarajna);
			BufferedImage simboloRecortado = recortaImagemBW(captchaImg, nextDimensao);
			BufferedImage resized = resize(simboloRecortado, LARGURA_SIMBOLO, ALTURA_SIMBOLO);
			simbolos.add(resized);
		}

		return simbolos;
	}

	private static boolean verificaIouJ(BufferedImage img) {
		for(int x = 0; x < img.getWidth()-2; x++){
			for(int y = 0; y < img.getHeight()-2; y++){
				if(img.getRGB(x, y) == LARANJA && img.getRGB(x+1, y) == LARANJA && img.getRGB(x+2, y) == LARANJA &&
						img.getRGB(x, y+1) != LARANJA && img.getRGB(x+1, y+1) != LARANJA && img.getRGB(x+2, y+1) != LARANJA &&
						img.getRGB(x, y+2) == LARANJA && img.getRGB(x+1, y+2) == LARANJA && img.getRGB(x+2, y+2) == LARANJA){
					img.setRGB(x+1, y+1, LARANJA);
					return true;
				}
			}
		}
		return false;
	}

	private static void isolaCorLaranja(BufferedImage img) {
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				if(img.getRGB(x, y) != LARANJA){
					img.setRGB(x, y, Color.white.getRGB());
				}
			}
		}
	}

	/*
	 * Acha algum ponto q seja laranja e faz floodfil com a cor CINZA.
	 */
	private static Coordenada getNextCoordenadaLaranja(BufferedImage img) {
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				if(img.getRGB(x, y) == LARANJA){
					floodFill(img, y, x, Color.GRAY.getRGB(), LARANJA);
					return new Coordenada(x, y);
				}
			}
		}
		return null;
	}

	/**
	 * Recorta o caracterem e redimensiona para as dimensoes LARGURA_SIMBOLO x ALTURA_SIMBOLO.
	 * 
	 * @param imagem
	 * @param dimensao
	 * @param cor
	 * @return
	 */
	private static BufferedImage recortaImagemBW(BufferedImage imagem, Dimensao dimensao) {
		int type = imagem.getType() == 0? BufferedImage.TYPE_INT_ARGB : imagem.getType();

		int largura = dimensao.getInferiorDireito().x - dimensao.getSuperiorEsquerdo().x + 1;
		int altura = dimensao.getInferiorDireito().y - dimensao.getSuperiorEsquerdo().y + 1;

		BufferedImage imagemRecortada = new BufferedImage(largura, altura, type);

		int x1 = dimensao.getSuperiorEsquerdo().x;
		int y1 = dimensao.getSuperiorEsquerdo().y;
		int x2 = dimensao.getInferiorDireito().x;
		int y2 = dimensao.getInferiorDireito().y;

		for(int y = y1; y <= y2; y++){
			for(int x = x1; x <= x2; x++){
				imagemRecortada.setRGB(x-x1, y-y1, imagem.getRGB(x, y));
			}
		}

		return imagemRecortada;

	}

	private static Dimensao getNextDimensao(BufferedImage img, Coordenada pontoInicial) {
		melhorX1 = pontoInicial.x;
		melhorX2 = pontoInicial.x;
		melhorY1 = pontoInicial.y;
		melhorY2 = pontoInicial.y;
		buscaDimensoes(img, pontoInicial.y, pontoInicial.x, Color.BLACK.getRGB(), Color.GRAY.getRGB());
		Dimensao d = new Dimensao();
		d.setSuperiorEsquerdo(new Coordenada(melhorX1, melhorY1));
		d.setInferiorDireito(new Coordenada(melhorX2, melhorY2));
		return d;
	}

	/**
	 * Busca e seta em variaveis globais o valor dos pontos que definem o quadro de um caractere que s�o
	 * da cor oldColor.
	 * 
	 * @param img
	 * @param y
	 * @param x
	 * @param newCor
	 * @param oldCor
	 */
	private static void buscaDimensoes(BufferedImage img, int y, int x, int newCor,
			int oldCor) {
		if(y < 0 || y >= img.getHeight() || x < 0 || x >= img.getWidth()) return;
		if(img.getRGB(x, y) == oldCor) {
			if(x < melhorX1) melhorX1 = x;
			if(x > melhorX2) melhorX2 = x;
			if(y < melhorY1) melhorY1 = y;
			if(y > melhorY2) melhorY2 = y;
			img.setRGB(x, y, newCor);
			buscaDimensoes(img, y-1, x, newCor, oldCor);
			buscaDimensoes(img, y-1, x+1, newCor, oldCor);
			buscaDimensoes(img, y, x+1, newCor, oldCor);
			buscaDimensoes(img, y+1, x+1, newCor, oldCor);
			buscaDimensoes(img, y+1, x, newCor, oldCor);
			buscaDimensoes(img, y+1, x-1, newCor, oldCor);
			buscaDimensoes(img, y, x-1, newCor, oldCor);
			buscaDimensoes(img, y, x-1, newCor, oldCor);
			buscaDimensoes(img, y-1, x-1, newCor, oldCor);
		}
	}


	private static void floodFill(BufferedImage img, int l, int c, int newCor,
			int oldCor) {
		if(l < 0 || l >= img.getHeight() || c < 0 || c >= img.getWidth()) return;
		if(img.getRGB(c, l) == oldCor) {
			img.setRGB(c, l, newCor);
			floodFill(img, l-1, c, newCor, oldCor);
			floodFill(img, l-1, c+1, newCor, oldCor);
			floodFill(img, l, c+1, newCor, oldCor);
			floodFill(img, l+1, c+1, newCor, oldCor);
			floodFill(img, l+1, c, newCor, oldCor);
			floodFill(img, l+1, c-1, newCor, oldCor);
			floodFill(img, l, c-1, newCor, oldCor);
			floodFill(img, l, c-1, newCor, oldCor);
			floodFill(img, l-1, c-1, newCor, oldCor);
		}
	}


	private static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	public static void main(String[] args) {
		File captchasdir = new File("treinamento" + File.separator + "captchas");
		File[] captchasFile = captchasdir.listFiles();
		
		int i = 1;
		for(File captchaFile: captchasFile){
			try {
				BufferedImage imageImage = ImageIO.read(captchaFile);

				List<BufferedImage> simbolos = MyImage.getSimbolos(imageImage);
				for (BufferedImage simbolo : simbolos) {
					ImageIO.write(simbolo, "png", new File("treinamento" + File.separator + "caracteres" + File.separator + "saida" + i+ ".png"));
					i++;
				}
			} catch (IOException e) {
				// do nothing
			}
		}
		
//		File captcha2 = new File("treinamento" + File.separator + "captchas" + File.separator + "captcha2.jpg");

	}
}
class Coordenada {
	int x, y;
	public Coordenada(int x, int y){
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "x = "+ x + ", y = " + y;
	}
}

class Dimensao {

	private Coordenada superiorEsquerdo;
	private Coordenada inferiorDireito;

	public Dimensao() {

	}

	public int getArea() {
		return Math.abs(getInferiorDireito().y - getSuperiorEsquerdo().y) * Math.abs(getInferiorDireito().x - getSuperiorEsquerdo().x);
	}

	public void setSuperiorEsquerdo(Coordenada superiorEsquerdo) {
		this.superiorEsquerdo = superiorEsquerdo;
	}

	public Coordenada getSuperiorEsquerdo() {
		return superiorEsquerdo;
	}

	public void setInferiorDireito(Coordenada inferiorDireito) {
		this.inferiorDireito = inferiorDireito;
	}

	public Coordenada getInferiorDireito() {
		return inferiorDireito;
	}

	public String toString() {
		return superiorEsquerdo.toString() + " " + inferiorDireito.toString();
	}

}
