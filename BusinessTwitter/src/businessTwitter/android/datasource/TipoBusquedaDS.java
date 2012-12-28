package businessTwitter.android.datasource;

import java.util.ArrayList;
import java.util.List;

public class TipoBusquedaDS {
	
	public static List<String> getKeyWordsByTipoBusqueda(String tipo){
		List<String> result = new ArrayList<String>();
		
		if(tipo.equals("En venta")){
			result = cargarKeyWordsEnVenta();
		}
		if(tipo.equals("Se compra")){
			result = cargarKeyWordsSeCompra();
		}
		if(tipo.equals("Alquiler")){
			result = cargarKeyWordsAlquiler();
		}
		
		if(tipo.equals("On Sale")){
			result = cargarKeyWordsOnSale();
		}
		if(tipo.equals("He Buys")){
			result = cargarKeyWordsHeBuys();
		}
		if(tipo.equals("Rental")){
			result = cargarKeyWordsRental();
		}
		
		return result;
	}

	private static List<String> cargarKeyWordsRental() {
		List<String> result = new ArrayList<String>();
		//result.add("alquiler");
		result.add("rent");
		result.add("rents");
		//result.add("alquilamos");
		//result.add("alquilan");
		return result;
	}

	private static List<String> cargarKeyWordsHeBuys() {
		List<String> result = new ArrayList<String>();
		result.add("purchased");
//		result.add("compro");
//		result.add("compra");
//		result.add("compramos");
//		result.add("compran");
		return result;
	}

	private static List<String> cargarKeyWordsOnSale() {
		List<String> result = new ArrayList<String>();		
		//result.add("vender");
		result.add("buy");
//		result.add("vende");
//		result.add("vendemos");
//		result.add("venden");		
		return result;
	}

	private static List<String> cargarKeyWordsAlquiler() {
		List<String> result = new ArrayList<String>();
		//result.add("alquiler");
		result.add("alquilo");
		result.add("alquila");
		result.add("alquilamos");
		result.add("alquilan");
		return result;
	}

	private static List<String> cargarKeyWordsSeCompra() {
		List<String> result = new ArrayList<String>();
		result.add("se compra");
		result.add("compro");
		result.add("compra");
		result.add("compramos");
		result.add("compran");
		return result;
	}

	private static List<String> cargarKeyWordsEnVenta() {
		List<String> result = new ArrayList<String>();		
		//result.add("vender");
		result.add("vendo");
		result.add("vende");
		result.add("vendemos");
		result.add("venden");		
		return result;
	}

}
