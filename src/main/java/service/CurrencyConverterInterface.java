package service;

public interface CurrencyConverterInterface {
	
	/**
	 * Converte um dado valor de uma moeda A para uma moeda B.
	 * @param value - valor a ser convertido
	 * @param from - moeda de origem
	 * @param to - moeda de destino
	 * @return JSON com valor convertido
	 */
	public String currencyAToB(String from, String to, Double value);
	
	/**
	 * Converte o valor de uma moeda A para todas as 32 moedas da base de dados.
	 * @param value - valor a ser convertido
	 * @param from - moeda de origem
	 * @return JSON com valores convertidos
	 */
	public String currencyAToAll(String from, Double value);
}
