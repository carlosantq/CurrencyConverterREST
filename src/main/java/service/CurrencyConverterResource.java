package service;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Stateless
@Path("/converter")
public class CurrencyConverterResource implements CurrencyConverterInterface{
	public CurrencyConverterResource() {
		
	}

	@GET
	@Path("{moedaOrigem}/{moedaDestino}/{valor}")
	@Produces(MediaType.APPLICATION_JSON)
	public String teste(@PathParam("moedaOrigem") String moedaOrigem,@PathParam("moedaDestino") String moedaDestino, @PathParam("valor") Double valor) {
		String json = new JSONObject().put("moedaOrigen", moedaOrigem).put("valor", valor).put("moedaDestino", moedaDestino).toString();		
		return json;
	}
	
	@GET
	@Path("{moedaOrigem}/{valor}")
	@Produces(MediaType.APPLICATION_JSON)
	public String teste2(@PathParam("moedaOrigem") String moedaOrigem, @PathParam("valor") Double valor) {
		return new JSONObject().put("moedaOrigem", moedaOrigem).put("valor", valor).toString();
	}
	
	
}
