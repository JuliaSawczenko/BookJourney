package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.entity.BookDetail;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


@Service
public class GoogleBooksService {

    @Value("${googlebooks.api.key}")
    private String apiKey;

    public BookDetail getBookDetails(String title, String author) throws BookNotFoundException {
        String url = buildSearchUrl(title, author);
        JSONObject volumeInfo = fetchVolumeInfo(url);
        return parseVolumeInfoToBookDetail(volumeInfo, title, author);
    }

    private String buildSearchUrl(String title, String author) {
        return "https://www.googleapis.com/books/v1/volumes?q=intitle:" + title + "+inauthor:" + author + "&langRestrict=en&key=" + apiKey;
    }

    private JSONObject fetchVolumeInfo(String url) throws BookNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        try {
            JSONObject json = new JSONObject(response);
            return json.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");
        } catch (JSONException e) {
            throw new BookNotFoundException();
        }
    }

    private BookDetail parseVolumeInfoToBookDetail(JSONObject volumeInfo, String title, String author) throws JSONException {
        String isbn = volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");
        String description = volumeInfo.optString("description", "No description available");
        String publishedDate = volumeInfo.optString("publishedDate", "No publication date available");

        Double averageRating = volumeInfo.has("averageRating") ? volumeInfo.getDouble("averageRating") : null;
        String imageUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

        List<String> categories = parseCategories(volumeInfo);

        return new BookDetail(title, author, isbn, description, publishedDate, imageUrl, categories, averageRating);
    }

    private List<String> parseCategories(JSONObject volumeInfo) {
        List<String> categories = new ArrayList<>();
        JSONArray categoriesJsonArray = volumeInfo.optJSONArray("categories");
        if (categoriesJsonArray != null) {
            for (int i = 0; i < categoriesJsonArray.length(); i++) {
                categories.add(categoriesJsonArray.optString(i, "Unknown Category"));
            }
        }
        return categories;
    }
}