package com.ducnt.bigcrawler.service;

import com.ducnt.bigcrawler.dto.ProductDTO;
import com.ducnt.bigcrawler.exception.NotCorrectJWTException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CrawlerService {
    public Set<ProductDTO> CrawlData(String tenantID) {
         switch (tenantID) {
             case "thegioididong": return CrawlDataAcrossThegioididong();
             case "cellphones": return CrawlDataAcrossCellphones();
             default: return null;
         }
    }

    private Set<ProductDTO> CrawlDataAcrossThegioididong() {
        try {
            Document document = Jsoup.connect("https://www.thegioididong.com/").get();
            Elements listElements = document.select("div.item a");
            Set<ProductDTO> productList = listElements.stream()
                    .filter(element -> !element.attr("data-price").isEmpty())
                    .map(element -> {
                        return ProductDTO.builder()
                                .name(element.attr("data-name"))
                                .brand(element.attr("data-brand"))
                                .category(element.attr("data-cate"))
                                .price(element.attr("data-price"))
                                .build();
                    })
                    .collect(Collectors.toSet());
            return productList;
        } catch (Exception ex) {
            throw new NotCorrectJWTException("Error");
        }
    }


    private Set<ProductDTO> CrawlDataAcrossCellphones() {
        try {
            Set<ProductDTO> result = new HashSet<>();
            String url = "https://api.cellphones.com.vn/v2/graphql/query";
            String jsonBody = "{\"query\":\"query { products( filter: { static: { categories: [\\\"3\\\"] province_id: 58, stock: { from: 1 } }, dynamic: { } }, page: 1, size: 50, sort: [{view: desc}] ) { general { product_id name manufacturer } filterable { name is_installment stock_available_id filter { id Label } price special_price promotion_information thumbnail promotion_pack sticker flash_sale { id is_valid shown_at } } } }\",\"variables\":{}}";

            //Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            headers.setContentType(MediaType.APPLICATION_JSON);

            //Create HttpEntity with json body and headers
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            RestTemplate restTemplate = new RestTemplate();

            //Get response by sending request
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String responseBody = responseEntity.getBody();

            //Read response JSON
            JSONObject dataJsonObject = new JSONObject(responseBody).getJSONObject("data");
            JSONArray productsJsonArray = dataJsonObject.getJSONArray("products");
            for (int i = 0; i < productsJsonArray.length(); i++) {
                JSONObject productJsonObject = productsJsonArray.getJSONObject(i);
                JSONObject generalData = productJsonObject.getJSONObject("general");
                JSONObject filterableData = productJsonObject.getJSONObject("filterable");
                result.add(
                        ProductDTO.builder()
                                .name(generalData.getString("name"))
                                .brand(generalData.getString("manufacturer"))
                                .price(String.format("%.2f", filterableData.getFloat("price")))
                                .category("Điện thoại")
                                .build()
                );
            }
            return result;
        } catch (Exception ex) {
            throw new NotCorrectJWTException("Error");
        }
    }
}
