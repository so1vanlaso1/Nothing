package com.YeuTech.Application.Services;

import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.FacebookTokenUpdateRequest;
import com.YeuTech.Dtos.TokenStatusDto;

public interface IFacebookConfigService {

    ApiResponseFormat<Void> updateAccessToken(String email, FacebookTokenUpdateRequest dto);

    ApiResponseFormat<TokenStatusDto> getTokenStatus(String email);

    ApiResponseFormat<Void> refreshToken(String email);
}
