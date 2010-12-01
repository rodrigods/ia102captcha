package captcha;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Connector {

	public static void main(String[] args) throws Throwable {
		try { 
			// Crio uma conexão com a página inicial do envio de torpedo.
			URL url = new URL(
					"http://torpedo.oiloja.com.br/oitorpedo/EnviaSMSController");
			URLConnection conn = url.openConnection();

			// Finjo que sou o Firefox
			conn.setRequestProperty("Host", "torpedo.oiloja.com.br");;
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; pt-BR; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			conn.setRequestProperty("Accept-Language", "pt-br,pt;q=0.8,en-us;q=0.5,en;q=0.3");
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			conn.setRequestProperty("Keep-Alive", "115");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Referer", "http://torpedo.oiloja.com.br/oitorpedo/EnviaSMSController?acao=envioTorpedo");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			conn.setDoOutput(true);

			// Parametro exigido pelo site da oi, para liberar o iframe de digitar a mensagem
			String data = URLEncoder.encode("hidParametro", "UTF-8") + "="
			+ URLEncoder.encode("LIBERADO", "UTF-8");
			
			// Envio a requisicao
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(data);
			wr.flush();

			// Para pegar a resposta por linha
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			String line;

			String[] input = new String[5];
			int i = 0;
			
			// Varre o html, procurando o nome dos parametros, que muda dinamicamente
			while ((line = rd.readLine()) != null) {

				if (line
						.contains("<td><input type=\"text\" class=\"inputs\" name=\"")) {
					System.out.println("PROXIMA LINHA");
					String resto = line
							.replaceFirst(
									"\\s+\\Q<td><input type=\"text\" class=\"inputs\" name=\"\\E",
									"");

					input[i++] = resto.substring(0, resto.indexOf('"')); // achou um parametro
				}

				if (line
						.contains("<td colspan=\"3\"><textarea class=\"inputs\" name=\"")) {
					System.out.println("PROXIMA LINHA");
					String resto = line
							.replaceFirst(
									"\\s+\\Q<td colspan=\"3\"><textarea class=\"inputs\" name=\"\\E",
									"");

					System.out.println(resto.substring(0, resto.indexOf('"')));
					input[i++] = resto.substring(0, resto.indexOf('"')); // achou um parametro -> este eh a mensagem
				}

				System.out.println(line);
			}

			// o ultimo parametro eh sempre fixo
			input[4] = "captcha";

			wr.close();
			rd.close();
			
			System.out.println(conn.getHeaderFields());

			System.out.println(Arrays.toString(input));

			// Crio uma segunda conexao para pegar a imagem do captcha
			URL image = new URL(
					"http://torpedo.oiloja.com.br/oitorpedo/captcha.jpg");

			URLConnection imageConnection = image.openConnection();

			// Mais uma vez, finjo ser o Firefox
			imageConnection.setRequestProperty("Host", "torpedo.oiloja.com.br");
			imageConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; pt-BR; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
			imageConnection.setRequestProperty("Accept", "image/png,image/*;q=0.8,*/*;q=0.5");
			imageConnection.setRequestProperty("Accept-Language", "pt-br,pt;q=0.8,en-us;q=0.5,en;q=0.3");
			imageConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			imageConnection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			imageConnection.setRequestProperty("Keep-Alive", "115");
			imageConnection.setRequestProperty("Connection", "keep-alive");
			imageConnection.setRequestProperty("Referer", "http://torpedo.oiloja.com.br/oitorpedo/EnviaSMSController");
			
			// O site da oi me respondeu a primeira requisicao com cookies
			// Ajusto os cookies para as proximas conexoes
			String cookie = "";
			for (String value : conn.getHeaderFields().get("Set-Cookie"))
				cookie += ";" + value;
			
			imageConnection.setRequestProperty("Cookie", cookie.substring(1));

			imageConnection.connect();

			// Crio a imagem do captcha
			BufferedImage buff = ImageIO.read(new BufferedInputStream(
					imageConnection.getInputStream()));
			ImageIO.write(buff, "png", new File("captcha.png"));

			// Peco pro usuario digitar o captcha
			String captcha = JOptionPane.showInputDialog("Digite o captcha:");

			// Insiro os dados da requisicao
			data = URLEncoder.encode(input[0], "UTF-8") + "="
					+ URLEncoder.encode("Diogo", "UTF-8");

			data += "&" + URLEncoder.encode(input[1], "UTF-8") + "="
					+ URLEncoder.encode("83", "UTF-8");

			data += "&" + URLEncoder.encode(input[2], "UTF-8") + "="
					+ URLEncoder.encode("88582685", "UTF-8");

			data += "&" + URLEncoder.encode(input[3], "UTF-8") + "="
					+ URLEncoder.encode("Me diz se funcionou.", "UTF-8");

			data += "&" + URLEncoder.encode(input[4], "UTF-8") + "="
					+ URLEncoder.encode(captcha, "UTF-8");

			System.out.println(data);

			// Crio uma nova conexao, agora pra enviar a mensagem
			URL send = new URL(
					"http://torpedo.oiloja.com.br/oitorpedo/EnviaSMSController?acao=envioTorpedo");

			URLConnection sendConn = send.openConnection();

			// Finjo que sou o Firefox
			sendConn.setRequestProperty("Host", "torpedo.oiloja.com.br");
			sendConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; pt-BR; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
			sendConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			sendConn.setRequestProperty("Accept-Language", "pt-br,pt;q=0.8,en-us;q=0.5,en;q=0.3");
			sendConn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			sendConn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			sendConn.setRequestProperty("Keep-Alive", "115");
			sendConn.setRequestProperty("Connection", "keep-alive");
			sendConn.setRequestProperty("Referer", "http://torpedo.oiloja.com.br/oitorpedo/EnviaSMSController");
			sendConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			sendConn.setRequestProperty("Content-Length", "180");
			
			sendConn.setRequestProperty("Cookie", cookie.substring(1));
			
			sendConn.setDoOutput(true);

			wr = new OutputStreamWriter(sendConn.getOutputStream());

			// Faco o envio da mensagem
			wr.write(data);
			wr.flush();

			rd = new BufferedReader(
					new InputStreamReader(sendConn.getInputStream()));

			// Imprimindo a resposta, soh para confirmar
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}

			wr.close();
			rd.close();

		} catch (Exception e) {
		}

	}

}
