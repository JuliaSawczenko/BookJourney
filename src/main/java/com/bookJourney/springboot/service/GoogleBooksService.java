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
        String url = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + title + "+inauthor:" + author + "&langRestrict=en&key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        try {
            JSONObject json = new JSONObject(response);
            JSONObject volumeInfo = json.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");

            String isbn = volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");
            String description = volumeInfo.getString("description");
            String publishedDate = volumeInfo.getString("publishedDate");

            Double averageRating = null;
            if (volumeInfo.has("averageRating")) {
                averageRating = volumeInfo.getDouble("averageRating");
            }

            String imageUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

            JSONArray categoriesJsonArray = volumeInfo.optJSONArray("categories");
            List<String> categories = new ArrayList<>();
            if (categoriesJsonArray != null) {
                for (int i = 0; i < categoriesJsonArray.length(); i++) {
                    categories.add(categoriesJsonArray.getString(i));
                }
            }

            return new BookDetail(title, author, isbn, description, publishedDate, imageUrl, categories, averageRating);
        } catch (JSONException e) {
            throw new BookNotFoundException();
        }
    }
}
