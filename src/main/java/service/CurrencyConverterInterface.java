package service;

import java.rmi.RemoteException;

public interface CurrencyConverterInterface {
	
	/**
	 * Converte um dado valor de uma moeda A para uma moeda B.
	 * @param value - valor a ser convertido
	 * @param from - moeda de origem
	 * @param to - moeda de destino
	 * @return Valor convertido
	 * @throws RemoteException
	 */
	public String currencyAToB(String from, String to, Double value) throws RemoteException;
	
	/**
	 * Converte o valor de uma moeda A para todas as 32 moedas da base de dados.
	 * @param value - valor a ser convertido
	 * @param from - moeda de origem
	 * @return Lista com valores convertidos
	 * @throws RemoteException
	 */
	public String currencyAToAll(String from, Double value) throws RemoteException;
}
