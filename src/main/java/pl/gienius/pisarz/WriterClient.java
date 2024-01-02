package pl.gienius.pisarz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class WriterClient {

    Logger logger = LoggerFactory.getLogger(WriterClient.class);

    private final String baseUrl = "http://localhost:3000/api/writers";
    private final RestTemplate restTemplate;

    public WriterClient() {
        this.restTemplate = new RestTemplate();
    }

    public Writer createWriter(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Writer> request = new HttpEntity<>(new Writer(name), headers);
        ResponseEntity<Writer> response = restTemplate.postForEntity(baseUrl+ "/addWriter", request, Writer.class);
        logger.info("Sent request to addWriter " + name);
        return response.getBody();
    }

    public Book addNewBook(Book book, Long writerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Writer-ID", writerId.toString());

        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        ResponseEntity<Book> response = restTemplate.postForEntity(baseUrl+ "/addBook", request, Book.class);
        logger.info("Book added: " + response.getBody());
        return response.getBody();
    }

    public void blockBook(Long bookId, Long writerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Writer-ID", writerId.toString());

        String blockUrl = baseUrl + "/block/" + bookId;

        HttpEntity<?> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(blockUrl, HttpMethod.POST, request, Void.class);
            if (response.getStatusCode() == HttpStatus.OK)
                logger.info("Blocked the book: " + bookId + " response: " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                logger.info("Writer " + writerId + " is not authorized to block the book " + bookId);
            }
            else if (e.getStatusCode() == HttpStatus.NOT_MODIFIED){
                logger.info("Writer: " + writerId + " could not block book: " + bookId);
            }
            else if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                logger.info("Did not found the book by id: " + bookId);
            }
            else {
                throw e; // rethrow the exception for other types of errors
            }
        }
    }

    public void unBlockBook(Long bookId, Long writerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Writer-ID", writerId.toString());

        String blockUrl = baseUrl + "/block/" + bookId;

        HttpEntity<?> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(blockUrl, HttpMethod.POST, request, Void.class);
            if (response.getStatusCode() == HttpStatus.OK)
                logger.info("Unblocked the book: " + bookId + " response: " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                logger.info("Writer " + writerId + " is not authorized to unblock the book " + bookId);
            }
            else if (e.getStatusCode() == HttpStatus.NOT_MODIFIED){
                logger.info("Writer: " + writerId + " could not unblock book: " + bookId);
            }
            else if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                logger.info("Did not found the book by id: " + bookId);
            }
            else {
                throw e; // rethrow the exception for other types of errors
            }
        }
    }

    public boolean checkBook(Long bookId, Long writerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Writer-ID", writerId.toString());

        String checkUrl = baseUrl + "/check/" + bookId;
        logger.info("Checking book " + bookId);

        HttpEntity<?> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(checkUrl, HttpMethod.GET, request, Void.class);
            logger.info("Checked the book: " + bookId + " response: " + response.getStatusCode());
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                logger.info("Writer " + writerId + " is not authorized to check book " + bookId);
                return false;
            }
            else if (e.getStatusCode() == HttpStatus.CONFLICT){
                logger.info("Writer: " + writerId + " checked the book: " + bookId + "\nbook is not ready to update");
                return false;
            }
            else if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                logger.info("Did not found the book by id: " + bookId);
                return false;
            }
            else {
                throw e; // rethrow the exception for other types of errors
            }
        }
    }

    public boolean updateBook(Long bookId, Book updatedBook, Long writerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Writer-ID", writerId.toString());

        String updateUrl = baseUrl + "/update/" + bookId;

        ResponseEntity<Boolean> response = restTemplate.exchange(updateUrl, HttpMethod.PUT, new HttpEntity<>(headers), Boolean.class);
        logger.info("Requested to update the book: " + bookId + "response: " + response);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public boolean removeBook(Long bookId, Long writerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Writer-ID", writerId.toString());

        String removeUrl = baseUrl + "/remove/" + bookId;

        ResponseEntity<Boolean> response = restTemplate.exchange(removeUrl, HttpMethod.DELETE, new HttpEntity<>(headers), Boolean.class);
        logger.info("Requested to remove the book: " + bookId + "response: " + response);
        return response.getStatusCode() == HttpStatus.OK;
    }

}

