package service;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

@Stateless
@Path("/converter")
public class CurrencyConverterResource implements CurrencyConverterInterface{
	//Atributos utilizados no acesso da API externa
	public static final String ACCESS_KEY = "7f7012ebf6b30f1cab604e8a4f0737fa";
	public static final String BASE_URL = "http://apilayer.net/api/";
	public static final String FROM = "&from=";
	public static final String TO = "&to=";
	public static final String AMOUNT = "&amount=1";
	public static final String ENDPOINT = "live";
	public static final String CONVERT = "convert";
	public static final String STATIC_DOLAR = "USD";
	static CloseableHttpClient httpClient = HttpClients.createDefault();
	
	public CurrencyConverterResource() {
		
	}

	@GET
	@Path("{moedaOrigem}/{moedaDestino}/{valor}")
	@Produces(MediaType.APPLICATION_JSON)
	public String currencyAToB(@PathParam("moedaOrigem") String from, @PathParam("moedaDestino") String to, @PathParam("valor") Double value){
		Double result = sendLiveRequest(from, to);
		if (result == null) {
			return "Not Available";
		}
		
		String json = new JSONObject().put("moedaOrigem", from).put("moedaDestino", to).put("valor", (value * result)).toString();
		
		return json;
	}

	@GET
	@Path("{moedaOrigem}/{valor}")
	@Produces(MediaType.APPLICATION_JSON)
	public String currencyAToAll(@PathParam("moedaOrigem") String from, @PathParam("valor") Double value) throws RemoteException {
		return "Teste";
	}
	
	/**
	 * Método auxiliar criado para extrair da API externa o valor de 1 unidade da moeda de origem e da de destino 
	 * em dólares, para então ser feita a conversão de uma moeda para a outra.
	 * @param value - valor a ser convertido
	 * @param from - moeda de origem
	 * @return o valor convertido de uma moeda para outra ou NULL no caso de requisições com erro da API
	 */
	private Double sendLiveRequest(String from, String to) {

		//Variáveis auxiliares
		Double from2 = 1.0;
		Double to2 = 1.0;
		
		// Inicializa o objeto HttpGet com a URL para mandar a requisição para a API
		HttpGet get = new HttpGet(BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY);
		try {
			CloseableHttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();

			// Converte a resposta JSON em um objeto equivalente em Java
			JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));

			//Lança mensagem no console de que o acesso à API foi iniciado
			System.out.println("Live Currency Exchange Rates");
			
			//Variável utilizada para capturar se a requisição à API teve status ou não
			boolean status = exchangeRates.getBoolean("success");

			if (!status) {
				String codigoErro = exchangeRates.getJSONObject("error").getString("code");

				String infoErro = exchangeRates.getJSONObject("error").getString("info");

				System.out.println("API reached its peak of access.");
				System.out.println("Error: " + codigoErro);
				System.out.println("Message: " + infoErro);
				//Retornar um JSON com essas informações e erro 500
				System.exit(0);
			}
			
			
			//Valor equivalente à 1 dólar na moeda de origem
			System.out.println("Converting " + STATIC_DOLAR + " in " + from + ": "
					+ exchangeRates.getJSONObject("quotes").getDouble(STATIC_DOLAR + from));
			
			//Divisão para transformar o valor recuperado da API em 1 unidade da moeda de origem
			from2 = 1 / exchangeRates.getJSONObject("quotes").getDouble(STATIC_DOLAR + from);
			
			//Impressão no console 
			System.out.println("1 unity of " + from + " in USD: " + from2);
			
			//Valor equivalente à 1 dólar na moeda de destino
			System.out.println("Converting " + STATIC_DOLAR + " in " + to + ": "
					+ exchangeRates.getJSONObject("quotes").getDouble(STATIC_DOLAR + to));
			
			//Divisão para transformar o valor recuperado da API em 1 unidade da moeda de destino
			to2 = 1 / exchangeRates.getJSONObject("quotes").getDouble(STATIC_DOLAR + to);
			
			//Impressão no console 
			System.out.println("1 unity of " + to + " in USD: " + to2);
			
			//Impressão no console do resultado da conversão da moeda de origem para a de destino
			System.out.println(from + " IN " + to + ": " + from2 / to2);

			response.close();
			return from2 / to2;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
	
}
