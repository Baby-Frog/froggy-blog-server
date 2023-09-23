package com.example.froggyblogserver.service;

import com.example.froggyblogserver.dto.ImageDto;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface ImageService extends GeneralService<ImageDto> {
    BaseResponse readAllImage();
    byte[] getImage(String image);
}
