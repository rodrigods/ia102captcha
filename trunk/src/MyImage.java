

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
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

		List<BufferedImage> simbolos = new LinkedList<BufferedImage>();

		//TODO eh presiso verificar se existe o caracter 'i' ou 'j'

		while((pontoLarajna = getNextCoordenadaLaranja(captchaImg)) != null){
			Dimensao nextDimensao = getNextDimensao(captchaImg, pontoLarajna);
			BufferedImage simboloRecortado = recortaImagemBW(captchaImg, nextDimensao);
			simbolos.add(simboloRecortado);
		}

		return simbolos;
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

		int largura = dimensao.getInferiorDireito().x - dimensao.getSuperiorEquesrdo().x + 1;
		int altura = dimensao.getInferiorDireito().y - dimensao.getSuperiorEquesrdo().y + 1;

		BufferedImage imagemRecortada = new BufferedImage(largura, altura, type);

		int x1 = dimensao.getSuperiorEquesrdo().x;
		int y1 = dimensao.getSuperiorEquesrdo().y;
		int x2 = dimensao.getInferiorDireito().x;
		int y2 = dimensao.getInferiorDireito().y;

		for(int y = y1; y <= y2; y++){
			for(int x = x1; x <= x2; x++){
				imagemRecortada.setRGB(x-x1, y-y1, imagem.getRGB(x, y));
			}
		}

		return resize(imagemRecortada, LARGURA_SIMBOLO, ALTURA_SIMBOLO);

	}

	private static Dimensao getNextDimensao(BufferedImage img, Coordenada pontoInicial) {
		melhorX1 = pontoInicial.x;
		melhorX2 = pontoInicial.x;
		melhorY1 = pontoInicial.y;
		melhorY2 = pontoInicial.y;
		buscaDimensoes(img, pontoInicial.y, pontoInicial.x, Color.BLACK.getRGB(), Color.GRAY.getRGB());
		Dimensao d = new Dimensao();
		d.setSuperiorEquesrdo(new Coordenada(melhorX1, melhorY1));
		d.setInferiorDireito(new Coordenada(melhorX2, melhorY2));
		return d;
	}

	/**
	 * Busca e seta em variaveis globais o valor dos pontos que definem o quadro de um caractere que são
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

	public static void main(String[] args) throws IOException {
		File captcha2 = new File("treinamento" + File.separator + "captcha1.jpg");
		BufferedImage imageImage = ImageIO.read(captcha2);
		List<BufferedImage> simbolos = MyImage.getSimbolos(imageImage);
		int i = 1;
		for (BufferedImage simbolo : simbolos) {
			ImageIO.write(simbolo, "png", new File("treinamento" + File.separator + "saida" + i+ ".png"));
			i++;
		}

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

	private Coordenada superiorEquesrdo;
	private Coordenada inferiorDireito;

	public Dimensao() {

	}

	public int getArea() {
		return Math.abs(getInferiorDireito().y - getSuperiorEquesrdo().y) * Math.abs(getInferiorDireito().x - getSuperiorEquesrdo().x);
	}

	public void setSuperiorEquesrdo(Coordenada superiorEquesrdo) {
		this.superiorEquesrdo = superiorEquesrdo;
	}

	public Coordenada getSuperiorEquesrdo() {
		return superiorEquesrdo;
	}

	public void setInferiorDireito(Coordenada inferiorDireito) {
		this.inferiorDireito = inferiorDireito;
	}

	public Coordenada getInferiorDireito() {
		return inferiorDireito;
	}

	public String toString() {
		return superiorEquesrdo.toString() + " " + inferiorDireito.toString();
	}

}
