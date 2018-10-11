package service;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
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
	
	/**
	 * Construtor Padrão
	 */
	public CurrencyConverterResource() {
	}

	/**
	 * Converte um dado valor de uma moeda A para uma moeda B.
	 * @param value - valor a ser convertido
	 * @param from - moeda de origem
	 * @param to - moeda de destino
	 * @return JSON com valor convertido
	 */
	@GET
	@Path("{moedaOrigem}/{moedaDestino}/{valor}")
	@Produces(MediaType.APPLICATION_JSON)
	public String currencyAToB(@PathParam("moedaOrigem") String from, @PathParam("moedaDestino") String to, @PathParam("valor") Double value){
		JSONObject apiJSON = requestAPI();
		Double result = convert(apiJSON, from, to);
		if (result == null && !apiJSON.getBoolean("success")) {
			return new JSONObject().put("status", false).put("error", apiJSON.getString("error")).toString();
		}else if (result == null && apiJSON.getBoolean("success")) {
			return new JSONObject().put("status", false).put("error", "Currency not found").toString();
		}
		
		String json = new JSONObject().put("status", true).put("moedaOrigem", from).put("moedaDestino", to).put("valor", (value * result)).toString();
		
		return json;
	}

	/**
	 * Converte o valor de uma moeda A para todas as 32 moedas da base de dados.
	 * @param value - valor a ser convertido
	 * @param from - moeda de origem
	 * @return JSON com valores convertidos
	 */
	@GET
	@Path("{moedaOrigem}/{valor}")
	@Produces(MediaType.APPLICATION_JSON)
	public String currencyAToAll(@PathParam("moedaOrigem") String from, @PathParam("valor") Double value){
		JSONObject apiJSON = null;
		JSONArray jsonArray = new JSONArray();
		try{
			apiJSON = requestAPI();
		} catch (NullPointerException npe) {
			return jsonArray.put(new JSONObject().put("status", false).put("error", "ERRO 500")).toString();
		}
		
		if(!validateCurrency(from)) {
			return new JSONObject().put("status", false).put("error", "Currency not found").toString();
		}
		
		if(!apiJSON.getBoolean("success")) {
			return new JSONObject().put("status", false).put("error", apiJSON.getString("error")).toString();
		}
		
		jsonArray.put(new JSONObject().put("status", true));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "DKK").put("valor", (value * convert(apiJSON, from, "DKK"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "NOK").put("valor", (value * convert(apiJSON, from, "NOK"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "SEK").put("valor", (value * convert(apiJSON, from, "SEK"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "CZK").put("valor", (value * convert(apiJSON, from, "CZK"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "GBP").put("valor", (value * convert(apiJSON, from, "GBP"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "TRY").put("valor", (value * convert(apiJSON, from, "TRY"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "INR").put("valor", (value * convert(apiJSON, from, "INR"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "IDR").put("valor", (value * convert(apiJSON, from, "IDR"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "PKR").put("valor", (value * convert(apiJSON, from, "PKR"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "THB").put("valor", (value * convert(apiJSON, from, "THB"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "USD").put("valor", (value * convert(apiJSON, from, "USD"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "AUD").put("valor", (value * convert(apiJSON, from, "AUD"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "CAD").put("valor", (value * convert(apiJSON, from, "CAD"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "SGD").put("valor", (value * convert(apiJSON, from, "SGD"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "HKD").put("valor", (value * convert(apiJSON, from, "HKD"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "TWD").put("valor", (value * convert(apiJSON, from, "TWD"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "NZD").put("valor", (value * convert(apiJSON, from, "NZD"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "EUR").put("valor", (value * convert(apiJSON, from, "EUR"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "HUF").put("valor", (value * convert(apiJSON, from, "HUF"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "CHF").put("valor", (value * convert(apiJSON, from, "CHF"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "JPY").put("valor", (value * convert(apiJSON, from, "JPY"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "ILS").put("valor", (value * convert(apiJSON, from, "ILS"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "CLP").put("valor", (value * convert(apiJSON, from, "CLP"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "PHP").put("valor", (value * convert(apiJSON, from, "PHP"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "MXN").put("valor", (value * convert(apiJSON, from, "MXN"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "ZAR").put("valor", (value * convert(apiJSON, from, "ZAR"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "BRL").put("valor", (value * convert(apiJSON, from, "BRL"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "MYR").put("valor", (value * convert(apiJSON, from, "MYR"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "RUB").put("valor", (value * convert(apiJSON, from, "RUB"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "KRW").put("valor", (value * convert(apiJSON, from, "KRW"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "CNY").put("valor", (value * convert(apiJSON, from, "CNY"))));
		jsonArray.put(new JSONObject().put("moedaOrigem", from).put("moedaDestino", "PLN").put("valor", (value * convert(apiJSON, from, "PLN"))));
		
		return jsonArray.toString();
		
	}
	
	/**
	 * Método encarregado de fazer a requisição para a API do CurrencyLayer para pegar os 
	 * valores de todas as moedas na unidade DÓLAR.
	 * @return JSON de resposta da requisição.
	 */
	private JSONObject requestAPI() {
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

				String infoErro = exchangeRates.getJSONObject("error").getString("info");

				JSONObject errorJson = new JSONObject();
				return errorJson.put("success", false).put("error", infoErro);
			}
			
			response.close();
			return exchangeRates;
			
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
	
	/**
	 * Método auxiliar utilizado para validar as moedas da conversão
	 * @param currency - sigla da moeda.
	 * @return true caso a moeda seja válida, false caso contrário.
	 */
	private boolean validateCurrency(String currency) {
		
		String[] currencies = { "DKK", "NOK", "SEK", "CZK", "GBP", "TRY", "INR",
				"IDR", "PKR", "THB", "USD", "AUD", "CAD", "SGD", "HKD", "TWD", 
				"NZD", "EUR", "HUF", "CHF", "JPY", "ILS", "CLP", "PHP", "MXN", 
				"ZAR", "BRL", "MYR", "RUB", "KRW", "CNY", "PLN" };
		
		int aux = 0; 
		
		//Confere se a moeda utilizada é alguma das suportadas pela aplicação
		for (int i=0; i<currencies.length; i++) {
			if (!currency.equals(currencies[i])) {
				aux++;
			}
		}
		if (aux == 32) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * Método auxiliar criado para extrair do JSON da API externa o valor de 1 unidade da moeda de origem e da de destino 
	 * em dólares, para então ser feita a conversão de uma moeda para a outra.
	 * @param exchangeRates JSON resposta da requisição do CurrencyLayer
	 * @param from - moeda de origem
	 * @param to - moeda de destino
	 * @return o valor convertido de uma moeda para outra ou NULL no caso de moeda ser inválida.
	 */
	private Double convert(JSONObject exchangeRates, String from, String to) {

		//Variáveis auxiliares
		Double from2 = 1.0;
		Double to2 = 1.0;
		boolean status = exchangeRates.getBoolean("success");
		
		if (!status) {
			return null;
		}if (validateCurrency(from) == false) {
			return null;
		}else if (validateCurrency(to) == false) {
			return null;
		}else {
			//Divisão para transformar o valor recuperado da API em 1 unidade da moeda de origem
			from2 = 1 / exchangeRates.getJSONObject("quotes").getDouble(STATIC_DOLAR + from);
			
			//Divisão para transformar o valor recuperado da API em 1 unidade da moeda de destino
			to2 = 1 / exchangeRates.getJSONObject("quotes").getDouble(STATIC_DOLAR + to);
			
			return from2 / to2;
		}

	}
	
}
