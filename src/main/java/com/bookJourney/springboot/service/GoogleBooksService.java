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
import java.util.stream.Collectors;


@Service
public class GoogleBooksService {

    @Value("${googlebooks.api.key}")
    private String apiKey;

    public BookDetail getBookDetails(String googleBookId) throws BookNotFoundException {
        String url = buildSearchUrl(googleBookId);
        JSONObject volumeInfo = fetchVolumeInfo(url);
        return parseVolumeInfoToBookDetail(volumeInfo, googleBookId);
    }

    private String buildSearchUrl(String googleBookId) {
        return "https://www.googleapis.com/books/v1/volumes/" + googleBookId + "?key=" + apiKey;
    }

    private JSONObject fetchVolumeInfo(String url) throws BookNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        try {
            return new JSONObject(response).getJSONObject("volumeInfo");
        } catch (JSONException e) {
            throw new BookNotFoundException();
        }
    }

    private BookDetail parseVolumeInfoToBookDetail(JSONObject volumeInfo, String googleBookId) throws JSONException {
        String title = volumeInfo.optString("title", "No title available");
        String author = volumeInfo.has("authors") ? String.join(", ", volumeInfo.getJSONArray("authors").toList().stream().map(Object::toString).collect(Collectors.toList())) : "No author available";
        String isbn = volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");
        String description = volumeInfo.optString("description", "No description available");
        String publishedDate = volumeInfo.optString("publishedDate", "No publication date available");
        Double averageRating = volumeInfo.has("averageRating") ? volumeInfo.getDouble("averageRating") : null;
        String imageUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

        List<String> categories = parseCategories(volumeInfo);

        return new BookDetail(googleBookId, title, author, isbn, description, publishedDate, imageUrl, categories, averageRating);
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