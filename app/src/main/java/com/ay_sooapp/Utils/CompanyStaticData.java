package com.ay_sooapp.Utils;

import java.util.HashMap;

public class CompanyStaticData {


    public static HashMap<String, String> companyStaticHashMap;
    private static CompanyStaticData companyStaticData = new CompanyStaticData();

    static {
        companyStaticHashMap = new HashMap<String, String>();
        companyStaticHashMap.put("ASOS", "http://www.asos.com/search/articleNumber?hrd=1&;q=articleNumber");
        companyStaticHashMap.put("Zalando", "https://www.zalando.de/katalog/?q=");
        companyStaticHashMap.put("TopShop", "http://eu.topshop.com/webapp/wcs/stores/servlet/CatalogNavigationSearchResultCmd?langId=-1&storeId=12556&catalogId=33057&Dy=1&Nty=1&beginIndex=1&pageNum=1&Ntt=");
        companyStaticHashMap.put("H&M", "http://www.hm.com/de/products/search?q=");
        companyStaticHashMap.put("ESPRIT", "http://www.esprit.de/suche?query=");
        companyStaticHashMap.put("SHOP .MANGO", "http://shop.mango.com/DE/search?kw=");
        companyStaticHashMap.put("BREUNINGER", "https://www.breuninger.com/search.cmd?searchTerm=");
        companyStaticHashMap.put("NET A PORTER", "https://www.net-a-porter.com/de/en/product/");
        companyStaticHashMap.put("UNIQLO", "http://www.uniqlo.com/de/de/search?q=");
    }

    public static CompanyStaticData getInstance() {
        return companyStaticData;
    }
}
