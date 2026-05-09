package com.YeuTech.Application.Services;

import com.YeuTech.Dtos.ApiResponseFormat;
// import com.YeuTech.Dtos.Request.FacebookConfigRequestDto;

public interface IFacebookService {
    void publishToFacebookByUserId(String userId, String message);

    String getUserPages(String accessToken);

    ApiResponseFormat<Object> getPageInfo(String email);
}
