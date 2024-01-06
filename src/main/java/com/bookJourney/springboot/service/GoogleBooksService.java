package com.bookJourney.springboot.service;

import com.bookJourney.springboot.config.BookNotFoundException;
import com.bookJourney.springboot.entity.BookDetail;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;



@Service
public class GoogleBooksService {

    @Value("${googlebooks.api.key}")
    private String apiKey;

    public BookDetail getBookDetails(String title, String author) throws BookNotFoundException {
        String url = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + title + "+inauthor:" + author + "&key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JSONObject json = new JSONObject(response);
        if (json.getJSONArray("items").length() > 0) {
            JSONObject volumeInfo = json.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");

            String isbn = volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");
            String description = volumeInfo.getString("description");
            String publishedDate = volumeInfo.getString("publishedDate");
            String imageUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

            return new BookDetail(title, author, isbn, description, publishedDate, imageUrl);
        } else {
            throw new BookNotFoundException();
        }
    }
}
