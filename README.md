# URL Shortener

## 📌 Overview

This is a URL Shortener application built using Spring Boot for the backend and React for the frontend. The application allows users to shorten long URLs and retrieve the original URLs using short codes.

## 🛠️ Tech Stack

### **Backend:**
- Java (Spring Boot)
- Spring Web, Spring Data JPA
- MySQL (Database)
- Redis (Caching)
- Hibernate

### **Frontend:**
- React (TypeScript)
- Tailwind CSS


## 🎯 Features
- Shorten long URLs
- Retrieve original URLs using short codes
- Cache shortened URLs using Redis for fast access
- RESTful API endpoints for managing URLs

## 🔗 API Endpoints

### 1⃣ Shorten URL

**Endpoint:** `POST /api/shorten`

```json
{
  "originalUrl": "https://example.com"
}
```

**Response:**

```json
{
  "shortCode": "abc123"
}
```

### 2⃣ Retrieve Original URL

**Endpoint:** `GET /api/{shortCode}`

### 3⃣ Redirect to Original URL

Visiting `http://localhost:8080/api/{shortCode}` will redirect to the original URL.

## 🎨 UI Preview

![UI Preview](https://drive.google.com/uc?export=view&id=1bedZCCAHqnOcxVeXrCiJU5eGN_aGRnM6)

