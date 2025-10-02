package com.ruc.payments.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Custom message converter to handle JSON content sent with text/html content type.
 * This allows Rahul's requirement of sending JSON data with text/html; charset=UTF-8 headers.
 */
@Component
public class CustomMessageConverter implements HttpMessageConverter<Object> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        // Support reading JSON content that comes with text/html content type
        return mediaType != null && 
               (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType) || 
                MediaType.TEXT_HTML.isCompatibleWith(mediaType));
    }
    
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false; // We only handle reading, not writing
    }
    
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML);
    }
    
    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) 
            throws IOException, HttpMessageNotReadableException {
        
        // Read the content as string
        String content = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);
        
        try {
            // Parse as JSON regardless of the content type
            return objectMapper.readValue(content, clazz);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException("Could not parse JSON content: " + e.getMessage(), inputMessage);
        }
    }
    
    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) 
            throws IOException, HttpMessageNotWritableException {
        // Not implemented - we only handle reading
        throw new UnsupportedOperationException("Writing not supported");
    }
}
