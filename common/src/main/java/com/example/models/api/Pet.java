package com.example.models.api;

import java.util.List;
import java.util.Map;

public class Pet {

    private long id;
    private Map<String, Object> category;
    private String name;
    private List<String> photoUrls;
    private List<Map<String, Object>> tags;
    private String status;

    // Пустой конструктор для десериализации
    public Pet() {}

    // Конструктор со всеми полями
    public Pet(long id, Map<String, Object> category, String name,
               List<String> photoUrls, List<Map<String, Object>> tags, String status) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.photoUrls = photoUrls;
        this.tags = tags;
        this.status = status;
    }

    // Геттеры и сеттеры
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Map<String, Object> getCategory() { return category; }
    public void setCategory(Map<String, Object> category) { this.category = category; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }

    public List<Map<String, Object>> getTags() { return tags; }
    public void setTags(List<Map<String, Object>> tags) { this.tags = tags; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}