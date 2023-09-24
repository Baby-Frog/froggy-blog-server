package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.ImageDto;
import com.example.froggyblogserver.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/image")
@CrossOrigin("*")
public class ImageController {

    @Autowired
    private ImageService service;
    @PostMapping("upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok().body(service.saveOrUpdate(ImageDto.builder().image(file).build()));
    }
    @GetMapping("getAllImage")
    public ResponseEntity<?> getAllImage(){
        return ResponseEntity.ok().body(service.readAllImage());
    }
    @GetMapping("get/{image}")
    public ResponseEntity<?> get(@PathVariable String image){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(service.getImage(image));
    }
}
