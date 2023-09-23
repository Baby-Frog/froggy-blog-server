package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.dto.ImageDto;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    String storage = "uploads";
    Path folder = Paths.get(storage);

    public ImageServiceImpl() {
        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public BaseResponse saveOrUpdate(ImageDto req) {
        var fileName = req.getImage().getOriginalFilename();
        var extension = FilenameUtils.getExtension(fileName);
        if (!checkExtension(extension.trim().toLowerCase()))
            throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        var size = req.getImage().getSize();
        final var maxsize = 5 * 1024 * 1024;
        if (size > maxsize)
            throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
        var saveFileToLocal = saveLocalImage(req.getImage());
        var baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        var urlImage = baseUrl + "/api/image/get/"  + saveFileToLocal;
        Map<String, String> urlImages = new HashMap<>();
        urlImages.put("urlImage", urlImage);
        return new BaseResponse(urlImages);
    }

    private boolean checkExtension(String extension) {
        var acceptExtension = new ArrayList<>();
        acceptExtension.add("jpg");
        acceptExtension.add("jpeg");
        acceptExtension.add("png");
        return acceptExtension.contains(extension);

    }

    private String saveLocalImage(MultipartFile file) {
        var fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        var pathFile = folder.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
        try {
            InputStream stream = file.getInputStream();
            Files.copy(stream, pathFile, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            throw new ValidateException(e.getMessage());
        }
    }

    @Override
    public BaseResponse readAllImage() {
        var baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        try {
            var readAll = Files.walk(folder, 1).filter(image -> !image.equals(folder)).map(image -> baseUrl + "/api/image/get/" + image.getFileName());
            Map<String, Object> listImage = new HashMap<>();
            listImage.put("listImage", readAll.collect(Collectors.toList()));
            return new BaseResponse(listImage);
        } catch (Exception e) {
            throw new ValidateException(e.getMessage());
        }
    }

    @Override
    public byte[] getImage(String image) {
        var file = folder.resolve(image);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            }
        } catch (Exception e) {
            throw new ValidateException(e.getMessage());
        }
        throw new ValidateException(MESSAGE.VALIDATE.INPUT_INVALID);
    }

    @Override
    public BaseResponse findById(String id) {
        return null;
    }
}
