package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.entity.BookDetail;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



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
            String imageUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

            return new BookDetail(title, author, isbn, description, publishedDate, imageUrl);
        } catch (JSONException e) {
            throw new BookNotFoundException();
        }
    }
}
