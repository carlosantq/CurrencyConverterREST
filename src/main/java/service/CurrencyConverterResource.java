package service;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/converter")
public class CurrencyConverterResource implements CurrencyConverterInterface{
	public CurrencyConverterResource() {
		
	}

	@GET
	@Path("/teste")
	@Produces(MediaType.TEXT_PLAIN)
	public String teste(@QueryParam("parametro") String parametro) {
		return parametro;
	}
	
}
